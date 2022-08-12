package com.Game.Projectile;


import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class WebSkillShot extends Projectile {
    public WebSkillShot(Entity owner, Vector2 aim, float speed, float damage) {
        super(owner, aim, damage, speed, 2500);
        this.rotate = false;
        setScale(18);
        setImage("web.png");
    }

    public WebSkillShot(Vector2 position, Vector2 aim, Projectile arrow) {
        super(position, aim, arrow);
    }
}
