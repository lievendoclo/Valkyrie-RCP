package org.valkyriercp.command.config;

import org.valkyriercp.command.support.AbstractCommand;

public interface CommandFaceDescriptorRegistry {
    public CommandFaceDescriptor getFaceDescriptor(AbstractCommand command, String faceDescriptorId);

}
