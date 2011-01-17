package org.valkyriercp.command.support;

import org.valkyriercp.command.config.CommandButtonConfigurer;
import org.valkyriercp.factory.ButtonFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SplitPaneExpansionToggleCommand extends ActionCommand
{
    private DefaultButtonModel model;

    public SplitPaneExpansionToggleCommand(String commandId, JSplitPane splitPane, boolean switchedAway)
    {
        super(commandId);
        this.model = new SplitPaneExpansionButtonModel(splitPane, switchedAway);
        applicationConfig.commandConfigurer().configure(this);
    }


    protected void doExecuteCommand()
    {
        //toggle
        this.model.setSelected(!this.model.isSelected());

    }

    public void doHide()
    {
        this.model.setSelected(false);
    }

    public void doShow()
    {
        this.model.setSelected(true);
    }

    public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
                                       CommandButtonConfigurer configurer)
    {
        AbstractButton button = buttonFactory.createToggleButton();
        attach(button, faceDescriptorId, configurer);
        return button;
    }

    public void attach(AbstractButton button, String faceDescriptorId,
                       CommandButtonConfigurer configurer)
    {
        super.attach(button, faceDescriptorId, configurer);
        button.setModel(this.model);
    }

    public static class SplitPaneExpansionButtonModel extends DefaultButtonModel
    {

        private final JSplitPane splitPane;

        private enum SWITCH_STATE
        {
            NO_ACTION, SWITCHED_AWAY, SWITCHED_AWAY_AND_MOVED
        }

        ;

        private SWITCH_STATE state = SWITCH_STATE.NO_ACTION;

        private transient PropertyChangeListener listener = new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                fireStateChanged();

                if (state == SWITCH_STATE.SWITCHED_AWAY)
                    state = SWITCH_STATE.SWITCHED_AWAY_AND_MOVED;
                else if (state == SWITCH_STATE.SWITCHED_AWAY_AND_MOVED)
                    state = SWITCH_STATE.NO_ACTION;
            }
        };

        public SplitPaneExpansionButtonModel(JSplitPane mySplitPane, boolean switchedAway)
        {
            super();
            this.splitPane = mySplitPane;

            this.splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
                    listener);

            this.splitPane.addComponentListener(new ComponentAdapter()
            {
                @Override
                public void componentResized(ComponentEvent e)
                {
                    if (state == SWITCH_STATE.SWITCHED_AWAY || state == SWITCH_STATE.SWITCHED_AWAY_AND_MOVED)
                        hidePanel();
                }

            });

            if (switchedAway)
                this.state = SWITCH_STATE.SWITCHED_AWAY;
        }

        public boolean isSelected()
        {
            return isShown();
        }

        public void setSelected(boolean makeVisible)
        {
            super.setSelected(makeVisible);
            if (makeVisible)
            {
                splitPane.resetToPreferredSizes();
                state = SWITCH_STATE.NO_ACTION;
            }
            else
            {
                hidePanel();
            }
        }

        private void hidePanel()
        {
            splitPane.setDividerLocation(getHideRightComponentDividerLocation());
            state = SWITCH_STATE.SWITCHED_AWAY;
        }

        private boolean isShown()
        {
            return splitPane.getDividerLocation() < (getHideRightComponentDividerLocation());
        }

        private int getHideRightComponentDividerLocation()
        {
            Dimension size = splitPane.getSize();
            int max = getRelevantDimensionpart(size);
            Insets insets = splitPane.getInsets();
            int relevantInset = getRelevantInsetpart(insets); // we need to correct with the border size
            int dividerSize = splitPane.getDividerSize();// we need to correct with the size of the divider!
            return max - (relevantInset + dividerSize);
        }

        private int getRelevantInsetpart(Insets insets)
        {
            int orientation = splitPane.getOrientation();
            int result = (orientation == JSplitPane.VERTICAL_SPLIT) ? // for vertical
                    (insets.bottom) : //take the height
                    (insets.right); //  else the width
            return result;
        }

        private int getRelevantDimensionpart(Dimension size)
        {
            int orientation = splitPane.getOrientation();
            int result = (orientation == JSplitPane.VERTICAL_SPLIT) ? // for vertical
                    (int) Math.ceil(size.getHeight()) : //take the height
                    (int) Math.ceil(size.getWidth()); //  else the width
            return result;
        }
    }

}


