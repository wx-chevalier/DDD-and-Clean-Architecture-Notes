package com.clean.example.businessrequirements.exchange.getcapacity;

import com.clean.example.core.entity.BroadbandAccessDevice;
import com.clean.example.core.entity.Capacity;
import com.clean.example.core.entity.DeviceType;
import com.clean.example.core.usecase.exchange.getcapacity.DoesExchangeExist;
import com.clean.example.core.usecase.exchange.getcapacity.GetAvailablePortsOfAllDevicesInExchange;
import com.clean.example.core.usecase.exchange.getcapacity.GetCapacityForExchangeUseCase;
import com.clean.example.endtoend.exchange.getcapacity.GetCapacityForExchangeEndToEndTest;
import com.clean.example.yatspec.YatspecTest;
import com.googlecode.yatspec.junit.LinkingNote;
import com.googlecode.yatspec.junit.Notes;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.clean.example.core.entity.DeviceType.ADSL;
import static com.clean.example.core.entity.DeviceType.FIBRE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Notes("Calculates the remaining capacity in an exchange in order to:\n" +
        "- decide what products can be sold to customers (ADSL, FIBRE, etc.)\n" +
        "- monitor remaining capacity and increase it when necessary")
@LinkingNote(message = "End to End test: %s", links = {GetCapacityForExchangeEndToEndTest.class})
public class GetCapacityForExchangeAcceptanceTest extends YatspecTest {

    private static final String EXCHANGE_CODE = "exch1";
    private static final String DEFAULT_SERIAL_NUMBER = "defaultSerialNumber";

    DoesExchangeExist doesExchangeExist = mock(DoesExchangeExist.class);
    GetAvailablePortsOfAllDevicesInExchange getAvailablePortsOfAllDevicesInExchange = mock(GetAvailablePortsOfAllDevicesInExchange.class);

    GetCapacityForExchangeUseCase getCapacityForExchangeUseCase = new GetCapacityForExchangeUseCase(doesExchangeExist, getAvailablePortsOfAllDevicesInExchange);
    Capacity capacity;

    @Before
    public void setUp() throws Exception {
        givenExchangeExists();
    }

    @Test
    public void hasAdslCapacityWhenThereAreAtLeast5AdslPortsAvailableAcrossAllDevices() throws Exception {
        givenSomeAdslDevicesInTheExchangeWithMoreThan5AdslPortsAvailable();

        whenTheCapacityForTheExchangeIsRequested();

        thenThereIsAdslCapacity();
    }

    @Test
    public void noAdslCapacityWhenThereAreLessThan5AdslPortsAvailable() throws Exception {
        givenSomeAdslDevicesInTheExchangeWithLessThan5AdslPortsAvailable();

        whenTheCapacityForTheExchangeIsRequested();

        thenThereIsNoAdslCapacity();
    }

    @Test
    public void noAdslCapacityWhenThereAreNoAdslDevices() throws Exception {
        givenThereAreNoAdslDevicesInTheExchance();

        whenTheCapacityForTheExchangeIsRequested();

        thenThereIsNoAdslCapacity();
    }

    @Test
    public void hasFibreCapacityWhenThereAreAtLeast5FibrePortsAvailable() throws Exception {
        givenSomeFibreDevicesInTheExchangeWithMoreThan5FibrePortsAvailable();

        whenTheCapacityForTheExchangeIsRequested();

        thenThereIsFibreCapacity();
    }

    @Test
    public void noFibreCapacityWhenThereAreLessThan5FibrePortsAvailable() throws Exception {
        givenSomeFibreDevicesInTheExchangeWithLessThan5FibrePortsAvailable();

        whenTheCapacityForTheExchangeIsRequested();

        thenThereIsNoFibreCapacity();
    }

    @Test
    public void noFibreCapacityWhenThereAreNoFibreDevices() throws Exception {
        givenThereAreNoFibreDevicesInTheExchance();

        whenTheCapacityForTheExchangeIsRequested();

        thenThereIsNoFibreCapacity();
    }

    // GIVENs
    private void givenExchangeExists() {
        when(doesExchangeExist.doesExchangeExist(EXCHANGE_CODE)).thenReturn(true);
    }

