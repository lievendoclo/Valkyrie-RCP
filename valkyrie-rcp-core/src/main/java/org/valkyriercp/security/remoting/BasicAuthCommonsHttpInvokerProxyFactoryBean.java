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
package org.valkyriercp.security.remoting;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.security.core.Authentication;
import org.valkyriercp.security.AuthenticationAware;

import java.io.IOException;

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
 * AuthenticationSimpleHttpInvokerRequestExecutor.
 * <p>
 * {@link AuthenticationAware} is implemented in order to get notifications of changes in
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
        setHttpInvokerRequestExecutor(new HttpComponentsHttpInvokerRequestExecutor());
    }


    /**
     * Handle a change in the current authentication token.
     * This method will fail fast if the executor isn't a CommonsHttpInvokerRequestExecutor.
     * @see org.valkyriercp.security.AuthenticationAware#setAuthenticationToken(org.springframework.security.core.Authentication)
     */
    public void setAuthenticationToken(Authentication authentication) {
        if( logger.isDebugEnabled() ) {
            logger.debug("New authentication token: " + authentication);
        }

        HttpComponentsHttpInvokerRequestExecutor executor
                = (HttpComponentsHttpInvokerRequestExecutor) getHttpInvokerRequestExecutor();
        DefaultHttpClient httpClient = (DefaultHttpClient) executor.getHttpClient();
        BasicCredentialsProvider provider = new BasicCredentialsProvider();
        httpClient.setCredentialsProvider(provider);
        httpClient.addRequestInterceptor(new PreemptiveAuthInterceptor());
        UsernamePasswordCredentials usernamePasswordCredentials;
        if (authentication != null) {
            usernamePasswordCredentials = new UsernamePasswordCredentials(
                    authentication.getName(), authentication.getCredentials().toString());
        } else {
            usernamePasswordCredentials = null;
        }
        provider.setCredentials(AuthScope.ANY, usernamePasswordCredentials);
    }

    static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {

        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));
                    if (creds == null) {
                        throw new HttpException("No credentials for preemptive authentication");
                    }
                    authState.setAuthScheme(authScheme);
                    authState.setCredentials(creds);
                }
            }

        }

    }
}
