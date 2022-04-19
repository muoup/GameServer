package com.Game.Entity.Enemy.Generic;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Entity;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.Timer;
import com.Game.WorldManagement.World;

import java.util.ArrayList;
import java.util.Vector;

public class Enemy extends Entity {

    // Static or mostly unchanging variables.
    public int id = 0;
    public String name = getClass() + " NAME NEEDS TO BE CHANGED";
    private int randomToken = 0;
    private float maxHealth = 0;
    public Vector2 spawnPosition;
    public float speed = 0;
    public ImageIdentifier image;
    public boolean temporary = false;
    public long targetLostTime = 60000;

    public AIRunnable idleAI = AIType.none;
    public AIRunnable targetAI = AIType.none;
    public ArrayList<Timer> projectileTimers;
    public ArrayList<Timer> passiveTimers;

    // Optional Static Variables
    public float moveRadius = 0;
    public float maxFollowDistance = 512f;
    public boolean useBounds = false;
    public Vector2 b1;
    public Vector2 b2;
    public long respawnTime = 0;
    public boolean passive = false;

    // References (Things Essentially Read-Only)
    public Player playerTarget = null;
    public Vector2 moveTo;
    public Vector2 movement;

    // Dynamic Variables
    private boolean enabled = true;
    private float health = 25000f;

    // Times
    public long currentRespawn = 0;
    public long previousDamage = 0;

    // AI Variables
    public float followDistance = 250f;

    public Enemy(World world, int x, int y) {
        super(world, new Vector2(x, y));
        spawnPosition = new Vector2(x, y);
        projectileTimers = new ArrayList<>();
        passiveTimers = new ArrayList<>();
        movement = Vector2.zero();
        moveTo = position.clone();

        setRandomToken();
    }

    private void setRandomToken() {
        while (tokenNotUnique() || randomToken == 0) {
            randomToken = (int) DeltaMath.range(0, 10000);
        }
    }

    private boolean tokenNotUnique() {
        for (Enemy e : world.enemies) {
            if (e.randomToken == randomToken)
                return true;
        }

        return false;
    }

    public void setMaxHealth(float amount) {
        this.maxHealth = amount;
        this.health = amount;

        informMaxHealth();
        informHealth();
    }

    public void setImage(String path, int sx, int sy) {
        image = ImageIdentifier.singleImage("/Entities/Enemies/" + path);
        image.setScale(sx, sy);

        world.newEnemy(this);
    }

    public void updateEnemy() {
        update();

        if (!enabled && System.currentTimeMillis() > currentRespawn) {
            setEnabled(true);
            setHealth(maxHealth);
            setPosition(spawnPosition);
            setMovement(Vector2.zero());

            informHealth();
            informEnabled();
            onRespawn();
        }

        if (!enabled)
            return;

        position.add(movement.scaleClone(speed * (float) Server.dTime()));

        if (playerTarget != null) {
            if (Vector2.distance(playerTarget.getPosition(), spawnPosition) > maxFollowDistance || !target()) {
                previousDamage = 0;
                loseTarget();
            }
        }

        if (target()) {
            targetAI.run(this);
            projectileTimers.forEach(Timer::update);
        } else {
            idleAI.run(this);
            passiveTimers.forEach(Timer::update);
            regenHealth();
        }
    }

    public void setTarget(Player target) {
        previousDamage = System.currentTimeMillis();
        playerTarget = target;
    }

    public boolean target() {
        return System.currentTimeMillis() < previousDamage + targetLostTime;
    }

    public void update() {

    }

    public void regenHealth() {
        float initHealth = health;

        if (health < maxHealth && !target())
            health += maxHealth / 10 * Server.dTime();
        if (health >= maxHealth && !target())
            health = maxHealth;

        if (initHealth != health) {
            informHealth();
        }
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

    public void loseTarget() {
        playerTarget = null;

        if (temporary) {
            die();
            return;
        }

        onTargetLost();
    }

    public void onTargetLost() {

    }

    public void damage(Player player, float amount) {
        if (playerTarget == null || DeltaMath.range(0, 10) < 1.5f)
            playerTarget = player;

        onHit(player);
        changeHealth(-amount);

        player.playSound("enemy_hit.wav");

        previousDamage = System.currentTimeMillis();

        if (health <= 0)
            player.playSound("default_death.wav");
    }

    public void setBounds(float x, float y, float x2, float y2) {
        b1 = new Vector2(x, y);
        b2 = new Vector2(x2, y2);
        useBounds = true;
    }

    public void setMoveTo(Vector2 moveTo) {
        this.moveTo = moveTo;

        movement = Vector2.magnitudeDirection(position, moveTo);

        informMovement();
    }

    public void onHit(Player player) {

    }

    public float range() {
        return Vector2.distance(position, playerTarget.getPosition());
    }

    public void handleDrops() {

    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        informEnabled();
    }

    public void setHealth(float health) {
        this.health = health;

        if (health <= 0) {
            playerTarget = null;
            setEnabled(false);
            handleDrops();
            currentRespawn = System.currentTimeMillis() + respawnTime;

            if (temporary)
                world.enemies.remove(this);

            return;
        }

        informHealth();
    }

    public void changeHealth(float deltaHealth) {
        setHealth(health + deltaHealth);
    }

    public void setPosition(Vector2 position) {
        this.position = position;

        informPosition();
    }

    public void setPosition(float x, float y) {
        setPosition(new Vector2(x, y));
    }

    public void setMovement(Vector2 movement) {
        this.movement = movement;

        informMovement();
    }

    public void die() {
        setHealth(Integer.MIN_VALUE);
    }

    public void stop() {
        this.movement = Vector2.zero();
        this.moveTo = position.clone();

        informMovement();
    }

    public void informMovement() {
        if (moveTo == null)
            informPosition();

        Client.enemyUpdate(world, randomToken, "MoveTo", position + " " + moveTo + " " + speed);
    }

    public void informMovement(Player player) {
        Client.enemyUpdateToPlayer(player, randomToken, "MoveTo", position + " " + moveTo + " " + speed);
    }

    public void informHealth() {
        Client.enemyUpdate(world, randomToken, "Health", health);
    }

    public void informHealth(Player player) {
        Client.enemyUpdateToPlayer(player, randomToken, "Health", health);
    }

    public void informMaxHealth() {
        Client.enemyUpdate(world, randomToken, "MaxHealth", maxHealth);
    }

    public void informEnabled() {
        Client.enemyUpdate(world, randomToken, "Enabled", enabled);
    }

    public void informPosition() {
        Client.enemyUpdate(world, randomToken, "Position", position);
    }

    public String packetInfo() {
        return position + ";" + image + ";" + randomToken + ";" + health + " " + maxHealth + ";" + name;
    }

    public float getAvgScale() {
        Vector2 scale = image.getScale();

        return (scale.x + scale.y) / 2;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getRandomToken() {
        return randomToken;
    }

    public void addProjTimer(long wait, Runnable run) {
        projectileTimers.add(new Timer(wait, run));
    }

    public void addProjTimer(long wait, Runnable run, long offset) {
        projectileTimers.add(new Timer(wait, run, offset));
    }

    public void addPassiveTimer(long wait, Runnable run) {
        passiveTimers.add(new Timer(wait, run));
    }
}
