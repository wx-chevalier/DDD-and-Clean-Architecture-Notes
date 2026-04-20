package com.clean.example.dataproviders.network.broadbandaccessdevice;

import com.clean.example.dataproviders.network.deviceclient.DeviceClient;
import com.clean.example.dataproviders.network.deviceclient.DeviceConnectionTimeoutException;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BroadbandAccessDeviceNetworkDataProviderTest {

    DeviceClient deviceClient = mock(DeviceClient.class);

    BroadbandAccessDeviceNetworkDataProvider broadbandAccessDeviceNetworkDataProvider = new BroadbandAccessDeviceNetworkDataProvider(deviceClient);

    @Test
    public void findsTheSerialNumberOfADevice() throws Exception {
        givenADeviceWithHostnameAndSerialNumber("hostname1", "serialNumber1");

        String serialNumber = broadbandAccessDeviceNetworkDataProvider.getSerialNumber("hostname1");

        assertThat(serialNumber).isEqualTo("serialNumber1");
    }

    @Test
    public void returnsNullWhenDeviceIsNotResponding() throws Exception {
        givenADeviceIsNotResponding("hostname1");

        String serialNumber = broadbandAccessDeviceNetworkDataProvider.getSerialNumber("hostname1");

        assertThat(serialNumber).isNull();
    }

    private void givenADeviceWithHostnameAndSerialNumber(String hostname, String serialNumber) {
        when(deviceClient.getSerialNumber(hostname)).thenReturn(serialNumber);
    }

    private void givenADeviceIsNotResponding(String hostname) {
        when(deviceClient.getSerialNumber(hostname)).thenThrow(new DeviceConnectionTimeoutException());
    }
}