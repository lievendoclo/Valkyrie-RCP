package org.valkyriercp.application.config.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.valkyriercp.application.ConfigurationException;
import org.valkyriercp.application.support.ApplicationWindowCommandManager;
import org.valkyriercp.command.config.AbstractCommandConfig;
import org.valkyriercp.command.support.CommandGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Keith Donald
 */
public class DefaultApplicationLifecycleAdvisor extends AbstractApplicationLifecycleAdvisor
        implements ApplicationListener {
    private String windowCommandBarDefinitions;

    private ConfigurableListableBeanFactory openingWindowCommandBarFactory;

    private Class<?> commandConfigClass;

    /**
     * Set of child command contexts created - used to bridge application events.
     */
    private ArrayList childContexts = new ArrayList();

    public void setWindowCommandBarDefinitions(String commandBarDefinitionLocation) {
        this.windowCommandBarDefinitions = commandBarDefinitionLocation;
    }

    public ApplicationWindowCommandManager createWindowCommandManager() {
        initNewWindowCommandBarFactory();
        return getCommandConfig().applicationWindowCommandManager();
    }

    protected void initNewWindowCommandBarFactory() {
        CommandBarApplicationContextFactory factory = new CommandBarApplicationContextFactory();
        factory.setParent(applicationContext);
        factory.setConfigClass(getCommandConfigClass());
        // Install our own application context so we can register needed post-processors
        final AnnotationConfigApplicationContext commandBarContext;
        try {
            commandBarContext = factory.getObject();
        } catch (Exception e) {
            throw new ConfigurationException("Command context could not be loaded", e);
        }
        addChildCommandContext(commandBarContext);
        this.openingWindowCommandBarFactory = commandBarContext.getBeanFactory();
    }

    public Class<?> getCommandConfigClass() {
        return commandConfigClass;
    }

    public void setCommandConfigClass(Class<?> commandConfigClass) {
        this.commandConfigClass = commandConfigClass;
    }

    protected AbstractCommandConfig getCommandConfig() {
        return openingWindowCommandBarFactory.getBean(AbstractCommandConfig.class);
    }

    public CommandGroup getMenuBarCommandGroup() {
        CommandGroup menuBarCommandGroup = getCommandConfig().menuBarCommandGroup();
        return menuBarCommandGroup != null ? menuBarCommandGroup : super.getMenuBarCommandGroup();
    }

    public CommandGroup getToolBarCommandGroup() {
        CommandGroup toolBarCommandGroup = getCommandConfig().toolBarCommandGroup();
        return toolBarCommandGroup != null ? toolBarCommandGroup : super.getToolBarCommandGroup();
    }


    /**
     * We need to deliver all application events down to the child command
     * contexts that have been created.
     *
     * @param event to deliver
     */
    public void onApplicationEvent(ApplicationEvent event) {
        // Dispatch the event to all the child command contexts
        for (Iterator iter = getChildCommandContexts().iterator(); iter.hasNext();) {
            ApplicationContext ctx = (ApplicationContext) iter.next();
            ctx.publishEvent(event);
        }
    }

    /**
     * Get all the child command contexts that have been created.
     * <p/>
     * <em>Note, theactual collection is being returned - so be careful what you
     * do to it.</em>
     *
     * @return list of contexts
     */
    protected List getChildCommandContexts() {
        return childContexts;
    }

    /**
     * Add a new child command context.
     *
     * @param context
     */
    protected void addChildCommandContext(ApplicationContext context) {
        childContexts.add(context);
    }

    private class CommandBarApplicationContextFactory implements FactoryBean<AnnotationConfigApplicationContext>
    {
        private ApplicationContext parent;
        private Class<?> configClass;

        public void setParent(ApplicationContext parent) {
            this.parent = parent;
        }

        public void setConfigClass(Class<?> configClass) {
            this.configClass = configClass;
        }

        @Override
        public AnnotationConfigApplicationContext getObject() throws Exception {
            CommandBarApplicationContext context = new CommandBarApplicationContext();
            context.setParent(parent);
            context.register(configClass);
            context.refresh();
            return context;
        }

        @Override
        public Class<?> getObjectType() {
            return AnnotationConfigApplicationContext.class;
        }

        @Override
        public boolean isSingleton() {
            return true;
        }
    }

    /**
     * Simple extension to allow us to inject our special bean post-processors
     * and control event publishing.
     */
    private class CommandBarApplicationContext extends AnnotationConfigApplicationContext {
        /**
         * Install our bean post-processors.
         *
         * @param beanFactory the bean factory used by the application context
         * @throws org.springframework.beans.BeansException
         *          in case of errors
         */
        protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            beanFactory.addBeanPostProcessor(new ApplicationWindowSetter(getOpeningWindow()));
        }
    }
}
