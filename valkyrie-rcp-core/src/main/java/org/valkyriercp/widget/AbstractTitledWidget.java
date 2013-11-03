package org.valkyriercp.widget;

import org.valkyriercp.component.TitlePane;
import org.valkyriercp.core.DefaultMessage;
import org.valkyriercp.core.Message;
import org.valkyriercp.core.Severity;
import org.valkyriercp.util.GuiStandardUtils;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

public abstract class AbstractTitledWidget extends AbstractWidget implements TitledWidget
{

    private Message description;

    private TitlePane titlePane = new TitlePane(1);

    private JComponent component;

    private String id;

    protected AbstractTitledWidget() {
        description = new DefaultMessage(getApplicationConfig().messageResolver().getMessage(
                "titledWidget", "defaultMessage", "description"), Severity.INFO);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setBeanName(String beanName)
    {
        if(id != null)
            setId(beanName);
    }

    public boolean isEnabled()
    {
        return false;
    }

    public void setEnabled(boolean enabled)
    {
    }

    public void setTitle(String title)
    {
        this.titlePane.setTitle(title);
    }

    public void setImage(Image image)
    {
        this.titlePane.setImage(image);
    }

    public void setMessage(Message message)
    {
        if (message != null)
            titlePane.setMessage(message);
        else
            titlePane.setMessage(getDescription());
    }

//    public ValidationResultsReporter newSingleLineResultsReporter(Messagable messagable)
//    {
//        return null;
//    }

    protected Message getDescription()
    {
        return description;
    }

    public void setDescription(String longDescription)
    {
        this.description = new DefaultMessage(longDescription);
        setMessage(this.description);
    }

    public void setCaption(String shortDescription)
    {
        // TODO needed to comply to interface DescriptionConfigurable where will this end up?
    }

    /**
     * Lazy creation of component
     * <p/>
     * {@inheritDoc}
     */
    public final JComponent getComponent()
    {
        if (component == null)
            component = createComponent();

        return component;
    }

    /**
     * @return JComponent with titlePane, widgetContent and border.
     */
    private JComponent createComponent()
    {
        JPanel titlePaneContainer = new JPanel(new BorderLayout());
        titlePaneContainer.add(titlePane.getControl());
        titlePaneContainer.add(new JSeparator(), BorderLayout.SOUTH);

        JPanel pageControl = new JPanel(new BorderLayout());
        pageControl.add(titlePaneContainer, BorderLayout.NORTH);
        JComponent content = createWidgetContent();
        GuiStandardUtils.attachDialogBorder(content);
        pageControl.add(content);

        return pageControl;
    }

    public abstract JComponent createWidgetContent();

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        this.titlePane.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String txt, PropertyChangeListener listener)
    {
        this.titlePane.addPropertyChangeListener(txt, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        this.titlePane.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String txt, PropertyChangeListener listener)
    {
        this.titlePane.removePropertyChangeListener(txt, listener);
    }
}

