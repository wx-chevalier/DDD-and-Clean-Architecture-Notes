package com.clean.example.core.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Exchange {

    private final String code;
    private final String name;
    private final String postCode;

    public Exchange(String code, String name, String postCode) {
        this.code = code;
        this.name = name;
        this.postCode = postCode;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getPostCode() {
        return postCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
