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
package org.valkyriercp.binding.form.support;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FieldFace;
import org.valkyriercp.core.support.AbstractPropertyChangePublisher;
import org.valkyriercp.core.support.LabelInfo;

import javax.swing.*;
import java.awt.*;

/**
 * A default implementation of FieldFace
 *
 * @author Oliver Hutchison
 */
public class DefaultFieldFace extends AbstractPropertyChangePublisher implements FieldFace {

    private final String displayName;

    private final String caption;

    private final String description;

    private final LabelInfo labelInfo;

    private final Icon icon;

    /**
     * Constructs a new DefaultFieldFace with the provided values.
     */
    public DefaultFieldFace(String displayName, String caption, String description, String encodedLabel, Icon icon) {
        this(displayName, caption, description, LabelInfo.valueOf(encodedLabel), icon);
    }

    public DefaultFieldFace(String displayName, String caption, String description, LabelInfo labelInfo, Icon icon) {
        this.displayName = displayName;
        this.caption = caption;
        this.description = description;
        this.labelInfo = labelInfo;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public LabelInfo getLabelInfo() {
        return labelInfo;
    }

    public Image getImage() {
        if (getIcon() instanceof ImageIcon) {
            return ((ImageIcon)getIcon()).getImage();
        }
        else {
            return null;
        }
    }

    public Icon getIcon() {
        return icon;
    }

    public void configure(JLabel label) {
        Assert.notNull(label, "The JLabel to configure is required");
        labelInfo.configureLabel(label);
        label.setIcon(icon);
    }

    public void configure(AbstractButton button) {
        Assert.notNull(button, "The AbstractButton to configure is required");
        labelInfo.configureButton(button);
        button.setIcon(icon);
    }
}