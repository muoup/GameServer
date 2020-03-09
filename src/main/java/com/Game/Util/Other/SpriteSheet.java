package com.Game.Util.Other;

import com.Game.Main.Main;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    public static final SpriteSheet bowSheet = new SpriteSheet("Items/bow_sheet.png", 48, 48);
    public static final SpriteSheet meleeSheet = new SpriteSheet("Items/melee_sheet.png", 48, 48);
    public static final SpriteSheet armorSheet = new SpriteSheet("Items/armor_sheet.png", 48, 48);
    public static final SpriteSheet oreSheet = new SpriteSheet("Items/ore_sheet.png", 48, 48);
    public static final SpriteSheet craftingSheet = new SpriteSheet("Items/crafting_sheet.png", 48, 48);
    public static final SpriteSheet questItemSheet = new SpriteSheet("Items/quest_sheet.png", 48, 48);
    public static final SpriteSheet woodSheet = new SpriteSheet("Items/wood_sheet.png", 48, 48);
    public static final SpriteSheet playerSheet = new SpriteSheet("player_spritesheet.png", 7, 19);


    BufferedImage image;
    int width, height;
    public int columns, rows;

    /**
     * @param path Path of Image
     * @param sW   Width of each sprite cell
     * @param sH   Height of each sprite cell
     */
    public SpriteSheet(String path, int sW, int sH) {
        this.image = Main.getImage(path);
        this.width = sW;
        this.height = sH;
        this.columns = image.getWidth() / sW;
        this.rows = image.getHeight() / sH;
    }

    public BufferedImage getCell(int x, int y) {
        return image.getSubimage(x * width, y * height, width, height);
    }
}