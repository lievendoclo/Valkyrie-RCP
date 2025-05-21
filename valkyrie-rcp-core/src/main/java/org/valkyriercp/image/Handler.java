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
package org.valkyriercp.image;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * <p>
 * A URL protocol handler that resolves images from an ImageSource.
 * </p>
 * <p>
 * The syntax of an "image:" URL is: <code>image:{imageKey}</code>
 * </p>
 *
 * There are three methods to register/use this custom protocol:
 * <ol>
 * <li>Supply the URLStreamHandler when constructing your URL object.</li>
 * <li>Create an URLStreamHandlerFactory and register it on the URL class by
 * using the setURLStreamFactory method.</li>
 * <li>Create the URLStreamHandler by naming it Handler and placing it in a
 * package which ends in the name if the protocol. Then register the package
 * prefix before the protocol name by supplying it to the vm with the property
 * -Djava.protocol.handler.pkgs. (thus setting it to eg 'my.company.protocols',
 * your protocol is named image and your class name must be Handler which lives
 * in package 'my.company.protocols.image') Multiple packages can be supplied by
 * separating them with a '|'.</li>
 * </ol>
 *
 * Now all of these have drawbacks:
 *
 * <ol>
 * <li>obviously you don't want to construct each URL object with its specific
 * handler.</li>
 * <li>the factory can be set only once, if set twice an exception will be
 * thrown. This was the initial error of this issue.</li>
 * <li>you need to supply this system parameter at startup. The static method
 * in the image Handler can only be used if no URL was created before and the
 * system parameter wasn't read yet.</li>
 * </ol>
 *
 * <p>
 * We recommend that you use the system parameter at startup to ensure that the
 * handler is registered:
 * </p>
 *
 * <pre>
 * -Djava.protocol.handler.pkgs=org.springframework.richclient
 * </pre>
 *
 * <p>
 * A static method {@link #installImageUrlHandler} is provided that extends the
 * system property and includes the 'org.springframework.richclient'. This
 * method can also be triggered by creating an imageSource using
 * {@link DefaultImageSource#DefaultImageSource(boolean, java.util.Map)}. Note
 * that this will only work if the system property isn't already read. If an URL
 * was created and an {@link java.net.URLStreamHandlerFactory} is available, extending
 * the system property won't have any effect.
 * </p>
 *
 * @author Oliver Hutchison
 * @author Jan Hoskens
 *
 */
public class Handler extends URLStreamHandler {

	private static final Log logger = LogFactory.getLog(Handler.class);

	private static ImageSource urlHandlerImageSource;

	/**
	 * Installs this class as a handler for the "image:" protocol. Images will
	 * be resolved from the provided image source.
	 */
	public static void installImageUrlHandler(ImageSource urlHandlerImageSource) {
		Assert.notNull(urlHandlerImageSource, "image source should not be null");

		Handler.urlHandlerImageSource = urlHandlerImageSource;

		try {
			// System properties should be set at JVM startup
			// Testcases in IDEA/Eclipse are at JVM startup, but not in Maven's
			// surefire...
			// TODO this entire implementation should be changed with a
			// java.net.URLStreamHandlerFactory instead.
			String packagePrefixList = System.getProperty("java.protocol.handler.pkgs");
			String newPackagePrefixList = null;
			String orgSpringFrameworkRichclientString = "org.springframework.richclient";
			if (packagePrefixList == null || packagePrefixList.equals("")) {
				newPackagePrefixList = orgSpringFrameworkRichclientString;
			}
			else if (("|" + packagePrefixList + "|").indexOf("|" + orgSpringFrameworkRichclientString + "|") < 0) {
				newPackagePrefixList = packagePrefixList + "|" + orgSpringFrameworkRichclientString;
			}
			if (newPackagePrefixList != null) {
				System.setProperty("java.protocol.handler.pkgs", newPackagePrefixList);
			}
		}
		catch (SecurityException e) {
			logger.warn("Unable to install image URL handler", e);
			Handler.urlHandlerImageSource = null;
		}
	}

	/**
	 * Creates an instance of <code>Handler</code>.
	 */
	public Handler() {
	}

	protected URLConnection openConnection(URL url) throws IOException {
		if (!StringUtils.hasText(url.getPath())) {
			throw new MalformedURLException("must provide an image key.");
		}
		else if (StringUtils.hasText(url.getHost())) {
			throw new MalformedURLException("host part should be empty.");
		}
		else if (url.getPort() != -1) {
			throw new MalformedURLException("port part should be empty.");
		}
		else if (StringUtils.hasText(url.getQuery())) {
			throw new MalformedURLException("query part should be empty.");
		}
		else if (StringUtils.hasText(url.getRef())) {
			throw new MalformedURLException("ref part should be empty.");
		}
		else if (StringUtils.hasText(url.getUserInfo())) {
			throw new MalformedURLException("user info part should be empty.");
		}
		urlHandlerImageSource.getImage(url.getPath());
		Resource image = urlHandlerImageSource.getImageResource(url.getPath());
		if (image != null)
			return image.getURL().openConnection();

		throw new IOException("null image returned for key [" + url.getFile() + "].");
	}
}