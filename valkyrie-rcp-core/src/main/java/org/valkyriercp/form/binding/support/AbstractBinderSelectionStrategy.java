package org.valkyriercp.form.binding.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.ClassEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.enums.LabeledEnum;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.BinderSelectionStrategy;
import org.valkyriercp.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Default implementation of <code>BinderSelectionStrategy</code>. Provides for
 * registering of binders by control type, property type and property name.
 *
 * @author Oliver Hutchison
 * @author Jim Moore
 */
public abstract class AbstractBinderSelectionStrategy implements BinderSelectionStrategy {

    private final Class defaultControlType;

    private final ClassEditor classEditor = new ClassEditor();

    private final Map controlTypeBinders = new HashMap();

    private final Map propertyTypeBinders = new HashMap();

    private final Map propertyNameBinders = new HashMap();

    private List bindersForPropertyNames = new ArrayList();

    @Autowired
    private ApplicationContext applicationContext;

    public AbstractBinderSelectionStrategy(Class defaultControlType) {
        this.defaultControlType = defaultControlType;
    }

    public Binder selectBinder(FormModel formModel, String propertyName) {
        // first try and find a binder for the specific property name
        Binder binder = findBinderByPropertyName(formModel.getFormObject().getClass(), propertyName);
        if (binder == null) {
            // next try and find a binder for the specific property type
            binder = findBinderByPropertyType(getPropertyType(formModel, propertyName));
        }
        if (binder == null) {
            // just find a binder for the default control type
            binder = selectBinder(defaultControlType, formModel, propertyName);
        }
        if (binder != null) {
            return binder;
        }
        throw new UnsupportedOperationException("Unable to select a binder for form model [" + formModel
                + "] property [" + propertyName + "]");
    }

    public Binder selectBinder(Class controlType, FormModel formModel, String propertyName) {
        Binder binder = findBinderByControlType(controlType);
        if (binder == null) {
            binder = selectBinder(formModel, propertyName);
        }
        if (binder != null) {
            return binder;
        }
        throw new UnsupportedOperationException("Unable to select a binder for form model [" + formModel
                + "] property [" + propertyName + "]");
    }

