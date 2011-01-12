package org.valkyriercp.component;

import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;

/**
 * A HTMLEditorKit that loads all images and HTML synchronously. This is useful
 * when you want to be sure that the entire document including images has been
 * loaded before you display it.
 *
 * @author Oliver Hutchison
 */
public class SynchronousHTMLEditorKit extends HTMLEditorKit {

    public Document createDefaultDocument() {
        HTMLDocument doc = (HTMLDocument)super.createDefaultDocument();
        doc.setAsynchronousLoadPriority(-1);
        return doc;
    }

    public ViewFactory getViewFactory() {
        return new HTMLFactory() {

            public View create(Element elem) {
                View view = super.create(elem);
                if (view instanceof ImageView) {
                    ((ImageView)view).setLoadsSynchronously(true);
                }
                return view;
            }
        };
    }
}
