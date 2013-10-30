package org.valkyriercp.binding.form.support;

import org.junit.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.StaticMessageSource;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.binding.form.FieldFace;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.test.TestIcon;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Testcase for MessageSourceFieldFaceSource
 *
 * @author Peter De Bruycker
 */
public class MessageSourceFieldFaceSourceTests extends AbstractValkyrieTest {

    @Test
	public void testLoadFieldFace() {
		Icon testIcon = new TestIcon(Color.RED);

		MessageSourceFieldFaceSource fieldFaceSource = new MessageSourceFieldFaceSource();

		StaticMessageSource messageSource = new StaticMessageSource();
		messageSource.addMessage("context.field.caption", Locale.getDefault(), "the caption");
		messageSource.addMessage("context.field.description", Locale.getDefault(), "the description");
		messageSource.addMessage("context.field.label", Locale.getDefault(), "the label");
		messageSource.addMessage("context.field.icon", Locale.getDefault(), "iconName");
		fieldFaceSource.setMessageSourceAccessor(new MessageSourceAccessor(messageSource));

		IconSource mockIconSource = mock(IconSource.class);
        when(mockIconSource.getIcon("iconName")).thenReturn(testIcon);
		fieldFaceSource.setIconSource(mockIconSource);

		FieldFace face = fieldFaceSource.loadFieldFace("field", "context");
		assertEquals("the caption", face.getCaption());
		assertEquals("the label", face.getDisplayName());
		assertEquals("the description", face.getDescription());
		assertEquals(testIcon, face.getIcon());
	}

}
