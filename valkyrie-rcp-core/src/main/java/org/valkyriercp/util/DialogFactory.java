package org.valkyriercp.util;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.config.ApplicationConfig;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@org.springframework.stereotype.Component
public class DialogFactory {
    @Autowired
    private ApplicationConfig applicationConfig;

    private Map<Integer, Object[]> optionsMap;

    public static final int YES_OPTION = 0;
    public static final int OK_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;

    protected void initOptions() {
        optionsMap = new HashMap<Integer, Object[]>();
        String yes = applicationConfig.messageResolver().getMessage(null, "OptionPane.yesButtonText", MessageConstants.LABEL);
        String no = applicationConfig.messageResolver().getMessage(null, "OptionPane.noButtonText", MessageConstants.LABEL);
        String cancel = applicationConfig.messageResolver().getMessage(null, "OptionPane.cancelButtonText", MessageConstants.LABEL);
        String ok = applicationConfig.messageResolver().getMessage(null, "OptionPane.okButtonText", MessageConstants.LABEL);

        optionsMap.put(Integer.valueOf(JOptionPane.DEFAULT_OPTION), new Object[]{ok});
        optionsMap.put(Integer.valueOf(JOptionPane.YES_NO_OPTION), new Object[]{yes, no});
        optionsMap.put(Integer.valueOf(JOptionPane.YES_NO_CANCEL_OPTION), new Object[]{yes, no, cancel});
        optionsMap.put(Integer.valueOf(JOptionPane.OK_CANCEL_OPTION), new Object[]{ok, cancel});
    }

    public void handleException(Throwable t) {
        applicationConfig.applicationLifecycleAdvisor().getRegisterableExceptionHandler().uncaughtException(
                Thread.currentThread(), t);
    }

    public void showErrorDialog(Throwable t) {
        String title = applicationConfig.messageResolver().getMessage(null, MessageConstants.ERROR_KEY, MessageConstants.TITLE);
        String shortMessage = applicationConfig.messageResolver().getMessage(t.getClass().getName() + "." + MessageConstants.MESSAGE);
        if (shortMessage == null || "".equals(shortMessage)) {
            shortMessage = t.getMessage();
            if (shortMessage == null || "".equals(shortMessage)) {
                shortMessage = applicationConfig.messageResolver().getMessage(null, MessageConstants.ERROR_KEY, MessageConstants.MESSAGE);
            }
        }
        showErrorDialog(null, new ErrorInfo(title, shortMessage, getDetailsAsHTML(title, Level.SEVERE, t),
                null, t, Level.SEVERE, null));
    }

    public void showSQLExceptionErrorDialog(SQLException sqlException) {
        String title = applicationConfig.messageResolver().getMessage(null, MessageConstants.ERROR_KEY, MessageConstants.TITLE);
        String shortMessage = applicationConfig.messageResolver().getMessage(sqlException.getClass().getName() + "."
                + sqlException.getErrorCode() + "." + MessageConstants.MESSAGE);
        if (!StringUtils.hasText(shortMessage)) {
            shortMessage = applicationConfig.messageResolver()
                    .getMessage(sqlException.getClass().getName() + "." + MessageConstants.MESSAGE);
            shortMessage += "\nSQL error " + sqlException.getErrorCode();
        }
        if (shortMessage == null || "".equals(shortMessage)) {
            shortMessage = sqlException.getMessage();
            if (shortMessage == null || "".equals(shortMessage)) {
                shortMessage = applicationConfig.messageResolver().getMessage(null, MessageConstants.ERROR_KEY, MessageConstants.MESSAGE);
            }
        }
        showErrorDialog(null, new ErrorInfo(title, shortMessage, getDetailsAsHTML(title, Level.SEVERE,
                sqlException), null, sqlException, Level.SEVERE, null));
    }

    /**
     * Converts the incoming string to an escaped output string. This method is far from perfect, only
     * escaping &lt;, &gt; and &amp; characters
     */
    private String escapeXml(String input) {
        return input == null ? "" : input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }


    /**
     * Creates and returns HTML representing the details of this incident info. This method is only called if
     * the details needs to be generated: ie: the detailed error message property of the incident info is
     * null.
     */
    private String getDetailsAsHTML(String title, Level level, Throwable e) {
        if (e != null) {
            // convert the stacktrace into a more pleasent bit of HTML
            StringBuffer html = new StringBuffer("<html>");
            html.append("<h2>" + escapeXml(title) + "</h2>");
            html.append("<HR size='1' noshade>");
            html.append("<div></div>");
            html.append("<b>Message:</b>");
            html.append("<pre>");
            html.append("    " + escapeXml(e.toString()));
            html.append("</pre>");
            html.append("<b>Level:</b>");
            html.append("<pre>");
            html.append("    " + level);
            html.append("</pre>");
            html.append("<b>Stack Trace:</b>");
            html.append("<pre>");
            for (StackTraceElement el : e.getStackTrace()) {
                html.append("    " + el.toString().replace("<init>", "&lt;init&gt;") + "\n");
            }
            if (e.getCause() != null) {
                html.append("</pre>");
                html.append("<b>Cause:</b>");
                html.append("<pre>");
                html.append(e.getCause().getMessage());
                html.append("</pre><pre>");
                for (StackTraceElement el : e.getCause().getStackTrace()) {
                    html.append("    " + el.toString().replace("<init>", "&lt;init&gt;") + "\n");
                }
            }
            html.append("</pre></html>");
            return html.toString();
        } else {
            return null;
        }
    }

