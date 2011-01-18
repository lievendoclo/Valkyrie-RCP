package org.valkyriercp.binding.form.support;

import junit.framework.TestCase;
import org.junit.Test;
import org.valkyriercp.binding.PropertyAccessStrategy;
import org.valkyriercp.binding.PropertyMetadataAccessStrategy;
import org.valkyriercp.test.TestBean;
import org.valkyriercp.test.TestBean;

import static org.junit.Assert.*;

public class FormModelPropertyAccessStrategyTests
{

    protected AbstractFormModel getFormModel(Object formObject)
    {
        return new TestAbstractFormModel(formObject);
    }

    @Test
    public void testReadOnlyPropertyAccess()
    {
        AbstractFormModel model = getFormModel(new TestBean());
        PropertyAccessStrategy propertyAccessStrategy = model.getPropertyAccessStrategy();
        PropertyMetadataAccessStrategy metaDataAccessStrategy = propertyAccessStrategy.getMetadataAccessStrategy();

        assertFalse("Property is readonly, isWriteable() should return false.", metaDataAccessStrategy.isWriteable("readOnly"));
        assertTrue("Property is readonly, isReadable() should return true.", metaDataAccessStrategy.isReadable("readOnly"));
    }
}

