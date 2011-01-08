package org.valkyriercp.sample.simple;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.valkyriercp.application.Application;
import org.valkyriercp.application.splash.DefaultSplashScreenConfig;
import org.valkyriercp.application.support.ApplicationLauncher;

import javax.swing.*;

public class SimpleSampleRunner {
    public static void main(String[] args) {
        new ApplicationLauncher(DefaultSplashScreenConfig.class, "/org/valkyriercp/sample/simple/context.xml");
    }
}
