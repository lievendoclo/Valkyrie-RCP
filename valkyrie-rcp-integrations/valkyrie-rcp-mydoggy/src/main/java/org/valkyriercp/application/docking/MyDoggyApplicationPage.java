package org.valkyriercp.application.docking;

import com.google.common.collect.Maps;
import org.noos.xing.mydoggy.*;
import org.noos.xing.mydoggy.event.ContentManagerUIEvent;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.noos.xing.mydoggy.plaf.ui.cmp.ExtendedTableLayout;
import org.noos.xing.mydoggy.plaf.ui.content.MyDoggyMultiSplitContentManagerUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.PageLayoutBuilder;
import org.valkyriercp.application.support.AbstractApplicationPage;
import org.valkyriercp.application.support.MultiViewPageDescriptor;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * This page represents one page for the spring rich client with
 * the mydoggy framework. In pure Swing apps one would use a JFrame instead.
 *
 * @author Peter Karich, peat_hal at users dot sourceforge dot net
 */
public class MyDoggyApplicationPage extends AbstractApplicationPage implements PageLayoutBuilder {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private JPanel rootComponent;
    private MyDoggyToolWindowManager toolWindowManager;
    private ContentManager contentManager;
    private MultiSplitContentManagerUI contentManagerUI;
    private Map<String, Map.Entry<Content, PageComponent>> contentAndPageComponentById;

    MyDoggyApplicationPage(ApplicationWindow window, PageDescriptor pageDescriptor) {
        super(window, pageDescriptor);
        contentAndPageComponentById = Maps.newHashMap();
        toolWindowManager = new MyDoggyToolWindowManager();
        contentManager = toolWindowManager.getContentManager();
        contentManagerUI = new MyDoggyMultiSplitContentManagerUI();
        contentManager.setContentManagerUI(contentManagerUI);

        assert SwingUtilities.isEventDispatchThread();

        contentManagerUI.setTabPlacement(TabbedContentManagerUI.TabPlacement.TOP);
        contentManagerUI.setShowAlwaysTab(true);
        contentManagerUI.setCloseable(true);
        contentManagerUI.setDetachable(true);
        contentManagerUI.setMinimizable(true);

        contentManagerUI.addContentManagerUIListener(new MyDoggyContentListener());
    }

    @Override
    protected void doAddPageComponent(PageComponent pageComponent) {
        Assert.notNull(pageComponent);

        // If we have at least one content, then dock into that one!
        MultiSplitConstraint constraint;
        if (contentAndPageComponentById.values().iterator().hasNext()) {
            constraint = new MultiSplitConstraint(contentAndPageComponentById.values().iterator().next().getKey());
        } else {
            constraint = new MultiSplitConstraint(AggregationPosition.DEFAULT);
        }

        // Create and register a new content to the mydoggy layout manager
        Content c = contentManager.addContent(
                pageComponent.getId(),
                pageComponent.getDisplayName(), // tabName
                pageComponent.getIcon(),
                pageComponent.getControl(),
                pageComponent.getDisplayName(), // toolTip
                constraint);

        Assert.notNull(c);
        Assert.isTrue(c.getId().equals(pageComponent.getId()));
        contentAndPageComponentById.put(pageComponent.getId(),
                new DoggyEntry<Content, PageComponent>(c, pageComponent));

        // trigger the createControl method of the PageComponent, so if a
        // PageComponentListener is added
        // in the createControl method, the componentOpened event is received.
        pageComponent.getControl();
    }

    @Override
    protected void doRemovePageComponent(PageComponent pageComponent) {
        Map.Entry<Content, PageComponent> e = contentAndPageComponentById.remove(pageComponent.getId());
        Assert.notNull(e);
        boolean ret = contentManager.removeContent(e.getKey());
        Assert.isTrue(ret);
    }

    @Override
    protected boolean giveFocusTo(PageComponent pageComponent) {
        return pageComponent.getControl().requestFocusInWindow();
    }

    @Override
    public void setActiveComponent(PageComponent pageComponent) {
        if (pageComponent != null) {
            // really necessary?
            Content c = getContent(pageComponent.getId());
            if (c != null) {
                c.ensureVisible();
            }
            setActive(pageComponent.getId());
        }
        super.setActiveComponent(pageComponent);
    }

    @Override
    protected JComponent createControl() {
        // At startup create the main component and set the mydoggy specific
        // layout manager.
        Assert.isNull(rootComponent, "Should not being initialized twice.");
        rootComponent = new JPanel();
        rootComponent.setLayout(new ExtendedTableLayout(new double[][]{{0, -1, 0}, {0, -1, 0}}));
        rootComponent.add(toolWindowManager, "1,1,");

        return rootComponent;
    }

