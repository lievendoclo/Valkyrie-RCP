package org.valkyriercp.binding.form.support;

import org.junit.Test;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.StaticMessageSource;
import org.valkyriercp.binding.form.FieldFace;
import org.valkyriercp.binding.format.InvalidFormatException;
import org.valkyriercp.binding.validation.ValidationMessage;
import org.valkyriercp.binding.value.support.ValueHolder;
import org.valkyriercp.core.support.LabelInfo;

import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Testcase for <code>DefaultBindingErrorMessageProvider</code>
 * 
 * @author Peter De Bruycker
 */

public class DefaultBindingErrorMessageProviderTests {

    @Test
	public void testGetErrorMessage() {
		DefaultBindingErrorMessageProvider provider = new DefaultBindingErrorMessageProvider();

		TestAbstractFormModel formModel = new TestAbstractFormModel(new Object()) {
			public FieldFace getFieldFace(String field) {
				return new DefaultFieldFace("Some Property", "", "", new LabelInfo("Some Property"), null);
			}
		};
		formModel.add("someProperty", new ValueHolder("value"));

		StaticMessageSource messageSource = new StaticMessageSource();
		messageSource.addMessage("typeMismatch", Locale.getDefault(), "{0} has an invalid format \"{1}\"");
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
		provider.setMessageSourceAccessor(messageSourceAccessor);

		ValidationMessage message = provider.getErrorMessage(formModel, "someProperty", "new value",
				new IllegalArgumentException());

		assertNotNull(message);
		assertEquals("someProperty", message.getProperty());
		assertEquals("Some Property has an invalid format \"new value\"", message.getMessage());
	}

	public void testGetMessageCodeForException() {
		DefaultBindingErrorMessageProvider provider = new DefaultBindingErrorMessageProvider();

		assertEquals("typeMismatch", provider.getMessageCodeForException(new TypeMismatchException(new Object(),
				String.class)));
		assertEquals("required", provider.getMessageCodeForException(new NullPointerException()));
		assertEquals("typeMismatch", provider.getMessageCodeForException(new InvalidFormatException("", "")));
		assertEquals("typeMismatch", provider.getMessageCodeForException(new IllegalArgumentException()));
		assertEquals("required", provider.getMessageCodeForException(new RuntimeException(new NullPointerException())));
		assertEquals("unknown", provider.getMessageCodeForException(new UnsupportedOperationException()));
	}

}
