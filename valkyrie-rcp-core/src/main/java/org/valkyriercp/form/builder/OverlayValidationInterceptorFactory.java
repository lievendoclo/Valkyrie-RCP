package org.valkyriercp.form.builder;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.component.DefaultMessageAreaModel;
import org.valkyriercp.component.MayHaveMessagableTab;
import org.valkyriercp.component.MessagableTab;
import org.valkyriercp.core.Guarded;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.core.Message;
import org.valkyriercp.core.Severity;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;

public class OverlayValidationInterceptorFactory implements FormComponentInterceptorFactory {;

    public FormComponentInterceptor getInterceptor(FormModel formModel) {
        return new OverlayValidationInterceptor(formModel);
    }

    public class OverlayValidationInterceptor extends AbstractOverlayFormComponentInterceptor {

        /**
         * Creates the form component interceptor given the form model.
         *
         * @param formModel the form model.
         */
        public OverlayValidationInterceptor(FormModel formModel) {

            super(formModel);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected int getPosition() {

            return SwingConstants.SOUTH_WEST;
        }

        /**
         * The validation overlay handler.
         *
         * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
         */
        private class ValidationOverlayHandler extends AbstractOverlayFormComponentInterceptor.AbstractOverlayHandler
                implements Guarded {

            /**
             * Creates the validation overlay handler given the property name and target component.
             * <p/>
             * Delegates logic into a <code>ValidationInteceptor</code>.
             *
             * @param propertyName    the property name.
             * @param targetComponent the target component.
             */
            public ValidationOverlayHandler(String propertyName, JComponent targetComponent) {

                super(propertyName, targetComponent);

                // Employ a validation interceptor to reuse its validation aware logic.
                new ValidationInterceptor(this.getFormModel()) {

                    /**
                     * Initializes the interceptor installing the guarded instance and registering the message receiver.
                     */
                    public void init() {

                        final ValidationOverlayHandler thiz = ValidationOverlayHandler.this;

                        this.registerGuarded(thiz.getPropertyName(), thiz);
                        this.registerMessageReceiver(thiz.getPropertyName(), (ValidationOverlay) thiz.getOverlay());
                    }
                } // #init (breakline to avoid CS warning)
                        .init();
            }

            /**
             * {@inheritDoc}
             */
            @Override
            protected JComponent createOverlay() {

                return new ValidationOverlay();
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isEnabled() {

                return Boolean.TRUE; // foo implementation
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void setEnabled(boolean enabled) {

                this.refreshOverlay(!enabled);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected AbstractOverlayHandler createOverlayHandler(String propertyName, JComponent component) {

            return new ValidationOverlayHandler(propertyName, component);
        }

        /**
         * The validation overlay component.
         * <p/>
         * It's a <code>JLabel</code> that implements <code>Messagable</code> and <code>MayHaveMessagableTab</code>.
         *
         * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
         */
        private class ValidationOverlay extends JLabel implements Messagable {

            /**
             * It's a <code>Serializable</code> class.
             */
            private static final long serialVersionUID = 383550730517671431L;

            /**
             * The message buffer.
             */
            private DefaultMessageAreaModel messageBuffer;

            /**
             * {@inheritDoc}
             */
            @SuppressWarnings("deprecation")
            public void setMessage(Message message) {

                // TODO, (JAF), 20100315, I think it's better to move this logic to the handler.
                this.getMessageBuffer().setMessage(message);

                final Message theMessage = this.getMessageBuffer().getMessage();
                this.setToolTipText(theMessage.getMessage());

                final Severity severity = theMessage.getSeverity();
                final Icon icon;
                if (severity != null) {
                    icon = getApplicationConfig().iconSource().getIcon("severity." + severity.getLabel() + ".overlay");
                } else {
                    icon = null;
                }
                this.setIcon(icon);
            }

            /**
             * Gets the message buffer and if does not exist then create it.
             *
             * @return the message buffer.
             */
            private DefaultMessageAreaModel getMessageBuffer() {

                if (this.messageBuffer == null) {
                    this.setMessageBuffer(new DefaultMessageAreaModel(this));
                }

                return this.messageBuffer;
            }

            /**
             * Sets the message buffer.
             *
             * @param messageBuffer the message buffer to set.
             */
            private void setMessageBuffer(DefaultMessageAreaModel messageBuffer) {

                Assert.notNull(messageBuffer, "messageBuffer");

                this.messageBuffer = messageBuffer;
            }
        }
    }

    private class ErrorReportingOverlay extends JLabel implements Messagable, Guarded, MayHaveMessagableTab {

        private DefaultMessageAreaModel messageBuffer = new DefaultMessageAreaModel(this);
        private MessagableTab messagableTab = null;
        private int tabIndex = 0;

        public boolean isEnabled() {
            return true;
        }

        public void setEnabled(boolean enabled) {
            setVisible(!enabled);
        }

        public void setMessagableTab(MessagableTab messagableTab, int tabIndex) {
            this.messagableTab = messagableTab;
            this.tabIndex = tabIndex;
        }

        public void setMessage(Message message) {
            // geef de messgage door aan de omringende tabbedpane als ie er is
            if (this.messagableTab != null)
                this.messagableTab.setMessage(this, message, this.tabIndex);
            messageBuffer.setMessage(message);
            message = messageBuffer.getMessage();
            setToolTipText(message.getMessage());
            Severity severity = message.getSeverity();
            if (severity != null)
                setIcon(ValkyrieRepository.getInstance().getApplicationConfig().iconSource().getIcon("severity." + severity.getLabel() + ".overlay"));
            else
                setIcon(null);
        }
    }
}