    @Override
    public void addView(String viewDescriptorId) {
        // called from pageDescriptor.buildInitialLayout (then pageBuilder.addView)
        showView(viewDescriptorId);
    }

    /* Now some helper methods and classes */
    private Content getContent(String id) {
        return contentManager.getContent(id);
    }

    private PageComponent getPageComponent(String id) {
        Map.Entry<Content, PageComponent> entry =
                contentAndPageComponentById.get(id);
        if (entry == null) {
            return null;
        }

        return entry.getValue();
    }

    void buildInitialLayout() {
        // add all the specified views (List of views)
        getPageDescriptor().buildInitialLayout(this);
    }

    /**
     * This method displays the component with specified key
     * @return true if it was successful.
     */
    private boolean setActive(final String id) {
        Assert.isTrue(SwingUtilities.isEventDispatchThread());

        Content c = getContent(id);
        if (c != null) {
            c.setSelected(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean close() {
        try {
            saveLayout();
        } catch (IOException ex) {
            logger.warn("IO Error while saving layout!", ex);
        }
        return super.close();
    }

    boolean loadLayout() throws IOException {
        File file = getLayoutFile();
        if (file != null && file.canRead()) {
            Assert.isTrue(SwingUtilities.isEventDispatchThread());
            Assert.isTrue(rootComponent.isVisible());

            FileInputStream inputStream = new FileInputStream(file);

            // load layout, but add the requested content before (via callback)!
            toolWindowManager.getPersistenceDelegate().merge(
                    inputStream, PersistenceDelegate.MergePolicy.UNION,
                    new PersistenceDelegateCallback() {

                        @Override
                        public ToolWindow toolwindowNotFound(ToolWindowManager toolWindowManager, String toolWindowId, PersistenceNode node) {
                            //logger.info("'toolwindow not found' Not supported yet.");
                            return null;
                        }

                        @Override
                        public Content contentNotFound(ToolWindowManager toolWindowManager, String contentId, PersistenceNode node) {
                            addView(contentId);
                            return contentAndPageComponentById.get(contentId).getKey();
                        }

                        @Override
                        public String validate(PersistenceNode node, String attribute, String attributeValue, Object attributeDefaultValue) {
                            return attributeValue;
                        }

                    });
            inputStream.close();
            logger.info("Loaded " + contentManager.getContentCount() + " view(s).");
            return true;
        }
        return false;
    }

    void saveLayout() throws IOException {
        File file = getLayoutFile();
        if (file != null) {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return;
                }
            }
            if (file.canWrite()) {
                FileOutputStream output = new FileOutputStream(file);
                toolWindowManager.getPersistenceDelegate().save(output);

                output.close();
                //logger.info("Successfully saved layout.");
                logger.info("Saved " + contentManager.getContentCount() + " view(s).");
            }
        }
    }

    private File getLayoutFile() throws IOException {
        PageDescriptor pageDescriptor = getPageDescriptor();

        if (pageDescriptor instanceof MyDoggyPageDescriptor) {
            return ((MyDoggyPageDescriptor) pageDescriptor).getLayoutFile().getFile();
        } else if(pageDescriptor instanceof MultiViewPageDescriptor) {
            return null;
        } else {
            throw new IOException("Wrong PageDescriptorType:" + pageDescriptor.getClass());
        }
    }

    /**
     *  SpringRC should be informed of a MyDoggy closing event.
     */
    private class MyDoggyContentListener implements ContentManagerUIListener {

        @Override
        public boolean contentUIRemoving(ContentManagerUIEvent cmEvent) {
            Content content = cmEvent.getContentUI().getContent();
            Assert.notNull(content);
            PageComponent pc = getPageComponent(content.getId());
            Assert.notNull(pc);
            close(pc);

            // let mydoggy remove the page ! (don't no for sure if this is the reason of the NPE in mydoggy if we return close(pc))
            return false;
        }

        @Override
        public void contentUIDetached(ContentManagerUIEvent cmEvent) {
        }
    }

    private static class DoggyEntry<K, V> implements Map.Entry<K, V> {

        private K key;
        private V innerValue;

        DoggyEntry(K key, V value) {
            this.key = key;
            this.innerValue = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return innerValue;
        }

        @Override
        public V setValue(V value) {
            V old = this.innerValue;
            this.innerValue = value;
            return old;
        }
    }
}