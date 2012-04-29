package org.valkyriercp.sample.showcase.widget;

import com.jgoodies.forms.layout.FormLayout;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.swing.TextComponentBinder;
import org.valkyriercp.form.binding.swing.text.MaxLengthDocumentFactory;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
import org.valkyriercp.widget.AbstractWidgetForm;
import org.valkyriercp.widget.TitledWidgetForm;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class BinderDemoWidget extends TitledWidgetForm {

    public BinderDemoWidget() {
        setForm(new BinderDemoForm());
    }

    public static class BinderDemoForm extends AbstractWidgetForm {

        @Override
        public FormModel createFormModel() {
            return formModelFactory.createFormModel(new BinderDemo(), "binderDemoForm");
        }

        @Override
        protected JComponent createFormControl() {
            FormLayout layout = new FormLayout("default, 3dlu, fill:pref:nogrow", "default");
            FormLayoutFormBuilder builder = new FormLayoutFormBuilder(getBindingFactory(), layout);
            builder.addBindingContextParameter("stringField", TextComponentBinder.DOCUMENT_FACTORY_KEY, new MaxLengthDocumentFactory(10));
            builder.addPropertyAndLabel("stringField");
            builder.nextRow();
            builder.addPropertyAndLabel("integerField", "integerBinder");
            builder.nextRow();
            builder.addPropertyAndLabel("euroField", "euroBinder");
            builder.nextRow();
            builder.addPropertyAndLabel("booleanField");
            builder.nextRow();
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
