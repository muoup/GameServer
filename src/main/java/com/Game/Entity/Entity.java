package com.Game.Entity;

import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class Entity {
    protected World world;
    protected Vector2 position;

    public Entity(World world, Vector2 position) {
        this.world = world;
        this.position = position;
    }

    public int getX() {
        return (int) position.x;
    }

    public int getY() {
        return (int) position.y;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
