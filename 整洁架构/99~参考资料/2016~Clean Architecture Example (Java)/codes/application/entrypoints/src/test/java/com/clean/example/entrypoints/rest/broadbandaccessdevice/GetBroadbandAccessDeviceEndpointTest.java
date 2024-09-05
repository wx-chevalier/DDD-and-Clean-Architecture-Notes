package com.clean.example.entrypoints.rest.broadbandaccessdevice;

import com.clean.example.core.entity.BroadbandAccessDevice;
import com.clean.example.core.entity.DeviceType;
import com.clean.example.core.entity.Exchange;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.DeviceNotFoundException;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.GetBroadbandAccessDeviceDetailsUseCase;
import com.clean.example.entrypoints.rest.exception.NotFoundException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetBroadbandAccessDeviceEndpointTest {

    private static final String HOSTNAME = "device1.exchange.com";
    private static final String SERIAL_NUMBER = "serialNumber1";
    private static final DeviceType DEVICE_TYPE = DeviceType.ADSL;
    private static final String EXCHANGE_CODE = "exchangeCode";
    private static final String EXCHANGE_NAME = "exchangeName";
    private static final String EXCHANGE_POSTCODE = "exchangePostcode";

    GetBroadbandAccessDeviceDetailsUseCase getBroadbandAccessDeviceDetailsUseCase = mock(GetBroadbandAccessDeviceDetailsUseCase.class);

    GetBroadbandAccessDeviceEndpoint getBroadbandAccessDeviceEndpoint = new GetBroadbandAccessDeviceEndpoint(getBroadbandAccessDeviceDetailsUseCase);

    @Test
    public void returnsTheDetailsOfTheDevice() throws Exception {
        givenADeviceExists();

        BroadbandAccessDeviceDto deviceDto = getBroadbandAccessDeviceEndpoint.getDetails(HOSTNAME);

        assertThat(deviceDto.getExchangeCode()).isEqualTo(EXCHANGE_CODE);
        assertThat(deviceDto.getHostname()).isEqualTo(HOSTNAME);
        assertThat(deviceDto.getSerialNumber()).isEqualTo(SERIAL_NUMBER);
        assertThat(deviceDto.getType()).isEqualTo(DEVICE_TYPE.name());
    }

    @Test
    public void errorWhenDeviceIsNotFound() throws Exception {
        givenADeviceDoesNotExist();

        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> getBroadbandAccessDeviceEndpoint.getDetails(HOSTNAME));
    }

    private void givenADeviceExists() {
        BroadbandAccessDevice device = new BroadbandAccessDevice(HOSTNAME, SERIAL_NUMBER, DEVICE_TYPE);
        Exchange exchange = new Exchange(EXCHANGE_CODE, EXCHANGE_NAME, EXCHANGE_POSTCODE);
        device.setExchange(exchange);
        when(getBroadbandAccessDeviceDetailsUseCase.getDeviceDetails(HOSTNAME)).thenReturn(device);
    }

    private void givenADeviceDoesNotExist() {
        when(getBroadbandAccessDeviceDetailsUseCase.getDeviceDetails(HOSTNAME)).thenThrow(new DeviceNotFoundException());
    }

}