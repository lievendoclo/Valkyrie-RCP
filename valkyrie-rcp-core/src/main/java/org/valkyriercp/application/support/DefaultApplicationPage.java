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

import org.valkyriercp.application.*;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a standard implementation of {@link ApplicationPage}
 */
public class DefaultApplicationPage extends AbstractApplicationPage implements PageLayoutBuilder {

    private JComponent control;

    public DefaultApplicationPage() {

    }

    public DefaultApplicationPage( ApplicationWindow window, PageDescriptor pageDescriptor ) {
        super( window, pageDescriptor );
    }

    // Initial Application Page Layout Builder methods
    public void addView( String viewDescriptorId ) {
        showView( viewDescriptorId );
    }

    protected void doAddPageComponent( PageComponent pageComponent ) {
        // trigger the createControl method of the PageComponent, so if a
        // PageComponentListener is added
        // in the createControl method, the componentOpened event is received.
        pageComponent.getControl();
    }

    /**
	 * {@inheritDoc}
	 *
	 * Only one pageComponent is shown at a time, so if it's the active one,
	 * remove all components from this page.
	 */
    protected void doRemovePageComponent( PageComponent pageComponent ) {
        if (pageComponent == getActiveComponent())
        {
	    	this.control.removeAll();
	        this.control.validate();
	        this.control.repaint();
        }
    }

    protected boolean giveFocusTo( PageComponent pageComponent ) {
        PageComponentPane pane = pageComponent.getContext().getPane();
        this.control.removeAll();
        this.control.add( pane.getControl() );
        this.control.validate();
        this.control.repaint();
        pane.getControl().requestFocusInWindow();

        return true;
    }

    protected JComponent createControl() {
        this.control = new JPanel( new BorderLayout() );
        this.getPageDescriptor().buildInitialLayout( this );

        return control;
    }
}

