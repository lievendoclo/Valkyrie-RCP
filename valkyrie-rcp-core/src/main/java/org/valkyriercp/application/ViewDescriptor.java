package org.valkyriercp.application;

/**
 * Metadata about a view; a view descriptor is effectively a singleton view
 * definition. A descriptor also acts as a factory which produces new instances
 * of a given view when requested, typically by a requesting application page. A
 * view descriptor can also produce a command which launches a view for display
 * on the page within the current active window.
 *
 * @author Keith Donald
 */
public interface ViewDescriptor extends PageComponentDescriptor {
}
