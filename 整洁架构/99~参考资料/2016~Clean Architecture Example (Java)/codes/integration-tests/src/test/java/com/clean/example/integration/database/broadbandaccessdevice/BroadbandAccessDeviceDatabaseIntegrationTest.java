package com.clean.example.integration.database.broadbandaccessdevice;

import com.clean.example.core.entity.BroadbandAccessDevice;
import com.clean.example.core.entity.DeviceType;
import com.clean.example.core.entity.Exchange;
import com.clean.example.dataproviders.database.broadbandaccessdevice.BroadbandAccessDeviceDatabaseDataProvider;
import com.clean.example.dataproviders.database.exchange.ExchangeDatabaseDataProvider;
import com.clean.example.integration.database.DatabaseIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.clean.example.core.entity.DeviceType.ADSL;
import static com.clean.example.core.entity.DeviceType.FIBRE;
import static org.assertj.core.api.Assertions.assertThat;

public class BroadbandAccessDeviceDatabaseIntegrationTest extends DatabaseIntegrationTest {

    public static final String EXCHANGE_CODE = "exch1";
    public static final String EXCHANGE_NAME = "Exchange for test";
    public static final String EXCHANGE_POSTCODE = "PostCode";

    @Autowired
    BroadbandAccessDeviceDatabaseDataProvider broadbandAccessDeviceDatabaseDataProvider;

    @Autowired
    ExchangeDatabaseDataProvider exchangeDatabaseDataProvider;

    @Before
    public void setUp() throws Exception {
        cleanUpDatabase();
        givenAnExistingExchange(EXCHANGE_CODE);
    }

    @Test
    public void getsAllDeviceHostnames() throws Exception {
        givenABroadbandAccessDevice("hostname1");
        givenABroadbandAccessDevice("hostname2");

        List<String> allDeviceHostnames = broadbandAccessDeviceDatabaseDataProvider.getAllDeviceHostnames();

        assertThat(allDeviceHostnames).containsOnly("hostname1", "hostname2");
    }

    @Test
    public void getsTheSerialNumberOfADevice() throws Exception {
        givenABroadbandAccessDevice("hostname1", "serialNumber1", FIBRE);

        String serialNumber = broadbandAccessDeviceDatabaseDataProvider.getSerialNumber("hostname1");

        assertThat(serialNumber).isEqualTo("serialNumber1");
    }

    @Test
    public void updatesSerialNumberOfADevice() throws Exception {
        givenABroadbandAccessDevice("hostname1", "serialNumber1", FIBRE);

        broadbandAccessDeviceDatabaseDataProvider.updateSerialNumber("hostname1", "newSerialNumber");

        String serialNumber = broadbandAccessDeviceDatabaseDataProvider.getSerialNumber("hostname1");
        assertThat(serialNumber).isEqualTo("newSerialNumber");
    }

    @Test
    public void getsDeviceDetails() throws Exception {
        givenABroadbandAccessDevice("hostname1", "serialNumber1", FIBRE, 123);

        BroadbandAccessDevice device = broadbandAccessDeviceDatabaseDataProvider.getDetails("hostname1");

        assertThat(device.getHostname()).isEqualTo("hostname1");
        assertThat(device.getSerialNumber()).isEqualTo("serialNumber1");
        assertThat(device.getType()).isEqualTo(FIBRE);
        assertThat(device.getAvailablePorts()).isEqualTo(123);
        assertThat(device.getExchange().getCode()).isEqualTo(EXCHANGE_CODE);
        assertThat(device.getExchange().getName()).isEqualTo(EXCHANGE_NAME);
        assertThat(device.getExchange().getPostCode()).isEqualTo(EXCHANGE_POSTCODE);
    }

    @Test
    public void returnsAvailablePortsOfAllDevicesInExchange() throws Exception {
        givenABroadbandAccessDevice(EXCHANGE_CODE, "hostname1", "serialNumber1", FIBRE, 123);
        givenABroadbandAccessDevice(EXCHANGE_CODE, "hostname2", "serialNumber2", ADSL, 456);

        givenAnExistingExchange("exch2");
        givenABroadbandAccessDevice("exch2", "hostname3", "serialNumber3", ADSL, 999);

        List<BroadbandAccessDevice> broadbandAccessDevices = broadbandAccessDeviceDatabaseDataProvider.getAvailablePortsOfAllDevicesInExchange(EXCHANGE_CODE);

        assertThat(broadbandAccessDevices).hasSize(2);
        thenTheDeviceHasAvailablePortsWithType(broadbandAccessDevices.get(0), 123, FIBRE);
        thenTheDeviceHasAvailablePortsWithType(broadbandAccessDevices.get(1), 456, ADSL);
    }

    private void givenAnExistingExchange(String exchangeCode) {
        exchangeDatabaseDataProvider.insert(new Exchange(exchangeCode, EXCHANGE_NAME, EXCHANGE_POSTCODE));
    }

    private void givenABroadbandAccessDevice(String hostname) {
        givenABroadbandAccessDevice(hostname, "aSerialNumber", FIBRE);
    }

    private void givenABroadbandAccessDevice(String hostname, String serialNumber, DeviceType deviceType) {
        broadbandAccessDeviceDatabaseDataProvider.insert(EXCHANGE_CODE, hostname, serialNumber, deviceType);
    }

    private void givenABroadbandAccessDevice(String hostname, String serialNumber, DeviceType deviceType, int availablePorts) {
        givenABroadbandAccessDevice(EXCHANGE_CODE, hostname, serialNumber, deviceType, availablePorts);
    }

    private void givenABroadbandAccessDevice(String exchangeCode, String hostname, String serialNumber, DeviceType deviceType, int availablePorts) {
        broadbandAccessDeviceDatabaseDataProvider.insert(exchangeCode, hostname, serialNumber, deviceType, availablePorts);
    }

    private void thenTheDeviceHasAvailablePortsWithType(BroadbandAccessDevice device, int expectedAvailablePorts, DeviceType expectedType) {
        assertThat(device.getAvailablePorts()).isEqualTo(expectedAvailablePorts);
        assertThat(device.getType()).isEqualTo(expectedType);
    }

}
