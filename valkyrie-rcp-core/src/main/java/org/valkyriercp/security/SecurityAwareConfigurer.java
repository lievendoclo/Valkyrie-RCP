package org.valkyriercp.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * This class performs two main functions:
 * <ol>
 * <li>It is a bean post-processor that will set the current authentication token on any
 * newly created beans that implement {@link AuthenticationAware}.</li>
 * <li>It listens for application {@link ClientSecurityEvent}s and updates all the beans
 * in the context that implement either {@link AuthenticationAware} or {@link LoginAware}
 * according to the event received.</li>
 * </ol>
 * In order for all this to take place, a singleton, non-lazy instance of this class must
 * be defined in the Spring ApplicationContext. This would be done like this:
 *
 * <pre>
 *              &lt;bean id=&quot;securityAwareConfigurer&quot;
 *                   class=&quot;org.springframework.richclient.security.SecurityAwareConfigurer&quot;
 *                   lazy-init=&quot;false&quot;/&gt;
 * </pre>
 *
 * @author Larry Streepy
 * @author Andy Depue
 * @author Inspiration from Ben Alex
 *
 * @see AuthenticationAware
 * @see LoginAware
 * @see ClientSecurityEvent
 *
 */
public class SecurityAwareConfigurer implements ApplicationListener, ApplicationContextAware, BeanPostProcessor {

    private final Log logger = LogFactory.getLog(getClass());

    private ApplicationContext applicationContext;

    private final List nonSingletonListeners = Collections.synchronizedList(new ArrayList());

    private Authentication currentAuthentication = null; // Until we know it

    /**
     * Get the installed application context.
     * @return context
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Broadcast an authentication event to all the AuthenticationAware beans.
     * @param authentication token
     */
    protected void broadcastAuthentication(Authentication authentication) {
        if( logger.isDebugEnabled() )
            logger.debug( "BROADCAST authentication: token=" + authentication );

        // Save this for any new beans that we post-process
        currentAuthentication = authentication;

        final Iterator iter = getBeansToUpdate( AuthenticationAware.class ).iterator();
        while( iter.hasNext() ) {
            ((AuthenticationAware) iter.next()).setAuthenticationToken( authentication );
        }
    }

    /**
     * Broadcast a Login event to all the LoginAware beans.
     * @param authentication token
     */
    protected void broadcastLogin(Authentication authentication) {
        if( logger.isDebugEnabled() )
            logger.debug( "BROADCAST login: token=" + authentication );

        final Iterator iter = getBeansToUpdate( LoginAware.class ).iterator();
        while( iter.hasNext() ) {
            ((LoginAware) iter.next()).userLogin( authentication );
        }
    }

    /**
     * Broadcast a Logout event to all the LoginAware beans.
     * @param authentication token
     */
    protected void broadcastLogout(Authentication authentication) {
        if( logger.isDebugEnabled() )
            logger.debug( "BROADCAST logout: token=" + authentication );

        final Iterator iter = getBeansToUpdate( LoginAware.class ).iterator();
        while( iter.hasNext() ) {
            ((LoginAware) iter.next()).userLogout( authentication );
        }
    }

    /**
     * Construct the list of all the beans we need to update.
     * @param beanType Type of bean to locate
     * @return List of all beans to udpate.
     */
    protected List getBeansToUpdate(Class beanType) {
        final ApplicationContext ac = getApplicationContext();
        final List listeners = new ArrayList();

        if( ac != null ) {
            if( logger.isDebugEnabled() )
                logger.debug( "Constructing list of beans to notify; bean type=" + beanType.getName() );

            final Map map = ac.getBeansOfType( beanType, false, true );

            if( logger.isDebugEnabled() )
                logger.debug( "bean map: " + map );

            listeners.addAll( map.values() );
            listeners.addAll( getNonSingletonListeners( beanType ) );
        }

        if( logger.isDebugEnabled() )
            logger.debug( "List of beans to notify:" + listeners );

        return listeners;
    }

    /**
     * Get the list of non-singleton beans we have registered that still exist. Update our
     * registration list if any have been GC'ed. Only return beans of the requested type.
     * @param beanType Type of bean to locate
     * @return list of extant non-singleton beans to notify
     */
    protected List getNonSingletonListeners(Class beanType) {

        final List listeners = new ArrayList();

        synchronized(nonSingletonListeners) {
            for( Iterator iter = nonSingletonListeners.iterator(); iter.hasNext(); ) {
                final Object bean = ((WeakReference) iter.next()).get();
                if( bean == null ) {
                    if( logger.isDebugEnabled() )
                        logger.debug( "REMOVED garbage collected AuthorizationAware non-singleton from list." );
                    iter.remove();
                } else if( beanType.isAssignableFrom( bean.getClass() ) ) {
                    listeners.add( bean );
                }
            }
        }
        return listeners;
    }

    /**
     * Add a non-singleton bean instance to our list for later notification.
     * @param bean
     */
    protected void addToNonSingletonListeners(final Object bean) {
        if( logger.isDebugEnabled() )
            logger.debug( "Adding Authentication/LoginAware bean to list of non-singleton listeners: bean='" + bean );

        nonSingletonListeners.add( new WeakReference( bean ) );
    }

    //
    // AplicationListener implementation
    //

    /*
     * (non-Javadoc)
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    public void onApplicationEvent(ApplicationEvent event) {

        // All events we care about are subtypes of ClientSecurityEvent
        if( event instanceof ClientSecurityEvent ) {
            Authentication authentication = (Authentication) event.getSource();

            if( logger.isDebugEnabled() ) {
                logger.debug( "RECEIVED ClientSecurityEvent: " + event );
                logger.debug( "Authentication token: " + authentication );
            }

            // Note that we need to inspect the new authentication token and see if it is
            // NO_AUTHENTICATION. If so, then we need to use null instead. This little
            // dance is required because the source of an event can't actually be null.

            if( authentication == ClientSecurityEvent.NO_AUTHENTICATION ) {
                if( logger.isDebugEnabled() ) {
                    logger.debug( "Converted NO_AUTHENTICATION to null" );
                }
                authentication = null;
            }

            // And dispatch according to the event type.

            if( event instanceof AuthenticationEvent ) {
                broadcastAuthentication( authentication );
            } else if( event instanceof LoginEvent ) {
                broadcastLogin( authentication );
            } else if( event instanceof LogoutEvent ) {
                broadcastLogout( authentication );
            } else {
                if( logger.isDebugEnabled() ) {
                    logger.debug( "Unsupported event not processed" );
                }
            }
        }
    }

    //
    // AplicationContextAware implementation
    //

    /*
     * (non-Javadoc)
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //
    // BeanPostProcessor implementation
    //

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
     *      java.lang.String)
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if( bean instanceof AuthenticationAware || bean instanceof LoginAware ) {

            // If the bean isn't a singleton, then add it to our list
            if( beanName == null || !getApplicationContext().containsBean( beanName )
                    || !getApplicationContext().isSingleton( beanName ) ) {
                addToNonSingletonListeners( bean );
            }

            // Install the last known authentication token
            if( bean instanceof AuthenticationAware ) {
                if( logger.isDebugEnabled() )
                    logger.debug( "NOTIFY bean '" + bean + "' of new authorization for '" + currentAuthentication
                            + "'" );

                AuthenticationAware aab = (AuthenticationAware) bean;
                aab.setAuthenticationToken(currentAuthentication);
            }
        }
        return bean;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
     *      java.lang.String)
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}

