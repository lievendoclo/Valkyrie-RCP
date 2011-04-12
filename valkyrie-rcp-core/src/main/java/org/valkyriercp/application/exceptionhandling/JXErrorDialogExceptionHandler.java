package org.valkyriercp.application.exceptionhandling;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorReporter;

import java.util.logging.Level;

/**
 * Error handler based on the {@link org.jdesktop.swingx.JXErrorPane} found in the swingx project.
 *
 * @author Jan Hoskens
 */
public class JXErrorDialogExceptionHandler<SELF extends JXErrorDialogExceptionHandler<SELF>> extends MessagesDialogExceptionHandler<SELF> {

    private ErrorReporter errorReporter;

    /**
     * No shutDownPolicy can be used in conjunction with the {@link org.jdesktop.swingx.JXErrorPane}.
     */
    public void setShutdownPolicy(ShutdownPolicy shutdownPolicy) {
        throw new UnsupportedOperationException(
                "JXErrorDialogExceptionHandler does not support setting of ShutdownPolicy");
    }

    /**
     * Add an {@link ErrorReporter} to the {@link org.jdesktop.swingx.JXErrorPane}.
     *
     * @param errorReporter error reporter to add.
     */
    public void setErrorReporter(ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

    public SELF reportingTo(ErrorReporter errorReporter) {
        setErrorReporter(errorReporter);
        return  self();
    }

    /**
     * Shows the {@link org.jdesktop.swingx.JXErrorPane} to the user.
     */
    public void notifyUserAboutException(Thread thread, Throwable throwable) {
        ErrorInfo errorInfo = new ErrorInfo(
                resolveExceptionCaption(throwable),
                (String) createExceptionContent(throwable),
                getDetailsAsHTML(throwable.getMessage(), throwable),
                null, throwable, Level.SEVERE,  null);
        JXErrorPane pane = new JXErrorPane();
        pane.setErrorInfo(errorInfo);
        if (errorReporter != null) {
            pane.setErrorReporter(errorReporter);
        }

        JXErrorPane.showDialog(resolveParentFrame(), pane);
    }

    /**
     * Converts the incoming string to an escaped output string. This method is
     * far from perfect, only escaping &lt;, &gt; and &amp; characters
     */
    private static String escapeXml(String input) {
        return input == null ? "" : input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /**
     * Creates and returns HTML representing the details of this incident info.
     * This method is only called if the details needs to be generated: ie: the
     * detailed error message property of the incident info is null.
     */
    private static String getDetailsAsHTML(String title, Throwable e) {
        if (e != null) {
            // convert the stacktrace into a more pleasent bit of HTML
            StringBuilder html = new StringBuilder("<html>");
            html.append("<h2>").append(escapeXml(title)).append("</h2>");
            html.append("<HR size='1' noshade>");
            html.append("<div></div>");
            html.append("<b>Message:</b>");
            html.append("<pre>");
            html.append("    ").append(escapeXml(e.toString()));
            html.append("</pre>");
            html.append("<b>Stack trace:</b>");
            html.append("<pre>");
            for (StackTraceElement el : e.getStackTrace()) {
                html.append("    ").append(el.toString().replace("<init>", "&lt;init&gt;")).append("\n");
            }
            if (e.getCause() != null) {
                html.append("</pre>");
                html.append("<b>Cause:</b>");
                html.append("<pre>");
                html.append(e.getCause().getMessage());
                html.append("</pre><pre>");
                for (StackTraceElement el : e.getCause().getStackTrace()) {
                    html.append("    ").append(el.toString().replace("<init>", "&lt;init&gt;")).append("\n");
                }
            }
            html.append("</pre></html>");
            return html.toString();
        } else {
            return null;
        }
    }

}
