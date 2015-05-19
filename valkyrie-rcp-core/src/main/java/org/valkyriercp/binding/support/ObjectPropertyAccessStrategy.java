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
package org.valkyriercp.binding.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessor;
import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.beans.DefaultMemberPropertyAccessor;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.ValueHolder;

/**
 * An implementation of <code>MutablePropertyAccessStrategy</code> that provides access
 * to the properties of any object.
 *
 * <p>As this class delegates to a <code>DefaultMemberPropertyAccessor</code> for property access,
 * there is full support for <b>nested properties</b> and collection types.
 *
 * @author Arne Limburg
 * @see DefaultMemberPropertyAccessor
 */
public class ObjectPropertyAccessStrategy extends AbstractPropertyAccessStrategy {

    private final DefaultMemberPropertyAccessor propertyAccessor;

    /**
     * Creates a new instance of <tt>ObjectPropertyAccessStrategy</tt>
     * that will provide access to the properties of the provided object.
     *
     * @param target object to be accessed through this class.
     */
    public ObjectPropertyAccessStrategy(Object target) {
        this(new ValueHolder(target));
    }

    /**
     * Creates a new instance of <tt>ObjectPropertyAccessStrategy</tt>
     * that will provide access to the object contained by the provided value model.
     *
     * @param domainObjectHolder value model that holds the object
     *                           to be accessed through this class
     */
    public ObjectPropertyAccessStrategy(final ValueModel domainObjectHolder) {
       this(domainObjectHolder, false);
    }

    /**
     * Creates a new instance of <tt>ObjectPropertyAccessStrategy</tt>
     * that will provide access to the object contained by the provided value model.
     *
     * @param domainObjectHolder value model that holds the object
     *                           to be accessed through this class
     * @param fieldAccessEnabled whether the fields of the objects
     *                           should be accessed directly where possible
     *                           instead of using methods
     */
    public ObjectPropertyAccessStrategy(final ValueModel domainObjectHolder, boolean fieldAccessEnabled) {
       this(domainObjectHolder, fieldAccessEnabled, true);
    }

    /**
     * Creates a new instance of <tt>ObjectPropertyAccessStrategy</tt>
     * that will provide access to the object contained by the provided value model.
     *
     * @param domainObjectHolder value model that holds the object
     *                           to be accessed through this class
     * @param fieldAccessEnabled whether the fields of the objects
     *                           should be accessed directly where possible
     *                           instead of using methods
     * @param strictNullValueHandling whether a <tt>NullValueInNestedPathException</tt>
     *                           should be thrown on nested null-values or <tt>null</tt> should be returned
     */
    public ObjectPropertyAccessStrategy(final ValueModel domainObjectHolder, boolean fieldAccessEnabled, boolean strictNullValueHandling) {
       super(domainObjectHolder);
        propertyAccessor = new DefaultMemberPropertyAccessor(domainObjectHolder.getValue(), fieldAccessEnabled, strictNullValueHandling);
    }

    /**
     * Creates a child instance of <tt>ObjectPropertyAccessStrategy<tt>
     * that will delegate to its parent for property access.
     *
     * @param parent <tt>ObjectPropertyAccessStrategy</tt> which will be used to provide property access
     * @param basePropertyPath property path that will as a base when accessing
     *                         the parent <tt>ObjectPropertyAccessStrategy</tt>
     */
    protected ObjectPropertyAccessStrategy(ObjectPropertyAccessStrategy parent, String basePropertyPath) {
       super(parent, basePropertyPath);
        propertyAccessor = parent.propertyAccessor;
   }

    /**
     * Provides <code>DefaultMemberPropertyAccessor</code> access to subclasses.
     */
    protected PropertyAccessor getPropertyAccessor() {
       return propertyAccessor;
    }

    public MutablePropertyAccessStrategy getPropertyAccessStrategyForPath(String propertyPath) throws BeansException {
        return new ObjectPropertyAccessStrategy(this, getFullPropertyPath(propertyPath));
    }

    public MutablePropertyAccessStrategy newPropertyAccessStrategy(ValueModel domainObjectHolder) {
        return new ObjectPropertyAccessStrategy(domainObjectHolder);
    }

    protected void domainObjectChanged() {
       propertyAccessor.setTarget(getDomainObject());
    }
}
