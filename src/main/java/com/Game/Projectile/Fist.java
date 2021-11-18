package com.Game.Projectile;

import com.Game.Entity.Player.Player;
import com.Game.Skills.Skills;
import com.Game.Util.Math.Vector2;

/**
 * Default Projectile When No Weapon is Equipped
 */
public class Fist extends Projectile {

    public Fist(Player player, Vector2 aim) {
        super(player, aim, 2.5f + player.getLevel(Skills.RANGED) * 0.035f, 2f, 200);
        this.rotate = true;
        this.attackStyle = 1;
        setScale(36);
        setImage("fist.png");
        setCooldown(0.5f);
    }
}
