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
package org.valkyriercp.application.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.*;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;
import org.valkyriercp.progress.ProgressMonitor;

/**
 * A {@code BeanFactoryPostProcessor} that notifies a specified
 * {@link ProgressMonitor} of progress made while loading a bean factory.
 *
 * <p>
 * The messages sent to the progress monitor can be internationalized by
 * providing a {@link org.springframework.context.MessageSource} to the constructor of this class. Note that
 * if a {@link org.springframework.context.MessageSource} is provided it must already be initialized in
 * order for it to successfully retrieve messages.
 * </p>
 *
 * <p>
 * The progress monitor will be notified once prior to initializing the beans in
 * the bean factory and once for each singleton bean before it is initialized.
 * The message keys used to find these messages are
 * {@value #LOADING_APP_CONTEXT_KEY} and {@value #LOADING_BEAN_KEY}. If the
 * message source is unable to find any messages under these keys, or if no
 * message source is provided, default messages (in English) will be used
 * instead.
 * </p>
 *
 * @author Kevin Stembridge
 * @since 0.3.0
 *
 * @see org.valkyriercp.progress.ProgressMonitor
 *
 */
public class ProgressMonitoringBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	/**
	 * The message key used to retrieve the message to be sent to the progress
	 * monitor when the application context begins loading.
	 */
	public static final String LOADING_APP_CONTEXT_KEY = "progress.loading.applicationContext";

	/**
	 * The message key used to retrieve the message to be sent to the progress
	 * monitor for each bean that is loaded.
	 */
	public static final String LOADING_BEAN_KEY = "progress.loading.bean";

	private static final Log logger = LogFactory.getLog(ProgressMonitoringBeanFactoryPostProcessor.class);

	private final ProgressMonitor progressMonitor;

    @Autowired(required = false)
	private MessageSource messageSource;

	private final String loadingAppContextMessage;

	/**
	 * Creates a new {@code ProgressMonitoringBeanFactoryPostProcessor} that
	 * will report the progress of loading the beans in a bean factory to the
	 * given progress monitor, optionally providing internationalized messages.
	 *
	 * @param progressMonitor The progress monitor that will be notified of
	 * progress while processing the bean factory.
     *
	 * @throws IllegalArgumentException if {@code progressMonitor} is null.
	 */
	public ProgressMonitoringBeanFactoryPostProcessor(ProgressMonitor progressMonitor) {

		Assert.notNull(progressMonitor, "The ProgressMonitor cannot be null");

		this.progressMonitor = progressMonitor;
		this.loadingAppContextMessage = getLoadingAppContextMessage();

	}

	/**
	 * Notifies this instance's associated progress monitor of progress made
	 * while processing the given bean factory.
	 *
	 * @param beanFactory the bean factory that is to be processed.
	 */
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {

		if (beanFactory == null) {
			return;
		}

		String[] beanNames = beanFactory.getBeanDefinitionNames();
		int singletonBeanCount = 0;

		for (int i = 0; i < beanNames.length; i++) {
			// using beanDefinition to check singleton property because when
			// accessing through
			// context (applicationContext.isSingleton(beanName)), bean will be
			// created already,
			// possibly bypassing other BeanFactoryPostProcessors
			BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanNames[i]);

			if (beanDefinition.isSingleton()) {
				singletonBeanCount++;
			}

		}

		this.progressMonitor.taskStarted(this.loadingAppContextMessage, singletonBeanCount);

		beanFactory.addBeanPostProcessor(new ProgressMonitoringBeanPostProcessor(beanFactory));

	}

	private String getLoadingAppContextMessage() {

		String defaultMessage = "Loading Application Context ...";

		if (this.messageSource == null) {
			return defaultMessage;
		}

		return this.messageSource.getMessage(LOADING_APP_CONTEXT_KEY, null, defaultMessage, null);

	}

	private class ProgressMonitoringBeanPostProcessor implements BeanPostProcessor {

		private final ConfigurableBeanFactory beanFactory;

		private ProgressMonitoringBeanPostProcessor(ConfigurableBeanFactory beanFactory) {
			Assert.notNull(beanFactory, "The bean factory cannot be null");
			this.beanFactory = beanFactory;
		}

		/**
		 * A default implementation that performs no operation on the bean.
		 */
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			return bean;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

			if (logger.isTraceEnabled()) {
				logger.trace("BEGIN: postProcessBeforeInitialization(" + beanName + ")");
			}

			if (this.beanFactory.containsLocalBean(beanName)) {
				String loadingBeanMessage = getLoadingBeanMessage(beanName);
				progressMonitor.subTaskStarted(loadingBeanMessage);
				progressMonitor.worked(1);
			}

			logger.trace("END: postProcessBeforeInitialization()");
			return bean;

		}

		private String getLoadingBeanMessage(String beanName) {

			String defaultMessage = "Loading " + beanName + " ...";

			if (messageSource == null) {
				return defaultMessage;
			}

			Object[] args = { beanName };

			return messageSource.getMessage(LOADING_BEAN_KEY, args, defaultMessage, null);

		}

	}

}

