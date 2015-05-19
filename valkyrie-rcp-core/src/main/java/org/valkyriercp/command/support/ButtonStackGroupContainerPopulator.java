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
package org.valkyriercp.command.support;

import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Size;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a buttonstack: a panel with buttons that are vertically positioned.
 *
 * @see ButtonBarGroupContainerPopulator
 * @see com.jgoodies.forms.builder.ButtonStackBuilder
 *
 * @author jh
 */
public class ButtonStackGroupContainerPopulator extends SimpleGroupContainerPopulator
{
    private RowSpec rowSpec;

    private ButtonStackBuilder builder;

    private List buttons = new ArrayList();

    /**
     * Constructor.
     */
    public ButtonStackGroupContainerPopulator() {
        super(new JPanel());
        builder = new ButtonStackBuilder((JPanel)getContainer());
    }

    /**
     * Define the minimum buttonsize of the buttonStack. This will actually
     * replace the rowSpec with a new one.
     *
     * @param minimumSize
     * @see #setRowSpec(RowSpec)
     */
    public void setMinimumButtonSize(Size minimumSize)
    {
        this.rowSpec = new RowSpec(minimumSize);
    }

    /**
     * This allows to completely customize the rowspec.
     *
     * @param rowSpec
     */
    public void setRowSpec(RowSpec rowSpec)
    {
        this.rowSpec = rowSpec;
    }

    /**
     * Set a custom columnSpec for the buttonstack.
     *
     * @param columnSpec
     */
    public void setColumnSpec(ColumnSpec columnSpec)
    {
        if (columnSpec != null)
            builder.getLayout().setColumnSpec(1, columnSpec);
    }

    /**
     * @return the created ButtonStack panel
     */
    public JPanel getButtonStack() {
        return builder.getPanel();
    }

    /**
     * @see SimpleGroupContainerPopulator#add(java.awt.Component)
     */
    public void add(Component c) {
        buttons.add(c);
    }

    /**
     * @see SimpleGroupContainerPopulator#addSeparator()
     */
    public void addSeparator() {
        buttons.add(CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE);
    }

    /**
     * @see SimpleGroupContainerPopulator#onPopulated()
     */
    public void onPopulated() {
        int length = buttons.size();
        for (int i = 0; i < length; i++) {
            Object o = buttons.get(i);
            if (o instanceof String && o == CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE) {
                builder.addUnrelatedGap();
            }
            else if (o instanceof AbstractButton) {
                AbstractButton button = (AbstractButton)o;
                if (this.rowSpec != null) {
                    addCustomGridded(button);
                }
                else {
                    builder.addGridded(button);
                }
                if (i < buttons.size() - 1) {
                    builder.addRelatedGap();
                }
            }
        }
        builder.addGlue();
    }

    /**
     * Handle the custom RowSpec.
     *
     * @param button
     */
    private void addCustomGridded(AbstractButton button) {
        builder.getLayout().appendRow(this.rowSpec);
        builder.getLayout().addGroupedRow(builder.getRow());
        button.putClientProperty("jgoodies.isNarrow", Boolean.TRUE);
        builder.add(button);
        builder.nextRow();
    }

}

