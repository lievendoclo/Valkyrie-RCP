package org.valkyriercp.dialog;

import org.springframework.util.StringUtils;
import org.valkyriercp.component.DefaultMessageAreaModel;
import org.valkyriercp.component.HtmlPane;
import org.valkyriercp.component.MessagePane;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.core.Message;
import org.valkyriercp.core.Severity;
import org.valkyriercp.factory.AbstractControlFactory;
import org.valkyriercp.util.GuiStandardUtils;
import org.valkyriercp.util.UIConstants;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.StringReader;

/**
 * MessagePane implementation used by {@link MessageDialog}.
 *
 * @author Peter De Bruycker
 */
public class AlertMessageAreaPane extends AbstractControlFactory implements MessagePane, PropertyChangeListener {

    private Icon warningIcon;
    private Icon errorIcon;
    private Icon infoIcon;
    private HtmlPane messageArea;
    private JLabel iconLabel;
    private DefaultMessageAreaModel messageAreaModel;

    /**
     * Creates a new uninitialized {@code AlertMessageAreaPane}.
     */
    public AlertMessageAreaPane() {
        init( this );
    }

    /**
     * Creates a new {@code AlertMessageAreaPane} that uses the given delegate
     * as a message container.
     * @param delegate The messagable delegate.
     */
    public AlertMessageAreaPane( Messagable delegate ) {
        init( delegate );
    }

    private void init( Messagable delegate ) {
        this.messageAreaModel = new DefaultMessageAreaModel( delegate );
        this.messageAreaModel.addPropertyChangeListener( this );

        iconLabel = new JLabel();
        messageArea = new HtmlPane();

        Font defaultFont = UIManager.getFont( "Button.font" );
        String stylesheet = "body {  font-family: " + defaultFont.getName() + "; font-size: " + defaultFont.getSize()
                + "pt;  }" + "a, p, li { font-family: " + defaultFont.getName() + "; font-size: "
                + defaultFont.getSize() + "pt;  }";
        try {
            ((HTMLDocument) messageArea.getDocument()).getStyleSheet().loadRules( new StringReader( stylesheet ), null );
        } catch( IOException e ) {
        }

        GuiStandardUtils.textComponentAsLabel(messageArea);
        messageArea.setFont( new JLabel().getFont() );
        messageArea.setFocusable(false);
    }

    public int getPreferredHeight() {
        return messageArea.getPreferredSize().height;
    }

    protected JComponent createControl() {
        JPanel panel = new JPanel( new BorderLayout( UIConstants.TWO_SPACES, 0 ) );
        panel.add( iconLabel, BorderLayout.LINE_START );
        panel.add( messageArea );

        return panel;
    }

    public Message getMessage() {
        return messageAreaModel.getMessage();
    }

    public void setMessage( Message message ) {
        messageAreaModel.setMessage( message );
    }

    public boolean isMessageShowing() {
        if( messageArea == null ) {
            return false;
        }
        return StringUtils.hasText(messageArea.getText()) && messageArea.isVisible();
    }

    public void addPropertyChangeListener( PropertyChangeListener listener ) {
        messageAreaModel.addPropertyChangeListener( listener );
    }

    public void addPropertyChangeListener( String propertyName, PropertyChangeListener listener ) {
        messageAreaModel.addPropertyChangeListener( propertyName, listener );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener ) {
        messageAreaModel.removePropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( String propertyName, PropertyChangeListener listener ) {
        messageAreaModel.removePropertyChangeListener( propertyName, listener );
    }

    public void propertyChange( PropertyChangeEvent evt ) {
        update( getMessage() );
    }

    private void update( Message message ) {
        String text = message.getMessage();

        // try to split it into two parts
        String[] parts = message.getMessage().split( "\\n" );
        if( parts.length > 1 ) {
            StringBuffer sb = new StringBuffer();
            sb.append( "<html>" );
            sb.append( "<b>" );
            sb.append( parts[0] );
            sb.append( "</b>" );

            for( int i = 1; i < parts.length; i++ ) {
                sb.append( "<p>" );
                sb.append( parts[i] );
            }

            text = sb.toString();
        }

        messageArea.setText( text );
        iconLabel.setIcon( getIcon( message.getSeverity() ) );
    }

    /**
     * Returns the icon for the given severity.
     * @param severity The severity level.
     * @return The icon for the given severity, never null.
     */
    private Icon getIcon( Severity severity ) {
        if( severity == Severity.ERROR ) {
            return getErrorIcon();
        }
        if( severity == Severity.WARNING ) {
            return getWarningIcon();
        }
        return getInfoIcon();
    }

    private Icon getErrorIcon() {
        if( errorIcon == null ) {
            errorIcon = UIManager.getIcon( "OptionPane.errorIcon" );
        }
        return errorIcon;
    }

    /**
     * Sets the icon to be shown when displaying messages with error-level severity.
     * @param icon The error icon.
     */
    public void setErrorIcon( Icon icon ) {
        errorIcon = icon;
    }

    private Icon getWarningIcon() {
        if( warningIcon == null ) {
            warningIcon = UIManager.getIcon( "OptionPane.warningIcon" );
        }
        return warningIcon;
    }

    /**
     * Sets the icon to be shown when displaying messages with warning-level severity.
     * @param icon The warning icon.
     */
    public void setWarningIcon( Icon icon ) {
        warningIcon = icon;
    }

    private Icon getInfoIcon() {
        if( infoIcon == null ) {
            infoIcon = UIManager.getIcon( "OptionPane.informationIcon" );
        }
        return infoIcon;
    }

    /**
     * The icon to be shown when dispalying messages with info-level severity.
     * @param icon The info icon.
     */
    public void setInfoIcon( Icon icon ) {
        infoIcon = icon;
    }

}


