package com.clean.example.businessrequirements.broadbandAccessDevice.reconcile;

import com.clean.example.core.usecase.broadbandaccessdevice.reconcile.*;
import com.clean.example.core.usecase.job.OnFailure;
import com.clean.example.core.usecase.job.OnSuccess;
import com.clean.example.endtoend.broadbandAccessDevice.reconcile.ReconcileBroadbandAccessDeviceEndToEndTest;
import com.clean.example.yatspec.YatspecTest;
import com.googlecode.yatspec.junit.LinkingNote;
import com.googlecode.yatspec.junit.Notes;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@Notes("Reconciles information about Broadband Access Devices between what is stored in our model and the data that comes back from the actual device")
@LinkingNote(message = "End to End test: %s", links = {ReconcileBroadbandAccessDeviceEndToEndTest.class})
public class ReconcileBroadbandAccessDeviceAcceptanceTest extends YatspecTest {

    // This acceptance test documents the business requirements, and demonstrates that they are fullfilled
    // DO:
    //   - it tests all the details that are considered important from a business point of view
    // DON'T
    //   - doesn't test technical details (e.g. the web layer, or the fact that it's a scheduled job)
    //   - doesn't test low level details (e.g. weird exception scenarios, null values, etc.). they are left to the unit tests

    private static final String HOSTNAME_1 = "device1.hostname.com";
    private static final String HOSTNAME_2 = "device2.hostname.com";
    private static final String SERIAL_NUMBER_1 = "originalSerialNumber1";
    private static final String SERIAL_NUMBER_2 = "originalSerialNumber2";
    private static final String NEW_SERIAL_NUMBER_1 = "newSerialNumber1";
    private static final String INVALID_SERIAL_NUMBER = "invalidSerialNumberBecauseItIsTooLong";

    GetAllDeviceHostnames getAllDeviceHostnames = mock(GetAllDeviceHostnames.class);
    GetSerialNumberFromModel getSerialNumberFromModel = mock(GetSerialNumberFromModel.class);
    GetSerialNumberFromReality getSerialNumberFromReality = mock(GetSerialNumberFromReality.class);
    UpdateSerialNumberInModel updateSerialNumberInModel = mock(UpdateSerialNumberInModel.class);
    OnSuccess onSuccess = mock(OnSuccess.class);
    OnFailure onFailure = mock(OnFailure.class);
    ReconcileBroadbandAccessDevicesUseCase reconcileBroadbandAccessDevicesUseCase = new ReconcileBroadbandAccessDevicesUseCase(getAllDeviceHostnames, getSerialNumberFromModel, getSerialNumberFromReality, updateSerialNumberInModel);

    @Test
    public void nothingToUpdateWhenModelAndRealityAreTheSame() throws Exception {
        givenSomeDevicesInTheModel();
        givenSomeDevicesInRealityWithTheSameSerialNumber();

        whenTheDeviceIsReconciled();

        thenTheDevicesInTheModelAreNotUpdated();
        thenNoSuccessOrFailureIsAudited();
    }

    @Test
    public void updatesModelWhenRealityHasChanged() throws Exception {
        givenSomeDevicesInTheModel();
        givenADeviceInRealityWithANewSerialNumber();

        whenTheDeviceIsReconciled();

        thenTheDeviceInTheModelIsUpdatedWithTheNewSerialNumber();
        thenASuccessIsAudited();
    }

    @Test
    public void recordsAnErrorWhenRealityInformationIsInvalid() throws Exception {
        givenSomeDevicesInTheModel();
        givenADeviceInRealityWithAnInvalidSerialNumber();

        whenTheDeviceIsReconciled();

        thenTheDevicesInTheModelAreNotUpdated();
        thenAnErrorIsAudited();
    }

    @Test
    public void recordsAnErrorWhenTheCommunicationWithTheDeviceHasProblems() throws Exception {
        givenSomeDevicesInTheModel();
        givenADeviceInRealityThatIsNotResponding();

        whenTheDeviceIsReconciled();

        thenTheDevicesInTheModelAreNotUpdated();
        thenAnErrorIsAudited();
    }

    // GIVENs
    private void givenSomeDevicesInTheModel() {
        when(getAllDeviceHostnames.getAllDeviceHostnames()).thenReturn(Arrays.asList(HOSTNAME_1, HOSTNAME_2));

        when(getSerialNumberFromModel.getSerialNumber(HOSTNAME_1)).thenReturn(SERIAL_NUMBER_1);
        when(getSerialNumberFromModel.getSerialNumber(HOSTNAME_2)).thenReturn(SERIAL_NUMBER_2);

        log("Devices in model before reconciliation",
                "Hostname: [" + HOSTNAME_1 + "], Serial Number: [" + SERIAL_NUMBER_1 + "]\n" +
                "Hostname: [" + HOSTNAME_2 + "], Serial Number: [" + SERIAL_NUMBER_2 + "]");
    }

