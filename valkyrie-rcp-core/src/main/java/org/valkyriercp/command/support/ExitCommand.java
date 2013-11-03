package org.valkyriercp.command.support;

import org.valkyriercp.application.Application;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * An action command that causes the application to exit.
 *
 * @author Keith Donald
 */
public class ExitCommand extends ApplicationWindowAwareCommand {

    /** The identifier of this command. */
    public static final String ID = "exitCommand";

    /**
     * Creates a new {@code ExitCommand} with an id of {@value #ID}.
     */
    public ExitCommand() {
        super(ID);
    }

    /**
     * Closes the single {@link Application} instance.
     *
     * @see Application#close()
     */
    protected void doExecuteCommand() {
        ValkyrieRepository.getInstance().getApplicationConfig().application().close();
    }

}
