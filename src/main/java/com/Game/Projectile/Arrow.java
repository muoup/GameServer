package com.Game.Projectile;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Entity;
import com.Game.Entity.Player.Player;
import com.Game.Util.Math.Vector2;

public class Arrow extends Projectile {
    public Arrow(Entity owner, Vector2 aim, float damage, float speed, long duration) {
        super(owner, aim, damage, speed, duration);
        this.rotate = true;
        this.attackStyle = 1;

        setScale(24);
        setImage("arrow.png");
    }

    public void update() {
//        for (Player player : owner.getWorld().players) {
//            Server.send(player, "pp" + position);
//        }
    }

    /*
        This should only be called from multiShotEnemy()
     */
//    public Arrow(Vector2 position, Vector2 aim, Projectile arrow) {
//        super(position, aim, arrow);
//    }
}
