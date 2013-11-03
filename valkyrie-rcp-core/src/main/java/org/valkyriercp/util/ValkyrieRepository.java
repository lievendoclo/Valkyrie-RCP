package org.valkyriercp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.valkyriercp.application.config.ApplicationConfig;

import java.util.concurrent.CountDownLatch;

public class ValkyrieRepository {
    private static ValkyrieRepository instance;

    @Autowired
    private ApplicationConfig applicationConfig;

    private static CountDownLatch latch = new CountDownLatch(1);

    public ValkyrieRepository() {
        if(instance != null)
            throw new IllegalStateException("This class should only be instantiated once!");
        instance = this;
        latch.countDown();
    }

    public static ValkyrieRepository getInstance() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    public <T> T getBean(Class<T> beanClass) {
        return getApplicationConfig().applicationContext().getBean(beanClass);
    }

    public <T> T getBean(String id, Class<T> beanClass) {
        return getApplicationConfig().applicationContext().getBean(id, beanClass);
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }
}
