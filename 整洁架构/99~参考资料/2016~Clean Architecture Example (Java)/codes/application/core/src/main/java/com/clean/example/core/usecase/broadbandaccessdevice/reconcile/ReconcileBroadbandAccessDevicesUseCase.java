package com.clean.example.core.usecase.broadbandaccessdevice.reconcile;

import com.clean.example.core.usecase.job.OnFailure;
import com.clean.example.core.usecase.job.OnSuccess;

import java.util.List;

public class ReconcileBroadbandAccessDevicesUseCase {

    public static final int MAX_SERIAL_NUMBER_LENGTH = 25;

    private final GetAllDeviceHostnames getAllDeviceHostnames;
    private final GetSerialNumberFromModel getSerialNumberFromModel;
    private final GetSerialNumberFromReality getSerialNumberFromReality;
    private final UpdateSerialNumberInModel updateSerialNumberInModel;

    public ReconcileBroadbandAccessDevicesUseCase(GetAllDeviceHostnames getAllDeviceHostnames, GetSerialNumberFromModel getSerialNumberFromModel, GetSerialNumberFromReality getSerialNumberFromReality, UpdateSerialNumberInModel updateSerialNumberInModel) {
        this.getAllDeviceHostnames = getAllDeviceHostnames;
        this.getSerialNumberFromModel = getSerialNumberFromModel;
        this.getSerialNumberFromReality = getSerialNumberFromReality;
        this.updateSerialNumberInModel = updateSerialNumberInModel;
    }

    public void reconcile(OnSuccess onSuccess, OnFailure onFailure) {
        List<String> allDeviceHostnames = getAllDeviceHostnames.getAllDeviceHostnames();

        for (String hostname : allDeviceHostnames) {

            String serialNumberInModel = getSerialNumberFromModel.getSerialNumber(hostname);

            String serialNumberFromReality = getSerialNumberFromReality.getSerialNumber(hostname);
            if(noSerialNumberInReality(serialNumberFromReality) || isInvalid(serialNumberFromReality)) {
                onFailure.auditFailure();
                continue;
            }

            if(noSerialNumberInModel(serialNumberInModel) || serialNumberIsDifferentInReality(serialNumberInModel, serialNumberFromReality)) {
                updateSerialNumberInModel.updateSerialNumber(hostname, serialNumberFromReality);
                onSuccess.auditSuccess();
            }

        }
    }

    private boolean noSerialNumberInModel(String serialNumberInModel) {
        return serialNumberInModel == null;
    }

    private boolean noSerialNumberInReality(String serialNumberFromReality) {
        return serialNumberFromReality == null;
    }

    private boolean serialNumberIsDifferentInReality(String serialNumberInModel, String serialNumberFromReality) {
        return !serialNumberInModel.equals(serialNumberFromReality);
    }

    private boolean isInvalid(String serialNumberFromReality) {
        return serialNumberFromReality.length() > MAX_SERIAL_NUMBER_LENGTH;
    }

}
