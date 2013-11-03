package org.valkyriercp.core;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.ObjectUtils;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.image.NoSuchImageResourceException;
import org.valkyriercp.util.LabelUtils;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.Serializable;

/**
 * The default implementation of the {@link Message} interface. This class is
 * capable of rendering itself on {@link javax.swing.text.JTextComponent}s and {@link javax.swing.JLabel}s.
 * In the case of labels, it is also able to lookup an icon to be displayed on
 * the label.
 *
 * @see #getIcon()
 *
 */
public class DefaultMessage implements Message, Serializable {

    private static final long serialVersionUID = -6524078363891514995L;

	private final long timestamp;

    private final String message;

    private final Severity severity;

    /**
     * A convenience instance representing an empty message. i.e. The message text
     * is empty and there is no associated severity.
     */
    public static final DefaultMessage EMPTY_MESSAGE = new DefaultMessage("", null);

    /**
     * Creates a new {@code DefaultMessage} with the given text and a default
     * severity of {@link Severity#INFO}.
     *
     * @param text The message text.
     */
    public DefaultMessage(String text) {
        this(text, Severity.INFO);
    }

    /**
     * Creates a new {@code DefaultMessage} with the given text and severity.
     *
     * @param message The message text.
     * @param severity The severity of the message. May be null.
     */
    public DefaultMessage(String message, Severity severity) {
        if (message == null) {
            message = "";
        }
        this.timestamp = System.currentTimeMillis();
        this.message = message;
        this.severity = severity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Severity getSeverity() {
        return severity;
    }

    /**
     * Renders this message on the given GUI component. This implementation only
     * supports components of type {@link javax.swing.text.JTextComponent} or {@link javax.swing.JLabel}.
     *
     * @throws IllegalArgumentException if {@code component} is not a {@link javax.swing.text.JTextComponent}
     * or a {@link javax.swing.JLabel}.
     */
    public void renderMessage(JComponent component) {
        if (component instanceof JTextComponent) {
            ((JTextComponent)component).setText(getMessage());
        }
        else if (component instanceof JLabel) {
            JLabel label = (JLabel)component;
            label.setText(LabelUtils.htmlBlock(getMessage()));
            label.setIcon(getIcon());
        }
        else {
            throw new IllegalArgumentException("Unsupported component type " + component);
        }
    }

    /**
     * Returns the icon associated with this instance's severity. The icon is
     * expected to be retrieved using a key {@code severity.&lt;SEVERITY_LABEL&gt;}.
     *
     * @return The icon associated with this instance's severity, or null if the
     * instance has no specified severity, or the icon could not be found.
     *
     * @see Severity#getLabel()
     * @see IconSource#getIcon(String)
     */
    public Icon getIcon() {
        if (severity == null) {
            return null;
        }
        try {
            IconSource iconSource = ValkyrieRepository.getInstance().getApplicationConfig().iconSource();
            return iconSource.getIcon("severity." + severity.getLabel());
        }
        catch (NoSuchImageResourceException e) {
            return null;
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof DefaultMessage)) {
            return false;
        }
        DefaultMessage other = (DefaultMessage)o;
        return this.message.equals(other.message) && ObjectUtils.nullSafeEquals(this.severity, other.severity);
    }

    public int hashCode() {
        return message.hashCode() + (severity != null ? severity.hashCode() : 0);
    }

    public String toString() {
        return new ToStringCreator(this).append("message", message).append("severity", severity).toString();
    }
}
