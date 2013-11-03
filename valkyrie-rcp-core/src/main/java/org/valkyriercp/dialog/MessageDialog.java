package org.valkyriercp.dialog;

import com.google.common.collect.Lists;
import org.springframework.util.Assert;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.core.DefaultMessage;
import org.valkyriercp.core.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Dialog for showing an message to the user. The severity of the message is used to
 * determine the icon.
 * <p>
 * If the message text contains line feeds ('\n'), the message is split into different
 * parts, and the first part is rendered in bold. This is to mimic the guidelines in
 * http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGWindows/chapter_17_section_6.html#//apple_ref/doc/uid/20000957-20000961-BACFBACB
 */
public class MessageDialog extends ApplicationDialog {

    private static final String OK_FACE_DESCRIPTOR_ID = "okCommand";

    private AlertMessageAreaPane messageAreaPane;

    private Message message;

    private float dialogScaleFactor = 0.55f;

    /** Minimum width for the dialog, in case the scale factor results in a tiny value. */
    private int minimumWidth = 600;

    /**
     * Constructs a new dialog.
     *
     * @param title the title
     * @param message the message
     */
    public MessageDialog( String title, Message message ) {
        this( title, null, message );
    }

    /**
     * Constructs a new dialog.
     *
     * @param title the title
     * @param parent the parent window
     * @param message the message
     */
    public MessageDialog( String title, Window parent, Message message ) {
        super( title, parent );
        setMessage( message );
    }

    /**
     * Constructs a new dialog, the message is converted to an information message
     *
     * @param title the title
     * @param message the message text
     */
    public MessageDialog( String title, String message ) {
        this( title, null, message );
    }

    /**
     * Constructs a new dialog, the message is converted to an information message
     *
     * @param title the title
     * @param parent the parent window
     * @param message the message text
     */
    public MessageDialog( String title, Window parent, String message ) {
        super( title, parent );
        setMessage( message );
    }

    /**
     * Set the message text, severity is info.
     *
     * @param text the message text
     */
    public void setMessage( String text ) {
        setMessage( new DefaultMessage( text ) );
    }

    /**
     * Set the message.
     *
     * @param message the message
     */
    public void setMessage( Message message ) {
        Assert.notNull(message, "The message is required");
        Assert.hasText( message.getMessage(), "The message text is required" );
        this.message = message;
    }

    /**
     * Get the message
     *
     * @return message
     */
    public Message getMessage() {
        return message;
    }

    protected String getCancelCommandId() {
        return OK_FACE_DESCRIPTOR_ID;
    }

    protected void registerDefaultCommand() {
        registerCancelCommandAsDefault();
    }

    protected java.util.List<AbstractCommand> getCommandGroupMembers() {
        return Lists.<AbstractCommand>newArrayList(getCancelCommand());
    }

    protected JComponent createDialogContentPane() {
        this.messageAreaPane = new AlertMessageAreaPane();
        this.messageAreaPane.setMessage( message );
        return messageAreaPane.getControl();
    }

    protected void disposeDialogContentPane() {
    	messageAreaPane = null;
    }

    protected final boolean onFinish() {
        // final because it can never get called
        return true;
    }

    protected void onAboutToShow() {
        int width = getDialog().getWidth();
        float scale = getDialogScaleFactor();
        int parentWidth = getDialog().getParent().getWidth();
        if( width > parentWidth * scale ) {
            final int messageAreaPaneHeight = messageAreaPane.getPreferredHeight();
            width = (int) (parentWidth * scale);
            if( width < getMinimumWidth() ) {
                width = getMinimumWidth();
            }

            // adjust the width
            getDialog().setSize( width, getDialog().getHeight() );

            // dirty hack, because messageAreaPane.getPreferredHeight() doesn't respond
            // immediately to dialog resize when dialog is not visible
            DialogSizeUpdater dialogSizeUpdater = new DialogSizeUpdater( messageAreaPaneHeight );
            getDialog().addComponentListener( dialogSizeUpdater );
        }
        getDialog().setResizable( false );
    }

    /**
     * Get the scale factor for the dialog size (as compared to the parent window). The
     * default returned here is 55% (.55).
     *
     * @return scale factor
     */
    public float getDialogScaleFactor() {
        return dialogScaleFactor;
    }

    /**
     * Set the scale factory for the dialog size.
     *
     * @param dialogScaleFactor New dialog scale factor
     */
    public void setDialogScaleFactor( float dialogScaleFactor ) {
        this.dialogScaleFactor = dialogScaleFactor;
    }

    /**
     * Get the minimum width for the dialog. This overrides the value calculated by the
     * scale factor.
     *
     * @return minimum width
     * @see #setDialogScaleFactor
     */
    public int getMinimumWidth() {
        return minimumWidth;
    }

    /**
     * Set the minimum width for the dialog. This overrides the value calculated by the
     * scale factor.
     *
     * @return minimum width
     */
    public void setMinimumWidth( int minimumWidth ) {
        this.minimumWidth = minimumWidth;
    }

    /**
     * Inner class to handle setting the size of the dialog heigth when it is shown.
     */
    private class DialogSizeUpdater extends ComponentAdapter {
        private final int height;

        private DialogSizeUpdater( int height ) {
            super();
            this.height = height;
        }

        public void componentShown( ComponentEvent e ) {
            // we must also change the height
            int newHeight = getDialog().getHeight() + messageAreaPane.getPreferredHeight() - height;
            getDialog().setSize( getDialog().getWidth(), newHeight );
        }
    }
}

