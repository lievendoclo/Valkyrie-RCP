package org.valkyriercp.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.application.View;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.util.ValkyrieRepository;

import java.util.Collections;
import java.util.List;

/**
 * Default behavior implementation of AbstractWidget
 */
public abstract class AbstractWidget implements Widget
{
    protected boolean showing = false;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private View view;

    /**
     * {@inheritDoc}
     */
    public void onAboutToShow()
    {
        showing = true;
    }

    /**
     * {@inheritDoc}
     */
    public void onAboutToHide()
    {
        showing = false;
    }

    public boolean isShowing()
    {
        return showing;
    }

    /**
     * {@inheritDoc}
     *
     * Default: Widget can be closed.
     */
    public boolean canClose()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public List<? extends AbstractCommand> getCommands()
    {
        return Collections.emptyList();
    }

    public View getView() {
        if(view == null)
            view = new WidgetView(this);
        return view;
    }

    @Override
    public void registerLocalCommandExecutors(PageComponentContext context) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected ApplicationConfig getApplicationConfig() {
        return ValkyrieRepository.getInstance().getApplicationConfig();
    }
}
