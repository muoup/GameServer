package com.Game.Entity.Enemy;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Entity;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;
import com.sun.tools.javac.Main;

public class Enemy extends Entity {

    public int id = 0;

    public Player playerTarget = null;

    public Vector2 spawnPosition;

    protected Vector2 moveTo;
    protected Vector2 movement;

    public boolean enabled = true;
    public boolean target = false;
    public boolean passive = false;

    public float respawnTimer = 0;

    public float timer = 0;
    public float timer2 = 0;

    public float maxTarget = 0;
    public float targetTimer = 0;

    public float maxHealth = 0;
    public float health = 25000f;

    public float maxRadius = 0;
    protected float speed = 0;

    private boolean useBounds = false;
    protected Vector2 b1;
    protected Vector2 b2;

    public ImageIdentifier image;

    public String name = getClass() + " NAME NEEDS TO BE CHANGED";
    public boolean temp = false;

    public Enemy(World world, int x, int y) {
        super(world, new Vector2(x, y));
        spawnPosition = new Vector2(x, y);
    }

    public static void createTemporary(Enemy enemy) {
        enemy.temp = true;
    }

    public void setMaxHealth(float amount) {
        this.maxHealth = amount;
        this.health = amount;
    }

    public void setImage(String path) {
        image = ImageIdentifier.singleImage("/Entities/Enemies/" + path);
    }

    public void setScale(int x, int y) {
        image.setScale(x, y);
    }

    public void updateEnemy() {
        update();

        if (!enabled) {
            if (timer > respawnTimer) {
                enabled = true;
                target = false;
                targetTimer = 0;
                health = maxHealth;
                position = spawnPosition.clone();
                timer = 0;
                setMoveTo();
                onRespawn();
            }

            return;
        }

        determineActive();

        if (target)
            AI();

        if (!target || (target && passive)) {
            if (health < maxHealth && !target)
                health += maxHealth / 10 * Server.dTime();
            if (health >= maxHealth && !target)
                health = maxHealth;
            passiveAI();
        }
    }

    public void update() {

    }

    public void passiveAI() {

    }

    public void onRespawn() {

    }

    public boolean withinBounds() {
        if (b1 == null || b2 == null) {
            System.err.println(getClass() + " does not contain a definition for its boundaries!");
            return false;
        }

        return position.compareTo(b1) != -1 && position.compareTo(b2) != 1;
    }

    public void moveToPlayer(Player player) {
        position.add(Vector2.magnitudeDirection(position, player.getPosition()).scale(speed));
    }

    public void loseTarget() {

    }

    public void determineActive() {
        targetTimer -= Server.dTime();

        if (targetTimer <= 0 && target) {
            target = false;
            loseTarget();
        }
    }

    public void damage(float amount) {
        target();
        health -= amount;
        playerTarget.playSound("enemy_hit.wav");

        if (health <= 0) {
            enabled = false;
            playerTarget.playSound("default_death.wav");
            handleDrops();
            if (temp)
                world.enemies.remove(this);
        }
    }

    public void setBounds(float x, float y, float x2, float y2) {
        b1 = new Vector2(x, y);
        b2 = new Vector2(x2, y2);
        useBounds = true;
    }

    public void moveToAI() {
        if (moveTo == null) {
            setMoveTo();
            return;
        }

        if (b1.greaterThan(moveTo) || moveTo.greaterThan(b2))
            setMoveTo();

        if (Vector2.distance(position, moveTo) < 32) {
            setMoveTo();
        } else {
            position.add(movement.scaleClone(speed));
        }
    }

    public void setMoveTo() {
        if (!useBounds)
            moveTo = spawnPosition.addClone(DeltaMath.range(-maxRadius, maxRadius), DeltaMath.range(-maxRadius, maxRadius));
        else
            moveTo = new Vector2(DeltaMath.range(b1.x, b2.x),
                    DeltaMath.range(b1.y, b2.y));

        movement = Vector2.magnitudeDirection(position, moveTo);

    }

    public void target() {
        target = true;
        targetTimer = maxTarget;
    }

    public float range() {
        return Vector2.distance(position, playerTarget.getPosition());
    }

    public void AI() {

    }

    public void handleDrops() {

    }
}
