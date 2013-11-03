package org.valkyriercp.rules.reporting;

import org.springframework.context.MessageSourceResolvable;

/**
 * @author Keith Donald
 */
public class TypeResolvableSupport implements TypeResolvable,
        MessageSourceResolvable {

	private String type;

	public TypeResolvableSupport() {

	}

	public TypeResolvableSupport(String type) {
		setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object[] getArguments() {
		return null;
	}

	public String[] getCodes() {
		return new String[]{type};
	}

	public String getDefaultMessage() {
		return type;
	}

}
