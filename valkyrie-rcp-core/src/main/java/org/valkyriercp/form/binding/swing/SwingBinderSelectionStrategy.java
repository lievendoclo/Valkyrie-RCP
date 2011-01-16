package org.valkyriercp.form.binding.swing;

import org.springframework.core.enums.LabeledEnum;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.support.AbstractBinderSelectionStrategy;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oliver Hutchison
 */
public class SwingBinderSelectionStrategy extends AbstractBinderSelectionStrategy
{

    private Map<String, Binder> idBoundBinders = new HashMap<String, Binder>();

    /*
    * Default constructor.
    */
    public SwingBinderSelectionStrategy()
    {
        super(JTextField.class);
    }

    /**
     * Set a map with predefined binders
     *
     * @param binders
     */
    public void setIdBoundBinders(Map binders)
    {
        this.idBoundBinders = binders;
    }

    /**
     * Register additional map of id bound binders
     *
     * @param binders
     */
    public void registerIdBoundBinders(Map<String, Binder> binders)
    {
        idBoundBinders.putAll(binders);
    }

    /**
     * Try to find a binder with a specified id. If no binder is found, try
     * to locate it into the application context, check whether it's a binder and
     * add it to the id bound binder map.
     *
     * @param id Id of the binder
     * @return Binder or <code>null</code> if not found.
     */
    public Binder getIdBoundBinder(String id)
    {
        Binder binder = idBoundBinders.get(id);
        if (binder == null) //  try to locate the binder bean
        {
            Object binderBean = getApplicationContext().getBean(id);
            if (binderBean instanceof Binder)
            {
                if (binderBean != null)
                {
                    idBoundBinders.put(id, (Binder) binderBean);
                    binder = (Binder) binderBean;
                }
            }
            else
            {
                throw new IllegalArgumentException("Bean '" + id + "' was found, but was not a binder");
            }
        }
        return binder;
    }

    /**
     * Select a binder based on a control type
     *
     * @param controlType Type of control
     * @return  The binder for that control
     */
    public Binder selectBinder(Class controlType)
    {
        return findBinderByControlType(controlType);
    }
}