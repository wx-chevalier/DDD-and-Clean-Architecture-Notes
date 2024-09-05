package com.clean.example.core.usecase.exchange.getcapacity;

import com.clean.example.core.entity.BroadbandAccessDevice;

import java.util.List;

public interface GetAvailablePortsOfAllDevicesInExchange {

    List<BroadbandAccessDevice> getAvailablePortsOfAllDevicesInExchange(String exchangeCode);

}
