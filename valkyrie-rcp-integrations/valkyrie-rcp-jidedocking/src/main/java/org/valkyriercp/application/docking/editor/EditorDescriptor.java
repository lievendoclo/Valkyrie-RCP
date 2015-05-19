package org.valkyriercp.application.docking.editor;

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.Assert;
import org.valkyriercp.application.Editor;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentDescriptor;
import org.valkyriercp.core.support.LabeledObjectSupport;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * Editor descriptor fashioned after the view descriptor, and taking a
 * lot of the implementation details from DefaultViewDescriptor. As the
 * actual interface does not do anything except bring togther a couple
 * of other interfaces I've not made a specific interface.
 *
 * @author Jonny Wray
 */
public class EditorDescriptor extends LabeledObjectSupport implements PageComponentDescriptor,
        BeanNameAware {

    private String id;
    private Class editorClass;
    private Map editorProperties;

    /**
     * The bean name is the default id
     */
    public void setBeanName(String beanName) {
        setId(beanName);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEditorClass(Class editorClass) {
        this.editorClass = editorClass;
    }

    public void setEditorProperties(Map editorProperties) {
        this.editorProperties = editorProperties;
    }

    /**
     * Constructs and returns the editor, as a page component. Note
     * that this will still need the editor object injecting into.
     *
     * @return
     */
    public PageComponent createPageComponent() {
        return createEditor();
    }

    private Editor createEditor() {
        Object o = BeanUtils.instantiateClass(editorClass);
        Assert.isTrue((o instanceof Editor), "Editor class '" +
                editorClass + "' was instantiated but is not an Editor");
        Editor editor = (Editor) o;
        editor.setDescriptor(this);
        ApplicationEventMulticaster multicaster = getApplicationEventMulticaster();
        if (editor instanceof ApplicationListener && multicaster != null) {
            multicaster.addApplicationListener((ApplicationListener) editor);
        }
        if (editorProperties != null) {
            BeanWrapper wrapper = new BeanWrapperImpl(editor);
            wrapper.setPropertyValues(editorProperties);
        }
        if (editor instanceof InitializingBean) {
            try {
                ((InitializingBean) editor).afterPropertiesSet();
            } catch (Exception e) {
                throw new BeanInitializationException("Problem running on " + editor, e);
            }
        }
        return editor;
    }

    public ApplicationEventMulticaster getApplicationEventMulticaster() {
        ApplicationContext ctx = ValkyrieRepository.getInstance().getApplicationConfig().applicationContext();
        final String beanName = AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;
        if (ctx.containsBean(beanName)) {
            return (ApplicationEventMulticaster) ctx.getBean(beanName);
        }
        return null;
    }
}
