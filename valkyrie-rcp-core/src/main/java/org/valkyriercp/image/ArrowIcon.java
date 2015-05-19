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
package org.valkyriercp.image;

import org.springframework.util.Assert;

import javax.swing.*;
import java.awt.*;

/**
 * A directional arrow icon; the direction can either be UP or DOWN.
 *
 * @author Keith Donald
 * @see ArrowIcon.Direction
 */
public class ArrowIcon implements Icon {

    /**
     * Type-safe enum for the ArrowIcon's directional capability.
     * <p>
     * Currently only up and down are supported since this is primarily designed
     * to be a table column sort indicator.
     *
     * @author Keith Donald
     */
    public static class Direction {
        public static final Direction DOWN = new Direction(0);

        public static final Direction UP = new Direction(1);

        private int value;

        private Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public String toString() {
            return String.valueOf(value);
        }
    }

    private Direction direction;

    private int size;

    private Color color;

    /**
     * Creates a ArrowIcon in the specified direction with the default size and
     * color.
     *
     * @param direction
     *            The icon direction.
     */
    public ArrowIcon(Direction direction) {
        setDirection(direction);
        setSize(4);
        setColor(SystemColor.controlDkShadow);
    }

    /**
     * Creates a ArrowIcon pointing in the specified direction with the
     * specified size and color.
     *
     * @param direction
     *            the direction the arrow should point.
     * @param size
     *            the size of the arrow in pixels (4 is a good one)
     * @param color
     *            the color of the arrow (consider using UIDefaults for current
     *            L&F)
     */
    public ArrowIcon(Direction direction, int size, Color color) {
        setDirection(direction);
        setSize(size);
        setColor(color);
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        for (int i = 0; i < size; i++) {
            if (direction == Direction.UP) {
                g.drawLine(x + (size - (i + 1)), y + i, x + (size + i), y + i);
            }
            else {
                g.drawLine(x + i, y + i, x + (size * 2 - (i + 1)), y + i);
            }
        }
    }

    public int getIconWidth() {
        return size * 2;
    }

    public int getIconHeight() {
        return size;
    }

    /**
     * @param size
     */
    private void setSize(int size) {
        this.size = size;
    }

    /**
     * @param color
     */
    private void setColor(Color color) {
        Assert.notNull(color);
        this.color = color;
    }

    /**
     * @param direction
     */
    private void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("[ArrowIcon").append(" direction=").append(direction).append(" size=")
                .append(size).append(" color=").append(color);
        return buf.toString();
    }
}

