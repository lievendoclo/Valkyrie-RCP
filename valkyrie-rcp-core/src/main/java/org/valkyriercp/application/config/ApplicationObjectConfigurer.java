package org.valkyriercp.application.config;

/**
 * Facade interface for configuring application objects. Relieves the burden of
 * the objects having to configure themselves.
 *
 * @author Keith Donald
 */
public interface ApplicationObjectConfigurer {

    /**
     * Configure the specified object.
     *
     * @param applicationObject The object to be configured. Must not be null.
     * @param objectName A name for the object that is unique within the application. Must not be null.
     *
     * @throws IllegalArgumentException if either argument is null.
     */
    public void configure(Object applicationObject, String objectName);

}

