package org.valkyriercp.component;

import org.valkyriercp.application.ApplicationException;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.net.URL;

/**
 * An implementation of HyperlinkListener that will open a web browser
 * when a URL is activated or, when an anchor link is activated scoll the
 * pane to display the anchor location.
 *
 * @author Oliver Hutchison
 */
public class DefaultHyperlinkActivationHandler implements HyperlinkListener {

    /**
     * Called when the user clicks on an anchor e.g. <br>
     * &lt;a href=&quot;#top&quot;&gt;Go to Top&lt;/a&gt;.
     * <p>
     * This default implementation will scroll the source pane so that the anchor
     * target becomes visible.
     */
    protected void handleAnchorActivated(HyperlinkEvent e, String anchor) {
        ((JTextPane)e.getSource()).scrollToReference(anchor);
    }

    /**
     * Called when the user clicks on a URL link e.g. <br>
     * &lt;a href=&quot;http://some.site&quot;&gt;Go to Some Site&lt;/a&gt;.
     * <p>
     * This default implementation attempt to open the link in the systems
     * default browser.
     */
    protected void handleUrlActivated(HyperlinkEvent e, URL url) {
        try {
            Desktop.getDesktop().browse(url.toURI());
        } catch (Exception ex) {
            throw new ApplicationException("Error handling URL " + url, ex);
        }
    }

    /**
     * Called when the user clicks on a link that is neither an anchor or URL. e.g<br>
     * &lt;a href=&quot;whatever&quot;&gt;what ever&lt;/a&gt;.
     * <p>
     * This default implementation does nothing.
     */
    protected void handleOtheActivated(HyperlinkEvent e) {
        // do nothing
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
            if (e.getDescription().startsWith("#")) {
                handleAnchorActivated(e, e.getDescription().substring(1));
            }
            else if (e.getURL() != null) {
                handleUrlActivated(e, e.getURL());
            } else {
                handleOtheActivated(e);
            }
        }
    }
}