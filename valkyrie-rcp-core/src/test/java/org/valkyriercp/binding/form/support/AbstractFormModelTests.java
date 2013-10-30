package org.valkyriercp.binding.form.support;

import org.junit.Test;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.convert.converters.StringToInteger;
import org.springframework.binding.convert.service.DefaultConversionService;
import org.springframework.binding.convert.service.GenericConversionService;
import org.springframework.binding.convert.service.StaticConversionExecutor;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.binding.form.CommitListener;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.support.BeanPropertyAccessStrategy;
import org.valkyriercp.test.TestBean;
import org.valkyriercp.test.TestPropertyChangeListener;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.ValueHolder;
import org.valkyriercp.test.TestBean;
import org.valkyriercp.test.TestPropertyChangeListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.Assert.*;

/**
 * Tests for
 * @link AbstractFormModel
 *
 * @author Oliver Hutchison
 */
public abstract class AbstractFormModelTests extends AbstractValkyrieTest {

	protected AbstractFormModel getFormModel(Object formObject) {
		return new TestAbstractFormModel(formObject);
	}

	protected AbstractFormModel getFormModel(BeanPropertyAccessStrategy pas, boolean buffering) {
		return new TestAbstractFormModel(pas, buffering);
	}

	protected AbstractFormModel getFormModel(ValueModel valueModel, boolean buffering) {
		return new TestAbstractFormModel(valueModel, buffering);
	}

    @Test
	public void testGetValueModelFromPAS() {
		TestBean p = new TestBean();
		TestPropertyAccessStrategy tpas = new TestPropertyAccessStrategy(p);
		AbstractFormModel fm = getFormModel(tpas, true);
		ValueModel vm1 = fm.getValueModel("simpleProperty");
		assertEquals(1, tpas.numValueModelRequests());
		assertEquals("simpleProperty", tpas.lastRequestedValueModel());
		ValueModel vm2 = fm.getValueModel("simpleProperty");
		assertEquals(vm1, vm2);
		assertEquals(1, tpas.numValueModelRequests());

		try {
			fm.getValueModel("iDontExist");
			fail("should't be able to get value model for invalid property");
		}
		catch (NotReadablePropertyException e) {
			// exprected
		}
	}

    @Test
	public void testUnbufferedWritesThrough() {
		TestBean p = new TestBean();
		BeanPropertyAccessStrategy pas = new BeanPropertyAccessStrategy(p);
		AbstractFormModel fm = getFormModel(pas, false);
		ValueModel vm = fm.getValueModel("simpleProperty");

		vm.setValue("1");
		assertEquals("1", p.getSimpleProperty());

		vm.setValue(null);
		assertEquals(null, p.getSimpleProperty());
	}

    @Test
	public void testBufferedDoesNotWriteThrough() {
		TestBean p = new TestBean();
		BeanPropertyAccessStrategy pas = new BeanPropertyAccessStrategy(p);
		AbstractFormModel fm = getFormModel(pas, true);
		ValueModel vm = fm.getValueModel("simpleProperty");

		vm.setValue("1");
		assertEquals(null, p.getSimpleProperty());

		vm.setValue(null);
		assertEquals(null, p.getSimpleProperty());
	}

    @Test
	public void testDirtyTrackingWithBuffering() {
		testDirtyTracking(true);
	}

    @Test
	public void testDirtyTrackingWithoutBuffering() {
		testDirtyTracking(false);
	}

	public void testDirtyTracking(boolean buffering) {
		TestBean p = new TestBean();
		BeanPropertyAccessStrategy pas = new BeanPropertyAccessStrategy(p);
		TestPropertyChangeListener pcl = new TestPropertyChangeListener(FormModel.DIRTY_PROPERTY);
		AbstractFormModel fm = getFormModel(pas, buffering);
		fm.addPropertyChangeListener(FormModel.DIRTY_PROPERTY, pcl);
		ValueModel vm = fm.getValueModel("simpleProperty");
		assertTrue(!fm.isDirty());

		vm.setValue("2");
		assertTrue(fm.isDirty());
		assertEquals(1, pcl.eventCount());

		fm.commit();
		assertTrue(!fm.isDirty());
		assertEquals(2, pcl.eventCount());

		vm.setValue("1");
		assertTrue(fm.isDirty());
		assertEquals(3, pcl.eventCount());

		fm.setFormObject(new TestBean());
		assertTrue(!fm.isDirty());
		assertEquals(4, pcl.eventCount());

		vm.setValue("2");
		assertTrue(fm.isDirty());
		assertEquals(5, pcl.eventCount());

		fm.revert();
		assertTrue(!fm.isDirty());
		assertEquals(6, pcl.eventCount());
	}

