package com.Game.Util.Other;

import com.Game.Main.Main;
import com.Game.World.World;
import com.Util.Math.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Render {
    public static void drawImage(Image image, Vector2 position) {
        Main.graphics.drawImage(image, (int) position.x, (int) position.y, null);
    }

    public static void drawImage(Image image, float x, float y) {
        Main.graphics.drawImage(image, (int) x, (int) y, null);
    }

    public static void drawRectangle(Vector2 position, Vector2 scale) {
        Main.graphics.fillRect((int) position.x, (int) position.y, (int) scale.x, (int) scale.y);
    }

    public static void drawRectangle(float x, float y, float width, float height) {
        Main.graphics.fillRect((int) x, (int) y, (int) width, (int) height);
    }

    public static void drawRectOutline(Vector2 position, Vector2 scale) {
        Main.graphics.drawRect((int) position.x, (int) position.y, (int) scale.x, (int) scale.y);
    }

    public static void drawRectOutline(float x, float y, float width, float height) {
        Main.graphics.drawRect((int) x, (int) y, (int) width, (int) height);
    }

    public static void setColor(Color color) {
        Main.graphics.setColor(color);
    }

    public static void setFont(Font font) {
        Main.graphics.setFont(font);
    }

    public static void drawText(String text, Vector2 position) {
        String[] split = text.split("/n");

        for (int i = 0; i < split.length; i++) {
            String line = split[i];
            Main.graphics.drawString(line, (int) position.x, (int) position.y + Render.getStringHeight() * i);
        }
    }

    public static Font getFont() {
        return Main.graphics.getFont();
    }

    public static void drawText(String text, float x, float y) {
        Main.graphics.drawString(text, (int) x, (int) y);
    }

    public static Vector2 getDimensions(Image image) {
        return new Vector2(image.getWidth(null), image.getHeight(null));
    }

    public static void drawBounds(Vector2 v1, Vector2 v2) {
        Vector2 size = v2.subtractClone(v1);
        Main.graphics.fillRect((int) v1.x, (int) v1.y, (int) size.x, (int) size.y);
    }

    public static boolean onScreen(Vector2 position, Image image) {
        Vector2 offset = World.curWorld.offset;

        return position.compareTo(offset.subtractClone(Render.getDimensions(image))) == 1
                && offset.addClone(Settings.curResolution()).compareTo(position) == 1;
    }

    public static int getStringHeight() {
        return Main.graphics.getFontMetrics().getHeight();
    }

    public static int getStringSpace() {
        FontMetrics metric = Main.graphics.getFontMetrics();
        return metric.getAscent() + metric.getHeight();

    }

    public static int getStringWidth(String string) {
        return Main.graphics.getFontMetrics().stringWidth(string);
    }

    public static float getStringAscent() {
        return Main.graphics.getFontMetrics().getAscent();
    }

    public static void drawBorderedBounds(Vector2 v1, Vector2 v2) {
        Color dcol = Main.graphics.getColor();

        Render.setColor(Color.BLACK);
        drawBounds(v1, v2);
        Render.setColor(dcol);
        drawBounds(v1.addClone(5f), v2.addClone(-5f));
    }

    public static void drawBorderedRect(Vector2 pos, Vector2 size) {
        drawBorderedBounds(pos, pos.addClone(size));
    }

    public static void drawBorderedRect(float px, float py, float sx, float sy) {
        drawBorderedRect(new Vector2(px, py), new Vector2(sx, sy));
    }

    public static BufferedImage getScaledImage(BufferedImage image, Vector2 scale) {
        return getScaledImage(image, scale.x, scale.y);
    }

    public static void drawCroppedText(String text, Vector2 position, Vector2 pos) {
        if (text.isEmpty())
            return;

        Vector2 stringSize = new Vector2(getStringWidth(text), getStringHeight());
        String[] lines = text.split("/n");
        BufferedImage string = new BufferedImage((int) stringSize.x, (int) stringSize.y * lines.length, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) string.getGraphics();
        graphics.setFont(Main.graphics.getFont());
        graphics.setColor(Main.graphics.getColor());
        for (int i = 0; i < lines.length; i++) {
            graphics.drawString(lines[i], 0, stringSize.y * i + Main.graphics.getFontMetrics().getAscent());
        }

        graphics.dispose();

        BufferedImage croppedString = string.getSubimage((int) pos.x, (int) pos.y, string.getWidth() - (int) pos.x, string.getHeight() - (int) pos.y);
        drawImage(croppedString, position.addClone(pos.x, pos.y));
    }

    public static void drawCroppedText(String text, Vector2 position, Vector2 pos, Vector2 size) {
        Vector2 stringSize = new Vector2(getStringWidth(text), getStringHeight());
        String[] lines = text.split("/n");
        BufferedImage string = new BufferedImage((int) stringSize.x, (int) stringSize.y * lines.length, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) string.getGraphics();
        graphics.setFont(Main.graphics.getFont());
        graphics.setColor(Main.graphics.getColor());
        for (int i = 0; i < lines.length; i++) {
            graphics.drawString(lines[i], 0, stringSize.y * i + Main.graphics.getFontMetrics().getAscent());
        }

        graphics.dispose();

        BufferedImage croppedString = string.getSubimage((int) pos.x, (int) pos.y, string.getWidth() - (int) size.x, string.getHeight() - (int) size.y);

        drawImage(croppedString, position.addClone(pos.x, pos.y));
    }

    public static BufferedImage getScaledImage(BufferedImage image, float x, float y) {
        int width = (int) x;
        int height = (int) y;

        BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = returnImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.drawImage(image, 0, 0, width, height, null);

        g2d.dispose();

        return returnImage;
    }

    public static BufferedImage rotateImage(BufferedImage image, double rads) {
        if (image == null)
            return null;

        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = image.getWidth();
        int h = image.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotated;
    }

    public static Vector2 getImageSize(Image image) {
        return new Vector2(image.getWidth(null), image.getHeight(null));
    }

    public static void drawLine(Vector2 v1, Vector2 v2) {
        Main.graphics.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
    }

    public static BufferedImage scaleFactorImage(BufferedImage image, float scaleFactor) {
        return getScaledImage(image, getImageSize(image).scale(scaleFactor));
    }

    public static Vector2 stringDimensions(String text) {
        return new Vector2(getStringWidth(text), getStringHeight() - getStringAscent() / 2);
    }

    public static void drawRectOutlineBounds(Vector2 guiPos, Vector2 guiEnd) {
        drawRectOutline(guiPos, guiEnd.subtractClone(guiPos));
    }

    public static BufferedImage mirrorImageHorizontally(BufferedImage simg) {
        // Get source image dimension
        int width = simg.getWidth();
        int height = simg.getHeight();

        // BufferedImage for mirror image
        BufferedImage mimg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        // Create mirror image pixel by pixel
        for (int y = 0; y < height; y++)
        {
            for (int lx = 0, rx = width - 1; lx < width; lx++, rx--)
            {
                // lx starts from the left side of the image
                // rx starts from the right side of the image
                // lx is used since we are getting pixel from left side
                // rx is used to set from right side
                // get source pixel value
                int p = simg.getRGB(lx, y);

                // set mirror image pixel value
                mimg.setRGB(rx, y, p);
            }
        }

        return mimg;
    }
}