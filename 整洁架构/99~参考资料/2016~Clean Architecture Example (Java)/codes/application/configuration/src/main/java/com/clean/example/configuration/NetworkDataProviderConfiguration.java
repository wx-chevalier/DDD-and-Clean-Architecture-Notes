package com.clean.example.configuration;

import com.clean.example.dataproviders.network.broadbandaccessdevice.BroadbandAccessDeviceNetworkDataProvider;
import com.clean.example.dataproviders.network.deviceclient.DeviceClient;
import com.clean.example.dataproviders.network.deviceclient.FakeDeviceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NetworkDataProviderConfiguration {

    @Bean
    public DeviceClient deviceClient() {
        return new FakeDeviceClient(); // use a fake one for the sake of this example project
    }

    @Bean
    public BroadbandAccessDeviceNetworkDataProvider broadbandAccessDeviceNetworkDataProvider(DeviceClient deviceClient) {
        return new BroadbandAccessDeviceNetworkDataProvider(deviceClient);
    }

}
