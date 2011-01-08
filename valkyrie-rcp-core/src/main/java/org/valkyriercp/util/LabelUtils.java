package org.valkyriercp.util;

public class LabelUtils {

    public static String addElipses(String text) {
        if (text != null) {
            text += UIConstants.ELLIPSIS;
        }
        return text;
    }

    public static String removeElipses(String text) {
        if (text != null) {
            int i = text.indexOf(UIConstants.ELLIPSIS);
            if (i != -1) {
                text = text.substring(0, i);
            }
        }
        return text;
    }

    public static String htmlBlock(String labelText) {
        return "<html>" + labelText + "</html>";
    }

}