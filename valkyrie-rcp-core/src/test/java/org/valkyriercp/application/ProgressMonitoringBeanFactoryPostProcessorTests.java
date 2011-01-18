package org.valkyriercp.application;

import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.context.support.StaticMessageSource;
import org.valkyriercp.application.support.ProgressMonitoringBeanFactoryPostProcessor;
import org.valkyriercp.progress.NullProgressMonitor;
import org.valkyriercp.progress.ProgressMonitor;

import java.util.Locale;

/**
 * This class provides a suite of unit tests for the
 * {@link org.valkyriercp.application.support.ProgressMonitoringBeanFactoryPostProcessor}.
 *
 * @author Kevin Stembridge
 * @since 0.3.0
 *
 */
public class ProgressMonitoringBeanFactoryPostProcessorTests {

	/**
	 * Confirms that the post-processor's constructor throws an
	 * IllegalArgumentException if a ProgressMonitor is not provided, but allows
	 * a null MessageSource.
	 */
	@Test
    public void testConstructor() {

		try {
			new ProgressMonitoringBeanFactoryPostProcessor(null);
			Assert.fail("Should have thrown an IllegalArgumentException");
		}
		catch (IllegalArgumentException e) {
			// do nothing, test succeeded
		}

		new ProgressMonitoringBeanFactoryPostProcessor(new NullProgressMonitor());

	}

	/**
	 * Confirms that the post processor correctly notifies a given progress
	 * monitor as the bean factory is loaded. The following assertions are made:
	 *
	 * <ul>
	 * <li>The {@link ProgressMonitor#taskStarted(String, int)} method is
	 * called exactly once with any message and the number of singleton beans in
	 * the bean factory.</li>
	 * <li>The {@link ProgressMonitor#subTaskStarted(String)} method is called,
	 * with the localized message provided by
	 * {@link org.valkyriercp.application.support.ProgressMonitoringBeanFactoryPostProcessor.ProgressMonitoringBeanPostProcessor#LOADING_BEAN_KEY}, for each
	 * singleton bean defined in the bean factory being loaded.</li>
	 * <li>The {@link ProgressMonitor#worked(int)} method is called with the
	 * argument '1' the same number of times as there are singleton beans
	 * defined in the bean factory.</li>
	 * </ul>
	 */
	@Test
    public void testLoadingBeans() {
		int expectedSingletonBeanCount = 2;
		String beanName1 = "beanName1";
		String beanName2 = "beanName2";
		String beanName3 = "beanName3";

		StaticApplicationContext appCtx = new StaticApplicationContext();
		appCtx.registerSingleton(beanName1, Object.class);
		appCtx.registerSingleton(beanName2, Object.class);
		appCtx.registerPrototype(beanName3, Object.class);

		ProgressMonitor mockProgressMonitor = (ProgressMonitor) EasyMock.createStrictMock(ProgressMonitor.class);
		mockProgressMonitor.taskStarted("Loading Application Context ...", expectedSingletonBeanCount);
		mockProgressMonitor.subTaskStarted("Loading " + beanName1 + " ...");
		mockProgressMonitor.worked(1);
		mockProgressMonitor.subTaskStarted("Loading " + beanName2 + " ...");
		mockProgressMonitor.worked(1);
		EasyMock.replay(mockProgressMonitor);

		ProgressMonitoringBeanFactoryPostProcessor processor = new ProgressMonitoringBeanFactoryPostProcessor(
				mockProgressMonitor);
		appCtx.addBeanFactoryPostProcessor(processor);

		appCtx.refresh();

		EasyMock.verify(mockProgressMonitor);
	}

}
