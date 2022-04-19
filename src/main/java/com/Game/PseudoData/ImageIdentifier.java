package com.Game.PseudoData;

/*
    Image Code List
    emp -> empty image
    sf -> normal image
    ss;x,y -> sprite sheet image
    an;w,h,amt -> sprite sheet animation
 */

import com.Game.Util.Math.Vector2;

/**
 * Used to send image data over a packet. All images are stored in the client, so they are editable, but the server
 * makes sure to tell the client which image(s) to use.
 */
public class ImageIdentifier {
    private String token;

    private Vector2 scale = null;
    private double rotation = 0;

    private ImageIdentifier(String token) {
        this.token = token;
        this.scale = Vector2.zero();
    }

    public static ImageIdentifier emptyImage() {
        return new ImageIdentifier("em");
    }

    public static ImageIdentifier singleImage(String root) {
        return new ImageIdentifier("sf" + root);
    }

    public static ImageIdentifier subImage(String root, int x, int y, int width, int height) {
        return new ImageIdentifier(String.format("ss%s<->%s,%s>-<%s,%s", root, x, y, width, height));
    }

    public static ImageIdentifier animatedImage(String root, int width, int height, int frames, int fps) {
        return new ImageIdentifier(String.format("an%s<->%s,%s>-<%s,%s", root, width, height, frames, fps));
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    public void setScale(int x, int y) {
        this.scale = new Vector2(x, y);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public String getToken() {
        return token + String.format("<-->%s,%s", (scale.equalTo(Vector2.zero())) ? null : scale, rotation);
    }

    public String toString() { return getToken(); }

    public Vector2 getScale() {
        return scale;
    }
}
