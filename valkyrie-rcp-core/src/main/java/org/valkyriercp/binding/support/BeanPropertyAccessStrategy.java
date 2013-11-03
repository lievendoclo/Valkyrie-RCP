package org.valkyriercp.binding.support;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessor;
import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.ValueHolder;

/**
 * An implementation of <code>MutablePropertyAccessStrategy</code> that provides access
 * to the properties of a JavaBean.
 *
 * <p>As this class delegates to a <code>BeanWrapper</code> for property access, there is
 * full support for <b>nested properties</b>, enabling the setting/getting
 * of properties on subproperties to an unlimited depth.
 *
 * @author Oliver Hutchison
 * @author Arne Limburg
 * @see org.springframework.beans.BeanWrapper
 */
public class BeanPropertyAccessStrategy extends AbstractPropertyAccessStrategy {

    private final BeanWrapperImpl beanWrapper;

    /**
     * Creates a new instance of BeanPropertyAccessStrategy that will provide access
     * to the properties of the provided JavaBean.
     *
     * @param bean JavaBean to be accessed through this class.
     */
    public BeanPropertyAccessStrategy(Object bean) {
        this(new ValueHolder(bean));
    }

    /**
     * Creates a new instance of BeanPropertyAccessStrategy that will provide access
     * to the JavaBean contained by the provided value model.
     *
     * @param domainObjectHolder value model that holds the JavaBean to
     * be accessed through this class
     */
    public BeanPropertyAccessStrategy(final ValueModel domainObjectHolder) {
    	super(domainObjectHolder);
        this.beanWrapper = new BeanWrapperImpl(false);
        this.beanWrapper. setWrappedInstance(domainObjectHolder.getValue());
    }

    /**
     * Creates a child instance of BeanPropertyAccessStrategy that will delegate to its
     * parent for property access.
     *
     * @param parent BeanPropertyAccessStrategy which will be used to provide property access
     * @param basePropertyPath property path that will as a base when accessing the parent
     * BeanPropertyAccessStrategy
     */
    protected BeanPropertyAccessStrategy(BeanPropertyAccessStrategy parent, String basePropertyPath) {
    	super(parent, basePropertyPath);
        this.beanWrapper = parent.beanWrapper;
    }

    /**
     * Provides <code>BeanWrapper</code> access to subclasses.
     * @return Spring <code>BeanWrapper</code> used to access the bean.
     */
    protected BeanWrapper getBeanWrapper() {
        return beanWrapper;
    }

    /**
     * Provides <code>BeanWrapper</code> access to subclasses.
     * @return Spring <code>BeanWrapper</code> used to access the bean.
     */
    protected PropertyAccessor getPropertyAccessor() {
    	return beanWrapper;
    }

    public MutablePropertyAccessStrategy getPropertyAccessStrategyForPath(String propertyPath) throws BeansException {
        return new BeanPropertyAccessStrategy(this, getFullPropertyPath(propertyPath));
    }

    public MutablePropertyAccessStrategy newPropertyAccessStrategy(ValueModel domainObjectHolder) {
        return new BeanPropertyAccessStrategy(domainObjectHolder);
    }

    protected void domainObjectChanged() {
    	beanWrapper.setWrappedInstance(getDomainObject());
    }
}
