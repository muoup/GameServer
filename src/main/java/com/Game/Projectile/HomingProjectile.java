package com.Game.Projectile;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Enemy;
import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class HomingProjectile extends Projectile {

    private Entity target;

    public HomingProjectile(Entity owner, Entity target, float damage, float speed) {
        super(owner, Vector2.zero(), damage, speed, 10000);
        this.target = target;
    }

    @Override
    public void projectileUpdate() {
        if (target.getWorld() != world) {
            destroy();
            return;
        }

        Vector2 targetPos = target.getPosition();
        position.add(Vector2.magnitudeDirection(position, targetPos).scale(speed));

        speed += Server.dTime();

        if (friendly) {
            for (Enemy e : world.enemies) {
                if (!e.enabled)
                    continue;

                if (Vector2.distance(e.getPosition(), position) < scale.x) {
                    e.damage(damage);
                    onHit(e, damage);
                    destroy();
                }
            }
        } else {
            if (Vector2.distance(player().getPosition(), position) < scale.x) {
                player().damage(damage);
                destroy();
            }
        }
    }
}