    private void givenSomeAdslDevicesInTheExchangeWithMoreThan5AdslPortsAvailable() {
        BroadbandAccessDevice device1 = device("device1", ADSL, 2);
        BroadbandAccessDevice device2 = device("device2", ADSL, 2);
        BroadbandAccessDevice device3 = device("device3", ADSL, 1);
        when(getAvailablePortsOfAllDevicesInExchange.getAvailablePortsOfAllDevicesInExchange(EXCHANGE_CODE)).thenReturn(Arrays.asList(device1, device2, device3));
    }

    private void givenSomeAdslDevicesInTheExchangeWithLessThan5AdslPortsAvailable() {
        BroadbandAccessDevice device1 = device("device1", ADSL, 1);
        BroadbandAccessDevice device2 = device("device2", ADSL, 2);
        BroadbandAccessDevice device3 = device("device3", ADSL, 1);
        when(getAvailablePortsOfAllDevicesInExchange.getAvailablePortsOfAllDevicesInExchange(EXCHANGE_CODE)).thenReturn(Arrays.asList(device1, device2, device3));
    }

    private void givenThereAreNoAdslDevicesInTheExchance() {
        BroadbandAccessDevice device1 = device("device1", FIBRE, 10);
        when(getAvailablePortsOfAllDevicesInExchange.getAvailablePortsOfAllDevicesInExchange(EXCHANGE_CODE)).thenReturn(Arrays.asList(device1));
    }

    private void givenSomeFibreDevicesInTheExchangeWithMoreThan5FibrePortsAvailable() {
        BroadbandAccessDevice device1 = device("device1", FIBRE, 2);
        BroadbandAccessDevice device2 = device("device2", FIBRE, 2);
        BroadbandAccessDevice device3 = device("device3", FIBRE, 1);
        when(getAvailablePortsOfAllDevicesInExchange.getAvailablePortsOfAllDevicesInExchange(EXCHANGE_CODE)).thenReturn(Arrays.asList(device1, device2, device3));
    }

    private void givenSomeFibreDevicesInTheExchangeWithLessThan5FibrePortsAvailable() {
        BroadbandAccessDevice device1 = device("device1", FIBRE, 1);
        BroadbandAccessDevice device2 = device("device2", FIBRE, 2);
        BroadbandAccessDevice device3 = device("device3", FIBRE, 1);
        when(getAvailablePortsOfAllDevicesInExchange.getAvailablePortsOfAllDevicesInExchange(EXCHANGE_CODE)).thenReturn(Arrays.asList(device1, device2, device3));
    }

    private void givenThereAreNoFibreDevicesInTheExchance() {
        BroadbandAccessDevice device1 = device("device1", ADSL, 10);
        when(getAvailablePortsOfAllDevicesInExchange.getAvailablePortsOfAllDevicesInExchange(EXCHANGE_CODE)).thenReturn(Arrays.asList(device1));
    }

    private BroadbandAccessDevice device(String hostname, DeviceType type, int availablePorts) {
        BroadbandAccessDevice device = new BroadbandAccessDevice(hostname, DEFAULT_SERIAL_NUMBER, type);
        device.setAvailablePorts(availablePorts);
        log(type + " Device: " + hostname, "Exchange code: " + EXCHANGE_CODE + "\nHostname: " + hostname + "\nType: " + type + "\nAvailable Ports: " + availablePorts);
        return device;
    }

    // WHENs
    private void whenTheCapacityForTheExchangeIsRequested() {
        capacity = getCapacityForExchangeUseCase.getCapacity(EXCHANGE_CODE);
        log("Capacity for exchange: " + EXCHANGE_CODE, "ADSL Capacity: " + capacity.hasAdslCapacity() + "\nFibre Capacity: " + capacity.hasFibreCapacity());
    }

    // THENs
    private void thenThereIsAdslCapacity() {
        assertThat(capacity.hasAdslCapacity()).isTrue();
    }

    private void thenThereIsNoAdslCapacity() {
        assertThat(capacity.hasAdslCapacity()).isFalse();
    }

    private void thenThereIsFibreCapacity() {
        assertThat(capacity.hasFibreCapacity()).isTrue();
    }

    private void thenThereIsNoFibreCapacity() {
        assertThat(capacity.hasFibreCapacity()).isFalse();
    }

}
