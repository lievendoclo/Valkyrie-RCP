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

import org.jdesktop.swingx.JXFrame;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.valkyriercp.application.*;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.util.EventListenerListHelper;
import org.valkyriercp.util.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Iterator;

public abstract class AbstractApplicationWindow implements ApplicationWindow, WindowFocusListener {
    private int number;
    private JXFrame control;
    private ApplicationPage currentApplicationPage;
    private ApplicationConfig applicationConfig;
    private ApplicationWindowConfigurer applicationWindowConfigurer;
    private EventListenerListHelper pageListeners = new EventListenerListHelper(PageListener.class);
    private CommandGroup menuBarCommandGroup;
    private CommandGroup toolBarCommandGroup;
    private StatusBar statusBar;
    private WindowManager windowManager;
    private ApplicationWindowCommandManager commandManager;

    public AbstractApplicationWindow(ApplicationConfig config) {
        this(config.windowManager().size(), config);
    }

    public AbstractApplicationWindow(int number, ApplicationConfig config) {
        this.number = number;
        applicationConfig = config;
        windowManager = applicationConfig.windowManager();
        applicationConfig.applicationLifecycleAdvisor().setOpeningWindow(this);
        init();
        getAdvisor().onCommandsCreated( this );
    }

    protected void init() {
        this.commandManager = getAdvisor().createWindowCommandManager();
        this.menuBarCommandGroup = getAdvisor().getMenuBarCommandGroup();
        this.toolBarCommandGroup = getAdvisor().getToolBarCommandGroup();
        this.statusBar = getAdvisor().getStatusBar();
    }

    public int getNumber() {
        return number;
    }

    @Override
    public JXFrame getControl() {
        return control;
    }

    @Override
    public ApplicationPage getPage() {
        return currentApplicationPage;
    }

    protected ApplicationLifecycleAdvisor getAdvisor() {
        return applicationConfig.applicationLifecycleAdvisor();
    }

    @Override
    public void enable() {
        getControl().setWaitPaneVisible(false);
    }

    @Override
    public void disable() {
        getControl().setWaitPaneVisible(true);
    }

    @Override
    public void showPage(String pageId) {
        if (pageId == null)
            throw new IllegalArgumentException("pageId == null");

        if (getPage() == null || !getPage().getId().equals(pageId)) {
            showPage(createPage(this, pageId));
        } else {
            // asking for the same page, so ignore
        }
    }

    @Override
    public void showPage(PageDescriptor pageDescriptor) {
        Assert.notNull(pageDescriptor, "pageDescriptor == null");

        if (getPage() == null || !getPage().getId().equals(pageDescriptor.getId())) {
            showPage(createPage(pageDescriptor));
        } else {
            // asking for the same page, so ignore
        }
    }

    /**
     * Set the given <code>ApplicationPage</code> active (visible + selected if
     * applicable)
     *
     * @param page the <code>ApplicationPage</code>
     */
    protected abstract void setActivePage(ApplicationPage page);

    @Override
    public void showPage(ApplicationPage page) {
        if (page == null)
            throw new IllegalArgumentException("page == null");

        if (this.currentApplicationPage == null) {
            this.currentApplicationPage = page;
            getAdvisor().onPreWindowOpen(getWindowConfigurer());
            this.control = createNewWindowControl();
            this.control.addWindowFocusListener(this);
            initWindowControl(this.control);
            getAdvisor().getOnWindowCreated().accept(this);
            setActivePage(page);
            this.control.setVisible(true);
            getAdvisor().getOnWindowOpened().accept(this);
        } else {
            if (!currentApplicationPage.getId().equals(page.getId())) {
                final ApplicationPage oldPage = this.currentApplicationPage;
                this.currentApplicationPage = page;
                setActivePage(page);
                pageListeners.fire("pageClosed", oldPage);
            } else {
                // asking for the same page, so ignore
            }
        }
        pageListeners.fire("pageOpened", this.currentApplicationPage);
    }

    protected final ApplicationPage createPage(ApplicationWindow window, String pageDescriptorId) {
        PageDescriptor descriptor = getPageDescriptor(pageDescriptorId);
        return createPage(descriptor);
    }

    /**
     * Factory method for creating the page area managed by this window. Subclasses may
     * override to return a custom page implementation.
     *
     * @param descriptor The page descriptor
     * @return The window's page
     */
    protected ApplicationPage createPage(PageDescriptor descriptor) {
        return applicationConfig.applicationPageFactory().createApplicationPage(this, descriptor);
    }

