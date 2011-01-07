package org.valkyriercp.command;

import org.valkyriercp.command.support.AbstractCommand;

public interface CommandConfigurer {
    /**
     * Configures the given command.
     *
     * @param command The command to be configured. Must not be null.
     * @return The configured command.
     *
     * @throws IllegalArgumentException if {@code command} is null.
     */
    public AbstractCommand configure(AbstractCommand command);
}
