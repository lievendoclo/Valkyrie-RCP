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
package org.valkyriercp.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.valkyriercp.binding.value.swing.ValueCommitPolicy;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;
import java.util.Date;

/**
 *
 * @author Keith Donald
 */
public class FormatterFactory extends JFormattedTextField.AbstractFormatterFactory {

    private static final Log logger = LogFactory.getLog(FormatterFactory.class);

    private ValueCommitPolicy valueCommitPolicy = ValueCommitPolicy.AS_YOU_TYPE;

    private Class valueClass;

    public FormatterFactory(Class valueClass) {
        this(valueClass, ValueCommitPolicy.AS_YOU_TYPE);
    }

    public FormatterFactory(Class valueClass, ValueCommitPolicy policy) {
        this.valueClass = valueClass;
        setValueCommitPolicy(policy);
    }

    public void setValueCommitPolicy(ValueCommitPolicy policy) {
        Assert.notNull(policy, "policy should not be null");
        this.valueCommitPolicy = policy;
    }

    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField source) {
        Object value = source.getValue();
        DefaultFormatter formatter;
        if (value instanceof Date) {
            formatter = new DateFormatter();
        }
        else if (value instanceof Number) {
            formatter = new NumberFormatter();
        }
        else {
            formatter = new DefaultFormatter();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Factory returning new formatter " + formatter + " for text field " + source);
        }
        valueCommitPolicy.configure(source, formatter);
        if (valueClass != null) {
            formatter.setValueClass(valueClass);
        }
        else if (value != null) {
            formatter.setValueClass(value.getClass());
        }
        return formatter;
    }
}

