package org.valkyriercp.security.remoting;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;
import org.springframework.security.core.Authentication;
import org.valkyriercp.security.AuthenticationAware;

/**
 * Extension of <code>HttpInvokerProxyFactoryBean</code> that supports the use of BASIC
 * authentication on each HTTP request. This factory takes care of instantiating the
 * proper invocation executor, {@link BasicAuthHttpInvokerRequestExecutor}, and keeping
 * it up to date with the latest user credentials. Once a more complete AOP implementation
 * is available, then this "token forwarding" can be removed as the default executor is
 * already wired to receive notifications when it is constructed by the application
 * context.
 * <p>
 * This configuration assumes that the user's credentials are "global" to the application
 * and every invocation should use the same credentials. If you need per-thread
 * authentication then you should look at using a combination of
 * {@link HttpInvokerProxyFactoryBean} and
 * {@link org.springframework.security.context.httpinvoker.AuthenticationSimpleHttpInvokerRequestExecutor}.
 * <p>
 * {@link AuthenticationAware} is implemented in order to get notifications of changes in
 * the user's credentials. Please see the class documentation for
 * <code>AuthenticationAware</code> to see how to configure the application context so
 * that authentication changes are broadcast properly.
 * <p>
 * @author Larry Streepy
 */
public class BasicAuthHttpInvokerProxyFactoryBean extends HttpInvokerProxyFactoryBean implements AuthenticationAware {

    /**
     * Constructor. Install the default executor.
     */
    public BasicAuthHttpInvokerProxyFactoryBean() {
        setHttpInvokerRequestExecutor( new BasicAuthHttpInvokerRequestExecutor() );
    }

    //
    // === AuthenticationAware implementation ===
    //

    /**
     * Handle a change in the current authentication token. Pass it along to the executor
     * if it's of the proper type.
     * @see BasicAuthHttpInvokerRequestExecutor
     * @see AuthenticationAware#setAuthenticationToken(org.springframework.security.Authentication)
     */
    public void setAuthenticationToken(Authentication authentication) {
        if( logger.isDebugEnabled() ) {
            logger.debug( "New authentication token: " + authentication );
        }

        final HttpInvokerRequestExecutor hire = getHttpInvokerRequestExecutor();
        if( hire instanceof BasicAuthHttpInvokerRequestExecutor ) {
            if( logger.isDebugEnabled() ) {
                logger.debug( "Pass it along to executor" );
            }
            ((BasicAuthHttpInvokerRequestExecutor) hire).setAuthenticationToken( authentication );
        }
    }
    
}
