package org.valkyriercp.command.config;

/**
 * An object that can be labeled; where a label consists of text, mnemonic, and
 * potentially an accelerator key.
 *
 * @author Keith Donald
 */
public interface CommandLabelConfigurable {
    public void setLabelInfo(CommandButtonLabelInfo labelInfo);
}