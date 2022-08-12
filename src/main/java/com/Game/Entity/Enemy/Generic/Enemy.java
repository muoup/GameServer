package com.Game.Entity.Enemy.Generic;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Entity;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemStack;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Rect2;
import com.Game.Util.Math.Util;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.Conditional;
import com.Game.Util.Other.IterationConditional;
import com.Game.Util.Other.Timer;
import com.Game.WorldManagement.World;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.concurrent.locks.Condition;

public class Enemy extends Entity {

    public String name = getClass() + " NAME NEEDS TO BE CHANGED";
    private int randomToken = 0;
    private float maxHealth = 0;
    public Vector2 spawnPosition;
    public float speed = 0;
    public ImageIdentifier image;
    public boolean temporary = false;
    public long loseTargetTime = 60000;

    public AIRunnable idleAI = AIType.none;
    public AIRunnable targetAI = AIType.none;
    public ArrayList<Timer> projectileTimers;
    public ArrayList<Timer> passiveTimers;
    public Dictionary<String, Object> references;

    // Optional Static Variables
    public float maxMoveRadius = 0;
    public float moveRadius = 25f;
    public float loseFocusDistance = 512f;
    public Rect2 bounds;
    public long respawnTime = 0;
    public boolean classLinked = false;

    // References (Things Essentially Read-Only)
    public Player playerTarget = null;
    public Vector2 moveTo;
    public Vector2 movement;

    // Dynamic Variables
    private boolean enabled = true;
    private float health = 25000f;
    protected long timeTargeted = 0;

    // Times
    public long currentRespawn = 0;
    public long previousDamage = 0;

    // AI Variables
    public float followDistance = 250f;
    public Vector2[] checkpoints;

    public Enemy(World world, float x, float y) {
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
        if (!enabled && System.currentTimeMillis() > currentRespawn) {
            startTimers();

            playerTarget = null;
            setEnabled(true);
            setHealth(maxHealth);
            setPosition(spawnPosition);
            setMoveTo(spawnPosition);

            informHealth();
            informEnabled();
            onRespawn();
        }

        if (!enabled)
            return;

        update();

        // Once the enemy has passed its moveTo, the direction it needs to move will be opposite of the direction it is currently moving
        // so stop the enemy and move it to its moveTo
        if (!movement.isZero() && !Vector2.approximatelyEqualAngles(movement, Vector2.magnitudeDirection(position, moveTo))) {
            position = moveTo.clone();
            movement = Vector2.zero();
        }

        position.add(movement.scaleClone(speed * (float) Server.dTime()));

        if (playerTarget != null) {
            if (Vector2.distance(playerTarget.getPosition(), spawnPosition) > getLoseFocusDistance() || !target()) {
                previousDamage = 0;
                loseTarget();
            }
        }

        if (target()) {
            targetAI.run(this);
            for (Timer projectileTimer : projectileTimers) {
                if (target())
                    projectileTimer.update();
            }
        } else if (!temporary) {
            idleAI.run(this);
            passiveTimers.forEach(Timer::update);
            regenHealth();
        }
    }

    public void targetPlayer(Player target) {
        setTarget(target);
        onPlayerTarget();

        if (classLinked) {
            for (int i = 0; i < world.enemies.size(); i++) {
                Enemy enemy = world.enemies.get(i);
                if (enemy.getClass() == getClass() && enemy.classLinked)
                    enemy.setTarget(target);
            }
        }
    }

    public void setTarget(Player target) {
        previousDamage = System.currentTimeMillis();
        playerTarget = target;
    }

    public void onPlayerTarget() {

    }

    public boolean target() {
        return System.currentTimeMillis() < previousDamage + loseTargetTime && playerTarget != null && enabled;
    }

    public void update() {

    }