	/**
	 * Test on dirty state of parent-child relations. When child gets dirty,
	 * parent should also be dirty. When parent reverts, child should revert
	 * too.
	 */
    @Test
	public void testDirtyTracksKids() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener(FormModel.DIRTY_PROPERTY);
		AbstractFormModel pfm = getFormModel(new TestBean());
		AbstractFormModel fm = getFormModel(new TestBean());
		pfm.addPropertyChangeListener(FormModel.DIRTY_PROPERTY, pcl);
		pfm.addChild(fm);
		ValueModel childSimpleProperty = fm.getValueModel("simpleProperty");
		ValueModel parentSimpleProperty = pfm.getValueModel("simpleProperty");
		// test child property dirty -> parent dirty
		childSimpleProperty.setValue("1");
		assertTrue(pfm.isDirty());
		assertEquals(1, pcl.eventCount());

		fm.revert();
		assertTrue(!pfm.isDirty());
		assertEquals(2, pcl.eventCount());
		// child dirty -> revert parent triggers revert on child
		childSimpleProperty.setValue("1");
		assertTrue(pfm.isDirty());
		assertEquals(3, pcl.eventCount());

		pfm.revert();
		assertTrue(!pfm.isDirty());
		assertTrue(!fm.isDirty());
		assertEquals(4, pcl.eventCount());
		// child & parent property dirty -> parent dirty, revert child, then
		// parent
		childSimpleProperty.setValue("1");
		assertTrue(pfm.isDirty());
		assertEquals(5, pcl.eventCount());

		parentSimpleProperty.setValue("2");
		assertTrue(pfm.isDirty());
		assertEquals(5, pcl.eventCount());

		fm.revert();
		assertTrue(pfm.isDirty());
		assertEquals(5, pcl.eventCount());

