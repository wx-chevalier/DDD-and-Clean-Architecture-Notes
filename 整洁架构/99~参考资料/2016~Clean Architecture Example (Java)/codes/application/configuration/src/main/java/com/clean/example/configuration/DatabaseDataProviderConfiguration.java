package com.clean.example.configuration;

import com.clean.example.dataproviders.database.broadbandaccessdevice.BroadbandAccessDeviceDatabaseDataProvider;
import com.clean.example.dataproviders.database.exchange.ExchangeDatabaseDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseDataProviderConfiguration {

    @Bean
    public BroadbandAccessDeviceDatabaseDataProvider broadbandAccessDeviceDatabaseDataProvider(JdbcTemplate jdbcTemplate) {
        return new BroadbandAccessDeviceDatabaseDataProvider(jdbcTemplate);
    }

    @Bean
    public ExchangeDatabaseDataProvider exchangeDatabaseDataProvider(JdbcTemplate jdbcTemplate) {
        return new ExchangeDatabaseDataProvider(jdbcTemplate);
    }

}
