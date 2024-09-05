package com.clean.example.core.usecase.broadbandaccessdevice.reconcile;

import com.clean.example.core.usecase.job.OnFailure;
import com.clean.example.core.usecase.job.OnSuccess;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ReconcileBroadbandAccessDevicesUseCaseTest {

    GetAllDeviceHostnames getAllDevicesHostname = mock(GetAllDeviceHostnames.class);
    GetSerialNumberFromModel getSerialNumberFromModel = mock(GetSerialNumberFromModel.class);
    GetSerialNumberFromReality getSerialNumberFromReality = mock(GetSerialNumberFromReality.class);
    UpdateSerialNumberInModel updateSerialNumberInModel = mock(UpdateSerialNumberInModel.class);

    OnSuccess onSuccess = mock(OnSuccess.class);
    OnFailure onFailure = mock(OnFailure.class);

    ReconcileBroadbandAccessDevicesUseCase reconcileBroadbandAccessDevicesUseCase = new ReconcileBroadbandAccessDevicesUseCase(getAllDevicesHostname, getSerialNumberFromModel, getSerialNumberFromReality, updateSerialNumberInModel);

    @Test
    public void nothingToUpdateWhenModeAndRealityAreTheSame() throws Exception {
        givenThereIsADeviceWithHostname("hostname1");
        givenDeviceHasSerialNumberInModel("hostname1", "serialNumber1");
        givenDeviceHasSerialNumberInReality("hostname1", "serialNumber1");

        reconcileBroadbandAccessDevicesUseCase.reconcile(onSuccess, onFailure);

        thenNothingHasBeenUpdated();
    }

    @Test
    public void updatesSerialNumberWhenRealityIsDifferentFromModel() throws Exception {
        givenThereIsADeviceWithHostname("hostname1");
        givenDeviceHasSerialNumberInModel("hostname1", "serialNumber1");
        givenDeviceHasSerialNumberInReality("hostname1", "newSerialNumber");

        reconcileBroadbandAccessDevicesUseCase.reconcile(onSuccess, onFailure);

        thenTheDeviceHasBeenUpdated("hostname1", "newSerialNumber");
    }

    @Test
    public void updatesSerialNumberWhenDeviceDoesNotHaveOneInModel() throws Exception {
        givenThereIsADeviceWithHostname("hostname1");
        givenDeviceHasNoSerialNumberInModel("hostname1");
        givenDeviceHasSerialNumberInReality("hostname1", "newSerialNumber");

        reconcileBroadbandAccessDevicesUseCase.reconcile(onSuccess, onFailure);

        thenTheDeviceHasBeenUpdated("hostname1", "newSerialNumber");
    }

    @Test
    public void auditsASuccessWhenUpdatesTheModel() throws Exception {
        givenThereIsADeviceWithHostname("hostname1");
        givenDeviceHasSerialNumberInModel("hostname1", "serialNumber1");
        givenDeviceHasSerialNumberInReality("hostname1", "newSerialNumber");

        reconcileBroadbandAccessDevicesUseCase.reconcile(onSuccess, onFailure);

        thenASuccessHasBeenAudited();
    }

    @Test
    public void auditsFailureWhenSerialNumberFromRealityIsLongerThanUsual() throws Exception {
        givenThereIsADeviceWithHostname("hostname1");
        givenDeviceHasSerialNumberInModel("hostname1", "serialNumber1");
        givenDeviceHasSerialNumberInReality("hostname1", "longerThanAllowedSerialNumber");

        reconcileBroadbandAccessDevicesUseCase.reconcile(onSuccess, onFailure);

        thenNothingHasBeenUpdated();
        thenAFailureHasBeenAudited();
    }

    @Test
    public void auditsFailureWhenItCantRetrieveSerialNumberFromReality() throws Exception {
        givenThereIsADeviceWithHostname("hostname1");
        givenDeviceHasSerialNumberInModel("hostname1", "serialNumber1");
        givenThereIsAProblemRetrievingTheSerialNumberFromReality("hostname1");

        reconcileBroadbandAccessDevicesUseCase.reconcile(onSuccess, onFailure);

        thenNothingHasBeenUpdated();
        thenAFailureHasBeenAudited();
    }

    private void givenThereIsADeviceWithHostname(String... hostnames) {
        when(getAllDevicesHostname.getAllDeviceHostnames()).thenReturn(Arrays.asList(hostnames));
    }

    private void givenDeviceHasSerialNumberInModel(String hostname, String serialNumber) {
        when(getSerialNumberFromModel.getSerialNumber(hostname)).thenReturn(serialNumber);
    }

    private void givenDeviceHasNoSerialNumberInModel(String hostname) {
        when(getSerialNumberFromModel.getSerialNumber(hostname)).thenReturn(null);
    }

    private void givenDeviceHasSerialNumberInReality(String hostname, String serialNumber) {
        when(getSerialNumberFromReality.getSerialNumber(hostname)).thenReturn(serialNumber);
    }

    private void givenThereIsAProblemRetrievingTheSerialNumberFromReality(String hostname) {
        when(getSerialNumberFromReality.getSerialNumber(hostname)).thenReturn(null);
    }

    private void thenNothingHasBeenUpdated() {
        verify(updateSerialNumberInModel, never()).updateSerialNumber(anyString(), anyString());
    }

    private void thenTheDeviceHasBeenUpdated(String hostname, String serialNumber) {
        verify(updateSerialNumberInModel).updateSerialNumber(hostname, serialNumber);
    }

    private void thenASuccessHasBeenAudited() {
        verify(onSuccess).auditSuccess();
    }

    private void thenAFailureHasBeenAudited() {
        verify(onFailure).auditFailure();
    }

}