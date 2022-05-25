package com.Game.Projectile;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Entity;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Skills.Skills;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.Settings;
import com.Game.WorldManagement.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Projectile {
    protected Vector2 initPos;
    protected Vector2 position;
    protected Vector2 aim;
    protected Vector2 direction;

    protected boolean rotate;

    protected Entity owner;
    protected World world;

    protected float speed;
    protected float damage;

    protected long endTime;

    protected Vector2 scale;

    protected int randomToken;

    public ImageIdentifier image;

    public int attackStyle;

    public Projectile(Entity owner, Vector2 aim, float damage, float speed, long duration) {
        this.position = owner.getPosition().clone();
        this.aim = aim.clone();
        this.damage = damage;
        this.speed = speed;
        this.world = owner.getWorld();
        this.rotate = false;
        this.endTime = System.currentTimeMillis() + duration;
        this.owner = owner;

        initPos = position.clone();

        direction = Vector2.magnitudeDirection(position, aim);

        setRandomToken();
    }

    public Projectile(Vector2 position, Vector2 aim, Projectile arrow) {
        this.position = position.clone();
        this.aim = aim.clone();
        this.damage = arrow.damage;
        this.owner = arrow.owner;
        this.speed = arrow.speed;
        this.rotate = arrow.rotate;
        this.attackStyle = arrow.attackStyle;
        this.endTime = arrow.endTime;
        this.image = arrow.image;
        this.scale = arrow.scale;

        initPos = position.clone();

        direction = Vector2.magnitudeDirection(position, aim).scale(speed);

        setRandomToken();
    }

    private void setRandomToken() {
        while (tokenNotUnique() || randomToken == 0) {
            randomToken = (int) DeltaMath.range(0, 10000);
        }
    }

    private boolean tokenNotUnique() {
        ArrayList<Projectile> projectiles = world.projectiles;
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            if (p.randomToken == randomToken)
                return true;
        }

        return false;
    }

    protected Object clone(Vector2 direction) {
        Projectile clone = new Projectile(owner, aim, damage, speed, endTime - System.currentTimeMillis());
        clone.setDirection(direction);
        clone.setScale((int) scale.x);
        clone.rotate = rotate;
        clone.attackStyle = attackStyle;
        clone.setImage(image);

        return clone;
    }

    public Player player() {
        if (owner instanceof Player) {
            return (Player) owner;
        }

        System.err.println("The owner of this projectile is not a player! Uh oh.");
        return null;
    }

    public boolean friendly() {
        return owner instanceof Player;
    }

    public void setCooldown(long timer) {
        if (friendly())
            player().shootTimer = System.currentTimeMillis() + timer;
    }

    public void setAim(Vector2 aim) {
        this.aim = aim;
        direction = Vector2.magnitudeDirection(position, aim).scale(speed);
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public void setImage(String root) {
        ImageIdentifier image = ImageIdentifier.singleImage("Projectiles/" + root);

        setImage(image);
    }

    public void setImage(ImageIdentifier image) {
        this.image = image;

        if (scale == null) {
            scale = Vector2.identity(8);
        }

        image.setScale(scale);

        if (rotate) {
            // calculate rotation
            float radians = (float) Math.atan2(direction.y, direction.x) + (float) Math.PI / 2;

            image.setRotation(radians);
        }

        Client.projectileSpawn(world, position, this.image.getToken(), direction, speed, friendly(), randomToken);
        world.projectiles.add(this);
    }

    public void setScale(int scale) {
        this.scale = new Vector2(scale);
        this.position.subtractClone(scale / 2, scale / 2);
    }

    public void updateProjectile() {
        Vector2 movement = direction.scaleClone(speed * (float) Server.dTime());

        position.add(movement);

        if (System.currentTimeMillis() > endTime)
            destroy();

        if (friendly()) {
            for (int i = 0; i < world.enemies.size(); i++) {
                Enemy e = world.enemies.get(i);
                if (!e.isEnabled())
                    continue;

                if (Vector2.distance(e.getPosition(), position) < scale.x / 2 + e.image.getScale().x / 2) {
                    e.damage(player(), damage);
                    onHit(e, damage);
                    destroy();
                }
            }
        } else {
            for (int i = 0; i < world.players.size(); i++) {
                Player e = world.players.get(i);

                if (Vector2.distance(e.getPosition(), position) < scale.x + 48) {
                    e.damage(damage);
                    destroy();
                }
            }
        }

        render();
        update();
    }

    protected void onHit(Enemy enemy, float damage) {
        switch (attackStyle) {
            case 1:
                player().addExperience(Skills.RANGED, (int) (damage * Settings.rangedXPMultiplier));
                break;
            case 2:
                player().addExperience(Skills.MELEE, (int) (damage * Settings.meleeXPMultiplier));
                break;
        }

        player().addExperience(Skills.LIFEPOINTS, (int) damage);
    }

    public void render() {
    }

    public void update() {
    }

    protected void destroy() {
        Client.projectileDestroy(world, randomToken);
        world.projectiles.remove(this);
    }

    public void multiShot(double degrees, int amount) {
        double baseAngle = Math.atan2(direction.y, direction.x);
        double deltaAngle = Math.toRadians(degrees);

        for (int i = -amount / 2; i <= amount / 2; i++) {

            if (i == 0)
                continue;

            double angle = baseAngle + deltaAngle * i;
            Vector2 direction = new Vector2((float) Math.cos(angle), (float) Math.sin(angle));

            clone(direction);
        }
    }
}