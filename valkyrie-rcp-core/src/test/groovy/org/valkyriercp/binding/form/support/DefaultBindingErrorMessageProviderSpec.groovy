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
import org.springframework.beans.TypeMismatchException
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.context.support.StaticMessageSource
import org.valkyriercp.binding.form.FieldFace
import org.valkyriercp.binding.format.InvalidFormatException
import org.valkyriercp.binding.validation.ValidationMessage
import org.valkyriercp.binding.value.support.ValueHolder
import org.valkyriercp.core.support.LabelInfo
import spock.lang.Specification
/**
 * Created by lievendoclo on 08/07/14.
 */
class DefaultBindingErrorMessageProviderSpec extends Specification {

    def testGetErrorMessage() {
        given:
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

        when:
        ValidationMessage message = provider.getErrorMessage(formModel, "someProperty", "new value",
                new IllegalArgumentException());
        then:
        message != null
        message.property == "someProperty"
        message.message == "Some Property has an invalid format \"new value\""
    }

    def testGetMessageCodeForException() {
        when:
        DefaultBindingErrorMessageProvider provider = new DefaultBindingErrorMessageProvider();
        then:
        provider.getMessageCodeForException(new TypeMismatchException(new Object(),
                String.class)) == "typeMismatch"
        provider.getMessageCodeForException(new NullPointerException()) == "required"
        provider.getMessageCodeForException(new InvalidFormatException("", "")) == "typeMismatch"
        provider.getMessageCodeForException(new IllegalArgumentException()) == "typeMismatch"
        provider.getMessageCodeForException(new RuntimeException(new NullPointerException())) == "required"
        provider.getMessageCodeForException(new UnsupportedOperationException()) == "unknown"
    }
}
