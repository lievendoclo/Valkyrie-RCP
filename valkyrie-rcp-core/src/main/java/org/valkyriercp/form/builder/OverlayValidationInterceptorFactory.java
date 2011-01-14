package org.valkyriercp.form.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.component.DefaultMessageAreaModel;
import org.valkyriercp.component.MayHaveMessagableTab;
import org.valkyriercp.component.MessagableTab;
import org.valkyriercp.core.Guarded;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.core.Message;
import org.valkyriercp.core.Severity;
import org.valkyriercp.form.HasValidationComponent;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.util.OverlayHelper;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@Configurable
public class OverlayValidationInterceptorFactory implements FormComponentInterceptorFactory
{
    @Autowired
    private IconSource iconSource;

    public FormComponentInterceptor getInterceptor(FormModel formModel) {
        return new OverlayValidationInterceptor(formModel);
    }

    public class OverlayValidationInterceptor extends ValidationInterceptor {

        public OverlayValidationInterceptor(FormModel formModel) {
            super(formModel);
        }

        public void processComponent(String propertyName, final JComponent component) {
            final ErrorReportingOverlay overlay = new ErrorReportingOverlay();

            registerGuarded(propertyName, overlay);
            registerMessageReceiver(propertyName, overlay);

            if (component.getParent() == null) {
                PropertyChangeListener waitUntilHasParentListener = new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        if (component.getParent() != null) {
                            component.removePropertyChangeListener("ancestor", this);
                            attachOverlay(overlay, component);
                        }
                    }
                };
                component.addPropertyChangeListener("ancestor", waitUntilHasParentListener);
            }
            else {
                attachOverlay(overlay, component);
            }
        }

        private void attachOverlay(ErrorReportingOverlay overlay, JComponent component) {
            JComponent componentToOverlay;
            if (component instanceof HasValidationComponent)
                componentToOverlay = ((HasValidationComponent) component).getValidationComponent();
            else
                componentToOverlay = hasParentScrollPane(component) ? getParentScrollPane(component) : component;
            int yOffset = componentToOverlay.getPreferredSize().height;
            OverlayHelper.attachOverlay(overlay, componentToOverlay, SwingConstants.NORTH_WEST, 0, Math.min(yOffset,
                    new JTextField().getPreferredSize().height));
        }

        private JScrollPane getParentScrollPane(JComponent component) {
            return (JScrollPane)component.getParent().getParent();
        }

        private boolean hasParentScrollPane(JComponent component) {
            return component.getParent() != null && component.getParent() instanceof JViewport
                    && component.getParent().getParent() instanceof JScrollPane;
        }
    }

    private class ErrorReportingOverlay extends JLabel implements Messagable, Guarded, MayHaveMessagableTab
    {

        private DefaultMessageAreaModel messageBuffer = new DefaultMessageAreaModel(this);
        private MessagableTab messagableTab = null;
        private int tabIndex = 0;

        public boolean isEnabled()
        {
            return true;
        }

        public void setEnabled(boolean enabled)
        {
            setVisible(!enabled);
        }

        public void setMessagableTab(MessagableTab messagableTab, int tabIndex)
        {
            this.messagableTab = messagableTab;
            this.tabIndex = tabIndex;
        }

        public void setMessage(Message message)
        {
            // geef de messgage door aan de omringende tabbedpane als ie er is
            if (this.messagableTab != null)
                this.messagableTab.setMessage(this, message, this.tabIndex);
            messageBuffer.setMessage(message);
            message = messageBuffer.getMessage();
            setToolTipText(message.getMessage());
            Severity severity = message.getSeverity();
            if (severity != null)
                setIcon(iconSource.getIcon("severity." + severity.getLabel() + ".overlay"));
            else
                setIcon(null);
        }
    }
}
