package org.valkyriercp.application.exceptionhandling;

import org.jdesktop.swingx.JXFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.MessageSourceAccessor;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.config.ApplicationConfig;

import javax.swing.*;

/**
 * Logs a throwable and shows a dialog about it to the user.
 *
 * @author Geoffrey De Smet
 * @since 0.3
 */
@Configurable
public abstract class AbstractDialogExceptionHandler<SELF extends AbstractDialogExceptionHandler<SELF>> extends AbstractLoggingExceptionHandler<SELF> {

    private static final String DIALOG_EXCEPTION_HANDLER_KEY = "dialogExceptionHandler";

    @Autowired
    protected MessageSourceAccessor messageSourceAccessor;

    @Autowired
    protected ApplicationConfig applicationConfig;

    protected boolean modalDialog = true;
    protected ShutdownPolicy shutdownPolicy = ShutdownPolicy.ASK;

    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    /**
     * Where or not the shown dialog should be modal (see JDialog API).
     * The default is true;
     *
     * @param modalDialog
     */
    public void setModalDialog(boolean modalDialog) {
        this.modalDialog = modalDialog;
    }

    public SELF usingModalDialog(boolean modalDialog) {
        setModalDialog(modalDialog);
        return self();
    }

    /**
     * Wheter or not the user should be asked or obligated to shutdown the application.
     * The default is ASK.
     *
     * @param shutdownPolicy
     */
    public void setShutdownPolicy(ShutdownPolicy shutdownPolicy) {
        this.shutdownPolicy = shutdownPolicy;
    }

    public SELF havingShutdownPolicy(ShutdownPolicy shutdownPolicy) {
        setShutdownPolicy(shutdownPolicy);
        return self();
    }

    public void notifyUserAboutException(Thread thread, Throwable throwable) {
        Object[] options;
        switch (shutdownPolicy) {
            case NONE:
                options = new String[]{
                        messageSourceAccessor.getMessage(DIALOG_EXCEPTION_HANDLER_KEY + ".none.ok")};
                break;
            case ASK:
                options = new String[]{
                        messageSourceAccessor.getMessage(DIALOG_EXCEPTION_HANDLER_KEY + ".ask.shutdown"),
                        messageSourceAccessor.getMessage(DIALOG_EXCEPTION_HANDLER_KEY + ".ask.continue")};
                break;
            case OBLIGATE:
                options = new String[]{
                        messageSourceAccessor.getMessage(DIALOG_EXCEPTION_HANDLER_KEY + ".obligate.shutdown")};
                break;
            default:
                // Can not occur and if it does it will crash the event thread
                throw new IllegalStateException("Unrecognized shutdownPolicy: " + shutdownPolicy);
        }
        int result = JOptionPane.showOptionDialog(
                resolveParentFrame(),
                createExceptionContent(throwable),
                resolveExceptionCaption(throwable),
                JOptionPane.DEFAULT_OPTION,
                resolveMessageType(), null,
                options, options[0]);
        if ((shutdownPolicy == ShutdownPolicy.ASK && result == 0)
                || shutdownPolicy == ShutdownPolicy.OBLIGATE) {
            logger.info("Shutting down due to uncaught exception.");
            try {
                applicationConfig.application().close(true, 1);
            } finally {
                // In case the instance() method throws an exception and an exit didn't occur
                System.exit(2);
            }
        }
    }

    protected JXFrame resolveParentFrame() {
        ApplicationWindow activeWindow = applicationConfig.windowManager().getActiveWindow();
        if(activeWindow == null)
            return null;
        return activeWindow.getControl();
    }

    public abstract Object createExceptionContent(Throwable throwable);

    public abstract String resolveExceptionCaption(Throwable throwable);

    private int resolveMessageType() {
        return JOptionPane.ERROR_MESSAGE;
    }

}

