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
      package org.valkyriercp.sample.showcase.widget;

      import net.miginfocom.swing.MigLayout;
      import org.valkyriercp.binding.form.FormModel;
      import org.valkyriercp.form.binding.swing.TextComponentBinder;
      import org.valkyriercp.form.binding.swing.text.MaxLengthDocumentFactory;
      import org.valkyriercp.form.builder.MigLayoutFormBuilder;
      import org.valkyriercp.widget.AbstractWidgetForm;
      import org.valkyriercp.widget.TitledWidgetForm;

      import javax.swing.*;
      import java.math.BigDecimal;

public class BinderDemoWidget extends TitledWidgetForm {

    public BinderDemoWidget() {
        setForm(new BinderDemoForm());
    }

    public static class BinderDemoForm extends AbstractWidgetForm {

        @Override
        public FormModel createFormModel() {
            return getApplicationConfig().formModelFactory().createFormModel(new BinderDemo(), "binderDemoForm");
        }

        @Override
        protected JComponent createFormControl() {
            MigLayoutFormBuilder builder = new MigLayoutFormBuilder(getBindingFactory(), new MigLayout("wrap 2", "[][fill]", ""));
            builder.addBindingContextParameter("stringField", TextComponentBinder.DOCUMENT_FACTORY_KEY, new MaxLengthDocumentFactory(10));
            builder.addPropertyAndLabel("stringField");
            builder.addPropertyAndLabelWithBinder("integerField", "integerBinder");
            builder.addPropertyAndLabelWithBinder("euroField", "euroBinder");
            builder.addPropertyAndLabel("booleanField");
            builder.addPropertyAndLabel("nullableBooleanField");
            return builder.getPanel();
        }

        public static class BinderDemo {
            private String stringField;
            private Integer integerField;
            private BigDecimal euroField;
            private boolean booleanField;
            private Boolean nullableBooleanField;

            public boolean isBooleanField() {
                return booleanField;
            }

            public void setBooleanField(boolean booleanField) {
                this.booleanField = booleanField;
            }

            public Boolean getNullableBooleanField() {
                return nullableBooleanField;
            }

            public void setNullableBooleanField(Boolean nullableBooleanField) {
                this.nullableBooleanField = nullableBooleanField;
            }

            public String getStringField() {
                return stringField;
            }

            public void setStringField(String stringField) {
                this.stringField = stringField;
            }

            public Integer getIntegerField() {
                return integerField;
            }

            public void setIntegerField(Integer integerField) {
                this.integerField = integerField;
            }

            public BigDecimal getEuroField() {
                return euroField;
            }

            public void setEuroField(BigDecimal euroField) {
                this.euroField = euroField;
            }
        }


    }
}
