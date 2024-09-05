package com.clean.example.dataproviders.database.broadbandaccessdevice;

import com.clean.example.core.entity.BroadbandAccessDevice;
import com.clean.example.core.entity.DeviceType;
import org.junit.Test;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.clean.example.core.entity.DeviceType.ADSL;
import static com.clean.example.core.entity.DeviceType.FIBRE;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class BroadbandAccessDeviceDatabaseDataProviderTest {

    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    BroadbandAccessDeviceDatabaseDataProvider broadbandAccessDeviceDatabaseDataProvider = new BroadbandAccessDeviceDatabaseDataProvider(jdbcTemplate);

    @Test
    public void returnsEmptyListWhenThereAreNoDevices() throws Exception {
        givenThereAreNoDevices();

        List<String> allDeviceHostnames = broadbandAccessDeviceDatabaseDataProvider.getAllDeviceHostnames();

        assertThat(allDeviceHostnames).isEmpty();
    }

    @Test
    public void returnsTheHostnameOfAllDevices() throws Exception {
        thereThereAreDevices("hostname1", "hostname2", "hostname3");

        List<String> allDeviceHostnames = broadbandAccessDeviceDatabaseDataProvider.getAllDeviceHostnames();

        assertThat(allDeviceHostnames).containsOnly("hostname1", "hostname2", "hostname3");
    }

    @Test
    public void returnsNullSerialNumberForADeviceThatDoesNotExist() throws Exception {
        givenDeviceDoesNotExist("hostname1");

        String serialNumber = broadbandAccessDeviceDatabaseDataProvider.getSerialNumber("hostname1");

        assertThat(serialNumber).isNull();
    }

    @Test
    public void returnsSerialNumberOfADevice() throws Exception {
        givenDeviceHasSerialNumber("hostname1", "serialNumber1");

        String serialNumber = broadbandAccessDeviceDatabaseDataProvider.getSerialNumber("hostname1");

        assertThat(serialNumber).isEqualTo("serialNumber1");
    }

    @Test
    public void updatesTheSerialNumberOfADevice() throws Exception {
        broadbandAccessDeviceDatabaseDataProvider.updateSerialNumber("hostname1", "newSerialNumber");

        verify(jdbcTemplate).update(anyString(), eq("newSerialNumber"), eq("hostname1"));
    }

    @Test
    public void returnsTheDetailsOfADevice() throws Exception {
        givenThereIsADevice("exchangeCode", "exchangeName", "exchangePostcode", "hostname", "serialNumber", ADSL, 123);

        BroadbandAccessDevice device = broadbandAccessDeviceDatabaseDataProvider.getDetails("hostname");

        assertThat(device.getHostname()).isEqualTo("hostname");
        assertThat(device.getSerialNumber()).isEqualTo("serialNumber");
        assertThat(device.getType()).isEqualTo(ADSL);
        assertThat(device.getAvailablePorts()).isEqualTo(123);
        assertThat(device.getExchange().getCode()).isEqualTo("exchangeCode");
        assertThat(device.getExchange().getName()).isEqualTo("exchangeName");
        assertThat(device.getExchange().getPostCode()).isEqualTo("exchangePostcode");
    }

    @Test
    public void returnsNullWhenTheDeviceIsNotFound() throws Exception {
        givenThereIsntADevice();

        BroadbandAccessDevice device = broadbandAccessDeviceDatabaseDataProvider.getDetails("hostname");

        assertThat(device).isNull();
    }

    @Test
    public void returnsAvailablePortsOfAllDevicesInAnExchange() throws Exception {
        Map<String, Object> device1 = detailsForDevice("exchangeCode", "exchangeName", "exchangePostcode1", "hostname1", "serialNumber1", ADSL, 2);
        Map<String, Object> device2 = detailsForDevice("exchangeCode", "exchangeName", "exchangePostcode2", "hostname2", "serialNumber2", FIBRE, 4);
        when(jdbcTemplate.queryForList(anyString(), eq("exchangeCode"))).thenReturn(asList(device1, device2));

        List<BroadbandAccessDevice> broadbandAccessDevices = broadbandAccessDeviceDatabaseDataProvider.getAvailablePortsOfAllDevicesInExchange("exchangeCode");

        assertThat(broadbandAccessDevices).hasSize(2);
        thenTheDeviceHasAvailablePortsWithType(broadbandAccessDevices.get(0), 2, ADSL);
        thenTheDeviceHasAvailablePortsWithType(broadbandAccessDevices.get(1), 4, FIBRE);
    }

    private void givenThereAreNoDevices() {
        when(jdbcTemplate.queryForList(anyString(), eq(String.class))).thenReturn(new ArrayList<>());
    }

    private void givenDeviceDoesNotExist(String hostname) {
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), eq(hostname))).thenThrow(new IncorrectResultSizeDataAccessException(1));
    }

    private void givenDeviceHasSerialNumber(String hostname, String serialNumber) {
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), eq(hostname))).thenReturn(serialNumber);
    }

    private void givenThereIsADevice(String exchangeCode, String exchangeName, String exchangePostcode, String hostname, String serialNumber, DeviceType type, int availablePorts) {
        Map<String, Object> details = detailsForDevice(exchangeCode, exchangeName, exchangePostcode, hostname, serialNumber, type, availablePorts);
        when(jdbcTemplate.queryForMap(anyString(), anyVararg())).thenReturn(details);
    }

    private Map<String, Object> detailsForDevice(String exchangeCode, String exchangeName, String exchangePostcode, String hostname, String serialNumber, DeviceType type, int availablePorts) {
        Map<String, Object> details = new HashMap<>();
        details.put("ex_code", exchangeCode);
        details.put("ex_name", exchangeName);
        details.put("ex_postcode", exchangePostcode);
        details.put("hostname", hostname);
        details.put("serial_number", serialNumber);
        details.put("type", type.name());
        details.put("available_ports", new BigDecimal(availablePorts));
        return details;
    }

    private void givenThereIsntADevice() {
        when(jdbcTemplate.queryForMap(anyString(), anyVararg())).thenThrow(new IncorrectResultSizeDataAccessException(1));
    }

    private void thereThereAreDevices(String... hostnames) {
        when(jdbcTemplate.queryForList(anyString(), eq(String.class))).thenReturn(asList(hostnames));
    }

    private void thenTheDeviceHasAvailablePortsWithType(BroadbandAccessDevice device, int expectedAvailablePorts, DeviceType expectedType) {
        assertThat(device.getAvailablePorts()).isEqualTo(expectedAvailablePorts);
        assertThat(device.getType()).isEqualTo(expectedType);
    }

}