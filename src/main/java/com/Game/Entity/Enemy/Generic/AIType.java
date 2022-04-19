package com.Game.Entity.Enemy.Generic;

import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.Settings;

public class AIType {
    public static final AIRunnable none = (enemy) -> {};

    // ------------- AI Types -------------------------

    public static void passiveRadiusWalk(Enemy enemy) {
        if (timeForNextMove(enemy)) {
            enemy.setMoveTo(enemy.spawnPosition.addClone(DeltaMath.range(-enemy.moveRadius, enemy.moveRadius)));
        }
    }

    public static void passiveBoundaryWalk(Enemy enemy) {
        if (timeForNextMove(enemy)) {
            enemy.setMoveTo(new Vector2(DeltaMath.range(enemy.b1.x, enemy.b2.x), DeltaMath.range(enemy.b1.y, enemy.b2.y)));
        }
    }

    public static void basicChase(Enemy enemy) {
        if (Vector2.distance(enemy.getPosition(), enemy.playerTarget.getPosition()) <= enemy.followDistance) {
            if (!enemy.movement.isZero())
                enemy.stop();

            return;
        }

        if (timeForNextMove(enemy)) {
            Vector2 dir = Vector2.magnitudeDirection(enemy.getPosition(), enemy.playerTarget.getPosition());
            dir.scale(getLatencyNoOverreach(enemy));

            enemy.setMoveTo(dir.addClone(enemy.getPosition()));
        }
    }

    // ------------- Common Procedures ----------------

    /***
     * If the entity has reached its moveTo point or is not currently moving, it is time for a new moveTo point to
     * be chosen, and thus this method returns true.
     * @param enemy Enemy to be moved
     * @return Should the enemy find a new moveTo point?
     */
    public static boolean timeForNextMove(Enemy enemy) {
        return enemy.movement.isZero() || Vector2.distance(enemy.getPosition(), enemy.moveTo) < enemy.getAvgScale();
    }

    /**
     * To avoid sending one enemy movement packet per tick, the enemy moves towards the player changing its position
     * every so often. However, the enemy should not pick a position that puts it closer to the player than it wants
     * to be.
     * @param enemy Enemy moving
     * @return Max distance to move so that it does not approach to close to the player.
     */
    public static float getLatencyNoOverreach(Enemy enemy) {
        float maxDistance = Vector2.distance(enemy.getPosition(), enemy.playerTarget.getPosition()) - enemy.followDistance + 1;

        return Math.min(Settings.enemyLatency, maxDistance);
    }
}
