package com.Game.Projectile;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Entity;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.Vector2;

public class HomingProjectile extends Projectile {

    private Entity target;

    public HomingProjectile(Entity owner, Entity target, float damage, float speed) {
        super(owner, Vector2.zero(), damage, speed, 100000);

        this.target = target;
    }

    public void updateProjectile() {
        if (target.getWorld() != world) {
            destroy();
            return;
        }

        Vector2 targetPos = target.getPosition();
        position.add(Vector2.magnitudeDirection(position, targetPos).scale(speed * (float) Server.dTime()));

        speed += speed * 0.25 * Server.dTime();

        Player playerTarget = (Player) target;

        if (friendly()) {
            for (Enemy e : world.enemies) {
                if (!e.isEnabled())
                    continue;

                if (Vector2.distance(e.getPosition(), position) < scale.x + e.image.getScale().x / 2) {
                    onHit(e, damage);
                    destroy();
                }
            }
        } else {
            if (Vector2.distance(playerTarget.getPosition(), position) < scale.x + 24) {
                playerTarget.damage(damage);
                destroy();
            }
        }
    }

    public void setImage(String root) {
        image = ImageIdentifier.singleImage("Projectiles/" + root);

        if (scale == null) {
            scale = Vector2.identity(8);
        }

        image.setScale(scale);

        if (target instanceof Player) {
            Client.hprojectileSpawn(world, position, image.getToken(), speed, ((Player) target).getUsername(), randomToken);
        } else {
            Client.hprojectileSpawn(world, position, image.getToken(), speed, ((Enemy) target).getRandomToken(), randomToken);
        }

        world.projectiles.add(this);
    }
}
