package org.valkyriercp.rules.reporting;

import org.springframework.util.Assert;

public class DefaultBeanPropertyNameRenderer implements BeanPropertyNameRenderer {

	/**
	 * Renders a unqualified bean property string in the format:
	 * "parentProperty.myBeanProperty" like "ParentProperty.MyBeanProperty".
	 * @param qualifiedName
	 * @return the formatted string
	 */
	public String renderQualifiedName(String qualifiedName) {
		Assert.notNull(qualifiedName, "No qualified name specified");
		StringBuffer sb = new StringBuffer(qualifiedName.length() + 5);
		char[] chars = qualifiedName.toCharArray();
		sb.append(Character.toUpperCase(chars[0]));
		boolean foundDot = false;
		for (int i = 1; i < chars.length; i++) {
			char c = chars[i];
			if (Character.isLetter(c)) {
				if (Character.isLowerCase(c)) {
					if (foundDot) {
						sb.append(Character.toUpperCase(c));
						foundDot = false;
					}
					else {
						sb.append(c);
					}
				}
				else {
					sb.append(c);
				}
			}
			else if (c == '.') {
				sb.append(c);
				foundDot = true;
			}
		}
		return sb.toString();
	}

	/**
	 * Renders a unqualified bean property string in the format:
	 * "myBeanProperty" like "My Bean Property".
	 * @return the formatted string
	 */
	public String renderShortName(String shortName) {
		Assert.notNull(shortName, "No short name specified");
		StringBuffer sb = new StringBuffer(shortName.length() + 5);
		char[] chars = shortName.toCharArray();
		sb.append(Character.toUpperCase(chars[0]));
		for (int i = 1; i < chars.length; i++) {
			char c = chars[i];
			if (Character.isUpperCase(c)) {
				sb.append(' ');
			}
			sb.append(c);
		}
		return sb.toString();
	}
}