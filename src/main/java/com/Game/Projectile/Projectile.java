package com.Game.Projectile;

import com.Game.Entity.Enemy.Enemy;
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

public class Projectile {
    protected Vector2 initPos;
    protected Vector2 position;
    protected Vector2 aim;
    protected Vector2 direction;

    protected boolean rotate;

    protected boolean friendly = false;

    protected Entity owner;
    protected World world;

    protected float speed;
    protected float damage;

    protected long endTime;

    protected Vector2 scale;

    public ImageIdentifier image;

    public int attackStyle;

    public Projectile(Entity owner, Vector2 aim, float damage, float speed, long duration) {
        this.position = owner.getPosition();
        this.aim = aim.clone();
        this.damage = damage;
        this.speed = speed * Settings.projLengthMultiplier;
        this.world = owner.getWorld();
        this.rotate = false;
        this.endTime = System.currentTimeMillis() + duration;
        this.owner = owner;

        initPos = position.clone();

        direction = Vector2.magnitudeDirection(position, aim).scale(speed);

        image = ImageIdentifier.emptyImage();

        // If the bullet is not going to move, there is no point in spawning it in.
        if (!position.equalTo(aim)) {
            setCooldown(0.35f);
        }
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

        // If the bullet is not going to move, there is no point in spawning it in.
        if (!position.equalTo(aim)) {
            setCooldown(0.35f);
        }
    }

    protected Object clone() {
        return null;
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

    public void setCooldown(float timer) {
        if (friendly())
            player().shootTimer = timer;
    }

    public void setAim(Vector2 aim) {
        this.aim = aim;
        direction = Vector2.magnitudeDirection(position, aim).scale(speed);
    }

    public void setImage(String root) {
        image = ImageIdentifier.singleImage("/images/Projectiles/" + root);

        if (scale == null) {
            scale = Vector2.identity(8);
        }

        image.setScale(scale);

        if (rotate) {
            double radians = Math.atan(-(aim.x - position.x) / (aim.y - position.y));

            if (aim.y > position.y)
                radians += Math.PI;

            image.setRotation(radians);
        }
    }

    public void setScale(int scale) {
        this.scale = new Vector2(scale);
        this.position.subtractClone(scale / 2, scale / 2);
    }

    public void projectileUpdate() {
        position.add(direction.scaleClone(speed));

        if (endTime > System.currentTimeMillis())
            destroy();

        if (friendly()) {
            for (int i = 0; i < world.enemies.size(); i++) {
                Enemy e = world.enemies.get(i);
                if (!e.enabled)
                    continue;

                if (Vector2.distance(e.getPosition(), position) < scale.x) {
                    e.damage(damage);
                    onHit(e, damage);
                    destroy();
                }
            }
        } else {
            for (int i = 0; i < world.players.size(); i++) {
                Player e = world.players.get(i);

                if (Vector2.distance(e.getPosition(), position) < scale.x) {
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
    }

    public void render() {
    }

    public void update() {
    }

    protected void destroy() {
        world.projectiles.remove(this);
    }

    public void multiShot(double degrees, float radius, int amount) {
        degrees = Math.toRadians(degrees);

        if (amount % 2 == 1) {
            double theta = Math.atan((aim.x - position.x) / (aim.y - position.y));

            if (aim.y - position.y <= 0) {
                theta += DeltaMath.pi;
            }

            Constructor projectileConstructor;

            Vector2 adjust = position.addClone(radius * Math.sin(theta), radius * Math.cos(theta));

            setAim(adjust);

            try {
                projectileConstructor = getClass().getConstructor(Vector2.class, Vector2.class, Projectile.class);
            } catch (NoSuchMethodException e) {
                System.err.println(getClass() + " does not contain a correct constructor!");
                return;
            }

            for (int i = -amount / 2; i < amount / 2 + 1; i++) {
                if (i == 0)
                    continue;

                /*
                    Point on Circle from Center = (r * sin(θ), r * cos(θ))
                    Where r = radius of circle and θ = degrees
                    NOTE: 0 degrees is located at (0, r)
                 */

                try {
                    Vector2 newAim = position.addClone(radius * Math.sin(theta + i * degrees), radius * Math.cos(theta + i * degrees));
                    projectileConstructor.newInstance(position, newAim, this);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void multiShotEnemy(Player target, double degrees, float radius, int amount) {
        degrees = Math.toRadians(degrees);

        if (amount % 2 == 1) {
            Vector2 aim = target.getPosition();

            double theta = Math.atan((aim.x - position.x) / (aim.y - position.y));

            if (aim.y - position.y <= 0) {
                theta += DeltaMath.pi;
            }

            Vector2 adjust = position.addClone(radius * Math.sin(theta), radius * Math.cos(theta));

            setAim(adjust);

            Constructor projectileConstructor;

            try {
                projectileConstructor = getClass().getConstructor(Vector2.class, Vector2.class, Projectile.class);
            } catch (NoSuchMethodException e) {
                System.err.println(getClass() + " does not contain a correct multi-shot constructor!");
                return;
            }

            for (int i = -amount / 2; i < amount / 2 + 1; i++) {
                if (i == 0)
                    continue;

                /*
                    Point on Circle from Center = (r * sin(θ), r * cos(θ))
                    Where r = radius of circle and θ = degrees
                    NOTE: 0 degrees is located at (0, r)
                 */

                try {
                    Vector2 newAim = position.addClone(radius * Math.sin(theta + i * degrees), radius * Math.cos(theta + i * degrees));
                    projectileConstructor.newInstance(position, newAim, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}