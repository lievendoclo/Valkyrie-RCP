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
package org.valkyriercp.application.support;

import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentPane;
import org.valkyriercp.component.SimpleInternalFrame;
import org.valkyriercp.factory.AbstractControlFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A <code>DefaultPageComponentPane</code> puts the <code>PageComponent</code> inside
 * a <code>SimpleInternalFrame</code>.
 *
 * @author Peter De Bruycker
 *
 */
public class DefaultPageComponentPane extends AbstractControlFactory implements PageComponentPane {
    private PageComponent component;
    private SimpleInternalFrame control;

    public DefaultPageComponentPane( PageComponent component ) {
        this.component = component;
    }

    public PageComponent getPageComponent() {
        return component;
    }

    protected JComponent createControl() {
        control = new SimpleInternalFrame( component.getIcon(), component.getDisplayName(), createViewToolBar(), component
                .getControl() );
        control.setTitle( component.getDisplayName() );
        control.setFrameIcon( component.getIcon() );
        control.setToolTipText( component.getCaption() );
        return control;
    }

    protected JToolBar createViewToolBar() {
        // todo
        return null;
    }
}

