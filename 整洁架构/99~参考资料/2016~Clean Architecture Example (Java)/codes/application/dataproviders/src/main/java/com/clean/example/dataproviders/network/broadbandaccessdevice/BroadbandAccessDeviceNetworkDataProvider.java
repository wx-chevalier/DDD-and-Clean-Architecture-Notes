package com.clean.example.dataproviders.network.broadbandaccessdevice;

import com.clean.example.core.usecase.broadbandaccessdevice.reconcile.GetSerialNumberFromReality;
import com.clean.example.dataproviders.network.deviceclient.DeviceClient;
import com.clean.example.dataproviders.network.deviceclient.DeviceConnectionTimeoutException;

public class BroadbandAccessDeviceNetworkDataProvider implements GetSerialNumberFromReality {

    private DeviceClient deviceClient;

    public BroadbandAccessDeviceNetworkDataProvider(DeviceClient deviceClient) {
        this.deviceClient = deviceClient;
    }

    @Override
    public String getSerialNumber(String hostname) {
        try {
            return deviceClient.getSerialNumber(hostname);
        } catch (DeviceConnectionTimeoutException e) {
            return null;
        }
    }

}
