package org.valkyriercp.sample.simple;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.valkyriercp.application.Application;

import javax.swing.*;

public class SimpleSampleRunner {
    public static void main(String[] args) {
        ApplicationContext config = new ClassPathXmlApplicationContext("/org/valkyriercp/sample/simple/context.xml");
        final Application app = config.getBean(Application.class);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app.start();
            }
        });
    }
}
