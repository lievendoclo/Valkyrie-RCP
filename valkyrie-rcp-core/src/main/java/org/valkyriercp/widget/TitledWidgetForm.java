package org.valkyriercp.widget;

import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.form.AbstractForm;
import org.valkyriercp.form.ValidationResultsReporter;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * A decorator to add a {@link org.valkyriercp.component.TitlePane} to a {@link org.valkyriercp.form.Form}. Adds the commit command as a default widget
 * command to show.
 *
 * TODO check all widget functionality
 *
 * @author Jan Hoskens
 *
 */
public class TitledWidgetForm extends AbstractTitledWidget
{

    private AbstractForm form;

    /**
     * Set the inner form that needs decorating.
     */

    public void setForm(AbstractForm form)
    {
        this.form = form;
        if(getId() == null) {
            setId(form.getId());
        }
    }

    /**
     * Returns the form.
     */
    public AbstractForm getForm()
    {
        return form;
    }

    @Override
    public JComponent createWidgetContent()
    {
        newSingleLineResultsReporter(this);
        return getForm().getControl();
    }

    @Override
    public List<? extends AbstractCommand> getCommands()
    {
        return Collections.emptyList();
    }

    public ValidationResultsReporter newSingleLineResultsReporter(Messagable messagable)
    {
        return getForm().newSingleLineResultsReporter(this);
    }

    @Override
    public void onAboutToHide()
    {
        super.onAboutToHide();
        // NOTE in future form should be a widget
        if (form instanceof Widget)
            ((Widget) form).onAboutToHide();
    }

    @Override
    public void onAboutToShow()
    {
        super.onAboutToShow();
        // NOTE in future form should be a widget
        if (form instanceof Widget)
            ((Widget) form).onAboutToShow();
    }
}


