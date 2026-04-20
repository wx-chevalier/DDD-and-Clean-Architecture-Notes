package com.clean.example.configuration;

import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.GetBroadbandAccessDeviceDetailsUseCase;
import com.clean.example.core.usecase.exchange.getcapacity.GetCapacityForExchangeUseCase;
import com.clean.example.entrypoints.rest.broadbandaccessdevice.GetBroadbandAccessDeviceEndpoint;
import com.clean.example.entrypoints.rest.exchange.capacity.GetCapacityForExchangeEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointConfiguration {

    @Bean
    public GetBroadbandAccessDeviceEndpoint getBroadbandAccessDeviceEndpoint(GetBroadbandAccessDeviceDetailsUseCase getBroadbandAccessDeviceDetailsUseCase) {
        return new GetBroadbandAccessDeviceEndpoint(getBroadbandAccessDeviceDetailsUseCase);
    }

    @Bean
    public GetCapacityForExchangeEndpoint getCapacityForExchangeEndpoint(GetCapacityForExchangeUseCase getCapacityForExchangeUseCase) {
        return new GetCapacityForExchangeEndpoint(getCapacityForExchangeUseCase);
    }

}
