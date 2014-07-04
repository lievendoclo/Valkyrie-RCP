package org.valkyriercp.sample.simple;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.valkyriercp.application.support.ApplicationLauncher;

public class SimpleSampleRunner {
	public static void main(String[] args) {
		// new ApplicationLauncher(SimpleSplashScreenConfig.class,
		// "/org/valkyriercp/sample/simple/context.xml");
		// XML free launch
		new ApplicationLauncher(SimpleSplashScreenConfig.class,
				new AnnotationConfigApplicationContext(
						SimpleSampleApplicationConfig.class));
	}
}
