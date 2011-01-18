package org.valkyriercp;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.valkyriercp.application.config.ApplicationConfig;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public abstract class AbstractValkyrieTest {
    @Autowired
    protected ApplicationConfig applicationConfig;
}
