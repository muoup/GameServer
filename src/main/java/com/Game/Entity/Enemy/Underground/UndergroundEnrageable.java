package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class UndergroundEnrageable extends Enemy {
    protected boolean enraged = false;

    public UndergroundEnrageable(World world, float x, float y) {
        super(world, x, y);
    }

    public void update() {
        for (Player player : world.players) {
            if (Vector2.distance(player.getPosition(), getPosition()) < getLoseFocusDistance()
                    && player.inventory.itemCount(ItemList.bugRelic) > 0) {
                setTarget(player);
                enraged = true;
                break;
            }
        }
    }

    public void onTargetLost() {
        enraged = false;
    }

    public boolean isEnraged() {
        return enraged;
    }

    public boolean notEnraged() {
        return !enraged;
    }

    public float getLoseFocusDistance() {
        return loseFocusDistance * (enraged ? 2 : 1);
    }
}
