package com.clean.example.integration.database.exchange;

import com.clean.example.core.entity.Exchange;
import com.clean.example.dataproviders.database.exchange.ExchangeDatabaseDataProvider;
import com.clean.example.integration.database.DatabaseIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeDatabaseIntegrationTest extends DatabaseIntegrationTest {

    @Autowired
    ExchangeDatabaseDataProvider exchangeDatabaseDataProvider;

    @Before
    public void setUp() throws Exception {
        cleanUpDatabase();
    }

    @Test
    public void knowsWhenExchangeExists() throws Exception {
        givenAnExistingExchange("exch1");

        boolean doesExchangeExist = exchangeDatabaseDataProvider.doesExchangeExist("exch1");

        assertThat(doesExchangeExist).isTrue();
    }

    @Test
    public void knowsWhenExchangeDoesNotExists() throws Exception {
        givenExchangeDoesNotExist("exch1");

        boolean doesExchangeExist = exchangeDatabaseDataProvider.doesExchangeExist("exch1");

        assertThat(doesExchangeExist).isFalse();
    }

    private void givenAnExistingExchange(String exchangeCode) {
        exchangeDatabaseDataProvider.insert(new Exchange(exchangeCode, "exchange name", "postCode"));
    }

    private void givenExchangeDoesNotExist(String exchangeCode) {
        // do nothing, simply don't insert it
    }

}
