package org.valkyriercp.command.support;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Size;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ButtonBarGroupContainerPopulator extends SimpleGroupContainerPopulator {
    private ColumnSpec columnSpec;

    private ButtonBarBuilder builder;

    private List buttons = new ArrayList();

    public ButtonBarGroupContainerPopulator() {
        super(new JPanel());
        builder = new ButtonBarBuilder((JPanel)getContainer());
    }

    public void setMinimumButtonSize(Size minimumSize) {
        this.columnSpec = new ColumnSpec(minimumSize);
    }

    public void setColumnSpec(final ColumnSpec columnSpec)
    {
        this.columnSpec = columnSpec;
    }

    public void setRowSpec(final RowSpec rowSpec)
    {
        if (rowSpec != null)
            builder.getLayout().setRowSpec(1, rowSpec);
    }

    public JPanel getButtonBar() {
        return builder.getPanel();
    }

    public void add(Component c) {
        buttons.add(c);
    }

    public void addSeparator() {
        buttons.add(CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE);
    }

    public void onPopulated() {
        builder.addGlue();
        int length = buttons.size();
        for (int i = 0; i < length; i++) {
            Object o = buttons.get(i);
            if (o instanceof String && o == CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE) {
                builder.addUnrelatedGap();
            }
            else if (o instanceof AbstractButton) {
                AbstractButton button = (AbstractButton)o;
                if (this.columnSpec != null) {
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
    }

    private void addCustomGridded(AbstractButton button) {
        builder.getLayout().appendColumn(this.columnSpec);
        builder.getLayout().addGroupedColumn(builder.getColumn());
        button.putClientProperty("jgoodies.isNarrow", Boolean.TRUE);
        builder.add(button);
        builder.nextColumn();
    }

}
