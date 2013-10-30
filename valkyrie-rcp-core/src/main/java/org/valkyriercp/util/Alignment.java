package org.valkyriercp.util;

import javax.swing.*;

public enum Alignment {
    LEFT(SwingConstants.LEFT, "left"),
    RIGHT(SwingConstants.RIGHT, "right")

    ;
    private Integer code;
    private String label;

    Alignment(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public Integer getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
