package com.clean.example.endtoend.exchange.getcapacity;

import com.clean.example.businessrequirements.exchange.getcapacity.GetCapacityForExchangeAcceptanceTest;
import com.clean.example.core.entity.Exchange;
import com.clean.example.dataproviders.database.broadbandaccessdevice.BroadbandAccessDeviceDatabaseDataProvider;
import com.clean.example.dataproviders.database.exchange.ExchangeDatabaseDataProvider;
import com.clean.example.endtoend.EndToEndYatspecTest;
import com.clean.example.entrypoints.rest.exchange.capacity.GetCapacityForExchangeEndpoint;
import com.googlecode.yatspec.junit.LinkingNote;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cedarsoftware.util.io.JsonWriter.formatJson;
import static com.clean.example.core.entity.DeviceType.ADSL;
import static com.clean.example.core.entity.DeviceType.FIBRE;
import static org.assertj.core.api.Assertions.assertThat;

@LinkingNote(message = "Business Requirements: %s", links = {GetCapacityForExchangeAcceptanceTest.class})
public class GetCapacityForExchangeEndToEndTest extends EndToEndYatspecTest {

    @Autowired
    ExchangeDatabaseDataProvider exchangeDatabaseDataProvider;

    @Autowired
    BroadbandAccessDeviceDatabaseDataProvider broadbandAccessDeviceDatabaseDataProvider;

    int responseStatus;
    String responseContent;

    @Test
    public void hasCapacityWhenThereAreAvailablePorts() throws Exception {
        givenAnExchange("exch1");
        givenThereAreAdslDevicesWithAvailablePortsInExchange("exch1");
        givenThereAreFibreDevicesWithAvailablePortsInExchange("exch1");

        givenAnExchange("exch2");
        givenThereAreAdslDevicesWithNoAvailablePortsInExchange("exch2");
        givenThereAreFibreDevicesWithAvailablePortsInExchange("exch2");

        givenAnExchange("exch3");
        givenThereAreAdslDevicesWithAvailablePortsInExchange("exch3");
        givenThereAreFibreDevicesWithNoAvailablePortsInExchange("exch3");

        whenTheCapacityIsRetrievedForExchange("exch1");

        thenThereIsAdslAndFibreCapacity();

        whenTheCapacityIsRetrievedForExchange("exch2");

        thenThereIsFibreCapacityButNoAdslCapacity();

        whenTheCapacityIsRetrievedForExchange("exch3");

        thenThereIsAdslCapacityButNoFibreCapacity();
    }

    private void givenAnExchange(String exchangeCode) {
        Exchange exchange = new Exchange(exchangeCode, "name", "postCode");
        exchangeDatabaseDataProvider.insert(exchange);
        log("Exchange " + exchangeCode, exchange);
    }

    private void givenThereAreAdslDevicesWithAvailablePortsInExchange(String exchangeCode) {
        broadbandAccessDeviceDatabaseDataProvider.insert(exchangeCode, "device1", "defaultSerialNumber", ADSL, 10);
        log("ADSL Device1", "Available Ports: 10");
        broadbandAccessDeviceDatabaseDataProvider.insert(exchangeCode, "device2", "defaultSerialNumber", ADSL, 5);
        log("ADSL Device2", "Available Ports: 5");
    }

    private void givenThereAreFibreDevicesWithAvailablePortsInExchange(String exchangeCode) {
        broadbandAccessDeviceDatabaseDataProvider.insert(exchangeCode, "device3", "defaultSerialNumber", FIBRE, 10);
        log("Fibre Device3", "Available Ports: 10");
        broadbandAccessDeviceDatabaseDataProvider.insert(exchangeCode, "device4", "defaultSerialNumber", FIBRE, 5);
        log("Fibre Device4", "Available Ports: 5");
    }

    private void givenThereAreAdslDevicesWithNoAvailablePortsInExchange(String exchangeCode) {
        broadbandAccessDeviceDatabaseDataProvider.insert(exchangeCode, "device1", "defaultSerialNumber", ADSL, 0);
        log("ADSL Device1", "Available Ports: 0");
        broadbandAccessDeviceDatabaseDataProvider.insert(exchangeCode, "device2", "defaultSerialNumber", ADSL, 1);
        log("ADSL Device2", "Available Ports: 1");
    }

    private void givenThereAreFibreDevicesWithNoAvailablePortsInExchange(String exchangeCode) {
        broadbandAccessDeviceDatabaseDataProvider.insert(exchangeCode, "device3", "defaultSerialNumber", FIBRE, 0);
        log("Fibre Device3", "Available Ports: 0");
        broadbandAccessDeviceDatabaseDataProvider.insert(exchangeCode, "device4", "defaultSerialNumber", FIBRE, 1);
        log("Fibre Device4", "Available Ports: 1");
    }

    private void whenTheCapacityIsRetrievedForExchange(String exchangeCode) throws UnirestException {
        String apiPath = GetCapacityForExchangeEndpoint.API_PATH.replace("{exchangeCode}", exchangeCode);
        String apiUrl = "http://localhost:8080" + apiPath;
        log("API Url", apiUrl);

        HttpResponse<String> httpResponse = Unirest.get(apiUrl).asString();

        responseStatus = httpResponse.getStatus();
        log("Response Status", responseStatus);

        responseContent = httpResponse.getBody();
        log("Response Content", formatJson(responseContent));
    }

    private void thenThereIsAdslAndFibreCapacity() {
        checkCapacity(true, true);
    }

    private void thenThereIsFibreCapacityButNoAdslCapacity() {
        checkCapacity(false, true);
    }

    private void thenThereIsAdslCapacityButNoFibreCapacity() {
        checkCapacity(true, false);
    }

    private void checkCapacity(boolean adslCapacity, boolean fibreCapacity) {
        assertThat(responseStatus).isEqualTo(200);

        String expectedResponse =
                "{\n" +
                        "  \"hasADSLCapacity\":" + adslCapacity + ",\n" +
                        "  \"hasFibreCapacity\":" + fibreCapacity + "\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseContent, false);
    }

}
