# Spring 中 ID 类设计实践指南

好的，我来整理一篇完整的文章。这篇文章将分为上下两部分回复，以确保内容完整且易于阅读。

# DDD 中的 ID 类设计实践指南

## 一、引言

在领域驱动设计(DDD)中，标识符(ID)的处理是一个看似简单但实际上值得深入探讨的话题。传统开发中，我们通常直接使用基本类型（如 String、Long）作为实体的标识符。但在 DDD 中，我们提倡为标识符创建专门的类型，这种方式能带来更多的优势。

## 二、为什么需要 ID 类

### 1. 类型安全

```java
// 使用原始类型，容易出错
public void process(String userId, String orderId) {
    // 参数顺序容易搞错
}

// 使用ID类，编译器帮助检查
public void process(UserId userId, OrderId orderId) {
    // 类型安全，无法传错参数
}
```

### 2. 封装验证逻辑

```java
public class OrderId {
    private final String value;

    public OrderId(String value) {
        if (value == null || !value.matches("^ORD-\\d{6}$")) {
            throw new IllegalArgumentException("Invalid order ID format");
        }
        this.value = value;
    }
}
```

### 3. 业务语义更清晰

```java
// 不清晰的原始类型
Map<String, Order> orderMap;

// 更清晰的领域语义
Map<OrderId, Order> orderMap;
```

## 三、完整实现示例

### 1. ID 类定义

```java:src/main/java/com/example/domain/OrderId.java
@Value
public class OrderId {
    String value;

    public OrderId(String value) {
        if (value == null || !value.matches("^ORD-\\d{6}$")) {
            throw new IllegalArgumentException("Invalid order ID format");
        }
        this.value = value;
    }

    public static OrderId generate() {
        return new OrderId("ORD-" + String.format("%06d", new Random().nextInt(999999)));
    }

    @Override
    public String toString() {
        return value;
    }
}
```

### 2. 实体类

```java:src/main/java/com/example/domain/Order.java
@Data
@TableName("orders")
public class Order {
    @TableId(value = "id", type = IdType.INPUT)
    private OrderId id;

    private String customerName;
    private BigDecimal amount;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
```

### 3. MyBatis Plus 类型处理器

```java:src/main/java/com/example/infrastructure/typehandler/OrderIdTypeHandler.java
@MappedTypes(OrderId.class)
public class OrderIdTypeHandler extends BaseTypeHandler<OrderId> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, OrderId parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public OrderId getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : new OrderId(value);
    }

    // ... 其他必要的方法实现
}
```

### 4. Spring MVC 转换器

```java:src/main/java/com/example/infrastructure/converter/OrderIdConverter.java
@Component
public class OrderIdConverter implements Converter<String, OrderId> {
    @Override
    public OrderId convert(String source) {
        try {
            return new OrderId(source);
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderIdException("Invalid order ID format: " + source);
        }
    }
}
```

### 5. Web 配置

```java:src/main/java/com/example/config/WebConfig.java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OrderIdConverter());
    }
}
```

[接上文...]

### 6. Service 层实现

```java:src/main/java/com/example/service/OrderService.java
@Service
@Transactional
public class OrderService {
    private final OrderMapper orderMapper;

    public OrderService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public Order createOrder(String customerName, BigDecimal amount) {
        Order order = new Order();
        order.setId(OrderId.generate());
        order.setCustomerName(customerName);
        order.setAmount(amount);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        orderMapper.insert(order);
        return order;
    }

    public Optional<Order> getOrder(OrderId orderId) {
        return Optional.ofNullable(orderMapper.selectById(orderId));
    }
}
```

### 7. Controller 层实现

```java:src/main/java/com/example/controller/OrderController.java
@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单管理", description = "订单相关的API接口")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{orderId}")
    @Operation(summary = "获取订单详情")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable OrderId orderId) {
        return orderService.getOrder(orderId)
            .map(order -> ResponseEntity.ok(OrderResponse.from(order)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "批量查询订单")
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(required = false) List<OrderId> orderIds) {
        List<Order> orders = orderService.getOrders(orderIds);
        return ResponseEntity.ok(
            orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList())
        );
    }
}
```

### 8. 异常处理

```java:src/main/java/com/example/exception/GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidOrderIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderId(InvalidOrderIdException ex) {
        ErrorResponse error = new ErrorResponse(
            "INVALID_ORDER_ID",
            ex.getMessage()
        );
        return ResponseEntity.badRequest().body(error);
    }
}
```

## 四、使用 ID 类的优缺点分析

### 优点：

1. **类型安全**

   - 编译时类型检查
   - 避免参数传递错误
   - 防止类型混淆

2. **封装验证逻辑**

   - 集中的验证规则
   - 一致的错误处理
   - 减少重复代码

3. **领域语义表达**

   - 更清晰的业务含义
   - 更好的代码可读性
   - 符合领域驱动设计原则

4. **便于重构和扩展**
   - 修改 ID 格式只需要改一处
   - 可以添加业务相关的方法
   - 支持更复杂的 ID 生成策略

### 缺点：

1. **代码复杂度增加**

   - 需要额外的类定义
   - 需要配置类型转换器
   - 需要处理序列化问题

2. **性能开销**

   - 对象创建的开销
   - 额外的内存占用
   - 类型转换的开销

3. **团队适应成本**
   - 需要团队理解和认同
   - 可能遇到对"过度设计"的质疑
   - 需要额外的学习成本

## 五、最佳实践建议

### 1. 何时使用 ID 类

- 复杂的领域模型
- 特定格式的 ID 要求
- 需要强类型安全的场景
- 有特殊 ID 生成规则的场景

### 2. 何时不使用 ID 类

- 简单的 CRUD 应用
- 性能要求极高的场景
- 团队技术栈不足
- 项目周期短暂

### 3. 实现建议

1. **使用工具减少样板代码**

   - 使用 Lombok 简化代码
   - 使用自动生成工具
   - 建立项目模板

2. **注意性能优化**

   - 考虑使用缓存
   - 批量操作时注意性能
   - 合理使用索引

3. **保证可测试性**

   - 编写完整的单元测试
   - 包含边界条件测试
   - 测试异常情况

4. **文档和规范**
   - 清晰的注释和文档
   - 统一的命名规范
   - 团队开发规范

## 六、总结

ID 类的使用是 DDD 中的一个重要实践，它能够提供更好的类型安全性和领域表达。通过与 Spring MVC 和 MyBatis Plus 的集成，我们可以在实际项目中优雅地实现这一模式。

选择是否使用 ID 类需要考虑：

1. 项目的复杂度和规模
2. 团队的技术能力和接受度
3. 性能要求和维护成本
4. 业务领域的特殊需求

最重要的是要确保团队对这种模式有统一的认识，并在实践中不断优化和改进。在合适的场景下使用 ID 类，可以显著提升代码的质量和可维护性。

## 七、参考资料

1. Domain-Driven Design by Eric Evans
2. Spring Framework Documentation
3. MyBatis Plus Documentation
4. Clean Code by Robert C. Martin

通过这样的实现，我们既保证了领域模型的完整性，又能与常用框架良好集成，是一个比较平衡的解决方案。
