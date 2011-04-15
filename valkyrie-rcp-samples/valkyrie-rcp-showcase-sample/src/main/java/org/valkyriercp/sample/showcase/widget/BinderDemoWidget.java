package org.valkyriercp.sample.showcase.widget;

import com.jgoodies.forms.layout.FormLayout;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
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
            return formModelFactory.createFormModel(new BinderDemo(), "binderDemoForm");
        }

        @Override
        protected JComponent createFormControl() {
            FormLayout layout = new FormLayout("default, 3dlu, fill:pref:nogrow", "default");
            FormLayoutFormBuilder builder = new FormLayoutFormBuilder(getBindingFactory(), layout);
            builder.addPropertyAndLabel("textFieldBinder");
            builder.nextRow();
            builder.addPropertyAndLabel("integerBinder", "integerBinder");
            builder.nextRow();
            builder.addPropertyAndLabel("euroBinder", "euroBinder");
            return builder.getPanel();
        }

        public static class BinderDemo {
            private String textFieldBinder;
            private Integer integerBinder;
            private BigDecimal euroBinder;

            public String getTextFieldBinder() {
                return textFieldBinder;
            }

            public void setTextFieldBinder(String textFieldBinder) {
                this.textFieldBinder = textFieldBinder;
            }

            public Integer getIntegerBinder() {
                return integerBinder;
            }

            public void setIntegerBinder(Integer integerBinder) {
                this.integerBinder = integerBinder;
            }

            public BigDecimal getEuroBinder() {
                return euroBinder;
            }

            public void setEuroBinder(BigDecimal euroBinder) {
                this.euroBinder = euroBinder;
            }
        }


    }
}