    private void givenSomeDevicesInRealityWithTheSameSerialNumber() {
        when(getSerialNumberFromReality.getSerialNumber(HOSTNAME_1)).thenReturn(SERIAL_NUMBER_1);
        when(getSerialNumberFromReality.getSerialNumber(HOSTNAME_2)).thenReturn(SERIAL_NUMBER_2);

        log("Devices in reality",
                "Hostname: [" + HOSTNAME_1 + "], Serial Number: [" + SERIAL_NUMBER_1 + "]\n" +
                "Hostname: [" + HOSTNAME_2 + "], Serial Number: [" + SERIAL_NUMBER_2 + "]");
    }

    private void givenADeviceInRealityWithANewSerialNumber() {
        when(getSerialNumberFromReality.getSerialNumber(HOSTNAME_1)).thenReturn(NEW_SERIAL_NUMBER_1);
        when(getSerialNumberFromReality.getSerialNumber(HOSTNAME_2)).thenReturn(SERIAL_NUMBER_2);

        log("Devices in reality",
                "Hostname: [" + HOSTNAME_1 + "], Serial Number: [" + NEW_SERIAL_NUMBER_1 + "]\n" +
                "Hostname: [" + HOSTNAME_2 + "], Serial Number: [" + SERIAL_NUMBER_2 + "]");
    }

    private void givenADeviceInRealityWithAnInvalidSerialNumber() {
        when(getSerialNumberFromReality.getSerialNumber(HOSTNAME_1)).thenReturn(INVALID_SERIAL_NUMBER);
        when(getSerialNumberFromReality.getSerialNumber(HOSTNAME_2)).thenReturn(SERIAL_NUMBER_2);

        log("Devices in reality",
                "Hostname: [" + HOSTNAME_1 + "], Serial Number: [" + INVALID_SERIAL_NUMBER + "]\n" +
                "Hostname: [" + HOSTNAME_2 + "], Serial Number: [" + SERIAL_NUMBER_2 + "]");
    }

    private void givenADeviceInRealityThatIsNotResponding() {
        when(getSerialNumberFromReality.getSerialNumber(HOSTNAME_1)).thenReturn(null);
        when(getSerialNumberFromReality.getSerialNumber(HOSTNAME_2)).thenReturn(SERIAL_NUMBER_2);

        log("Devices in reality",
                "Hostname: [" + HOSTNAME_1 + "] -> Device not responding\n" +
                "Hostname: [" + HOSTNAME_2 + "], Serial Number: [" + SERIAL_NUMBER_2 + "]");
    }

    // WHENs
    private void whenTheDeviceIsReconciled() {
        reconcileBroadbandAccessDevicesUseCase.reconcile(onSuccess, onFailure);
    }

    // THENs
    private void thenTheDevicesInTheModelAreNotUpdated() {
        verify(updateSerialNumberInModel, never()).updateSerialNumber(anyString(), anyString());
        log("Devices in model after reconciliation",
                "Hostname: [" + HOSTNAME_1 + "], Serial Number: [" + SERIAL_NUMBER_1 + "]\n" +
                "Hostname: [" + HOSTNAME_2 + "], Serial Number: [" + SERIAL_NUMBER_2 + "]");
    }

    private void thenTheDeviceInTheModelIsUpdatedWithTheNewSerialNumber() {
        verify(updateSerialNumberInModel).updateSerialNumber(HOSTNAME_1, NEW_SERIAL_NUMBER_1);
        verify(updateSerialNumberInModel, never()).updateSerialNumber(eq(HOSTNAME_2), anyString());

        log("Devices in model after reconciliation",
                "Hostname: [" + HOSTNAME_1 + "], Serial Number: [" + NEW_SERIAL_NUMBER_1 + "]\n" +
                "Hostname: [" + HOSTNAME_2 + "], Serial Number: [" + SERIAL_NUMBER_2 + "]");
    }

    private void thenNoSuccessOrFailureIsAudited() {
        verify(onSuccess, never()).auditSuccess();
        verify(onFailure, never()).auditFailure();
        log("Successes and Failures", "Successes: 0\nFailures: 0");
    }

    private void thenASuccessIsAudited() {
        verify(onSuccess).auditSuccess();
        verify(onFailure, never()).auditFailure();
        log("Successes and Failures", "Successes: 1\nFailures: 0");
    }

    private void thenAnErrorIsAudited() {
        verify(onSuccess, never()).auditSuccess();
        verify(onFailure).auditFailure();
        log("Successes and Failures", "Successes: 0\nFailures: 1");
    }

}
