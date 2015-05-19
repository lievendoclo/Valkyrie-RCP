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