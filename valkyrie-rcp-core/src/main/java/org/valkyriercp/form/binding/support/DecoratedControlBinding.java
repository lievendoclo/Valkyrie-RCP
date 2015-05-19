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
package org.valkyriercp.form.binding.support;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;

/**
 * Wraps a source {@link Binding} and returns an alternate control in place
 * of the source binding's control.  This allows one to decorate the source
 * binding's control (for example, by wrapping a JList in a JScrollPane).
 * Note that this method will call the source Binding's {@link #getControl()}
 * method whenever its <code>getControl()</code> method is called in order
 * to ensure that any actual initialization/binding done in the source
 * Binding's <code>getControl()</code> method is performed.
 *
 * @author Andy DePue
 */
public class DecoratedControlBinding implements Binding
{
  private final Binding source;
  private final JComponent decoratingComponent;

  public DecoratedControlBinding(final Binding source, final JComponent decoratingComponent)
  {
    this.source = source;
    this.decoratingComponent = decoratingComponent;
  }

  public Binding getSource()
  {
    return this.source;
  }

  public JComponent getDecoratingComponent()
  {
    return this.decoratingComponent;
  }


  //
  // METHODS FROM INTERFACE Binding
  //

  public FormModel getFormModel()
  {
    return getSource().getFormModel();
  }

  public String getProperty()
  {
    return getSource().getProperty();
  }

  public JComponent getControl()
  {
    getSource().getControl();
    return getDecoratingComponent();
  }
}