    public void showErrorDialogResolveMessages(String id) {
        String title = applicationConfig.messageResolver().getMessage(id, MessageConstants.ERROR_KEY, MessageConstants.TITLE);
        String message = applicationConfig.messageResolver().getMessage(id, MessageConstants.ERROR_KEY, MessageConstants.MESSAGE);
        String detail = applicationConfig.messageResolver().getMessage(id, MessageConstants.ERROR_KEY, MessageConstants.DETAIL);
        detail = detail == MessageConstants.ERROR_KEY ? "" : detail;
        showErrorDialog(title, message, detail);
    }

    public void showErrorDialog(String message) {
        showErrorDialog(message, (String) null);
    }

    public void showErrorDialog(String message, String detail) {
        showErrorDialog(applicationConfig.messageResolver().getMessage(null, MessageConstants.ERROR_KEY, MessageConstants.TITLE), message, detail);
    }

    public void showErrorDialog(String id, Throwable cause) {
        String title = applicationConfig.messageResolver().getMessage(id, MessageConstants.ERROR_KEY, MessageConstants.TITLE);
        String message = applicationConfig.messageResolver().getMessage(id, MessageConstants.ERROR_KEY, MessageConstants.MESSAGE);
        showErrorDialog(null, new ErrorInfo(title, message, getDetailsAsHTML(title, Level.SEVERE, cause),
                null, cause, Level.SEVERE, null));
    }

    public void showErrorDialog(String title, String message, String detail) {
        showErrorDialog(null, new ErrorInfo(title, message, detail, null, null, Level.SEVERE, null));
    }

    public void showErrorDialog(Component parent, ErrorInfo errorInfo) {

        if (parent == null) {

            ApplicationWindow activeWindow = applicationConfig.windowManager().getActiveWindow();
            if (activeWindow != null)
                parent = activeWindow.getControl();
        }

        JXErrorPane pane = new JXErrorPane();
        pane.setErrorInfo(errorInfo);
//        pane.setErrorReporter(new JdicEmailNotifierErrorReporter());

        JXErrorPane.showDialog(parent, pane);
    }

    public int showWarningDialog(String id, int optionType) {
        return showWarningDialog(applicationConfig.windowManager().getActiveWindow().getControl(), id, optionType);
    }

    public int showWarningDialog(Component parent, String id, int optionType) {
        return showWarningDialog(parent, id, null, optionType);
    }

    public int showWarningDialog(Component parent, String id, Object[] parameters, int optionType) {
        String message = applicationConfig.messageResolver().getMessage(null, id, MessageConstants.TEXT, parameters);
        String title = applicationConfig.messageResolver().getMessage(null, id, MessageConstants.TITLE);
        return JOptionPane.showConfirmDialog(parent, message, title, optionType, JOptionPane.WARNING_MESSAGE);
    }

    public int showWarningDialog(String id, int optionType, int initialValue) {
        return showWarningDialog(applicationConfig.windowManager().getActiveWindow().getControl(), id, null, optionType,
                initialValue);
    }

    public int showWarningDialog(Component parent, String id, Object[] parameters, int optionType,
                                 int initialValue) {
        Object[] options = optionsMap.get(Integer.valueOf(optionType));
        String message = applicationConfig.messageResolver().getMessage(null, id, MessageConstants.TEXT, parameters);
        String title = applicationConfig.messageResolver().getMessage(null, id, MessageConstants.TITLE);

        if (optionType == JOptionPane.OK_CANCEL_OPTION && initialValue == CANCEL_OPTION)
            initialValue = 1;

        if (initialValue >= options.length)
            throw new IllegalArgumentException(
                    "De waarde van het argument initialValue is niet gekend door het gekozen optionType");

        return JOptionPane.showOptionDialog(parent, message, title, optionType, JOptionPane.WARNING_MESSAGE,
                null, options, options[initialValue]);
    }
    
    public  void showWarningDialog(String id)
    {
        showWarningDialog(applicationConfig.windowManager().getActiveWindow().getControl(), id);
    }

    public  void showWarningDialog(Component parent, String id)
    {
        showWarningDialog(parent, id, null);
    }

    public  void showWarningDialog(Component parent, String id, Object[] parameters)
    {
        String message = applicationConfig.messageResolver().getMessage(null, id, MessageConstants.TEXT, parameters);
        String title = applicationConfig.messageResolver().getMessage(null, id, MessageConstants.TITLE);
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public  int showConfirmationDialog(String id)
    {
        return showConfirmationDialog(applicationConfig.windowManager().getActiveWindow().getControl(), id);
    }

    public  int showConfirmationDialog(Component parent, String id)
    {
        return showConfirmationDialog(parent, id, null);
    }

    public  int showConfirmationDialog(Component parent, String id, Object[] parameters)
    {
        return showConfirmationDialog(parent, id, parameters, JOptionPane.YES_NO_CANCEL_OPTION);
    }

    public  int showConfirmationDialog(Component parent, String id, Object[] parameters, int optionType)
    {
        String message = applicationConfig.messageResolver().getMessage(null, id, MessageConstants.TEXT, parameters);
        String title = applicationConfig.messageResolver().getMessage(null, id, MessageConstants.TITLE);
        return JOptionPane.showConfirmDialog(parent, message, title, optionType);
    }

    public  void showMessageDialog(Component parent, String id, Object[] parameters, int optionType)
    {
        String message = applicationConfig.messageResolver().getMessage(null, id, MessageConstants.TEXT, parameters);
        String title = applicationConfig.messageResolver().getMessage(null, id, MessageConstants.TITLE);
        JOptionPane.showMessageDialog(parent, message, title, optionType);
    }
}
