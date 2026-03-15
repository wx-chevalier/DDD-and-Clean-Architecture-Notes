package com.clean.example.entrypoints.rest.exchange.capacity;

import com.clean.example.core.entity.Capacity;
import com.clean.example.core.usecase.exchange.getcapacity.ExchangeNotFoundException;
import com.clean.example.core.usecase.exchange.getcapacity.GetCapacityForExchangeUseCase;
import com.clean.example.entrypoints.rest.exception.NotFoundException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetCapacityForExchangeEndpointTest {

    private static final String EXCHANGE_CODE = "exch1";

    GetCapacityForExchangeUseCase getCapacityForExchangeUseCase = mock(GetCapacityForExchangeUseCase.class);

    GetCapacityForExchangeEndpoint getCapacityForExchangeEndpoint = new GetCapacityForExchangeEndpoint(getCapacityForExchangeUseCase);

    @Test
    public void returnsTheCapacityForAnExchange() throws Exception {
        givenThereIsCapacityForAnExchange();

        CapacityDto capacity = getCapacityForExchangeEndpoint.getCapacity(EXCHANGE_CODE);

        assertThat(capacity.getHasADSLCapacity()).isTrue();
        assertThat(capacity.getHasFibreCapacity()).isTrue();
    }

    @Test
    public void errorWhenDeviceIsNotFound() throws Exception {
        givenAnExchangeThatDoesNotExist();

        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> getCapacityForExchangeEndpoint.getCapacity(EXCHANGE_CODE));
    }

    private void givenThereIsCapacityForAnExchange() {
        when(getCapacityForExchangeUseCase.getCapacity(EXCHANGE_CODE)).thenReturn(new Capacity(true, true));
    }

    private void givenAnExchangeThatDoesNotExist() {
        when(getCapacityForExchangeUseCase.getCapacity(EXCHANGE_CODE)).thenThrow(new ExchangeNotFoundException());
    }

}