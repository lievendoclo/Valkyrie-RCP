package org.valkyriercp.security.remoting;

import org.apache.commons.codec.binary.Base64;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.security.core.Authentication;
import org.valkyriercp.security.AuthenticationAware;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Adds BASIC authentication support to
 * <code>SimpleHttpInvokerRequestExecutor</code>. This class assumes that a
 * single authentication credential will be used for the whole application (as
 * is typical with a rich client). So, regardless of the thread involved, it
 * will use the Authentication object obtained from the last authentication
 * token received.
 * <p>
 * In comparison, see
 * {@link org.springframework.security.context.httpinvoker.AuthenticationSimpleHttpInvokerRequestExecutor}
 * for a class that manages the Authentication per-thread. If you need to have
 * threads with different authentication credentials, then you should use the
 * Spring Security class instead.
 * <p>
 * Typically, instances of this class will be automatically created by using
 * {@link BasicAuthHttpInvokerProxyFactoryBean}, which will take care of keeping
 * this instance updated with the latest authentication token. In case you want
 * to use this executor with a different invoker factory, this class implements
 * {@link AuthenticationAware} so that it will automatically receive
 * notification of changes in the authentication token.
 * <p>
 * Note that this configuration could lead to instances of this class receiving
 * two notifications of an authentication token change. However, that would only
 * happen if both the {@link BasicAuthHttpInvokerProxyFactoryBean} is used and
 * this class is created as a bean in the application context. This seemed
 * unlikely enough that I erred on the side of "ease of use". Also, the current
 * implementation does nothing more than store a reference to the new token, so
 * receiving two notifications isn't a problem.
 * 
 * @author Larry Streepy
 * @see org.springframework.richclient.security.remoting.BasicAuthHttpInvokerProxyFactoryBean
 * 
 */
public class BasicAuthHttpInvokerRequestExecutor extends
		SimpleHttpInvokerRequestExecutor implements
		org.valkyriercp.security.AuthenticationAware {

	private Authentication authentication;

	/**
	 * Constructor.
	 */
	public BasicAuthHttpInvokerRequestExecutor() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.security.AuthenticationAware#
	 * setAuthenticationToken(org.springframework.security.Authentication)
	 */
	public void setAuthenticationToken(Authentication authentication) {
		this.authentication = authentication;
	}

	/**
	 * Get the Authentication object for the current user, if any.
	 */
	public Authentication getAuthenticationToken() {
		return authentication;
	}

	//
	// === SimpleHttpInvokerRequestExecutor methods ===
	//

	/**
	 * Provided so subclasses can perform additional configuration if required
	 * (eg set additional request headers for non-security related information
	 * etc).
	 * 
	 * @param con
	 *            the HTTP connection to prepare
	 * @param contentLength
	 *            the length of the content to send
	 * 
	 * @throws IOException
	 *             if thrown by HttpURLConnection methods
	 */
	protected void doPrepareConnection(HttpURLConnection con, int contentLength)
			throws IOException {
	}

	/**
	 * Called every time a HTTP invocation is made.
	 * <p>
	 * Simply allows the parent to setup the connection, and then adds an
	 * <code>Authorization</code> HTTP header property that will be used for
	 * BASIC authentication. Following that a call to
	 * {@link #doPrepareConnection} is made to allow subclasses to apply any
	 * additional configuration desired to the connection prior to invoking the
	 * request.
	 * <p>
	 * The previously saved authentication token is used to obtain the principal
	 * and credentials. If the saved token is null, then the "Authorization"
	 * header will not be added to the request.
	 * 
	 * @param con
	 *            the HTTP connection to prepare
	 * @param contentLength
	 *            the length of the content to send
	 * 
	 * @throws IOException
	 *             if thrown by HttpURLConnection methods
	 */
	protected void prepareConnection(HttpURLConnection con, int contentLength)
			throws IOException {

		super.prepareConnection(con, contentLength);

		Authentication auth = getAuthenticationToken();

		if ((auth != null) && (auth.getName() != null)
				&& (auth.getCredentials() != null)) {
			String base64 = auth.getName() + ":"
					+ auth.getCredentials().toString();
			con.setRequestProperty("Authorization", "Basic "
					+ new String(Base64.encodeBase64(base64.getBytes())));

			if (logger.isDebugEnabled()) {
				logger.debug("HttpInvocation now presenting via BASIC authentication with token:: "
						+ auth);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to set BASIC authentication header as Authentication token is invalid: "
						+ auth);
			}
		}

		doPrepareConnection(con, contentLength);
	}

}
