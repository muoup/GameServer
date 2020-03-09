package com.Game.PseudoData;

/*
    Image Code List
    emp -> empty image
    nr -> normal image
    ss;x,y -> sprite sheet image
 */

/**
 * Used to send image data over a packet. All images are stored in the client, so they are editable, but the server
 * makes sure to tell the client which image(s) to use.
 */
public class ImageIdentifier {
    private String token;

    private ImageIdentifier(String token) {
        this.token = token;
    }

    public static ImageIdentifier emptyImage() {
        return new ImageIdentifier("emp");
    }

    public static ImageIdentifier singleImage(String root) {
        return new ImageIdentifier("nr--" + root);
    }

    public static ImageIdentifier spriteSheet(String root, int x, int y) {
        return new ImageIdentifier(String.format("ss;%s,%s--%s", x, y, root));
    }

    public String getToken() {
        return token;
    }
}
