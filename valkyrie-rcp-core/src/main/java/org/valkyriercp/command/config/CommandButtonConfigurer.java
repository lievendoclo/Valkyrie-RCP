package org.valkyriercp.command.config;

import org.valkyriercp.command.support.AbstractCommand;

import javax.swing.*;

/**
 * A configurer for Swing buttons that have an associated command.
 *
 * @author Keith Donald
 */
public interface CommandButtonConfigurer {

    /**
     * Configures the given button and optional command object with the properties of the given
     * descriptor.
     *
     * @param button The button to be configured. Must not be null.
     * @param command The command to be configured. May be null.
     * @param faceDescriptor The object describing the visual properties of the command button.
     * Must not be null.
     *
     * @throws IllegalArgumentException if {@code button} or {@code faceDescriptor} are null.
     */
    void configure(AbstractButton button, AbstractCommand command, CommandFaceDescriptor faceDescriptor);
}
