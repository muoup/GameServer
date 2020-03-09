package com.Game.Util.Other;

import com.Game.Main.Main;
import com.Util.Math.Vector2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimatedSprite {
    public float timer = 0;
    private SpriteSheet spriteSheet;
    private Vector2[] cellList;
    private BufferedImage image;
    private float fps;
    public int row = -1;

    public AnimatedSprite(SpriteSheet sheet, int fps) {
        this.spriteSheet = sheet;
        this.fps = fps;
        calculateCells();
    }

    public AnimatedSprite(int fps, SpriteSheet sheet, int totalCells) {
        this.spriteSheet = sheet;
        this.fps = fps;
        calculateCells(totalCells);
    }

    public AnimatedSprite(SpriteSheet sheet, Vector2[] cellList, int fps) {
        this.fps = fps;
        this.spriteSheet = sheet;
        this.cellList = cellList;
    }

    public AnimatedSprite(SpriteSheet sheet, int fps, int column) {
        this(sheet, fps);
        this.row = column;
    }

    public void update() {
        timer += Main.dTime();
    }

    public void reset() {
        timer = 0;

    }

    public BufferedImage getImage() {
        if (cellList.length == 0)
            return null;

        if (row >= 0)
            return getColumnImage();

        int cellNum = (int) ((timer * fps) % cellList.length);

        Vector2 cell = cellList[cellNum];
        return spriteSheet.getCell((int) cell.x, (int) cell.y);
    }

    public BufferedImage getFrame(int frame) {
        if (cellList.length == 0 || frame >= cellList.length)
            return null;

        Vector2 cell = cellList[frame];
        return spriteSheet.getCell((int) cell.x, (int) cell.y);
    }

    private BufferedImage getColumnImage() {
        int cellNum = (int) ((timer * fps) % spriteSheet.columns);
        return spriteSheet.getCell(cellNum, row);
    }

    public BufferedImage getImage(Vector2 scale) {
        return Render.getScaledImage(getImage(), scale);
    }

    public void calculateCells() {
        ArrayList<Vector2> cells = new ArrayList<>();

        for (int y = 0; y < spriteSheet.rows; y++) {
            for (int x = 0; x < spriteSheet.columns; x++) {
                cells.add(new Vector2(x, y));
            }
        }

        cellList = cells.toArray(new Vector2[cells.size()]);
    }

    public void calculateCells(int totalCells) {
        ArrayList<Vector2> cells = new ArrayList<>();

        loop:
        for (int y = 0; y < spriteSheet.rows; y++) {
            for (int x = 0; x < spriteSheet.columns; x++) {
                if (y * spriteSheet.columns + x > totalCells - 1)
                    break loop;
                cells.add(new Vector2(x, y));
            }
        }

        cellList = cells.toArray(new Vector2[cells.size()]);
    }

    public String toString() {
        return spriteSheet.columns + " x " + spriteSheet.rows;
    }

    public int getFrame() {
        return (int) ((timer * fps) % cellList.length);
    }

    public boolean equivalent(AnimatedSprite animated) {
        return spriteSheet == animated.spriteSheet
               && cellList == animated.cellList;
    }
}
