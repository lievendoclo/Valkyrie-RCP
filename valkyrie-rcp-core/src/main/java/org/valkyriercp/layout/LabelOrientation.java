package org.valkyriercp.layout;

import org.springframework.core.enums.ShortCodedLabeledEnum;

import javax.swing.*;

public enum LabelOrientation {

	TOP(SwingConstants.TOP, "Top"),

	BOTTOM(SwingConstants.BOTTOM, "Bottom"),

	LEFT(SwingConstants.LEFT, "Left"),

	RIGHT(SwingConstants.RIGHT, "Right");

    private int code;
    private String label;

    LabelOrientation(int code, String label) {
        this.code = code;
        this.label = label;
    }
}