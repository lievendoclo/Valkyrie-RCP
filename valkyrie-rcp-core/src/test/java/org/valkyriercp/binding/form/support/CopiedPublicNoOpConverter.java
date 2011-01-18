package org.valkyriercp.binding.form.support;

import org.springframework.binding.convert.converters.Converter;

public class CopiedPublicNoOpConverter implements Converter {

	private Class sourceClass;

	private Class targetClass;

	/**
	 * Create a "no op" converter from given source to given target class.
	 */
	public CopiedPublicNoOpConverter(Class sourceClass, Class targetClass) {
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}

	public Class getSourceClass() {
		return sourceClass;
	}

	public Class getTargetClass() {
		return targetClass;
	}

	public Object convertSourceToTargetClass(Object source, Class targetClass) throws Exception {
		return source;
	}

	public boolean isTwoWay() {
		return true;
	}

	public Object convertTargetToSourceClass(Object target, Class sourceClass) throws Exception,
			UnsupportedOperationException {
		return target;
	}
}