    /**
     * Try to find a binder for the provided parentObjectType and propertyName. If no
     * direct match found try to find binder for any superclass of the provided
     * objectType which also has the same propertyName.
     */
    protected Binder findBinderByPropertyName(Class parentObjectType, String propertyName) {
        PropertyNameKey key = new PropertyNameKey(parentObjectType, propertyName);
        Binder binder = (Binder)propertyNameBinders.get(key);
        if (binder == null) {
            // if no direct match was found try to find a match in any super classes
            final Map potentialMatchingBinders = new HashMap();
            for (Iterator i = propertyNameBinders.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry)i.next();
                if (((PropertyNameKey)entry.getKey()).getPropertyName().equals(propertyName)) {
                    potentialMatchingBinders.put(((PropertyNameKey)entry.getKey()).getParentObjectType(),
                            entry.getValue());
                }
            }
            binder = (Binder) ClassUtils.getValueFromMapForClass(parentObjectType, potentialMatchingBinders);
            if (binder != null) {
                // remember the lookup so it doesn't have to be discovered again
                registerBinderForPropertyName(parentObjectType, propertyName, binder);
            }
        }
        return binder;
    }

    /**
     * Try to find a binder for the provided propertyType. If no direct match found,
     * try to find binder for closest superclass of the given control type.
     */
    protected Binder findBinderByPropertyType(Class propertyType) {
        return (Binder)ClassUtils.getValueFromMapForClass(propertyType, propertyTypeBinders);
    }

    /**
     * Try to find a binder for the provided controlType. If no direct match found,
     * try to find binder for closest superclass of the given control type.
     */
    protected Binder findBinderByControlType(Class controlType) {
        return (Binder)ClassUtils.getValueFromMapForClass(controlType, controlTypeBinders);
    }

    @Override
    public void registerBinderForPropertyName(Class parentObjectType, String propertyName, Binder binder) {
        propertyNameBinders.put(new PropertyNameKey(parentObjectType, propertyName), binder);
    }

    /**
     * Add a list of binders that are bound to propertyNames. Each element in the list should
     * be a Properties element describing the binder and propertyName. For more information
     * about the structure of the properties see {@link #setBinderForPropertyName(Properties)}.

     * <br><br>
     *  &lt;list&gt;<br>
     *   &lt;props&gt;<br>
     *     &lt;prop key="..."&gt;...&lt;/prop&gt;<br>
     *     &lt;!-- More info in docs of setBinderForPropertyName(Properties)--&gt;<br>
     *   &lt;/props&gt;<br>
     *  &lt;/list&gt;<br>
     *
     * @param binders List of <code>Properties</code> elements
     * @see #setBinderForPropertyName(Properties)
     */
    public void setBindersForPropertyNames(List binders)
    {
        bindersForPropertyNames = binders;
    }

    /**
     * Create/link a <code>Binder</code> to a propertyName from the given <code>Properties</code>.
     * <p>
     * The used keys are:
     * <ul>
     * <li><b>objectClass</b>: The bean which has the property.
     * <li><b>propertyName</b>: The property that will need the binder.
     * <li><b>binder</b>: The Fully Qualified ClassName that will be used to instantiate the <code>Binder</code>.
     * <li><b>binderRef</b>: The beanId that identifies the <code>Binder</code> which is defined elsewhere.
     * </ul>
     * <p>
     * The first two keys are mandatory in combination with one of the two latter (binder or binderRef)
     * The following two cases can be used to define a binder/propertyName combination:
     * <br><br>
     *  &lt;props&gt;<br>
     *    &lt;prop key="objectClass"&gt;mypackage.MyBean&lt;/prop&gt;<br>
     *    &lt;prop key="propertyName"&gt;myProperty&lt;/prop&gt;<br>
     *    &lt;prop key="binder"&gt;mypackage.MyBinder&lt;/prop&gt;<br>
     *  &lt;/props&gt;<br>
     *  <br>
     *  &lt;props&gt;<br>
     *    &lt;prop key="objectClass"&gt;mypackage.MyBean&lt;/prop&gt;<br>
     *    &lt;prop key="propertyName"&gt;myProperty&lt;/prop&gt;<br>
     *    &lt;prop key="binderRef"&gt;myBinderBeanId&lt;/prop&gt;<br>
     *    &lt;!-- myBinderBeanId identifies a bean defined elsewhere--&gt;<br>
     *  &lt;/props&gt;<br>
     *
     * @param binder The <code>Properties</code> object containing the correct keys.
     */
    public void setBinderForPropertyName(Properties binder)
    {
        String objectClassName = (String) binder.get("objectClass");
        if (objectClassName == null)
            throw new IllegalArgumentException("objectClass is required");

        classEditor.setAsText(objectClassName);
        Class objectClass = (Class) classEditor.getValue();

        String propertyName = (String) binder.get("propertyName");
        if (propertyName == null)
            throw new IllegalArgumentException("propertyName is required");

        if (binder.containsKey("binder"))
        {
            Object binderParameter = binder.get("binder");
            classEditor.setAsText((String) binderParameter);
            Class binderClass = (Class) classEditor.getValue();
            try
            {
                registerBinderForPropertyName(objectClass, propertyName, (Binder) binderClass.newInstance());
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException(
                        "Could not instantiate new binder with default constructor: " + binderParameter);
            }
        }
        else if (binder.containsKey("binderRef"))
        {
            String binderID = (String) binder.get("binderRef");
            Binder binderBean = (Binder) getApplicationContext().getBean(binderID);
            registerBinderForPropertyName(objectClass, propertyName, binderBean);
        }
        else
            throw new IllegalArgumentException("binder or binderRef is required");
    }

    @Override
    public void registerBinderForPropertyType(Class propertyType, Binder binder) {
        propertyTypeBinders.put(propertyType, binder);
    }

    /**
     * Registers property type binders by extracting the key and value from each entry
     * in the provided map using the key to specify the property type and the value
     * to specify the binder.
     *
     * <p>Binders specified in the provided map will override any binders previously
     * registered for the same property type.
     * @param binders the map containing the entries to register; keys must be of type
     * <code>Class</code> and values of type <code>Binder</code>.
     */
    public void setBindersForPropertyTypes(Map binders) {
        for (Iterator i = binders.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();
            registerBinderForPropertyType((Class)entry.getKey(), (Binder)entry.getValue());
        }
    }

    @Override
    public void registerBinderForControlType(Class controlType, Binder binder) {
        controlTypeBinders.put(controlType, binder);
    }

    /**
     * Registers control type binders by extracting the key and value from each entry
     * in the provided map using the key to specify the property type and the value
     * to specify the binder.
     *
     * <p>Binders specified in the provided map will override any binders previously
     * registered for the same control type.
     * @param binders the map containing the entries to register; keys must be of type
     * <code>Class</code> and values of type <code>Binder</code>.
     */
    public void setBindersForControlTypes(Map binders) {
        for (Iterator i = binders.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();
            registerBinderForControlType((Class)entry.getKey(), (Binder)entry.getValue());
        }
    }

    protected Class getPropertyType(FormModel formModel, String formPropertyPath) {
        return formModel.getFieldMetadata(formPropertyPath).getPropertyType();
    }

    protected boolean isEnumeration(FormModel formModel, String formPropertyPath) {
        return LabeledEnum.class.isAssignableFrom(getPropertyType(formModel, formPropertyPath));
    }

    private static class PropertyNameKey {
        private final Class parentObjectType;

        private final String propertyName;

        public PropertyNameKey(Class parentObjectType, String propertyName) {
            Assert.notNull(parentObjectType, "parentObjectType must not be null.");
            Assert.notNull(propertyName, "propertyName must not be null.");
            this.parentObjectType = parentObjectType;
            this.propertyName = propertyName;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public Class getParentObjectType() {
            return parentObjectType;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PropertyNameKey)) {
                return false;
            }
            final PropertyNameKey propertyNameKey = (PropertyNameKey)o;
            return propertyName.equals(propertyNameKey.propertyName)
                    && parentObjectType.equals(propertyNameKey.parentObjectType);
        }

        public int hashCode() {
            return (propertyName.hashCode() * 29) + parentObjectType.hashCode();
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    protected ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception
    {
        for (Iterator i = bindersForPropertyNames.iterator(); i.hasNext();)
        {
            setBinderForPropertyName((Properties) i.next());
        }
    }
}
