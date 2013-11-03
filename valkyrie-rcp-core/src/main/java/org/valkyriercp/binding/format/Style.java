package org.valkyriercp.binding.format;

import java.text.DateFormat;

public enum Style {
    FULL(DateFormat.FULL, "Full"),
    LONG(DateFormat.LONG, "Long"),
    MEDIUM(DateFormat.MEDIUM, "Medium"),
    SHORT(DateFormat.SHORT, "Short")

    ;
    int code;
   String label;

    Style(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}