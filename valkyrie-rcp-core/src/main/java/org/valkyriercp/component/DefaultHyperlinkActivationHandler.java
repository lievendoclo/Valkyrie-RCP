/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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