package org.valkyriercp.binding.support;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.NullValueInNestedPathException;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.PropertyMetadataAccessStrategy;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.rules.closure.Closure;
import org.valkyriercp.rules.closure.support.Block;
import org.valkyriercp.test.TestBean;
import org.valkyriercp.test.TestBeanWithPCP;
import org.valkyriercp.test.TestPropertyChangeListener;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests class {@link AbstractPropertyAccessStrategy}.
 *
 * @author Oliver Hutchison
 * @author Arne Limburg
 */
public abstract class AbstractPropertyAccessStrategyTests extends AbstractValkyrieTest {

    protected AbstractPropertyAccessStrategy pas;

    protected TestBean testBean;

    protected ValueModel vm;

    protected TestPropertyChangeListener pcl;

    @Before
    public void doSetUp() throws Exception {
        testBean = new TestBean();
        pas = createPropertyAccessStrategy(testBean);
        pcl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
    }

    protected abstract AbstractPropertyAccessStrategy createPropertyAccessStrategy(Object target);

    protected boolean isStrictNullHandlingEnabled() {
        return true;
    }

    @Test
    public void testSimpleProperty() {
        vm = pas.getPropertyValueModel("simpleProperty");
        Block setValueDirectly = new Block() {
            public void handle(Object newValue) {
                testBean.setSimpleProperty((String) newValue);
            }
        };
        Closure getValueDirectly = new Closure() {
            public Object call(Object ignore) {
                return testBean.getSimpleProperty();
            }
        };
        Object[] valuesToTest = new Object[]{"1", "2", null, "3"};

        testSettingAndGetting(valuesToTest, getValueDirectly, setValueDirectly);
    }

    @Test
    public void testNestedProperty() {
        final TestBean nestedProperty = new TestBean();
        testBean.setNestedProperty(nestedProperty);
        vm = pas.getPropertyValueModel("nestedProperty.simpleProperty");
        Block setValueDirectly = new Block() {
            public void handle(Object newValue) {
                nestedProperty.setSimpleProperty((String) newValue);
            }
        };
        Closure getValueDirectly = new Closure() {
            public Object call(Object ignored) {
                return nestedProperty.getSimpleProperty();
            }
        };
        Object[] valuesToTest = new Object[]{"1", "2", null, "3"};

        testSettingAndGetting(valuesToTest, getValueDirectly, setValueDirectly);
    }

    @Test
    public void testChildPropertyAccessStrategy() {
        final TestBean nestedProperty = new TestBean();
        testBean.setNestedProperty(nestedProperty);
        MutablePropertyAccessStrategy cpas = pas.getPropertyAccessStrategyForPath("nestedProperty");

        assertEquals("Child domainObjectHolder should equal equivalent parent ValueModel",
                pas.getPropertyValueModel("nestedProperty"), cpas.getDomainObjectHolder());

        vm = cpas.getPropertyValueModel("simpleProperty");
        assertEquals("Child should return the same ValueModel as parent",
                pas.getPropertyValueModel("nestedProperty.simpleProperty"), vm);

        Block setValueDirectly = new Block() {
            public void handle(Object newValue) {
                nestedProperty.setSimpleProperty((String) newValue);
            }
        };
        Closure getValueDirectly = new Closure() {
            public Object call(Object ignore) {
                return nestedProperty.getSimpleProperty();
            }
        };
        Object[] valuesToTest = new Object[]{"1", "2", null, "3"};

        testSettingAndGetting(valuesToTest, getValueDirectly, setValueDirectly);

        try {
            pas.getPropertyValueModel("nestedProperty").setValue(null);
            if (isStrictNullHandlingEnabled())
                fail("Should have thrown a NullValueInNestedPathException");
        } catch (NullValueInNestedPathException e) {
            if (!isStrictNullHandlingEnabled())
                fail("Should not have thrown a NullValueInNestedPathException");
        }
    }

    @Test
    public void testMapProperty() {
        final Map map = new HashMap();
        testBean.setMapProperty(map);
        vm = pas.getPropertyValueModel("mapProperty[.key]");
        Block setValueDirectly = new Block() {
            public void handle(Object newValue) {
                map.put(".key", newValue);
            }
        };
        Closure getValueDirectly = new Closure() {
            public Object call(Object ignore) {
                return map.get(".key");
            }
        };
        Object[] valuesToTest = new Object[]{"1", "2", null, "3"};
        testSettingAndGetting(valuesToTest, getValueDirectly, setValueDirectly);

        try {
            pas.getPropertyValueModel("mapProperty").setValue(null);
            if (isStrictNullHandlingEnabled())
                fail("Should have thrown a NullValueInNestedPathException");
        } catch (NullValueInNestedPathException e) {
            if (!isStrictNullHandlingEnabled())
                fail("Should not have thrown a NullValueInNestedPathException");
        } catch (InvalidPropertyException e) {
            if (e.getCause() instanceof NullValueInNestedPathException) {
                if (!isStrictNullHandlingEnabled())
                    fail("Should not have thrown a NullValueInNestedPathException");
            } else {
                throw e;
            }
        }

    }

