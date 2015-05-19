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
package org.valkyriercp.taskpane;

import org.valkyriercp.command.support.AbstractCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DefaultTaskPaneIconGenerator implements IconGenerator<AbstractCommand>
{
    public static final int ROUND = 0;
    public static final int DIAMOND = 1;
    public static final int SQUARE = 2;
    public static final int OCTAGON = 3;

    private Color iconColor = Color.DARK_GRAY;
    private int iconShape = SQUARE;

    public Color getIconColor()
    {
        return iconColor;
    }

    public void setIconColor(Color iconColor)
    {
        this.iconColor = iconColor;
    }

    public int getIconShape()
    {
        return iconShape;
    }

    public void setIconShape(int iconShape)
    {
        this.iconShape = iconShape;
    }

    public ImageIcon generateIcon(AbstractCommand forObject)
    {
        char textChar = forObject.getText().charAt(0);
        return new ImageIcon(createIcon(getIconShape(), getIconColor(), textChar));
    }

    private static BufferedImage createIcon(int style, Color color, char text)
    {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        Color lighterColor = color.brighter().brighter();
        GradientPaint paint = new GradientPaint(0, 0, lighterColor, 16, 16, color);
        g.setPaint(paint);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Polygon p = new Polygon();
        switch (style)
        {
            case ROUND:
                g.fillOval(0, 0, 16, 16);
                break;
            case SQUARE:
                g.fillRect(0, 0, 16, 16);
                break;
            case DIAMOND:
                p.addPoint(8, 0);
                p.addPoint(16, 8);
                p.addPoint(8, 16);
                p.addPoint(0, 8);
                g.fillPolygon(p);
                break;
            case OCTAGON:
                p.addPoint(0, 5);
                p.addPoint(5, 0);
                p.addPoint(11, 0);
                p.addPoint(16, 5);
                p.addPoint(16, 11);
                p.addPoint(11, 16);
                p.addPoint(5, 16);
                p.addPoint(0, 11);
                g.fillPolygon(p);
                break;
            default:
        }

        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.setColor(Color.white);
        char uppercaseText = Character.toUpperCase(text);
        if (uppercaseText == 'W' || uppercaseText == 'M')
        {
            g.drawString(Character.toString(text), 3f, 11f);
        }
        else
        {
            g.drawString(Character.toString(text), 4.5f, 11f);
        }
        img.flush();
        return img;
    }
}

