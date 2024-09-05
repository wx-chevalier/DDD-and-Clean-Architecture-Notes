package com.clean.example.entrypoints.rest.exchange.capacity;

import com.clean.example.core.entity.Capacity;
import com.clean.example.core.usecase.exchange.getcapacity.ExchangeNotFoundException;
import com.clean.example.core.usecase.exchange.getcapacity.GetCapacityForExchangeUseCase;
import com.clean.example.entrypoints.rest.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class GetCapacityForExchangeEndpoint {

    public static final String API_PATH = "/exchange/{exchangeCode}/capacity";

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCapacityForExchangeEndpoint.class);

    private GetCapacityForExchangeUseCase getCapacityForExchangeUseCase;

    public GetCapacityForExchangeEndpoint(GetCapacityForExchangeUseCase getCapacityForExchangeUseCase) {
        this.getCapacityForExchangeUseCase = getCapacityForExchangeUseCase;
    }

    @RequestMapping(value = API_PATH, method = GET)
    public CapacityDto getCapacity(@PathVariable String exchangeCode) {
        LOGGER.info("Retrieving capacity for exchange: {}", exchangeCode);
        try {
            Capacity capacity = getCapacityForExchangeUseCase.getCapacity(exchangeCode);
            return toDto(capacity);
        } catch (ExchangeNotFoundException e) {
            LOGGER.info("Exchange not found: {}", exchangeCode);
            throw new NotFoundException();
        }
    }

    private CapacityDto toDto(Capacity capacity) {
        return new CapacityDto(capacity.hasAdslCapacity(), capacity.hasFibreCapacity());
    }
}
