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
package org.valkyriercp.util;

import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Sizes;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Utility functions that help enforce a standard look and feel in accordance
 * with the Java Look and Feel Design Guidelines.
 *
 * @author Keith Donald
 */
public class GuiStandardUtils {

    private GuiStandardUtils() {
    }

    public static JComponent attachBorder(JComponent c, Border border) {
        c.setBorder(border);
        return c;
    }

    public static JComponent attachBorder(JComponent c) {
        return attachDialogBorder(c);
    }

    public static JComponent attachDialogBorder(JComponent c) {
        if (c instanceof JTabbedPane) {
            c.setBorder(Borders.TABBED_DIALOG_BORDER);
        }
        else {
            c.setBorder(Borders.DIALOG_BORDER);
        }
        return c;
    }

    public static Border getStandardDialogBorder() {
        return Borders.DIALOG_BORDER;
    }

    public static Border createEvenlySpacedBorder(int spacePx) {
        return createEvenlySpacedBorder(Sizes.pixel(spacePx));
    }

    public static Border createEvenlySpacedBorder(ConstantSize space) {
        return Borders.createEmptyBorder(space, space, space, space);
    }

    public static Border createLeftAndRightBorder(int spacePx) {
        return createLeftAndRightBorder(Sizes.pixel(spacePx));
    }

    public static Border createLeftAndRightBorder(ConstantSize space) {
        return Borders.createEmptyBorder(Sizes.ZERO, space, Sizes.ZERO, space);
    }

    public static Border createTopAndBottomBorder(int spacePx) {
        return createTopAndBottomBorder(Sizes.pixel(spacePx));
    }

    public static Border createTopAndBottomBorder(ConstantSize space) {
        return Borders.createEmptyBorder(space, Sizes.ZERO, space, Sizes.ZERO);
    }

    /**
     * Return text which conforms to the Look and Feel Design Guidelines for the
     * title of a dialog : the application name, a colon, then the name of the
     * specific dialog.
     *
     * @param dialogName
     *            the short name of the dialog.
     */
    public static String createDialogTitle(String appName, String dialogName) {
        if (appName != null) {
            StringBuffer buf = new StringBuffer(appName);
            buf.append(": ");
            buf.append(dialogName);
            return buf.toString();
        }

        return dialogName;
    }

    /**
     * Make a horizontal row of buttons of equal size, whch are equally spaced,
     * and aligned on the right.
     *
     * <P>
     * The returned component has border spacing only on the top (of the size
     * recommended by the Look and Feel Design Guidelines). All other spacing
     * must be applied elsewhere ; usually, this will only mean that the
     * dialog's top-level panel should use {@link #buildStandardBorder}.
     *
     * @param buttons
     *            contains <code>JButton</code> objects.
     * @return A row displaying the buttons horizontally.
     */
    public static JComponent createCommandButtonRow(JButton[] buttons) {
        return ButtonBarFactory.buildRightAlignedBar(buttons);
    }

    /**
     * Make a vertical row of buttons of equal size, whch are equally spaced,
     * and aligned on the right.
     *
     * <P>
     * The returned component has border spacing only on the left (of the size
     * recommended by the Look and Feel Design Guidelines). All other spacing
     * must be applied elsewhere ; usually, this will only mean that the
     * dialog's top-level panel should use {@link #buildStandardBorder}.
     *
     * @param buttons
     *            contains <code>JButton</code> objects.
     * @return A column displaying the buttons vertically.
     */
    public static JComponent createCommandButtonColumn(JButton[] buttons) {
        ButtonStackBuilder builder = new ButtonStackBuilder();

        for (int i = 0; i < buttons.length; i++) {
            if (i > 0) {
                builder.addRelatedGap();
            }
            builder.addGridded(buttons[i]);
        }
        return builder.getPanel();
    }

    /**
     * Sets the items in <code>aComponents</code> to the same size.
     *
     * Sets each component's preferred and maximum sizes. The actual size is
     * determined by the layout manager, which adjusts for locale-specific
     * strings and customized fonts. (See this <a
     * href="http://java.sun.com/products/jlf/ed2/samcode/prefere.html">Sun doc
     * </a> for more information.)
     *
     * @param components
     *            contains <code>JComponent</code> objects.
     */
    public static void equalizeSizes(JComponent[] components) {
        Dimension targetSize = new Dimension(0, 0);
        for (int i = 0; i < components.length; i++) {
            JComponent comp = components[i];
            Dimension compSize = comp.getPreferredSize();
            double width = Math.max(targetSize.getWidth(), compSize.getWidth());
            double height = Math.max(targetSize.getHeight(), compSize.getHeight());
            targetSize.setSize(width, height);
        }
        setSizes(components, targetSize);
    }

