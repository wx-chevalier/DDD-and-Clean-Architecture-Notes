package com.clean.example.endtoend.broadbandAccessDevice.reconcile;

import com.clean.example.businessrequirements.broadbandAccessDevice.reconcile.ReconcileBroadbandAccessDeviceAcceptanceTest;
import com.clean.example.core.entity.BroadbandAccessDevice;
import com.clean.example.core.entity.DeviceType;
import com.clean.example.core.entity.Exchange;
import com.clean.example.dataproviders.database.broadbandaccessdevice.BroadbandAccessDeviceDatabaseDataProvider;
import com.clean.example.dataproviders.database.exchange.ExchangeDatabaseDataProvider;
import com.clean.example.dataproviders.network.deviceclient.DeviceClient;
import com.clean.example.dataproviders.network.deviceclient.DeviceConnectionTimeoutException;
import com.clean.example.endtoend.EndToEndYatspecTest;
import com.clean.example.entrypoints.job.scheduledjob.ScheduledJob;
import com.googlecode.yatspec.junit.LinkingNote;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@LinkingNote(message = "Business Requirements: %s", links = {ReconcileBroadbandAccessDeviceAcceptanceTest.class})
public class ReconcileBroadbandAccessDeviceEndToEndTest extends EndToEndYatspecTest {
    public static final String EXCHANGE_CODE = "exCode";

    // this end-to-end test starts the application and fires up a real job, using a real database (but mocking third-parties)
    // it sits at the top of the testing pyramid for automated tests, as it's the most expensive type
    // DO:
    //   - it makes sure that all layers are integrated correctly and the critical user path works as expected
    //   - it mocks third-parties as they would make our build unreliable, and it's not really our job to test their code (we rely on a contract that has been tested separately)
    // DON't:
    //   - doesn't test all the details that could happen, they are tested in cheaper tests (e.g. unit, integration, acceptance)

    @Autowired
    ScheduledJob reconcileBroadbandAccessDeviceJob;

    @Autowired
    BroadbandAccessDeviceDatabaseDataProvider broadbandAccessDeviceDatabaseDataProvider;

    @Autowired
    DeviceClient mockDeviceClient;

    @Autowired
    ExchangeDatabaseDataProvider exchangeDatabaseDataProvider;

    @Before
    public void setUp() throws Exception {
        givenAnExistingExchange();
    }

    @Test
    public void jobAuditsSuccessesAndFailures() throws Exception {
        givenADeviceInTheModel(with(hostname("hostname1")), and(serialNumber("serial1")));
        givenADeviceInTheModel(with(hostname("hostname2")), and(serialNumber("serial2")));
        givenADeviceInTheModel(with(hostname("hostname3")), and(serialNumber("serial3")));

        givenADeviceInReality(with(hostname("hostname1")), and(serialNumber("serial1")));
        givenADeviceInReality(with(hostname("hostname2")), and(serialNumber("changed")));
        givenADeviceThatIsNotResponding(with(hostname("hostname3")));

        whenTheReconcileBroadbandAccessDeviceJobRuns();

        thenTheModelHasNotBeenUpdatedFor(hostname("hostname1"), with(serialNumber("serial1")));
        thenTheModelHasBeenUpdatedFor(hostname("hostname2"), to(serialNumber("changed")));
        thenTheModelHasNotBeenUpdatedFor(hostname("hostname3"), with(serialNumber("serial3")));
    }

    private void givenAnExistingExchange() {
        Exchange exchange = new Exchange(EXCHANGE_CODE, "exName", "postcode");
        exchangeDatabaseDataProvider.insert(exchange);
    }

    private void givenADeviceInTheModel(String hostname, String serialNumber) {
        broadbandAccessDeviceDatabaseDataProvider.insert(EXCHANGE_CODE, hostname, serialNumber, DeviceType.ADSL);
        log("Device " + hostname + " in model before reconciliation", "Hostname: " + hostname + ", Serial Number: " + serialNumber);
    }

    private void givenADeviceInReality(String hostname, String serialNumber) {
        when(mockDeviceClient.getSerialNumber(hostname)).thenReturn(serialNumber);
        log("Device " + hostname + " in reality", "Hostname: " + hostname + ", Serial Number: " + serialNumber);
    }

    private void givenADeviceThatIsNotResponding(String hostname) {
        when(mockDeviceClient.getSerialNumber(hostname)).thenThrow(new DeviceConnectionTimeoutException());
        log("Device " + hostname + " in reality", "Hostname: " + hostname + " -> NOT RESPONDING");
    }

    private String hostname(String hostname) {
        return hostname;
    }

    private String serialNumber(String serialNumber) {
        return serialNumber;
    }

    private void whenTheReconcileBroadbandAccessDeviceJobRuns() {
        reconcileBroadbandAccessDeviceJob.run();
    }

    private void thenTheModelHasBeenUpdatedFor(String hostname, String expectedSerialNumber) {
        BroadbandAccessDevice device = broadbandAccessDeviceDatabaseDataProvider.getDetails(hostname);
        assertThat(device.getSerialNumber()).isEqualTo(expectedSerialNumber);
        log("Device " + hostname + " in model after reconciliation", "Hostname: " + hostname + ", Serial Number: " + expectedSerialNumber);
    }

    private void thenTheModelHasNotBeenUpdatedFor(String hostname, String expectedSerialNumber) {
        BroadbandAccessDevice device = broadbandAccessDeviceDatabaseDataProvider.getDetails(hostname);
        assertThat(device.getSerialNumber()).isEqualTo(expectedSerialNumber);
        log("Device " + hostname + " in model after reconciliation", "Hostname: " + hostname + ", Serial Number: " + expectedSerialNumber);
    }

}
