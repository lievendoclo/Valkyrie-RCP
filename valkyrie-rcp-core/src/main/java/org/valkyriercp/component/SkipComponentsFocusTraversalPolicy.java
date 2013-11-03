package org.valkyriercp.component;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.List;

public class SkipComponentsFocusTraversalPolicy extends LayoutFocusTraversalPolicy
{
    List<JComponent> componentsToSkip;

    public static final SkipComponentsFocusTraversalPolicy skipJTextComponentTraversalPolicy = new SkipComponentsFocusTraversalPolicy();

    public SkipComponentsFocusTraversalPolicy()
    {}

    public SkipComponentsFocusTraversalPolicy(List<JComponent> componentsToSkip)
    {
        this.componentsToSkip = componentsToSkip;
    }

    @Override
    protected boolean accept(Component aComponent)
    {
        if( !super.accept(aComponent))
            return false;

        if (aComponent instanceof JTextComponent && ((JTextComponent)aComponent).isEditable() == false)
            return false;

        if (componentsToSkip != null)
        {
            for (JComponent component : componentsToSkip)
            {
                if (component == aComponent || component.isAncestorOf(aComponent))
                    return false;
            }
        }
        return true;
    }
}