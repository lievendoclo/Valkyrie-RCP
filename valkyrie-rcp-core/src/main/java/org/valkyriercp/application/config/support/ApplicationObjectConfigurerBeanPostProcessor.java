package org.valkyriercp.application.config.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.valkyriercp.application.config.ApplicationObjectConfigurer;

public class ApplicationObjectConfigurerBeanPostProcessor implements BeanPostProcessor {
    @Autowired
    private ApplicationObjectConfigurer applicationObjectConfigurer;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
       applicationObjectConfigurer.configure(bean, beanName);
	   return bean;
    }
}
