package org.valkyriercp.application.support;

import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageDescriptor;

/**
 * Factory for <code>TabbedApplicationPage</code> instances.
 *
 * @author Peter De Bruycker
 */
public class TabbedApplicationPageFactory implements ApplicationPageFactory {

    private int tabPlacement = -1;
    private int tabLayoutPolicy = -1;

    public ApplicationPage createApplicationPage( ApplicationWindow window, PageDescriptor descriptor ) {
        TabbedApplicationPage page = new TabbedApplicationPage();
        page.setApplicationWindow( window );
        page.setDescriptor( descriptor );
        if( tabPlacement != -1 ) {
            page.setTabPlacement( tabPlacement );
        }
        if( tabLayoutPolicy != -1 ) {
            page.setTabLayoutPolicy( tabLayoutPolicy );
        }

        return page;
    }

    public void setTabPlacement( int tabPlacement ) {
        this.tabPlacement = tabPlacement;
    }

    public int getTabPlacement() {
        return tabPlacement;
    }

    public int getTabLayoutPolicy() {
        return tabLayoutPolicy;
    }

    public void setTabLayoutPolicy( int tabLayoutPolicy ) {
        this.tabLayoutPolicy = tabLayoutPolicy;
    }
}