		pfm.revert();
		assertTrue(!pfm.isDirty());
		assertEquals(6, pcl.eventCount());
	}

    @Test
	public void testSetFormObjectDoesNotRevertChangesToPreviousFormObject() {
		TestBean p1 = new TestBean();
		BeanPropertyAccessStrategy pas = new BeanPropertyAccessStrategy(p1);
		AbstractFormModel fm = getFormModel(pas, false);
		fm.getValueModel("simpleProperty").setValue("1");
		fm.setFormObject(new TestBean());
		assertEquals("1", p1.getSimpleProperty());
	}

    @Test
	public void testCommitEvents() {
		TestBean p = new TestBean();
		BeanPropertyAccessStrategy pas = new BeanPropertyAccessStrategy(p);
		TestCommitListener cl = new TestCommitListener();
		AbstractFormModel fm = getFormModel(pas, false);
		fm.addCommitListener(cl);
		ValueModel vm = fm.getValueModel("simpleProperty");

		vm.setValue("1");
		fm.commit();
		assertEquals(1, cl.preEditCalls);
		assertEquals(1, cl.postEditCalls);
	}

    @Test
	public void testCommitWritesBufferingThrough() {
		TestBean p = new TestBean();
		BeanPropertyAccessStrategy pas = new BeanPropertyAccessStrategy(p);
		TestCommitListener cl = new TestCommitListener();
		AbstractFormModel fm = getFormModel(pas, true);
		fm.addCommitListener(cl);
		ValueModel vm = fm.getValueModel("simpleProperty");

		vm.setValue("1");
		fm.commit();
		assertEquals("1", p.getSimpleProperty());
	}

    @Test
	public void testRevertWithBuffering() {
		testRevert(true);
	}

    @Test
	public void testRevertWithoutBuffering() {
		testRevert(false);
	}

	public void testRevert(boolean buffering) {
		TestBean p = new TestBean();
		BeanPropertyAccessStrategy pas = new BeanPropertyAccessStrategy(p);
		TestPropertyChangeListener pcl = new TestPropertyChangeListener(FormModel.DIRTY_PROPERTY);
		AbstractFormModel fm = getFormModel(pas, buffering);
		fm.addPropertyChangeListener(FormModel.DIRTY_PROPERTY, pcl);
		ValueModel vm = fm.getValueModel("simpleProperty");

		vm.setValue("1");
		fm.revert();

		assertEquals(null, vm.getValue());
		assertEquals(null, p.getSimpleProperty());

		TestBean tb2 = new TestBean();
		tb2.setSimpleProperty("tb2");
		fm.setFormObject(tb2);

		vm.setValue("1");
		fm.revert();

		assertEquals("tb2", vm.getValue());
		assertEquals("tb2", tb2.getSimpleProperty());
	}

    @Test
	public void testEnabledEvents() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener(FormModel.ENABLED_PROPERTY);
		AbstractFormModel fm = getFormModel(new Object());
		fm.addPropertyChangeListener(FormModel.ENABLED_PROPERTY, pcl);

		assertTrue(fm.isEnabled());

		fm.setEnabled(false);
		assertTrue(!fm.isEnabled());
		assertEquals(1, pcl.eventCount());

		fm.setEnabled(false);
		assertTrue(!fm.isEnabled());
		assertEquals(1, pcl.eventCount());

		fm.setEnabled(true);
		assertTrue(fm.isEnabled());
		assertEquals(2, pcl.eventCount());

		fm.setEnabled(true);
		assertTrue(fm.isEnabled());
		assertEquals(2, pcl.eventCount());
	}

    @Test
	public void testEnabledTracksParent() {
		TestPropertyChangeListener pcl = new TestPropertyChangeListener(FormModel.ENABLED_PROPERTY);
		AbstractFormModel pfm = getFormModel(new Object());
		AbstractFormModel fm = getFormModel(new Object());
		fm.addPropertyChangeListener(FormModel.ENABLED_PROPERTY, pcl);
		pfm.addChild(fm);

		pfm.setEnabled(false);
		assertTrue(!fm.isEnabled());
		assertEquals(1, pcl.eventCount());

		pfm.setEnabled(true);
		assertTrue(fm.isEnabled());
		assertEquals(2, pcl.eventCount());

		pfm.setEnabled(false);
		assertTrue(!fm.isEnabled());
		assertEquals(3, pcl.eventCount());

		fm.setEnabled(false);
		assertTrue(!fm.isEnabled());
		assertEquals(3, pcl.eventCount());

		pfm.setEnabled(true);
		assertTrue(!fm.isEnabled());
		assertEquals(3, pcl.eventCount());

		fm.setEnabled(true);
		assertTrue(fm.isEnabled());
		assertEquals(4, pcl.eventCount());
	}

    @Test
	public void testConvertingValueModels() {
		AbstractFormModel fm = getFormModel(new TestBean());
		TestConversionService cs = new TestConversionService();
		fm.setConversionService(cs);

		ValueModel vm = fm.getValueModel("simpleProperty", String.class);
		assertEquals(fm.getValueModel("simpleProperty"), vm);
		assertEquals(0, cs.calls);

		cs.executer = new StaticConversionExecutor(String.class, Integer.class, new CopiedPublicNoOpConverter(String.class,
				Integer.class));
		ValueModel cvm = fm.getValueModel("simpleProperty", Integer.class);
		assertEquals(2, cs.calls);
		assertEquals(Integer.class, cs.lastSource);
		assertEquals(String.class, cs.lastTarget);

		assertEquals(fm.getValueModel("simpleProperty", Integer.class), cvm);
		assertEquals(2, cs.calls);
	}

    @Test
	public void testFieldMetadata() {
		AbstractFormModel fm = getFormModel(new TestBean());

		assertEquals(String.class, fm.getFieldMetadata("simpleProperty").getPropertyType());
		assertTrue(!fm.getFieldMetadata("simpleProperty").isReadOnly());

		assertEquals(Object.class, fm.getFieldMetadata("readOnly").getPropertyType());
		assertTrue(fm.getFieldMetadata("readOnly").isReadOnly());
	}

    @Test
	public void testSetFormObjectUpdatesDirtyState() {
		final AbstractFormModel fm = getFormModel(new TestBean());
		fm.add("simpleProperty");
		fm.add("singleSelectListProperty");

		assertTrue(!fm.isDirty());

		fm.getValueModel("simpleProperty").addValueChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				fm.getValueModel("singleSelectListProperty").setValue(null);
			}
		});

		TestBean newBean = new TestBean();
		newBean.setSimpleProperty("simpleProperty");
		newBean.setSingleSelectListProperty("singleSelectListProperty");
		fm.setFormObject(newBean);
		assertEquals(null, fm.getValueModel("singleSelectListProperty").getValue());
		assertTrue(fm.isDirty());
		fm.getValueModel("singleSelectListProperty").setValue("singleSelectListProperty");
		assertTrue(!fm.isDirty());
	}

    @Test
	public void testFormPropertiesAreAccessableFromFormObjectChangeEvents() {
		final AbstractFormModel fm = getFormModel(new TestBean());
		assertEquals(null, fm.getValueModel("simpleProperty").getValue());
		TestBean newTestBean = new TestBean();
		newTestBean.setSimpleProperty("NewValue");
		fm.getFormObjectHolder().addValueChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				assertEquals("NewValue", fm.getValueModel("simpleProperty").getValue());
			}
		});
		fm.setFormObject(newTestBean);
	}

    @Test
	public void testFormObjectChangeEventComesBeforePropertyChangeEvent() {
		final TestBean testBean = new TestBean();
		final AbstractFormModel fm = getFormModel(testBean);
		final TestBean newTestBean = new TestBean();
		newTestBean.setSimpleProperty("NewValue");
		final boolean[] formObjectChangeCalled = new boolean[1];
		fm.getFormObjectHolder().addValueChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				formObjectChangeCalled[0] = true;
			}
		});
		fm.getValueModel("simpleProperty").addValueChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				assertEquals("Form property change event was called before form object change event", true,
						formObjectChangeCalled[0]);
			}
		});
		fm.setFormObject(newTestBean);
	}

    @Test
	public void testFormObjectChangeEvents() {
		TestBean testBean = new TestBean();
		final AbstractFormModel fm = getFormModel(testBean);
		TestBean newTestBean = new TestBean();
		newTestBean.setSimpleProperty("NewValue");
		TestPropertyChangeListener testPCL = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
		fm.getFormObjectHolder().addValueChangeListener(testPCL);
		fm.setFormObject(newTestBean);
		assertEquals(1, testPCL.eventCount());
		assertEquals(testBean, testPCL.lastEvent().getOldValue());
		assertEquals(newTestBean, testPCL.lastEvent().getNewValue());
	}


	public static class TestCommitListener implements CommitListener {
		int preEditCalls;

		int postEditCalls;

		public void preCommit(FormModel formModel) {
			preEditCalls++;
		}

		public void postCommit(FormModel formModel) {
			postEditCalls++;
		}
	}

	public class TestConversionService extends GenericConversionService {

		public int calls;

		public Class lastSource;

		public Class lastTarget;

		public ConversionExecutor executer;

        public ConversionExecutor getConversionExecutor(String converterId, Class source, Class target) {
			calls++;
			lastSource = source;
			lastTarget = target;
			if (executer != null) {
				return executer;
			}
			throw new IllegalArgumentException("no converter found");
		}

        public ConversionExecutor getConversionExecutor(Class source, Class target) {
			calls++;
			lastSource = source;
			lastTarget = target;
			if (executer != null) {
				return executer;
			}
			throw new IllegalArgumentException("no converter found");
		}

		public ConversionExecutor getConversionExecutorByTargetAlias(Class arg0, String arg1)
				throws IllegalArgumentException {
			fail("this method should never be called");
			return null;
		}

		public Class getClassByAlias(String arg0) {
			fail("this method should never be called");
			return null;
		}

		public ConversionExecutor[] getConversionExecutorsForSource(Class sourceClass) throws ConversionException {
			fail("this method should never be called");
			return null;
		}
	}

	/**
	 * <p>
	 * <b>Summary: </b>Setting a new FormObject should always result in a clean
	 * model (not dirty). Using buffered=<code>true</code>.
	 * </p>
	 *
	 * <p>
	 * This test checks that when a valueModel is dirty and a new FormObject is
	 * set which has the same value for that valueModel, the formModel should
	 * not be dirty.
	 * </p>
	 */
    @Test
	public void testBufferedFormModelSetFormObjectNotDirty() {
		String someString = "someString";
		FormModel model = getFormModel(new TestBean());
		ValueModel valueModel = model.getValueModel("simpleProperty");

		assertEquals("Initial check, formmodel not dirty.", false, model.isDirty());

		valueModel.setValue(someString);
		assertEquals("Value changed, model should be dirty.", true, model.isDirty());

		TestBean newFormObject = new TestBean();
		newFormObject.setSimpleProperty(someString);
		model.setFormObject(newFormObject);
		assertEquals("New formObject is set, model should not be dirty.", false, model.isDirty());
	}

	/**
	 * <p>
	 * <b>Summary: </b>Setting a new FormObject should always result in a clean
	 * model (not dirty). Using buffered=<code>false</code>.
	 * </p>
	 *
	 * <p>
	 * This test checks that when a valueModel is dirty and a new FormObject is
	 * set which has the same value for that valueModel, the formModel should
	 * not be dirty.
	 * </p>
	 */
    @Test
	public void testFormModelSetFormObjectNotDirty() {
		String someString = "someString";
		FormModel model = getFormModel(new ValueHolder(new TestBean()), false);
		ValueModel valueModel = model.getValueModel("simpleProperty");

		assertEquals("Initial check, formmodel not dirty.", false, model.isDirty());

		valueModel.setValue(someString);
		assertEquals("Value changed, model should be dirty.", true, model.isDirty());

		TestBean newFormObject = new TestBean();
		newFormObject.setSimpleProperty(someString);
		model.setFormObject(newFormObject);
		assertEquals("New formObject is set, model should not be dirty.", false, model.isDirty());
	}

	/**
	 * <p>
	 * Test whether the enabled state is correctly propagated between
	 * parent-child formModel and that the proper events are fired.
	 * </p>
	 * <p>
	 * In detail:
	 * <ul>
	 * <li>if parent is enabled: should allow child to handle it's own state</li>
	 * <li>if parent is disabled: should override child's enabled state</li>
	 * </ul>
	 * </p>
	 */
    @Test
	public void testParentChildEnabledState() {
		TestBean formObject = new TestBean();
		AbstractFormModel parent = getFormModel(formObject);
		AbstractFormModel child = getFormModel(formObject);
		BooleanStatelistener listener = new BooleanStatelistener(FormModel.ENABLED_PROPERTY);
		listener.state = child.isEnabled();
		child.addPropertyChangeListener(FormModel.ENABLED_PROPERTY, listener);

		parent.addChild(child);

		// check if parent->enabled then (child->enabled or child->disabled)
		parent.setEnabled(true);
		child.setEnabled(true);
		assertTrue(listener.state);
		child.setEnabled(false);
		assertFalse(listener.state);

		// check if parent->disabled then always child->disabled
		parent.setEnabled(false);
		child.setEnabled(false);
		assertFalse(listener.state);
		child.setEnabled(true);
		assertFalse(listener.state);

		parent.removeChild(child);

		// check initial state when adding a child formModel, state should be synchronized at setup and reverted when removing the relation

		// check parent->disabled is correctly overriding child state
		parent.setEnabled(false);
		child.setEnabled(true);
		parent.addChild(child);
		assertFalse(listener.state);
		parent.removeChild(child);
		assertTrue(listener.state);
		parent.setEnabled(false);
		child.setEnabled(false);
		parent.addChild(child);
		assertFalse(listener.state);
		parent.removeChild(child);
		assertFalse(listener.state);

		// check parent->enabled is correctly allowing child state to override
		parent.setEnabled(true);
		child.setEnabled(false);
		parent.addChild(child);
		assertFalse(listener.state);
		parent.removeChild(child);
		assertFalse(listener.state);
		parent.setEnabled(true);
		child.setEnabled(true);
		parent.addChild(child);
		assertTrue(listener.state);
		parent.removeChild(child);
		assertTrue(listener.state);
	}

	/**
	 * <p>
	 * Test whether the read-only state is correctly propagated between
	 * parent-child formModel and that the proper events are fired.
	 * </p>
	 * <p>
	 * In detail:
	 * <ul>
	 * <li>if parent is readOnly: child should be readOnly</li>
	 * <li>if parent isn't readOnly: child can handle it's own state</li>
	 * </ul>
	 * </p>
	 */
    @Test
	public void testParentChildReadOnlyState() {
		TestBean formObject = new TestBean();
		AbstractFormModel parent = getFormModel(formObject);
		AbstractFormModel child = getFormModel(formObject);
		BooleanStatelistener listener = new BooleanStatelistener(FormModel.READONLY_PROPERTY);
		listener.state = child.isReadOnly();
		child.addPropertyChangeListener(FormModel.READONLY_PROPERTY, listener);

		parent.addChild(child);

		// if parent->readOnly then child->readOnly
		parent.setReadOnly(true);
		child.setReadOnly(false);
		assertTrue(listener.state);
		child.setReadOnly(true);
		assertTrue(listener.state);

		// if parent->writable then (child->writable or child->readOnly)
		parent.setReadOnly(false);
		child.setReadOnly(false);
		assertFalse(listener.state);
		child.setReadOnly(true);
		assertTrue(listener.state);

		parent.removeChild(child);

		// check initial state when adding a child formModel, state should be synchronized at setup and reverted when removing the relation

		// check parent->writable is correctly allowing child to override
		parent.setReadOnly(false);
		child.setReadOnly(true);
		parent.addChild(child);
		assertTrue(listener.state);
		parent.removeChild(child);
		assertTrue(listener.state);
		parent.setReadOnly(false);
		child.setReadOnly(false);
		parent.addChild(child);
		assertFalse(listener.state);
		parent.removeChild(child);
		assertFalse(listener.state);

		// check parent->readOnly is correctly overriding child state
		parent.setReadOnly(true);
		child.setReadOnly(false);
		parent.addChild(child);
		assertTrue(listener.state);
		parent.removeChild(child);
		assertFalse(listener.state);
		parent.setReadOnly(true);
		child.setReadOnly(true);
		parent.addChild(child);
		assertTrue(listener.state);
		parent.removeChild(child);
		assertTrue(listener.state);
	}

	/**
	 * Listener to register on boolean properties to check if they are in the expected state.
	 */
	protected static class BooleanStatelistener implements PropertyChangeListener {

		final String property;

		boolean state = false;

		public BooleanStatelistener(final String property) {
			this.property = property;
		}

		public void propertyChange(PropertyChangeEvent evt) {
			if (property.equals(evt.getPropertyName()))
				state = Boolean.parseBoolean(evt.getNewValue().toString());
		}
	}

	/**
	 * <p>
	 * Test whether the dirty state is correctly propagated between
	 * parent-child formModel and that the proper events are fired.
	 * </p>
	 * <p>
	 * In detail:
	 * <ul>
	 * <li>if child is dirty then parent MUST be dirty</li>
	 * <li>if child isn't dirty then parent CAN be dirty</li>
	 * </ul>
	 */
    @Test
	public void testParentChildDirtyState() {
		TestBean formObject = new TestBean();
		AbstractFormModel parent = getFormModel(formObject);
		ValueModel parentValueModel = parent.getValueModel("simpleProperty");
		AbstractFormModel child = getFormModel(formObject);
		ValueModel childValueModel = child.getValueModel("booleanProperty");
		BooleanStatelistener listener = new BooleanStatelistener(FormModel.DIRTY_PROPERTY);
		listener.state = parent.isDirty();
		parent.addPropertyChangeListener(FormModel.DIRTY_PROPERTY, listener);

		parent.addChild(child);

		// check if child->dirty then parent->dirty
		assertFalse(listener.state);
		childValueModel.setValue(Boolean.TRUE);
		assertTrue(listener.state);
		parentValueModel.setValue("x");
		assertTrue(listener.state);
		parentValueModel.setValue(null); // original value
		assertTrue(listener.state);
		childValueModel.setValue(Boolean.FALSE); //original value
		assertFalse(listener.state);

		// check if child->clean then (parent->clean or parent->dirty)
		parentValueModel.setValue("x");
		assertTrue(listener.state);
		parentValueModel.setValue(null); // original value
		assertFalse(listener.state);

		parent.removeChild(child);

		// check initial state when adding a child formModel, state should be synchronized at setup and reverted when removing the relation

		// check if dirty child sets parent dirty
		childValueModel.setValue(Boolean.TRUE);
		assertFalse(listener.state);
		parent.addChild(child);
		assertTrue(listener.state);
		parent.removeChild(child);
		assertFalse(listener.state);
		parentValueModel.setValue("x");
		assertTrue(listener.state);
		parent.addChild(child);
		assertTrue(listener.state);
		parent.removeChild(child);
		assertTrue(listener.state);

		// check if clean child allows parent to override
		child.revert();
		parent.revert();
		assertFalse(listener.state);
		parent.addChild(child);
		assertFalse(listener.state);
		parent.removeChild(child);
		parentValueModel.setValue("x");
		assertTrue(listener.state);
		parent.addChild(child);
		assertTrue(listener.state);
		parent.removeChild(child);
		assertTrue(listener.state);
	}
}
