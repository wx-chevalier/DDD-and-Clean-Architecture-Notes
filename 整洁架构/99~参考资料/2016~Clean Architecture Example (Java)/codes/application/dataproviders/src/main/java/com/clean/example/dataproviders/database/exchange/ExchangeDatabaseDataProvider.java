package com.clean.example.dataproviders.database.exchange;

import com.clean.example.core.entity.Exchange;
import com.clean.example.core.usecase.exchange.getcapacity.DoesExchangeExist;
import org.springframework.jdbc.core.JdbcTemplate;

public class ExchangeDatabaseDataProvider implements DoesExchangeExist {

    private JdbcTemplate jdbcTemplate;

    public ExchangeDatabaseDataProvider(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Exchange exchange) {
        jdbcTemplate.update("INSERT INTO clean_architecture.exchange (id, code, name, postcode, created_date) VALUES (clean_architecture.exchange_id_seq.NEXTVAL, ?, ?, ?, SYSDATE)", exchange.getCode(), exchange.getName(), exchange.getPostCode());
    }

    @Override
    public boolean doesExchangeExist(String exchangeCode) {
        Integer numberOfResults = jdbcTemplate.queryForObject("SELECT count(*) FROM clean_architecture.exchange WHERE code = ?", Integer.class, exchangeCode);
        return numberOfResults >= 1;
    }
}
