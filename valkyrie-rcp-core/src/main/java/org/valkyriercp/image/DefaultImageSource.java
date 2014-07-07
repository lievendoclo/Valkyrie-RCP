package org.valkyriercp.image;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.style.StylerUtils;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A collection of image resources, each indexed by a common key alias.
 * <p>
 * For example, <code>action.edit.copy = /images/edit/copy.gif</code>
 * <p>
 * This class by default performs caching of all loaded image resources using
 * soft references (TODO it just lazy loads them, but it doesn't use
 * SoftReference).
 * 
 * <p>
 * Image resources can be set in the constructor or through the property
 * <code>imageResources</code>. The may should be a String-Resource or
 * String-String where a string value will be converted to a resource using the
 * configured <code>ResourceLoader</code>. Because the value part of the pairs
 * can be a string you can use the following code in your context to configure
 * the map:
 * 
 * <pre>
 * &lt;bean id="imageResourcesFactory" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
 *     &lt;property name="locations"&gt;
 *       &lt;list&gt;
 *         &lt;value&gt;classpath:org/springframework/richclient/image/images.properties&lt;/value&gt;
 *         &lt;value&gt;...your other locations here...&lt;/value&gt;
 *       &lt;/list&gt;
 *     &lt;/property&gt;
 *   &lt;/bean&gt;
 * 
 *   &lt;bean id="imageSource" class="test.DefaultImageSource"&gt;
 *     &lt;property name="brokenImageIndicator"
 *       value="/org/springframework/richclient/images/alert/error_obj.gif" /&gt;
 *     &lt;property name="imageResources" ref="imageResourcesFactory"/&gt;
 *   &lt;/bean&gt;
 * </pre>
 * 
 * Spring 2.5 users can also use the deprecated
 * <code>ResourceMapFactoryBean</code>.
 * 
 * <p>
 * An image {@link Handler} is available that handles the 'image' protocol.
 * Check the javadocs of the handler to know how to use/register it.
 * </p>
 * 
 * @author Keith Donald
 */
@Component
public class DefaultImageSource implements ImageSource {
	protected static final Log logger = LogFactory
			.getLog(DefaultImageSource.class);

	private Map imageResources;

	private ImageCache imageCache;

	private AwtImageResource brokenImageIndicatorResource;

	private Image brokenImageIndicator;

	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * Creates a image resource bundle containing the specified map of keys to
	 * resource paths.
	 * <p>
	 * A custom URL protocol {@link Handler handler}will be installed for the
	 * "image:" protocol. This allows for images in this image source to be
	 * located using the Java URL classes: <br>
	 * <code>URL imageUrl = new URL("image:the.image.key")</code>
	 * 
	 * @param imageResources
	 *            a map of key-to-image-resources.
	 */
	public DefaultImageSource(Map imageResources) {
		this(true, imageResources);
	}

	/**
	 * Creates a image resource bundle containing the specified map of keys to
	 * resource paths.
	 * 
	 * @param installUrlHandler
	 *            should a URL handler be installed.
	 * @param imageResources
	 *            a map of key-to-image-resources.
	 */
	public DefaultImageSource(boolean installUrlHandler, Map imageResources) {
		Assert.notNull(imageResources);
		this.imageResources = new HashMap(imageResources);
		debugPrintResources();
		this.imageCache = new ImageCache();
		if (installUrlHandler) {
			Handler.installImageUrlHandler(this);
		}
	}

	public DefaultImageSource() {
		this.imageCache = new ImageCache();
		Handler.installImageUrlHandler(this);
	}

	public void setImageResources(Map imageResources) {
		this.imageResources = new HashMap(imageResources);
		debugPrintResources();
	}

	private void debugPrintResources() {
		if (logger.isDebugEnabled()) {
			logger.debug("Initialing image source with resources: "
					+ StylerUtils.style(this.imageResources));
		}
	}

	public Image getImage(String key) {
		Assert.notNull(key);
		AwtImageResource resource = getImageResource(key);
		try {
			return (Image) imageCache.get(resource);
		} catch (RuntimeException e) {
			if (brokenImageIndicator != null) {
				return returnBrokenImageIndicator(resource);
			}
			throw e;
		}
	}

	public AwtImageResource getImageResource(String key) {
		Assert.notNull(key);
		Resource resource = null;
		final Object tmp = imageResources.get(key);
		if (tmp instanceof Resource)
			resource = (Resource) tmp;
		if (tmp instanceof String) {
			resource = resourceLoader.getResource((String) tmp);
			Assert.notNull(resourceLoader,
					"Resource loader must be set to resolve resources");
		}
		if (resource == null) {
			throw new NoSuchImageResourceException(key);
		}
		try {
			resource.getInputStream();
			return new AwtImageResource(resource);
		} catch (IOException e) {
			if (brokenImageIndicatorResource == null) {
				throw new NoSuchImageResourceException(resource, e);
			}
			logger.warn("Unable to load image resource at '" + resource
					+ "'; returning the broken image indicator.");
			return brokenImageIndicatorResource;
		}
	}

	public boolean containsKey(Object key) {
		return imageResources.containsKey(key);
	}

	private Image returnBrokenImageIndicator(Resource resource) {
		logger.warn("Unable to load image resource at '" + resource
				+ "'; returning the broken image indicator.");
		return brokenImageIndicator;
	}

	public Image getImageAtLocation(Resource location) {
		try {
			return new AwtImageResource(location).getImage();
		} catch (IOException e) {
			if (brokenImageIndicator == null) {
				throw new NoSuchImageResourceException(location, e);
			}
			return returnBrokenImageIndicator(location);
		}
	}

	public int size() {
		return imageResources.size();
	}

	public void setBrokenImageIndicator(Resource resource) {
		try {
			brokenImageIndicatorResource = new AwtImageResource(resource);
			brokenImageIndicator = brokenImageIndicatorResource.getImage();
		} catch (IOException e) {
			brokenImageIndicatorResource = null;
			throw new NoSuchImageResourceException(resource, e);
		}
	}

	public String toString() {
		return new ToStringCreator(this).append("imageResources",
				imageResources).toString();
	}

	private static class ImageCache extends
			org.valkyriercp.util.CachingMapDecorator {
		public ImageCache() {
			super(true);
		}

		public Object create(Object resource) {
			try {
				return ((AwtImageResource) resource).getImage();
			} catch (IOException e) {
				throw new NoSuchImageResourceException(
						"No image found at resource '" + resource + '"', e);
			}
		}
	}
}