    @Test
    public void testListProperty() {
        final List list = new ArrayList();
        list.add(null);
        testBean.setListProperty(list);
        vm = pas.getPropertyValueModel("listProperty[0]");

        Block setValueDirectly = new Block() {
            public void handle(Object newValue) {
                list.set(0, newValue);
            }
        };
        Closure getValueDirectly = new Closure() {
            public Object call(Object ignore) {
                return list.get(0);
            }
        };
        Object[] valuesToTest = new Object[]{"1", "2", null, "3"};
        testSettingAndGetting(valuesToTest, getValueDirectly, setValueDirectly);

        list.add("a");
        ValueModel vm2 = pas.getPropertyValueModel("listProperty[1]");
        assertEquals("a", vm2.getValue());

        try {
            List newList = new ArrayList();
            pas.getPropertyValueModel("listProperty").setValue(newList);
            if (isStrictNullHandlingEnabled())
                fail("Should have thrown an InvalidPropertyException");
        } catch (InvalidPropertyException e) {
            if (!isStrictNullHandlingEnabled())
                fail("Should not have thrown an InvalidPropertyException");
        }

        try {
            pas.getPropertyValueModel("listProperty").setValue(null);
            if (isStrictNullHandlingEnabled())
                fail("Should have thrown a NullValueInNestedPathException");
        } catch (NullValueInNestedPathException e) {
            if (!isStrictNullHandlingEnabled())
                fail("Should not have thrown a NullValueInNestedPathException");
        } catch (InvalidPropertyException e) {
            if (e.getCause() instanceof NullValueInNestedPathException) {
                if (!isStrictNullHandlingEnabled())
                    fail("Should not have thrown a NullValueInNestedPathException");
            } else {
                throw e;
            }
        }
    }

    @Test
    public void testReadOnlyProperty() {
        vm = pas.getPropertyValueModel("readOnly");

        testBean.readOnly = "1";
        assertEquals(testBean.readOnly, vm.getValue());

        try {
            vm.setValue("2");
            fail("should have thrown NotWritablePropertyException");
        } catch (NotWritablePropertyException e) {
            // expected
        }
    }

    @Test
    public void testWriteOnlyProperty() {
        vm = pas.getPropertyValueModel("writeOnly");

        vm.setValue("2");
        assertEquals("2", testBean.writeOnly);

        try {
            vm.getValue();
            fail("should have thrown NotReadablePropertyException");
        } catch (NotReadablePropertyException e) {
            // expected
        }
    }

    @Test
    public void testBeanThatImplementsPropertyChangePublisher() {
        TestBeanWithPCP testBeanPCP = new TestBeanWithPCP();
        pas.getDomainObjectHolder().setValue(testBeanPCP);

        vm = pas.getPropertyValueModel("boundProperty");
        assertEquals("ValueModel should have registered a PropertyChangeListener", 1,
                testBeanPCP.getPropertyChangeListeners("boundProperty").length);

        vm.addValueChangeListener(pcl);
        testBeanPCP.setBoundProperty("1");
        assertEquals("Change to bound property should have been detected by ValueModel", 1, pcl.getEventsRecevied()
                .size());
        PropertyChangeEvent e = (PropertyChangeEvent) pcl.getEventsRecevied().get(0);
        assertEquals(vm, e.getSource());
        assertEquals(null, e.getOldValue());
        assertEquals("1", e.getNewValue());

        pcl.reset();
        vm.setValue("2");
        assertEquals("Change to bound property should have been detected by ValueModel", 1, pcl.getEventsRecevied().size());
        e = (PropertyChangeEvent) pcl.getEventsRecevied().get(0);
        assertEquals(vm, e.getSource());
        assertEquals("1", e.getOldValue());
        assertEquals("2", e.getNewValue());

        TestBeanWithPCP testBeanPCP2 = new TestBeanWithPCP();
        pas.getDomainObjectHolder().setValue(testBeanPCP2);

        assertEquals("ValueModel should have removed the PropertyChangeListener", 0,
                testBeanPCP.getPropertyChangeListeners("boundProperty").length);
        assertEquals("ValueModel should have registered a PropertyChangeListener", 1,
                testBeanPCP2.getPropertyChangeListeners("boundProperty").length);
    }

    private void testSettingAndGetting(Object[] valuesToTest, Closure getValueDirectly, Block setValueDirectly) {
        vm.addValueChangeListener(pcl);
        for (int i = 0; i < valuesToTest.length; i++) {
            final Object valueToTest = valuesToTest[i];
            pcl.reset();
            assertEquals("ValueModel does not have same value as bean property", getValueDirectly.call(null), vm.getValue());
            setValueDirectly.call(valueToTest);
            assertEquals("Change to bean not picked up by ValueModel", valueToTest, vm.getValue());
            setValueDirectly.call(null);
            assertEquals("Change to bean not picked up by ValueModel", null, vm.getValue());
            vm.setValue(valueToTest);
            assertEquals("Change to ValueModel not reflected in bean", valueToTest, getValueDirectly.call(null));
            assertEquals("Change to ValueModel had no effect", valueToTest, vm.getValue());
            if (valueToTest != null) {
                assertEquals("Incorrect number of property change events fired by value model",
                        1, pcl.getEventsRecevied().size());
                PropertyChangeEvent e = (PropertyChangeEvent) pcl.getEventsRecevied().get(0);
                assertEquals(vm, e.getSource());
                assertEquals(null, e.getOldValue());
                assertEquals(valueToTest, e.getNewValue());
            }
        }
    }

    protected void assertPropertyMetadata(PropertyMetadataAccessStrategy mas, String property, Class type, boolean isReadable, boolean isWriteable) {
        assertEquals(type, mas.getPropertyType(property));
        assertEquals(isReadable, mas.isReadable(property));
        assertEquals(isWriteable, mas.isWriteable(property));
    }
}
