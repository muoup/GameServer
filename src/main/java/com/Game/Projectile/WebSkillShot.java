package com.Game.Projectile;


import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class WebSkillShot extends Projectile {
    public WebSkillShot(Entity owner, Vector2 aim) {
        super(owner, aim, 12, 2.25f, 2500);
        this.rotate = false;
        setScale(18);
        setImage("web.png");
    }

    public WebSkillShot(Vector2 position, Vector2 aim, Projectile arrow) {
        super(position, aim, arrow);
    }
}
