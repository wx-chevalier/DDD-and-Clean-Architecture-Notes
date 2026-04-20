# 领域驱动设计 (Domain-Driven Design) 快速入门

领域驱动设计(DDD)是一种以业务领域为核心的软件开发方法论，特别适合处理复杂业务系统的开发。

## 核心概念

### 1. 分层架构

DDD 通常采用四层架构：

- **表现层**（UI Layer）：负责用户界面和用户交互
- **应用层**（Application Layer）：编排领域对象，处理业务用例
- **领域层**（Domain Layer）：包含核心业务逻辑和规则
- **基础设施层**（Infrastructure Layer）：提供技术实现和外部服务集成

### 2. 战略设计要素

- **限界上下文**（Bounded Context）：定义清晰的业务边界，确保模型的一致性
- **通用语言**（Ubiquitous Language）：团队共享的业务术语和概念
- **上下文映射**（Context Mapping）：定义不同上下文间的关系和集成方式

### 3. 战术设计要素

- **实体**（Entity）：具有唯一标识的对象
- **值对象**（Value Object）：无标识的不可变对象
- **聚合**（Aggregate）：确保数据一致性的边界
- **领域服务**（Domain Service）：处理跨实体的业务逻辑
- **领域事件**（Domain Event）：捕获领域中的重要变化

## 代码示例

### 基础架构示例

```typescript
// 领域层：值对象
class Email {
  private constructor(private readonly value: string) {
    this.validate(value);
  }

  static create(email: string): Email {
    return new Email(email);
  }

  private validate(email: string): void {
    if (!email.includes("@")) {
      throw new Error("Invalid email format");
    }
  }
}

// 领域层：实体
class User {
  private constructor(
    private readonly id: UserId,
    private email: Email,
    private status: UserStatus
  ) {}

  static create(email: string): User {
    return new User(UserId.generate(), Email.create(email), UserStatus.ACTIVE);
  }

  deactivate(): void {
    if (this.status !== UserStatus.ACTIVE) {
      throw new Error("User is not active");
    }
    this.status = UserStatus.INACTIVE;
  }
}

// 应用层：用例
class UserApplicationService {
  constructor(
    private userRepository: IUserRepository,
    private eventBus: IEventBus
  ) {}

  async createUser(email: string): Promise<void> {
    const user = User.create(email);
    await this.userRepository.save(user);
    await this.eventBus.publish(new UserCreatedEvent(user));
  }
}
```

### CQRS 集成示例

```typescript
// 命令部分
interface CreateOrderCommand {
  userId: string;
  products: Array<{ productId: string; quantity: number }>;
}

class CreateOrderCommandHandler {
  async handle(command: CreateOrderCommand): Promise<void> {
    const order = Order.create({
      userId: command.userId,
      products: command.products,
    });

    await this.orderRepository.save(order);
    await this.eventBus.publish(order.getDomainEvents());
  }
}

// 查询部分
interface OrderSummaryDTO {
  orderId: string;
  totalAmount: number;
  status: string;
}

class OrderQueryService {
  async getOrderSummary(orderId: string): Promise<OrderSummaryDTO> {
    return this.readDatabase.orders.findOne({
      where: { id: orderId },
      select: ["id", "totalAmount", "status"],
    });
  }
}
```

## 最佳实践

1. **聚合设计**

   - 保持聚合尽可能小
   - 通过领域事件处理跨聚合的业务逻辑
   - 确保聚合边界的一致性

2. **领域事件使用**

   - 捕获重要的业务变更
   - 用于解耦聚合之间的依赖
   - 支持异步处理和系统集成

3. **仓储模式**
   - 为每个聚合根提供仓储
   - 封装持久化细节
   - 支持领域模型的重建

## 适用场景

✅ 适合：

- 复杂业务系统
- 需要长期演进的核心系统
- 团队需要与领域专家紧密协作

❌ 不适合：

- 简单的 CRUD 系统
- 技术导向的工具类系统
- 短期项目或原型验证

## 与其他架构的结合

DDD 可以与多种架构模式结合：

- CQRS（命令查询职责分离）
- 事件驱动架构
- 微服务架构
- 整洁架构
