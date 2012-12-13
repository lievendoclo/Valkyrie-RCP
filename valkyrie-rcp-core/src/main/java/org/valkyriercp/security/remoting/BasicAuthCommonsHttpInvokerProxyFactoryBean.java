package org.valkyriercp.security.remoting;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.valkyriercp.security.AuthenticationAware;
import org.springframework.security.core.Authentication;

/**
 * Extension of <code>HttpInvokerProxyFactoryBean</code> that supports the use of BASIC
 * authentication on each HTTP request while using commons-httpclient.
 * Commons-httpclient can be easily configured to use SSL (so the BASIC authentication isn't sniffable):
 * <code>
 *       ProtocolSocketFactory authSSLProtocolSocketFactory = new AuthSSLProtocolSocketFactory(null, null,
 *               truststoreUrl, TRUSTSTORE_PASSWORD);
 *       Protocol.registerProtocol("https", new Protocol("https", authSSLProtocolSocketFactory, 443));
 * </code>
 * <p>
 * This factory takes care of instantiating the proper invocation executor and keeping
 * it up to date with the latest user credentials. Once a more complete AOP implementation
 * is available, then this "token forwarding" can be removed as the default executor is
 * already wired to receive notifications when it is constructed by the application
 * context.
 * <p>
 * This configuration assumes that the user's credentials are "global" to the application
 * and every invocation should use the same credentials. If you need per-thread
 * authentication then you should look at using a combination of
 * {@link org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean} and
 * {@link org.springframework.security.context.httpinvoker.AuthenticationSimpleHttpInvokerRequestExecutor}.
 * <p>
 * {@link org.springframework.richclient.security.AuthenticationAware} is implemented in order to get notifications of changes in
 * the user's credentials. Please see the class documentation for
 * <code>AuthenticationAware</code> to see how to configure the application context so
 * that authentication changes are broadcast properly.
 * <p>
 * @author Geoffrey De Smet
 * @author Larry Streepy
 */
public class BasicAuthCommonsHttpInvokerProxyFactoryBean extends HttpInvokerProxyFactoryBean implements AuthenticationAware {

    /**
     * Constructor. Install the default executor.
     */
    public BasicAuthCommonsHttpInvokerProxyFactoryBean() {
        setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor());
    }


    /**
     * Handle a change in the current authentication token.
     * This method will fail fast if the executor isn't a CommonsHttpInvokerRequestExecutor.
     * @see org.springframework.richclient.security.AuthenticationAware#setAuthenticationToken(org.springframework.security.Authentication)
     */
    public void setAuthenticationToken(Authentication authentication) {
        if( logger.isDebugEnabled() ) {
            logger.debug("New authentication token: " + authentication);
        }

        CommonsHttpInvokerRequestExecutor executor
                = (CommonsHttpInvokerRequestExecutor) getHttpInvokerRequestExecutor();
        HttpClient httpClient = executor.getHttpClient();
        httpClient.getParams().setAuthenticationPreemptive(authentication != null);
        UsernamePasswordCredentials usernamePasswordCredentials;
        if (authentication != null) {
            usernamePasswordCredentials = new UsernamePasswordCredentials(
                    authentication.getName(), authentication.getCredentials().toString());
        } else {
            usernamePasswordCredentials = null;
        }
        httpClient.getState().setCredentials(AuthScope.ANY, usernamePasswordCredentials);
    }
}
