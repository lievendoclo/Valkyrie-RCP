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
package org.valkyriercp.binding.form.support;

import org.springframework.beans.BeanUtils;
import org.springframework.binding.collection.AbstractCachingMapDecorator;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.convert.converters.Converter;
import org.springframework.binding.convert.service.DefaultConversionService;
import org.springframework.binding.convert.service.GenericConversionService;
import org.springframework.util.Assert;
import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.PropertyAccessStrategy;
import org.valkyriercp.binding.PropertyMetadataAccessStrategy;
import org.valkyriercp.binding.form.*;
import org.valkyriercp.binding.support.BeanPropertyAccessStrategy;
import org.valkyriercp.binding.value.CommitTrigger;
import org.valkyriercp.binding.value.DirtyTrackingValueModel;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.BufferedValueModel;
import org.valkyriercp.binding.value.support.MethodInvokingDerivedValueModel;
import org.valkyriercp.binding.value.support.TypeConverter;
import org.valkyriercp.binding.value.support.ValueHolder;
import org.valkyriercp.core.support.AbstractPropertyChangePublisher;
import org.valkyriercp.util.ClassUtils;
import org.valkyriercp.util.EventListenerListHelper;
import org.valkyriercp.util.ValkyrieRepository;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Base implementation of HierarchicalFormModel and ConfigurableFormModel
 * subclasses need only implement the 4 value model interception methods.
 *
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public abstract class AbstractFormModel extends AbstractPropertyChangePublisher implements HierarchicalFormModel,
        ConfigurableFormModel {

	private String id;

	private final FormModelMediatingValueModel formObjectHolder;

	private final MutablePropertyAccessStrategy propertyAccessStrategy;

	private HierarchicalFormModel parent;

	private final List children = new ArrayList();

	private boolean buffered = false;

	private boolean enabled = true;

	private boolean oldEnabled = true;

	private boolean readOnly = false;

	private boolean oldReadOnly = false;

	private boolean authorized = true;

	private boolean oldDirty;

	private boolean oldCommittable = true;

	private final CommitTrigger commitTrigger = new CommitTrigger();

	private final Map mediatingValueModels = new HashMap();

	private final Map propertyValueModels = new HashMap();

	private final Map convertingValueModels = new HashMap();

	private final Map fieldMetadata = new HashMap();

	private final Set dirtyValueAndFormModels = new HashSet();

	private final Map propertyConversionServices = new AbstractCachingMapDecorator() {
		public Object create(Object key) {
			return new DefaultConversionService() {
				protected void addDefaultConverters() {
				}
			};
		}
	};

	protected final PropertyChangeListener parentStateChangeHandler = new ParentStateChangeHandler();

	protected final PropertyChangeListener childStateChangeHandler = new ChildStateChangeHandler();

	private final EventListenerListHelper commitListeners = new EventListenerListHelper(CommitListener.class);

	private Class defaultInstanceClass;
    private ConversionService conversionService;

    protected AbstractFormModel() {
		this(new ValueHolder());
	}

	protected AbstractFormModel(Object domainObject) {
		this(new ValueHolder(domainObject), true);
	}

	public AbstractFormModel(Object domainObject, boolean buffered) {
		this(new ValueHolder(domainObject), buffered);
	}

	protected AbstractFormModel(ValueModel domainObjectHolder, boolean buffered) {
		this(new BeanPropertyAccessStrategy(domainObjectHolder), buffered);
	}

	protected AbstractFormModel(MutablePropertyAccessStrategy propertyAccessStrategy, boolean buffered) {
		ValueModel domainObjectHolder = propertyAccessStrategy.getDomainObjectHolder();
		prepareValueModel(domainObjectHolder);
		this.formObjectHolder = new FormModelMediatingValueModel(domainObjectHolder, false);
		this.propertyAccessStrategy = propertyAccessStrategy;
		this.buffered = buffered;
		if (domainObjectHolder.getValue() != null)
			this.defaultInstanceClass = domainObjectHolder.getValue().getClass();
	}

	/**
	 * Prepare the provided value model for use in this form model.
	 * @param valueModel to prepare
	 */
	protected void prepareValueModel(ValueModel valueModel) {
		if (valueModel instanceof BufferedValueModel) {
			((BufferedValueModel) valueModel).setCommitTrigger(commitTrigger);
		}

		// If the value model that we were built on is "dirty trackable" then we
		// need to monitor it for changes in its dirty state
		if (valueModel instanceof DirtyTrackingValueModel) {
			((DirtyTrackingValueModel) valueModel).addPropertyChangeListener(DIRTY_PROPERTY, childStateChangeHandler);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getFormObject() {
		return getFormObjectHolder().getValue();
	}

	public void setFormObject(Object formObject) {
        setDeliverValueChangeEvents(false);
		if (formObject == null) {
			handleSetNullFormObject();
		}
		else {
			getFormObjectHolder().setValue(formObject);
			setEnabled(true);
		}
		// this will cause all buffered value models to revert
		// to the new form objects property values
		commitTrigger.revert();
        clearValueModelsDirtyState();
		setDeliverValueChangeEvents(true);
	}

    /**
     * Disconnect view from data in MediatingValueModels
     *
     * @param enable <code>true</code> if events should be
     * delivered.
     */
    private void setDeliverValueChangeEvents(boolean enable) {
        formObjectHolder.setDeliverValueChangeEvents(enable);
        for (Object o : mediatingValueModels.values()) {
            FormModelMediatingValueModel valueModel = (FormModelMediatingValueModel) o;
            valueModel.setDeliverValueChangeEvents(enable);
        }
    }

    private void clearValueModelsDirtyState() {
        for (Object o : mediatingValueModels.values()) {
            ((FormModelMediatingValueModel) o).clearDirty();
        }
    }


	private void setDeliverValueChangeEvents(boolean deliverValueChangeEvents, boolean clearValueModels) {
		formObjectHolder.setDeliverValueChangeEvents(deliverValueChangeEvents);

        for (Object o : mediatingValueModels.values()) {
            FormModelMediatingValueModel valueModel = (FormModelMediatingValueModel) o;
            valueModel.setDeliverValueChangeEvents(deliverValueChangeEvents);
        }
	}

	public void setDefaultInstanceClass(Class defaultInstanceClass) {
		this.defaultInstanceClass = defaultInstanceClass;
	}

	public Class getDefaultInstanceClass() {
		return defaultInstanceClass;
	}

	protected void handleSetNullFormObject() {
		if (logger.isInfoEnabled()) {
			logger.info("New form object value is null; resetting to a new fresh object instance and disabling form");
		}
		if (getDefaultInstanceClass() != null) {
			getFormObjectHolder().setValue(BeanUtils.instantiateClass(getDefaultInstanceClass()));
		}
		else { // old behaviour
			getFormObjectHolder().setValue(BeanUtils.instantiateClass(getFormObject().getClass()));
		}
		setEnabled(false);
	}

	/**
	 * Returns the value model which holds the object currently backing this
	 * form.
	 */
	public ValueModel getFormObjectHolder() {
		return formObjectHolder;
	}

	public HierarchicalFormModel getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 *
	 * When the parent is set, the enabled and read-only states are bound and
	 * updated as needed.
	 */
	public void setParent(HierarchicalFormModel parent) {
		Assert.notNull(parent, "parent");
		this.parent = parent;
		this.parent.addPropertyChangeListener(ENABLED_PROPERTY, parentStateChangeHandler);
		this.parent.addPropertyChangeListener(READONLY_PROPERTY, parentStateChangeHandler);
		enabledUpdated();
		readOnlyUpdated();
	}

	public void removeParent() {
		this.parent.removePropertyChangeListener(READONLY_PROPERTY, parentStateChangeHandler);
		this.parent.removePropertyChangeListener(ENABLED_PROPERTY, parentStateChangeHandler);
		this.parent = null;
		readOnlyUpdated();
		enabledUpdated();
	}

	public FormModel[] getChildren() {
		return (FormModel[]) children.toArray(new FormModel[children.size()]);
	}

	/**
	 * Add child to this FormModel. Dirty and committable changes are forwarded
	 * to parent model.
	 * @param child FormModel to add as child.
	 */
	public void addChild(HierarchicalFormModel child) {
		Assert.notNull(child, "child");
		if (child.getParent() == this)
			return;
		Assert.isTrue(child.getParent() == null, "Child form model '" + child + "' already has a parent");
		child.setParent(this);
		children.add(child);
		child.addPropertyChangeListener(DIRTY_PROPERTY, childStateChangeHandler);
		child.addPropertyChangeListener(COMMITTABLE_PROPERTY, childStateChangeHandler);
		if (child.isDirty())
		{
			dirtyValueAndFormModels.add(child);
			dirtyUpdated();
		}
	}

	/**
	 * Remove a child FormModel. Dirty and committable listeners are removed.
	 * When child was dirty, remove the formModel from the dirty list and update
	 * the dirty state.
	 * @param child FormModel to remove from childlist.
	 */
	public void removeChild(HierarchicalFormModel child) {
		Assert.notNull(child, "child");
		child.removeParent();
		children.remove(child);
		child.removePropertyChangeListener(DIRTY_PROPERTY, childStateChangeHandler);
		child.removePropertyChangeListener(COMMITTABLE_PROPERTY, childStateChangeHandler);
		// when dynamically adding/removing childModels take care of
		// dirtymessages:
		// removing child that was dirty: remove from dirty map and update dirty
		// state
		if (dirtyValueAndFormModels.remove(child))
			dirtyUpdated();
	}

	public boolean hasValueModel(String formProperty) {
		return propertyValueModels.containsKey(formProperty);
	}

	public ValueModel getValueModel(String formProperty) {
		ValueModel propertyValueModel = (ValueModel) propertyValueModels.get(formProperty);
		if (propertyValueModel == null) {
			propertyValueModel = add(formProperty);
		}
		return propertyValueModel;
	}

	public ValueModel getValueModel(String formProperty, Class targetClass) {
		final ConvertingValueModelKey key = new ConvertingValueModelKey(formProperty, targetClass);
		ValueModel convertingValueModel = (ValueModel) convertingValueModels.get(key);
		if (convertingValueModel == null) {
			convertingValueModel = createConvertingValueModel(formProperty, targetClass);
			convertingValueModels.put(key, convertingValueModel);
		}
		return convertingValueModel;
	}

	/**
	 * Creates a new value mode for the the given property. Usually delegates to
	 * the underlying property access strategy but subclasses may provide
	 * alternative value model creation strategies.
	 */
	protected ValueModel createValueModel(String formProperty) {
		Assert.notNull(formProperty, "formProperty");
		if (logger.isDebugEnabled()) {
			logger.debug("Creating " + (buffered ? "buffered" : "") + " value model for form property '" + formProperty
                    + "'.");
		}
		return buffered ? new BufferedValueModel(propertyAccessStrategy.getPropertyValueModel(formProperty))
				: propertyAccessStrategy.getPropertyValueModel(formProperty);
	}

	protected ValueModel createConvertingValueModel(String formProperty, Class targetClass) {
		if (logger.isDebugEnabled()) {
			logger.debug("Creating converting value model for form property '" + formProperty
					+ "' converting to type '" + targetClass + "'.");
		}
		final ValueModel sourceValueModel = getValueModel(formProperty);
		Assert.notNull(sourceValueModel, "Form does not have a property called '" + formProperty + "'.");
		final Class sourceClass = ClassUtils
				.convertPrimitiveToWrapper(getFieldMetadata(formProperty).getPropertyType());
		// sour.required(ceClass can be null when using eg Map, assume that given
		// targetClass is the correct one
		if ((sourceClass == null) || (sourceClass == targetClass)) {
			return sourceValueModel;
		}

		final ConversionService conversionService = getConversionService();
		ConversionExecutor convertTo = null;
		ConversionExecutor convertFrom = null;

		// Check for locally registered property converters
		if (propertyConversionServices.containsKey(formProperty)) {
			// TODO - extract ConfigurableConversionService interface...
			final GenericConversionService propertyConversionService = (GenericConversionService) propertyConversionServices
					.get(formProperty);

			if (propertyConversionService != null) {
				convertTo = propertyConversionService.getConversionExecutor(sourceClass, targetClass);
				convertFrom = propertyConversionService.getConversionExecutor(targetClass, sourceClass);
			}
		}

		// If we have nothing from the property level, then try the conversion
		// service
		if (convertTo == null) {
			convertTo = conversionService.getConversionExecutor(sourceClass, targetClass);
		}
		Assert.notNull(convertTo, "conversionService returned null ConversionExecutor");

		if (convertFrom == null) {
			convertFrom = conversionService.getConversionExecutor(targetClass, sourceClass);
		}
		Assert.notNull(convertFrom, "conversionService returned null ConversionExecutor");

		ValueModel convertingValueModel = preProcessNewConvertingValueModel(formProperty, targetClass,
				new TypeConverter(sourceValueModel, convertTo, convertFrom));
		preProcessNewConvertingValueModel(formProperty, targetClass, convertingValueModel);
		return convertingValueModel;
	}

	/**
	 * Register converters for a given property name.
	 * @param propertyName name of property on which to register converters
	 * @param toConverter Convert from source to target type
	 * @param fromConverter Convert from target to source type
	 */
	public void registerPropertyConverter(String propertyName, Converter toConverter, Converter fromConverter) {
		DefaultConversionService propertyConversionService = (DefaultConversionService) propertyConversionServices
				.get(propertyName);
		propertyConversionService.addConverter(toConverter);
		propertyConversionService.addConverter(fromConverter);
	}

	public ValueModel add(String propertyName) {
		return add(propertyName, createValueModel(propertyName));
	}

	public ValueModel add(String formProperty, ValueModel valueModel) {
		// XXX: this assert should be active but it breaks the
		// code in SwingBindingFactory#createBoundListModel
		// Assert.isTrue(!hasValueModel(formProperty), "A property called '" +
		// formProperty + "' already exists.");
		if (valueModel instanceof BufferedValueModel) {
			((BufferedValueModel) valueModel).setCommitTrigger(commitTrigger);
		}

		PropertyMetadataAccessStrategy metadataAccessStrategy = getFormObjectPropertyAccessStrategy()
				.getMetadataAccessStrategy();

		FormModelMediatingValueModel mediatingValueModel = new FormModelMediatingValueModel(valueModel,
				metadataAccessStrategy.isWriteable(formProperty));
		mediatingValueModels.put(formProperty, mediatingValueModel);

		FieldMetadata metadata = new DefaultFieldMetadata(this, mediatingValueModel, metadataAccessStrategy
				.getPropertyType(formProperty), !metadataAccessStrategy.isWriteable(formProperty),
				metadataAccessStrategy.getAllUserMetadata(formProperty));
		metadata.addPropertyChangeListener(FieldMetadata.DIRTY_PROPERTY, childStateChangeHandler);
		return add(formProperty, mediatingValueModel, metadata);
	}

	/**
	 * {@inheritDoc}
	 */
	public ValueModel add(String propertyName, ValueModel valueModel, FieldMetadata metadata) {
		fieldMetadata.put(propertyName, metadata);

		valueModel = preProcessNewValueModel(propertyName, valueModel);
		propertyValueModels.put(propertyName, valueModel);

		if (logger.isDebugEnabled()) {
			logger.debug("Registering '" + propertyName + "' form property, property value model=" + valueModel);
		}
		postProcessNewValueModel(propertyName, valueModel);
		return valueModel;
	}

	/**
	 * Provides a hook for subclasses to optionally decorate a new value model
	 * added to this form model.
	 */
	protected abstract ValueModel preProcessNewValueModel(String formProperty, ValueModel formValueModel);

	/**
	 * Provides a hook for subclasses to perform some processing after a new
	 * value model has been added to this form model.
	 */
	protected abstract void postProcessNewValueModel(String formProperty, ValueModel valueModel);

	/**
	 * Provides a hook for subclasses to optionally decorate a new converting
	 * value model added to this form model.
	 */
	protected abstract ValueModel preProcessNewConvertingValueModel(String formProperty, Class targetClass,
			ValueModel formValueModel);

	/**
	 * Provides a hook for subclasses to perform some processing after a new
	 * converting value model has been added to this form model.
	 */
	protected abstract void postProcessNewConvertingValueModel(String formProperty, Class targetClass,
			ValueModel valueModel);

	public FieldMetadata getFieldMetadata(String propertyName) {
		FieldMetadata metadata = (FieldMetadata) fieldMetadata.get(propertyName);
		if (metadata == null) {
			add(propertyName);
			metadata = (FieldMetadata) fieldMetadata.get(propertyName);
		}
		return metadata;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set getFieldNames() {
		return Collections.unmodifiableSet(propertyValueModels.keySet());
	}

	protected FieldFaceSource getFieldFaceSource() {
		return ValkyrieRepository.getInstance().getApplicationConfig().fieldFaceSource();
	}

	public FieldFace getFieldFace(String field) {
		return getFieldFaceSource().getFieldFace(field, this);
	}

	public ValueModel addMethod(String propertyMethodName, String derivedFromProperty) {
		return addMethod(propertyMethodName, new String[] { derivedFromProperty });
	}

	public ValueModel addMethod(String propertyMethodName, String[] derivedFromProperties) {
		ValueModel[] propertyValueModels = new ValueModel[derivedFromProperties.length];
		for (int i = 0; i < propertyValueModels.length; i++) {
			propertyValueModels[i] = getValueModel(derivedFromProperties[i]);
		}
		ValueModel valueModel = new MethodInvokingDerivedValueModel(this, propertyMethodName, propertyValueModels);
		return add(propertyMethodName, valueModel);
	}

	public ConversionService getConversionService() {
        if(conversionService == null)
		    return ValkyrieRepository.getInstance().getApplicationConfig().conversionService();
        return conversionService;
	}

	public MutablePropertyAccessStrategy getFormObjectPropertyAccessStrategy() {
		return propertyAccessStrategy;
	}

	public PropertyAccessStrategy getPropertyAccessStrategy() {
		return new FormModelPropertyAccessStrategy(this);
	}

	public void commit() {
		if (logger.isDebugEnabled()) {
			logger.debug("Commit requested for this form model " + this);
		}
		if (getFormObject() == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Form object is null; nothing to commit.");
			}
			return;
		}
		if (isCommittable()) {
			for (Iterator i = commitListeners.iterator(); i.hasNext();) {
				((CommitListener) i.next()).preCommit(this);
			}
			preCommit();
			if (isCommittable()) {
				doCommit();
				postCommit();
				for (Iterator i = commitListeners.iterator(); i.hasNext();) {
					((CommitListener) i.next()).postCommit(this);
				}
			}
			else {
				throw new IllegalStateException("Form model '" + this
						+ "' became non-committable after preCommit phase");
			}
		}
		else {
			throw new IllegalStateException("Form model '" + this + "' is not committable");
		}
	}

	private void doCommit() {
		for (Iterator i = children.iterator(); i.hasNext();) {
			((FormModel) i.next()).commit();
		}
		commitTrigger.commit();
		for (Iterator i = mediatingValueModels.values().iterator(); i.hasNext();) {
			((DirtyTrackingValueModel) i.next()).clearDirty();
		}
	}

	/**
	 * Hook for subclasses to intercept before a commit.
	 */
	protected void preCommit() {
	}

	/**
	 * Hook for subclasses to intercept after a successful commit has finished.
	 */
	protected void postCommit() {
	}

	/**
	 * Revert state. If formModel has children, these will be reverted first.
	 * CommitTrigger is used to revert bufferedValueModels while
	 * revertToOriginal() is called upon FormMediatingValueModels.
	 */
	public void revert() {
		for (Iterator i = children.iterator(); i.hasNext();) {
			((FormModel) i.next()).revert();
		}
		// this will cause all buffered value models to revert
		commitTrigger.revert();
		// this will then go back and revert all unbuffered value models
		for (Iterator i = mediatingValueModels.values().iterator(); i.hasNext();) {
			((DirtyTrackingValueModel) i.next()).revertToOriginal();
		}
	}

	/**
	 * Complex forms with parent-child relations can use derived formModels.
	 * Such a Hierarchical tree cannot have its children reset on its own as it
	 * would break the top-down structure. see RCP-329 and the cvs maillist.
	 *
	 * TODO add a unit test with such a complex use case
	 *
	 * @see FormModel#reset()
	 */
	public void reset() {
		setFormObject(null);
	}

	public boolean isBuffered() {
		return buffered;
	}

	/**
	 * Returns <code>true</code> if this formModel or any of its children has
	 * dirty valueModels.
	 */
	public boolean isDirty() {
		return dirtyValueAndFormModels.size() > 0;
	}

	/**
	 * Fires the necessary property change event for changes to the dirty
	 * property. Must be called whenever the value of dirty is changed.
	 */
	protected void dirtyUpdated() {
		boolean dirty = isDirty();
		if (hasChanged(oldDirty, dirty)) {
			oldDirty = dirty;
			firePropertyChange(DIRTY_PROPERTY, !dirty, dirty);
		}
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		readOnlyUpdated();
	}

	public boolean isReadOnly() {
		return readOnly || !authorized || (parent != null && parent.isReadOnly());
	}

	/**
	 * Check if the form has the correct authorization and can be edited.
	 *
	 * @return <code>true</code> if this form is authorized and may be edited.
	 */
	public boolean isAuthorized() {
		return authorized;
	}

	/**
	 * Set whether or not the form is authorized and can be edited.
	 *
	 * @param authorized <code>true</code> if this form may be edited.
	 */
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
		readOnlyUpdated();
	}

	/**
	 * Fires the necessary property change event for changes to the readOnly
	 * property. Must be called whenever the value of readOnly is changed.
	 */
	protected void readOnlyUpdated() {
		boolean localReadOnly = isReadOnly();
		if (hasChanged(oldReadOnly, localReadOnly)) {
			oldReadOnly = localReadOnly;
			firePropertyChange(READONLY_PROPERTY, !localReadOnly, localReadOnly);
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		enabledUpdated();
	}

	public boolean isEnabled() {
		return enabled && (parent == null || parent.isEnabled());
	}

	/**
	 * Fires the necessary property change event for changes to the enabled
	 * property. Must be called whenever the value of enabled is changed.
	 */
	protected void enabledUpdated() {
		boolean enabled = isEnabled();
		if (hasChanged(oldEnabled, enabled)) {
			oldEnabled = enabled;
			firePropertyChange(ENABLED_PROPERTY, !enabled, enabled);
		}
	}

	public boolean isCommittable() {
		for (Iterator i = children.iterator(); i.hasNext();) {
			final FormModel childFormModel = (FormModel) i.next();
			if (!childFormModel.isCommittable()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Fires the necessary property change event for changes to the committable
	 * property. Must be called whenever the value of committable is changed.
	 */
	protected void committableUpdated() {
		boolean committable = isCommittable();
		if (hasChanged(oldCommittable, committable)) {
			oldCommittable = committable;
			firePropertyChange(COMMITTABLE_PROPERTY, !committable, committable);
		}
	}

	public void addCommitListener(CommitListener listener) {
		commitListeners.add(listener);
	}

	public void removeCommitListener(CommitListener listener) {
		commitListeners.remove(listener);
	}

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
	 * Listener to be registered on properties of the parent form model. Calls
	 * are delegated to
	 * {@link AbstractFormModel#parentStateChanged(java.beans.PropertyChangeEvent)}. This
	 * way subclasses can extend the parent-&gt;child behaviour meaning state
	 * changes in the parent that influence the children.
	 */
	protected class ParentStateChangeHandler implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			parentStateChanged(evt);
		}
	}

	/**
	 * Events from the parent form model that have side-effects on this form
	 * model should be handled here. This includes:
	 *
	 * <ul>
	 * <li><em>Enabled state:</em> when parent gets disabled, child should be
	 * disabled as well. If parent is enabled, child should go back to its
	 * original state.</li>
	 * <li><em>Read-only state:</em> when a parent is set read-only, child
	 * should be read-only as well. If parent is set editable, child should go
	 * back to its original state.</li>
	 * </ul>
	 */
	protected void parentStateChanged(PropertyChangeEvent evt) {
		if (ENABLED_PROPERTY.equals(evt.getPropertyName())) {
			enabledUpdated();
		}
		else if (READONLY_PROPERTY.equals(evt.getPropertyName())) {
			readOnlyUpdated();
		}
	}

	/**
	 * Listener to be registered on properties of child form models and other
	 * valueModels. Calls are delegated to
	 * {@link AbstractFormModel#childStateChanged(PropertyChangeEvent)}. This
	 * way subclasses can extend the child-&gt;parent behaviour meaning state
	 * changes in the child that influence the parent.
	 */
	protected class ChildStateChangeHandler implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			childStateChanged(evt);
		}
	}

	/**
	 * Events from the child form model or value models that have side-effects
	 * on this form model should be handled here. This includes:
	 *
	 * <ul>
	 * <li><em>Dirty state:</em> when a child is dirty, the parent should be
	 * dirty.</li>
	 * <li><em>Committable state:</em> when a child is committable, the
	 * parent might be committable as well. The committable state of the parent
	 * should be taken into account and revised when a child sends this event.</li>
	 * </ul>
	 *
	 * Note that we include value models and their metadata as being children.
	 * As these are low level models, they cannot be parents and therefore don't
	 * show up in
	 * {@link AbstractFormModel#parentStateChanged(PropertyChangeEvent)}.
	 */
	protected void childStateChanged(PropertyChangeEvent evt) {
		if (FormModel.DIRTY_PROPERTY.equals(evt.getPropertyName())) {
			Object source = evt.getSource();

			if (source instanceof FieldMetadata) {
				FieldMetadata metadata = (FieldMetadata) source;
				if (metadata.isDirty()) {
					dirtyValueAndFormModels.add(metadata);
				}
				else {
					dirtyValueAndFormModels.remove(metadata);
				}
			}
			else if (source instanceof FormModel) {
				FormModel formModel = (FormModel) source;
				if (formModel.isDirty()) {
					dirtyValueAndFormModels.add(formModel);
				}
				else {
					dirtyValueAndFormModels.remove(formModel);
				}
			}
			else {
				DirtyTrackingValueModel valueModel = (DirtyTrackingValueModel) source;
				if (valueModel.isDirty()) {
					dirtyValueAndFormModels.add(valueModel);
				}
				else {
					dirtyValueAndFormModels.remove(valueModel);
				}
			}
			dirtyUpdated();
		}
		else if (COMMITTABLE_PROPERTY.equals(evt.getPropertyName())) {
			committableUpdated();
		}
	}

	/**
	 * Class for keys in the convertingValueModels map.
	 */
	protected static class ConvertingValueModelKey {

		private final String propertyName;

		private final Class targetClass;

		public ConvertingValueModelKey(String propertyName, Class targetClass) {
			this.propertyName = propertyName;
			this.targetClass = targetClass;
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof ConvertingValueModelKey))
				return false;

			final ConvertingValueModelKey key = (ConvertingValueModelKey) o;
			return propertyName.equals(key.propertyName) && (targetClass == key.targetClass);
		}

		public int hashCode() {
			return (propertyName.hashCode() * 29) + (targetClass == null ? 7 : targetClass.hashCode());
		}
	}
}