package com.clean.example.entrypoints.rest.exchange.capacity;

public class CapacityDto {

    private final boolean hasADSLCapacity;
    private final boolean hasFibreCapacity;

    public CapacityDto(boolean hasADSLCapacity, boolean hasFibreCapacity) {
        this.hasADSLCapacity = hasADSLCapacity;
        this.hasFibreCapacity = hasFibreCapacity;
    }

    public boolean getHasADSLCapacity() {
        return hasADSLCapacity;
    }

    public boolean getHasFibreCapacity() {
        return hasFibreCapacity;
    }
}
