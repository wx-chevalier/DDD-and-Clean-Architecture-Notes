package com.clean.example.dataproviders.database.broadbandaccessdevice;

import com.clean.example.core.entity.BroadbandAccessDevice;
import com.clean.example.core.entity.DeviceType;
import com.clean.example.core.entity.Exchange;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.GetDeviceDetails;
import com.clean.example.core.usecase.broadbandaccessdevice.reconcile.GetAllDeviceHostnames;
import com.clean.example.core.usecase.broadbandaccessdevice.reconcile.GetSerialNumberFromModel;
import com.clean.example.core.usecase.broadbandaccessdevice.reconcile.UpdateSerialNumberInModel;
import com.clean.example.core.usecase.exchange.getcapacity.GetAvailablePortsOfAllDevicesInExchange;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class BroadbandAccessDeviceDatabaseDataProvider implements GetAllDeviceHostnames, GetSerialNumberFromModel,
        UpdateSerialNumberInModel, GetDeviceDetails, GetAvailablePortsOfAllDevicesInExchange {

    private JdbcTemplate jdbcTemplate;

    public BroadbandAccessDeviceDatabaseDataProvider(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> getAllDeviceHostnames() {
        return jdbcTemplate.queryForList("SELECT hostname FROM clean_architecture.bb_access_device", String.class);
    }

    @Override
    public String getSerialNumber(String hostname) {
        try {
            return jdbcTemplate.queryForObject("SELECT serial_number FROM clean_architecture.bb_access_device WHERE hostname = ?", String.class, hostname);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateSerialNumber(String hostname, String serialNumber) {
        jdbcTemplate.update("UPDATE clean_architecture.bb_access_device SET serial_number = ? WHERE hostname = ?", serialNumber, hostname);
    }

    public void insert(String exchangeCode, String hostname, String serialNumber, DeviceType deviceType) {
        int availablePorts = 100;
        insert(exchangeCode, hostname, serialNumber, deviceType, availablePorts);
    }

    public void insert(String exchangeCode, String hostname, String serialNumber, DeviceType deviceType, int availablePorts) {
        jdbcTemplate.update("INSERT INTO clean_architecture.bb_access_device (id, exchange_id, hostname, serial_number, device_type_id, available_ports, created_date) " +
                " VALUES (bb_access_device_id_seq.nextval, " +
                " (SELECT id FROM clean_architecture.exchange WHERE code = ?), " +
                " ?, " +
                " ?, " +
                " (SELECT id FROM clean_architecture.device_type WHERE name = ?), " +
                " ?, " +
                "sysdate)", exchangeCode, hostname, serialNumber, deviceType.name(), availablePorts);
    }


    @Override
    public BroadbandAccessDevice getDetails(String hostname) {
        try {
            return doGetDetails(hostname);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    private BroadbandAccessDevice doGetDetails(String hostname) {
        Map<String, Object> result = jdbcTemplate.queryForMap(
                "SELECT e.code as ex_code, e.name as ex_name, e.postcode as ex_postcode, " +
                "       d.hostname, d.serial_number, dt.name as type, d.available_ports " +
                "FROM clean_architecture.bb_access_device d, clean_architecture.exchange e, clean_architecture.device_type dt " +
                "WHERE d.exchange_id = e.id " +
                "AND d.device_type_id = dt.id " +
                "AND d.hostname = ?"
                , hostname);

        String exchangeCode = (String) result.get("ex_code");
        String exchangeName = (String) result.get("ex_name");
        String exchangePostcode = (String) result.get("ex_postcode");
        Exchange exchange = new Exchange(exchangeCode, exchangeName, exchangePostcode);

        String resultHostname = (String) result.get("hostname");
        String resultSerialNumber = (String) result.get("serial_number");
        DeviceType deviceType = DeviceType.valueOf((String) result.get("type"));
        int availablePorts = ((BigDecimal) result.get("available_ports")).intValue();
        BroadbandAccessDevice device = new BroadbandAccessDevice(resultHostname, resultSerialNumber, deviceType);
        device.setAvailablePorts(availablePorts);
        device.setExchange(exchange);

        return device;
    }

    @Override
    public List<BroadbandAccessDevice> getAvailablePortsOfAllDevicesInExchange(String exchangeCode) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT e.code as ex_code, e.name as ex_name, e.postcode as ex_postcode, " +
                "       d.hostname, d.serial_number, dt.name as type, d.available_ports " +
                "FROM clean_architecture.bb_access_device d, clean_architecture.exchange e,  clean_architecture.device_type dt " +
                "WHERE d.device_type_id = dt.id " +
                "AND d.exchange_id = e.id " +
                "AND e.code = ?", exchangeCode);
        List<BroadbandAccessDevice> broadbandAccessDevices = rows.stream().map(row -> {
                    String hostname = (String) row.get("hostname");
                    String serialNumber = (String) row.get("serial_number");
                    DeviceType deviceType = DeviceType.valueOf((String) row.get("type"));
                    int availablePorts = ((BigDecimal) row.get("available_ports")).intValue();
                    BroadbandAccessDevice broadbandAccessDevice = new BroadbandAccessDevice(hostname, serialNumber, deviceType);
                    broadbandAccessDevice.setAvailablePorts(availablePorts);
                    return broadbandAccessDevice;
        }).collect(toList());

        return broadbandAccessDevices;
    }
}