    public void startTimers() {
        // loop through all timers and set the time to be the specified time from the current time
        projectileTimers.forEach(Timer::setNext);
        passiveTimers.forEach(Timer::setNext);
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


    public final void loseTarget() {
        playerTarget = null;

        if (temporary) {
            kill();
            return;
        }

        targetAI.run(this);
        onTargetLost();
    }

    public void onTargetLost() {
    }

    public void damage(Player player, float amount) {
        if (!target()) {
            if (classLinked)
                for (int i = 0; i < world.enemies.size(); i++) {
                    Enemy enemy = world.enemies.get(i);
                    if (enemy.getClass() == getClass() && enemy.classLinked)
                        enemy.timeTargeted = System.currentTimeMillis();
                }
            else
                timeTargeted = System.currentTimeMillis();
        }

        onHit(player);
        tryTarget(player);
        changeHealth(-amount);

        player.playSound("enemy_hit.wav");

        if (health <= 0)
            player.playSound("default_death.wav");
    }

    public void tryTarget(Player player) {
        if (playerTarget == null || DeltaMath.range(0, 10) < 1.5f)
            targetPlayer(player);
        else
            targetPlayer(playerTarget);
    }

    public void setBounds(float x, float y, float x2, float y2) {
        bounds = new Rect2(x, y, x2 - x, y2 - y);
    }

    public void setCheckpoints(Vector2... checkpoints) {
        this.checkpoints = new Vector2[checkpoints.length];

        for (int i = 0; i < checkpoints.length; i++) {
            this.checkpoints[i] = checkpoints[i].clone();
        }
    }

    public void setRelativeCheckpoints(Vector2 start, Vector2... offset) {
        this.checkpoints = new Vector2[offset.length + 1];
        this.checkpoints[0] = start.clone();

        Vector2 temp = start.clone();
        for (int i = 0; i < offset.length; i++) {
            temp.add(offset[i]);
            this.checkpoints[i + 1] = temp.clone();
        }
    }

    public void reorderCheckpoints() {
        for (int i = 0; i < checkpoints.length; i++) {
            if (spawnPosition.approximately(checkpoints[i])) {
                setCheckpoints(Util.cycleArray(checkpoints, i));
                return;
            }
        }
    }

    public void setMoveTo(Vector2 moveTo) {
        Vector2 moveToClone = moveTo.clone();

        if (bounds != null && !bounds.contains(moveTo)) {
            moveToClone = bounds.clamp(moveTo);
        }

        this.moveTo = moveToClone.clone();

        movement = Vector2.magnitudeDirection(position, moveToClone);

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
            handleDeath();
            return;
        }

        informHealth();
    }

    private void handleDeath() {
        playerTarget = null;
        currentRespawn = System.currentTimeMillis() + respawnTime;
        setEnabled(false);
        onDeath();

        drops:
        if (!temporary) {
            if (classLinked) {
                for (int i = 0; i < world.enemies.size(); i++) {
                    Enemy enemy = world.enemies.get(i);
                    if (enemy.getClass() == getClass() && enemy.isEnabled() && enemy.classLinked) {
                        currentRespawn = Long.MAX_VALUE;
                        break drops;
                    }
                }
            }

            handleDrops();

            for (int i = 0; i < world.enemies.size(); i++) {
                Enemy e = world.enemies.get(i);

                if (e.getClass() == getClass() && e.classLinked) {
                    e.currentRespawn = currentRespawn;
                }
            }
        }

        if (temporary)
            world.enemies.remove(this);
    }

    public void onDeath() {

    }

    public void changeHealth(float deltaHealth) {
        setHealth(health + deltaHealth);
    }

    public void setPosition(Vector2 position) {
        this.position = position.clone();

        informPosition();
    }

    public void setSpawnPosition(Vector2 spawnPosition) {
        this.spawnPosition = spawnPosition;

        setPosition(spawnPosition);
        setMoveTo(spawnPosition);
    }

    public void setPosition(float x, float y) {
        setPosition(new Vector2(x, y));
    }

    public void kill() {
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
        projectileTimers.add(new Timer(wait, false, run));
    }

    public void addProjTimer(long wait, Runnable run, long offset) {
        projectileTimers.add(new Timer(wait, offset, false, run));
    }

    public void addProjTimer(long wait, Conditional condition, Runnable run) {
        projectileTimers.add(new Timer(wait, false, () -> {
            if (condition.isTrue())
                run.run();
        }));
    }

    public void addPassiveTimer(long wait, Runnable run) {
        passiveTimers.add(new Timer(wait, false, run));
    }

    public void announceDrop(ItemStack stack) {

    }

    public <T> void killMinions(ArrayList<T> minions) {
        while (minions.size() > 0) {
            T minion = minions.get(0);
            minions.remove(0);
            ((Enemy) minion).kill();
        }
    }

    public Vector2 predict(float projSpeed) {
        Vector2 predict = Vector2.movementPredictVector(position, playerTarget.getPosition(),
                playerTarget.estimatedVelocity, projSpeed);

        return position.addClone(predict);
    }

    public float getLoseFocusDistance() {
        return loseFocusDistance;
    }
}