    private static void setSizes(JComponent[] components, final Dimension dimension) {
        for (int i = 0; i < components.length; i++) {
            JComponent comp = components[i];
            // shouldn't have to clone these (hopefully awt does it for us)
            comp.setPreferredSize(dimension);
            comp.setMaximumSize(dimension);
        }
    }

    public static JTextArea createStandardTextArea(int rows, int columns) {
        JTextArea area = createStandardTextArea("");
        area.setRows(rows);
        area.setColumns(columns);
        return area;
    }

    /**
     * An alternative to multi-line labels, for the presentation of several
     * lines of text, and for which the line breaks are determined solely by the
     * control.
     *
     * @param text
     *            text that does not contain newline characters or html.
     * @return <code>JTextArea</code> which is not editable, has improved
     *         spacing over the supplied default (placing
     *         {@link UIConstants#ONE_SPACE}on the left and right), and which
     *         wraps lines on word boundarie.
     */
    public static JTextArea createStandardTextArea(String text) {
        JTextArea result = new JTextArea(text);
        return configureStandardTextArea(result);
    }

    public static JTextArea configureStandardTextArea(JTextArea textArea) {
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(0, UIConstants.ONE_SPACE, 0, UIConstants.ONE_SPACE));
        return textArea;
    }

    /**
     * An alternative to multi-line labels, for the presentation of several
     * lines of text, and for which line breaks are determined solely by
     * <code>aText</code>, and not by the control.
     *
     * @param text
     *            the text to be placed in the text area.
     * @return <code>JTextArea</code> which is not editable and has improved
     *         spacing over the supplied default (placing
     *         {@link UIConstants#ONE_SPACE}on the left and right).
     */
    public static JTextArea createStandardTextAreaHardNewLines(String text) {
        JTextArea result = new JTextArea(text);
        result.setEditable(false);
        result.setMargin(new Insets(0, UIConstants.ONE_SPACE, 0, UIConstants.ONE_SPACE));
        return result;
    }

    /**
     * If aLabel has text which is longer than MAX_LABEL_LENGTH, then truncate
     * the label text and place an ellipsis at the end; the original text is
     * placed in a tooltip.
     *
     * This is particularly useful for displaying file names, whose length can
     * vary widely between deployments.
     *
     * @param label
     *            The label to truncate if length() > MAX_LABEL_LENGTH.
     */
    public static void truncateLabelIfLong(JLabel label) {
        String originalText = label.getText();
        if (originalText.length() > UIConstants.MAX_LABEL_LENGTH) {
            label.setToolTipText(originalText);
            String truncatedText = originalText.substring(0, UIConstants.MAX_LABEL_LENGTH) + "...";
            label.setText(truncatedText);
        }
    }

    /**
     * This will allow selection and copy to work but still retain the label
     * look
     */
    public static JTextArea textAreaAsLabel(JTextArea textArea) {
        //  Turn on word wrap
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        // Perform the other changes to complete the look
        textComponentAsLabel(textArea);
        return textArea;
    }

    /**
     * This will allow selection and copy to work but still retain the label
     * look
     */
    public static JTextComponent textComponentAsLabel(JTextComponent textcomponent) {
        //  Make the text component non editable
        textcomponent.setEditable(false);
        // Make the text area look like a label
        textcomponent.setBackground((Color)UIManager.get("Label.background"));
        textcomponent.setForeground((Color)UIManager.get("Label.foreground"));
        textcomponent.setBorder(null);
        return textcomponent;
    }

    /**
     * Useful debug function to place a colored, line border around a component
     * for layout management debugging.
     *
     * @param c
     *            the component
     * @param color
     *            the border color
     */
    public static void createDebugBorder(JComponent c, Color color) {
        if (color == null) {
            color = Color.BLACK;
        }
        c.setBorder(BorderFactory.createLineBorder(color));
    }

}
