package com.clean.example.endtoend;

import com.clean.example.yatspec.SpringSpecRunner;
import com.clean.example.yatspec.YatspecTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringSpecRunner.class)
@SpringApplicationConfiguration(classes = {ApplicationConfigurationForEndToEndTests.class, MocksConfigurationForEndToEndTests.class})
@IntegrationTest
@WebAppConfiguration
public abstract class EndToEndYatspecTest extends YatspecTest {

}
