package com.clean.example.dataproviders.network.deviceclient;

public class FakeDeviceClient implements DeviceClient {

    @Override
    public String getSerialNumber(String hostname) {
        return "default serial number";
    }

}
