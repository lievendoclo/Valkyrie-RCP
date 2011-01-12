package org.valkyriercp.component;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;

/**
 * An extension of JTextPane for displaying HTML with the system LaF.
 *
 * @author Oliver Hutchison
 */
public class HtmlPane extends JTextPane {

    private boolean antiAlias;

    private Caret caret;

    private boolean allowSelection;

    /**
     * Creates a new HtmlPane. A default hyperlink activation handler will be
     * installed.
     */
    public HtmlPane() {
        this(true);
    }

    /**
     * Creates a new HtmlPane.
     *
     * @param installHyperlinkActivationHandler
     *            whether to install a default hyperlink activation handler.
     */
    public HtmlPane(boolean installHyperlinkActivationHandler) {
        setEditorKit(new SynchronousHTMLEditorKit());
        setEditable(false);
        installLaFStyleSheet();
        HyperlinkEnterExitBugFixer bugFixer = new HyperlinkEnterExitBugFixer();
        addMouseListener(bugFixer);
        addHyperlinkListener(bugFixer);
        if (installHyperlinkActivationHandler) {
            DefaultHyperlinkActivationHandler hyperlinkActivationHandler = new DefaultHyperlinkActivationHandler();
            addHyperlinkListener(hyperlinkActivationHandler);
        }
    }

    /**
     * Is the HTML rendered with anti-aliasing.
     */
    public boolean getAntiAlias() {
        return antiAlias;
    }

    /**
     * Set whether the pane should render the HTML using anti-aliasing.
     */
    public void setAntiAlias(boolean antiAlias) {
        if (this.antiAlias == antiAlias) {
            return;
        }
        this.antiAlias = antiAlias;
        firePropertyChange("antiAlias", !antiAlias, antiAlias);
        repaint();
    }

    /**
     * Is selection allowed in this pane.
     */
    public boolean getAllowSelection() {
        return allowSelection;
    }

    /**
     * Set whether or not selection should be allowed in this pane.
     */
    public void setAllowSelection(boolean allowSelection) {
        if (this.allowSelection == allowSelection) {
            return;
        }
        this.allowSelection = allowSelection;
        setCaretInternal();
        firePropertyChange("allowSelection", !allowSelection, allowSelection);
    }

    public void setCaret(Caret caret) {
        this.caret = caret;
        setCaretInternal();
    }

    public Caret getCaret() {
        return caret;
    }

    private void setCaretInternal() {
        if (allowSelection) {
            super.setCaret(caret);
        }
        else {
            super.setCaret(new NoSelectionCaret());
        }
    }

    /**
     * Applies the current LaF font setting to the document.
     */
    protected void installLaFStyleSheet() {
        Font defaultFont = UIManager.getFont("Button.font");
        String stylesheet = "body {  font-family: " + defaultFont.getName() + "; font-size: " + defaultFont.getSize()
                + "pt;  }" + "a, p, li { font-family: " + defaultFont.getName() + "; font-size: "
                + defaultFont.getSize() + "pt;  }";
        try {
            ((HTMLDocument)getDocument()).getStyleSheet().loadRules(new StringReader(stylesheet), null);
        }
        catch (IOException e) {
        }
    }

    public void paintComponent(Graphics g) {
        if (antiAlias) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        super.paintComponent(g);
    }

    private static class NoSelectionCaret extends DefaultCaret {
        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }
    }

    /*
     * Fixes a bug in the handling of HyperlinkEvents in JEditorPane. If a
     * hyperlink is active when the mouse exits the pane no Hyperlink EXITED
     * event is fired.
     */
    private class HyperlinkEnterExitBugFixer extends MouseAdapter implements HyperlinkListener {
        private boolean hyperlinkActive;

        public void mouseExited(MouseEvent e) {
            if (hyperlinkActive) {
                fireHyperlinkUpdate(new HyperlinkEvent(HtmlPane.this, HyperlinkEvent.EventType.EXITED, null));
                hyperlinkActive = true;
            }
        }

        public void mouseEntered(MouseEvent e) {
            if (hyperlinkActive) {
                fireHyperlinkUpdate(new HyperlinkEvent(HtmlPane.this, HyperlinkEvent.EventType.ENTERED, null));
            }
        }

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ENTERED)) {
                hyperlinkActive = true;
            }
            else if (e.getEventType().equals(HyperlinkEvent.EventType.EXITED)) {
                hyperlinkActive = false;
            }
        }
    }
}

