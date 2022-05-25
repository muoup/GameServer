package com.Game.Entity.Enemy.Generic;

import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.Settings;

public class AIType {
    public static final AIRunnable none = (enemy) -> {};

    // ------------- AI Types -------------------------

    /**
     * Staggered movement only a certain distance from the spawn point. Useful for spiders and other creepy-crawlies.
     *
     * Required settings:
     * moveRadius - The maximum movement distance from one point to another
     * maxMoveRadius - The maximum distance from the spawn point that the enemy can move
     *
     * @param enemy
     */
    public static void passiveRadiusCreep(Enemy enemy) {
        if (timeForNextMove(enemy)) {
            Vector2 moveTo;

            do {
                moveTo = enemy.getPosition().addClone(new Vector2(DeltaMath.range(-enemy.moveRadius, enemy.moveRadius),
                        DeltaMath.range(-enemy.moveRadius, enemy.moveRadius)));
            } while (Vector2.distance(enemy.spawnPosition, moveTo) > enemy.maxMoveRadius);

            enemy.setMoveTo(moveTo);
        }
    }

    /**
     * Staggered movement only within bounds. Useful for spiders and other creepy-crawlies.
     *
     * Required settings:
     * moveRadius - The maximum movement distance from one point to another
     * bounds - The bounds that the enemy can move within
     *
     * @param enemy
     */
    public static void passiveBoundaryCreep(Enemy enemy) {
        if (timeForNextMove(enemy)) {
            Vector2 moveTo;

            do {
                moveTo = enemy.getPosition().addClone(new Vector2(DeltaMath.range(-enemy.moveRadius, enemy.moveRadius),
                        DeltaMath.range(-enemy.moveRadius, enemy.moveRadius)));
            } while (!enemy.bounds.contains(moveTo));

            enemy.setMoveTo(moveTo);
        }
    }

    /**
     * Passive movement within a boundary, picks a point within the bounds, moves to the point, and then picks a new one.
     *
     * Required settings:
     * bounds - The bounds that the enemy can move within
     *
     * @param enemy
     */
    public static void passiveBoundaryWalk(Enemy enemy) {
        if (timeForNextMove(enemy)) {
            enemy.setMoveTo(enemy.bounds.randomPoint());
        }
    }

    /**
     * A basic AI that moves towards the player, stopping when it is within a certain distance of the player.
     *
     * Required settings:
     * followDistance - The distance that the enemy will stop moving towards the player
     * scale - The scale of the enemy
     *
     * @param enemy
     */
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
