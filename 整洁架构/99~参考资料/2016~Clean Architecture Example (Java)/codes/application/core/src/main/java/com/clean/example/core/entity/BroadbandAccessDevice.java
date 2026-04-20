package com.clean.example.core.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BroadbandAccessDevice {

    private Exchange exchange;

    private String hostname;
    private String serialNumber;
    private DeviceType type;
    private int availablePorts;

    public BroadbandAccessDevice(String hostname, String serialNumber, DeviceType type) {
        this.hostname = hostname;
        this.serialNumber = serialNumber;
        this.type = type;
    }

    public String getHostname() {
        return hostname;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public DeviceType getType() {
        return type;
    }

    public int getAvailablePorts() {
        return availablePorts;
    }

    public void setAvailablePorts(int availablePorts) {
        this.availablePorts = availablePorts;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
