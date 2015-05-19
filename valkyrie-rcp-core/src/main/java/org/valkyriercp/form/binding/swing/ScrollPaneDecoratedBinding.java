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
package org.valkyriercp.form.binding.swing;

import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.DecoratedControlBinding;

import javax.swing.*;

/**
 * A convenience class that decorates the component produced from a source
 * Binding with a JScrollPane.  Useful for placing JList (and JTextArea)
 * bindings, among others, in a scroll pane when needed.
 *
 * @author Andy DePue
 */
public class ScrollPaneDecoratedBinding extends DecoratedControlBinding {

    public ScrollPaneDecoratedBinding(ComponentFactory componentFactory, final Binding source) {
        this(source, componentFactory.createScrollPane(source.getControl()));
    }

    public ScrollPaneDecoratedBinding(ComponentFactory componentFactory, final Binding source, final int vsbPolicy, final int hsbPolicy) {
        this(source, componentFactory.createScrollPane(source.getControl(), vsbPolicy,
                hsbPolicy));
    }

    public ScrollPaneDecoratedBinding(final Binding source, JComponent scrollPane) {
        super(source, scrollPane);
    }
}
