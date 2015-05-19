/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.valkyriercp.application.config.ApplicationConfig;

import java.util.concurrent.CountDownLatch;

public class ValkyrieRepository {
    private static ValkyrieRepository instance;

    @Autowired
    private ApplicationConfig applicationConfig;

//    private static CountDownLatch latch = new CountDownLatch(1);

    public ValkyrieRepository() {
        if(instance != null)
            throw new IllegalStateException("This class should only be instantiated once!");
        instance = this;
//        latch.countDown();
    }

    public static boolean isCurrentlyRunningInContext() {
        return instance != null;
    }

    public static ValkyrieRepository getInstance() {
        if(instance == null)
            throw new IllegalStateException("Instance not yet initialized by context");
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
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
