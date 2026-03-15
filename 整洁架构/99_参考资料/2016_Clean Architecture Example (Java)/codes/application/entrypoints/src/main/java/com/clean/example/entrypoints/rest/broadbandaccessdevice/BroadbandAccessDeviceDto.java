package com.clean.example.entrypoints.rest.broadbandaccessdevice;

public class BroadbandAccessDeviceDto {

    private final String exchangeCode;
    private final String hostname;
    private final String serialNumber;
    private final String type;

    public BroadbandAccessDeviceDto(String exchangeCode, String hostname, String serialNumber, String type) {
        this.exchangeCode = exchangeCode;
        this.hostname = hostname;
        this.serialNumber = serialNumber;
        this.type = type;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public String getHostname() {
        return hostname;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getType() {
        return type;
    }
}
