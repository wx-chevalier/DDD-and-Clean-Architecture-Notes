package com.clean.example.integration.database;

import com.clean.example.configuration.DatabaseDataProviderConfiguration;
import com.clean.example.configuration.DatasourceConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = {DatasourceConfiguration.class, DatabaseDataProviderConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class DatabaseIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    protected void cleanUpDatabase() {
        jdbcTemplate.update("DELETE FROM CLEAN_ARCHITECTURE.BB_ACCESS_DEVICE");
        jdbcTemplate.update("DELETE FROM CLEAN_ARCHITECTURE.EXCHANGE");
    }

}
