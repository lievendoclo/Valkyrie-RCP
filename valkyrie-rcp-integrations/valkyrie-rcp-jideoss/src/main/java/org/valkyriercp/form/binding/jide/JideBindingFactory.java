package org.valkyriercp.form.binding.jide;

import com.jidesoft.swing.DefaultOverlayable;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.swing.SwingBindingFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * A binding factory implementation with <code>Overlayable</code> support. This implementation ensures any binding
 * created by this factory has overlay support based on Jide OSS {@link DefaultOverlayable}.
 * <p>
 * A first approach was to intercept methods returning <code>Binding</code> objects using Spring AOP, this has a pitfall
 * consisting on exists user level code used that invokes <code>SwingBindingFactory</code> extension methods. When using
 * AOP interception the returned binding factory is a <code>$Proxy</code> class that cannot be assigned to
 * <code>SwingBindingFactory</code>. That's the reason why rewriting interface methods is the more suitable way.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
 *
 * @see JideRepaintManager, since according to <a href="http://forums.sun.com/thread.jspa?threadID=725127">this
 *      thread</a> the "unique" way to listen for repaint changes is overriding <code>RepaintManager</code>. This is not
 *      a recommended practice but works anyway...
 */
public class JideBindingFactory extends SwingBindingFactory {

    /**
     * The default insets to be applied to components.
     */
    public static final Insets DEFAULT_INSETS = new Insets(5, 0, 5, 0);

    /**
     * Creates a binding factory given the target form model.
     *
     * @param formModel
     *            the form model.
     */
    public JideBindingFactory(FormModel formModel) {

        super(formModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binding bindControl(JComponent control, String formPropertyPath) {

        return new OverlayableBinding(super.bindControl(control, formPropertyPath));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binding bindControl(JComponent control, String formPropertyPath, @SuppressWarnings("rawtypes") Map context) {

        return new OverlayableBinding(super.bindControl(control, formPropertyPath, context));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binding createBinding(String formPropertyPath) {

        return new OverlayableBinding(super.createBinding(formPropertyPath));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binding createBinding(String formPropertyPath, @SuppressWarnings("rawtypes") Map context) {

        return new OverlayableBinding(super.createBinding(formPropertyPath, context));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binding createBinding(@SuppressWarnings("rawtypes") Class controlType, String formPropertyPath) {

        return new OverlayableBinding(super.createBinding(controlType, formPropertyPath));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binding createBinding(@SuppressWarnings("rawtypes") Class controlType, String formPropertyPath,
            @SuppressWarnings("rawtypes") Map context) {

        return new OverlayableBinding(super.createBinding(controlType, formPropertyPath, context));
    }

    /**
     * A binding implementation that wraps the original control into an overlay ready component.
     *
     * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
     *
     * @see DefaultOverlayable
     */
    private static class OverlayableBinding implements Binding {

        /**
         * The control.
         */
        private JComponent control;

        /**
         * The form model.
         */
        private FormModel formModel;

        /**
         * The property name.
         */
        private String property;

        /**
         * Constructs the binding given its source.
         *
         * @param source
         *            the source binding.
         */
        public OverlayableBinding(Binding source) {

            this(source.getControl(), source.getFormModel(), source.getProperty());
        }

        /**
         * Constructs the binding given its control, form model and property name.
         *
         * @param control
         *            the control.
         * @param formModel
         *            the form model.
         * @param property
         *            the property name.
         */
        private OverlayableBinding(JComponent control, FormModel formModel, String property) {

            super();

            this.setControl(this.addOverlaySupportIfNeeded(control));
            this.setFormModel(formModel);
            this.setProperty(property);

            // Install the JideRepaintManagerWrapper if not already done.
            // Note it is installed in a lazy way to avoid unnecessary installations when the binding factory is
            // instantiated (but not employed)
            JideRepaintManager.installJideRepaintManagerIfNeeded();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getControl() {

            return this.control;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FormModel getFormModel() {

            return this.formModel;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getProperty() {

            return this.property;
        }

        /**
         * Returns an overlable component given the target one.
         *
         * @param control
         *            the target control.
         *
         * @return an overlayable component over the original one.
         */
        protected final JComponent addOverlaySupportIfNeeded(JComponent control) {

            if (!(control instanceof DefaultOverlayable)) {

                // Set a default overlay location insets to avoid overlayable bounds be changed after adding overlays
                final DefaultOverlayable overlayable = new DefaultOverlayable(control);
                overlayable.setOverlayLocationInsets(JideBindingFactory.DEFAULT_INSETS);

                return overlayable;
            }

            return control;
        }

        /**
         * Sets the control.
         *
         * @param control
         *            the control to set.
         */
        private void setControl(JComponent control) {

            Assert.notNull(control, "control");

            this.control = control;
        }

        /**
         * Sets the form model.
         *
         * @param formModel
         *            model the form model to set.
         */
        private void setFormModel(FormModel formModel) {

            Assert.notNull(formModel, "formModel");

            this.formModel = formModel;
        }

        /**
         * Sets the property name.
         *
         * @param property
         *            the property name to set.
         */
        private void setProperty(String property) {

            Assert.notNull(property, "property");

            this.property = property;
        }
    }
}
