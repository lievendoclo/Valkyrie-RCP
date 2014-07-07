package org.valkyriercp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.valkyriercp.application.config.ApplicationConfig
import spock.lang.Specification

@ContextConfiguration(classes = TestApplicationConfig.class)
abstract class AbstractValkyrieSpec extends Specification{
    @Autowired
    protected ApplicationConfig applicationConfig;
}
