package org.valkyriercp.application.exceptionhandling;

import com.google.common.base.Joiner;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorReporter;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.MessageSourceAccessor;
import org.valkyriercp.util.ValkyrieRepository;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class EmailNotifierErrorReporter implements ErrorReporter, BeanNameAware {
    private String id = null;

    private Desktop getDesktop() {
        return Desktop.getDesktop();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBeanName(String beanName) {
        if (getId() == null) {
            setId(beanName);
        }
    }

    public MessageSourceAccessor getMessageSourceAccessor() {
        return ValkyrieRepository.getInstance().getApplicationConfig().messageSourceAccessor();
    }

    public void reportError(ErrorInfo info) {
        String mailTo = getMessageByKeySuffix(".mailTo");
        String[] mailToTokens = mailTo.split(";");
        List<String> toAddrs = new ArrayList<String>(mailToTokens.length);

        Throwable errorException = info.getErrorException();
        Object[] messageParams;
        if (errorException != null) {
            messageParams = new Object[]{
                    errorException,
                    getStackTraceString(errorException)
            };
        } else {
            messageParams = new Object[]{
                    info.getBasicErrorMessage(),
                    info.getDetailedErrorMessage()
            };
        }

        String subject = getMessageByKeySuffix(".subject", messageParams);
        String body = getMessageByKeySuffix(".body", messageParams);

        String uriStr = String.format("mailto:%s?subject=%s&body=%s",
                Joiner.on(',').join(toAddrs), // use semicolon ";" for Outlook!
                urlEncode(subject),
                urlEncode(body));

        try {
            getDesktop().mail(new URI(uriStr));
        }  catch (UnsupportedOperationException e) {
            String message = getMessageByKeySuffix(".mailNotSupported");
            throw new IllegalStateException(message, e);
        } catch (IOException e) {
            String message = getMessageByKeySuffix(".noMailClient");
            throw new IllegalStateException(message, e);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static final String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessageByKeySuffix(String keySuffix) {
        return getMessageByKeySuffix(keySuffix, null);
    }

    protected String getMessageByKeySuffix(String keySuffix, Object[] params) {
        List<String> messageKeyList = new ArrayList<String>();
        if (getId() != null) {
            messageKeyList.add(getId() + keySuffix);
        }
        messageKeyList.add("emailNotifierErrorReporter" + keySuffix);
        String[] messagesKeys = messageKeyList.toArray(new String[messageKeyList.size()]);
        return getMessageSourceAccessor().getMessage(new DefaultMessageSourceResolvable(
                messagesKeys, params, messagesKeys[0]));
    }

    protected String getStackTraceString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        t.printStackTrace(printWriter);
        printWriter.flush();
        stringWriter.flush();
        return stringWriter.toString();
    }
}
