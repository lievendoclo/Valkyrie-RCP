/*
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