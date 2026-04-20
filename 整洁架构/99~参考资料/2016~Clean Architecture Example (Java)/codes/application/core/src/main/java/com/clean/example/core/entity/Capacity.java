package com.clean.example.core.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Capacity {

    private boolean adsl;
    private boolean fibre;

    public Capacity(boolean adsl, boolean fibre) {
        this.fibre = fibre;
        this.adsl = adsl;
    }

    public boolean hasAdslCapacity() {
        return adsl;
    }

    public boolean hasFibreCapacity() {
        return fibre;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
