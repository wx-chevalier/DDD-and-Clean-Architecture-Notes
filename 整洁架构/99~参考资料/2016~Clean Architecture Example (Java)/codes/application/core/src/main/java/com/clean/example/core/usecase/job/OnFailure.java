package com.clean.example.core.usecase.job;

@FunctionalInterface
public interface OnFailure {

    void auditFailure();

}
