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

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
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
