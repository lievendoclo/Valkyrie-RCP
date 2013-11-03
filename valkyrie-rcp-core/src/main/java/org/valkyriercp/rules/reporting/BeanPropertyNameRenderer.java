package org.valkyriercp.rules.reporting;

/**
 * A interface for rendering bean properties.  For example, a
 * SpacedBeanPropertyRenderer might render the property
 * 'source.address.netmask' like 'Source Address Netmask'
 *
 * @author  Keith Donald
 */
public interface BeanPropertyNameRenderer {

	public String renderQualifiedName(String qualifiedName);

	public String renderShortName(String shortName);
}