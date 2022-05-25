package com.Game.Util.Math;

public class Rect2 {
    private Vector2 pos;
    private Vector2 size;

    public Rect2(float x, float y, float width, float height) {
        this.pos = new Vector2(x, y);
        this.size = new Vector2(width, height);
    }

    public Rect2(Vector2 pos, Vector2 size) {
        this.pos = pos.clone();
        this.size = size.clone();
    }

    public Rect2(Vector2 addClone, float stackedSize, float stringHeight) {
        this.pos = addClone.clone();
        this.size = new Vector2(stackedSize, stringHeight);
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getEnd() {
        return pos.addClone(size);
    }

    public boolean contains(Vector2 point) {
        return point.getX() >= pos.getX() && point.getX() <= pos.getX() + size.getX() &&
                point.getY() >= pos.getY() && point.getY() <= pos.getY() + size.getY();
    }

    public Vector2 randomPoint() {
            return new Vector2(pos.getX() + (float) Math.random() * size.getX(), pos.getY() + (float) Math.random() * size.getY());
    }
}
