package org.valkyriercp.component;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Idea originally from http://jroller.com/page/gfx?entry=better_shadow
 *
 * @author Romain Guy
 * @author Peter De Bruycker
 */
public class ShadowBorderFrame extends JFrame {
    private BufferedImage backgroundImage;
    private static final int SHADOW_WIDTH = 14;

    public void paint(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, this);
        super.paint(g);
    }

    public void show() {
        createShadowBorder();
        super.show();
        setSize(getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH);
    }

    private void createShadowBorder() {
        backgroundImage =
            new BufferedImage(getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) backgroundImage.getGraphics();

        try {
            Robot robot = new Robot(getGraphicsConfiguration().getDevice());
            BufferedImage capture =
                robot.createScreenCapture(
                    new Rectangle(getX(), getY(), getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH));
            g2.drawImage(capture, null, 0, 0);
        }
        catch (AWTException e) {
            e.printStackTrace();
        }

        BufferedImage shadow =
            new BufferedImage(getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = shadow.getGraphics();
        graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
        graphics.fillRoundRect(6, 6, getWidth(), getHeight(), 12, 12);

        g2.drawImage(shadow, getBlurOp(7), 0, 0);
    }


    private ConvolveOp getBlurOp(int size) {
        float[] data = new float[size * size];
        float value = 1 / (float) (size * size);
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
        return new ConvolveOp(new Kernel(size, size, data));
    }
}