    protected PageDescriptor getPageDescriptor(String pageDescriptorId) {
        ApplicationContext ctx = applicationConfig.applicationContext();
        Assert.state(ctx.containsBean(pageDescriptorId), "Do not know about page or view descriptor with name '"
                + pageDescriptorId + "' - check your context config");
        Object desc = ctx.getBean(pageDescriptorId);
        if (desc instanceof PageDescriptor) {
            return (PageDescriptor) desc;
        } else if (desc instanceof ViewDescriptor) {
            return new SingleViewPageDescriptor((ViewDescriptor) desc);
        } else {
            throw new IllegalArgumentException("Page id '" + pageDescriptorId
                    + "' is not backed by an ApplicationPageDescriptor");
        }
    }

    protected ApplicationWindowConfigurer getWindowConfigurer() {
        if (applicationWindowConfigurer == null) {
            this.applicationWindowConfigurer = initWindowConfigurer();
        }
        return applicationWindowConfigurer;
    }

    protected ApplicationWindowConfigurer initWindowConfigurer() {
        return new DefaultApplicationWindowConfigurer(this);
    }

    protected JXFrame createNewWindowControl() {
        JXFrame frame = new JXFrame();
        frame.setIdleThreshold(10000);
        frame.setWaitPane(new JPanel());
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        WindowAdapter windowCloseHandler = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        };
        frame.addWindowListener(windowCloseHandler);
        new DefaultButtonFocusListener();
        return frame;
    }

    /**
     * Close this window. First checks with the advisor by calling the
     * {@link ApplicationLifecycleAdvisor#onPreWindowClose(ApplicationWindow)}
     * method. Then tries to close it's currentPage. If both are successfull,
     * the window will be disposed and removed from the {@link WindowManager}.
     *
     * @return boolean <code>true</code> if both, the advisor and the
     *         currentPage allow the closing action.
     */
    public boolean close() {
        boolean canClose = getAdvisor().onPreWindowClose(this);
        if (canClose) {
            // check if page can be closed
            if (currentApplicationPage != null) {
                canClose = currentApplicationPage.close();
                // page cannot be closed, exit method and do not dispose
                if (!canClose)
                    return canClose;
            }

            if (control != null) {
                control.dispose();
                control = null;
            }

            if (windowManager != null) {
                windowManager.remove(this);
            }
            windowManager = null;
        }
        return canClose;
    }

    @Override
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    /**
     * When gaining focus, set this window as the active one on it's manager.
     */
    public void windowGainedFocus(WindowEvent e) {
        if (this.windowManager != null)
            this.windowManager.setActiveWindow(this);
    }

    /**
     * When losing focus no action is done. This way the last focussed window will stay
     * listed as the activeWindow.
     */
    public void windowLostFocus(WindowEvent e) {
    }

    protected void initWindowControl(JFrame windowControl) {
        ApplicationWindowConfigurer configurer = getWindowConfigurer();
        applyStandardLayout(windowControl, configurer);
        prepareWindowForView(windowControl, configurer);
    }

    protected void applyStandardLayout(JFrame windowControl, ApplicationWindowConfigurer configurer) {
        windowControl.setTitle(configurer.getTitle());
        windowControl.setIconImage(configurer.getImage());
        windowControl.setJMenuBar(createMenuBarControl());
        windowControl.getContentPane().setLayout(new BorderLayout());
        windowControl.getContentPane().add(createToolBarControl(), BorderLayout.NORTH);
        windowControl.getContentPane().add(createWindowContentPane());
        windowControl.getContentPane().add(createStatusBarControl(), BorderLayout.SOUTH);
    }

    protected void prepareWindowForView(JFrame windowControl, ApplicationWindowConfigurer configurer) {
        windowControl.pack();
        windowControl.setSize(configurer.getInitialSize());
        WindowUtils.centerOnScreen(windowControl);
    }

    @Override
    public void addPageListener(PageListener listener) {
        this.pageListeners.add(listener);
    }

    @Override
    public void removePageListener(PageListener listener) {
        this.pageListeners.remove(listener);
    }

     protected JMenuBar createMenuBarControl() {
        JMenuBar menuBar = menuBarCommandGroup.createMenuBar();
        menuBarCommandGroup.setVisible( getWindowConfigurer().getShowMenuBar() );
        return menuBar;
    }

    protected JComponent createToolBarControl() {
        JComponent toolBar = toolBarCommandGroup.createToolBar();
        toolBarCommandGroup.setVisible( getWindowConfigurer().getShowToolBar() );
        return toolBar;
    }

    protected JComponent createStatusBarControl() {
        JComponent statusBarControl = statusBar.getControl();
        statusBarControl.setVisible( getWindowConfigurer().getShowStatusBar() );
        return statusBarControl;
    }

    /**
     * Implementors create the component that contains the contents of this window.
     *
     * @return the content pane
     */
    protected abstract JComponent createWindowContentPane();

    public Iterator getSharedCommands() {
        return commandManager.getSharedCommands();
    }

    @Override
    public ApplicationWindowCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public StatusBar getStatusBar() {
        return statusBar;
    }
}
