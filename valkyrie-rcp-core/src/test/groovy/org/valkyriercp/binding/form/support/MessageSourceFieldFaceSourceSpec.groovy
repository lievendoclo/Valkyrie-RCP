package org.valkyriercp.binding.form.support
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.context.support.StaticMessageSource
import org.valkyriercp.binding.form.FieldFace
import org.valkyriercp.image.IconSource
import org.valkyriercp.test.TestIcon
import spock.lang.Specification

import javax.swing.*
import java.awt.*

class MessageSourceFieldFaceSourceSpec extends Specification {
    def testLoadFieldFace() {
        given:
        Icon testIcon = new TestIcon(Color.RED);
        MessageSourceFieldFaceSource fieldFaceSource = new MessageSourceFieldFaceSource();
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("context.field.caption", Locale.getDefault(), "the caption");
        messageSource.addMessage("context.field.description", Locale.getDefault(), "the description");
        messageSource.addMessage("context.field.label", Locale.getDefault(), "the label");
        messageSource.addMessage("context.field.icon", Locale.getDefault(), "iconName");
        fieldFaceSource.setMessageSourceAccessor(new MessageSourceAccessor(messageSource));

        IconSource mockIconSource = Mock(IconSource.class);
        mockIconSource.getIcon("iconName") >> testIcon
        fieldFaceSource.setIconSource(mockIconSource);

        when:
        FieldFace face = fieldFaceSource.loadFieldFace("field", "context");
        then:
        face.caption == "the caption"
        face.description == "the description"
        face.displayName == "the label"
        face.getIcon() == testIcon
    }
}