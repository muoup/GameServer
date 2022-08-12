package com.Game.Entity.Enemy.ChessDungeon;

import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.ChessProjectile;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class Bishop extends Enemy {

    private boolean white;

    public Bishop(World world, float x, float y, boolean white) {
        super(world, x, y);
        this.name = "Bishop";
        this.respawnTime = 10000;
        this.loseTargetTime = 7000;
        this.loseFocusDistance = 2000;
        this.speed = 250f;
        this.idleAI = AIType.none;
        this.targetAI = AIType::checkPoint;
        this.classLinked = true;
        this.white = white;

        setMaxHealth(650);
        setImage("ChessDungeon/" + (white ? "" : "black_") + "bishop.png", 96,96);

        if (white) {
            setRelativeCheckpoints(spawnPosition,
                    new Vector2(-96, 96),
                    new Vector2(96, 96),
                    new Vector2(96 * 2, -96 * 2),
                    new Vector2(-96, -96));
        } else {
            setRelativeCheckpoints(spawnPosition,
                    new Vector2(96, 96),
                    new Vector2(-96, 96),
                    new Vector2(-96 * 2, -96 * 2),
                    new Vector2(96, -96));
        }

        addProjTimer(125, this::spiral);
    }

    public void spiral() {
        float degrees = ((System.currentTimeMillis() - timeTargeted) * (360f / 2000f)) % 360;
        float radians = (float) Math.toRadians(degrees);

        Vector2 target = getPosition().addClone(
                (float) Math.cos(radians) * 96 * (white ? 1 : -1),
                (float) Math.sin(radians) * 96);

        radians += Math.PI;

        Vector2 target2 = getPosition().addClone(
                (float) Math.cos(radians) * 96 * (white ? 1 : -1),
                (float) Math.sin(radians) * 96);

        new ChessProjectile(this, target, 300, 200);
        new ChessProjectile(this, target2, 300, 200);
    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.addItem(ItemList.gold, 500, 0.75);
        table.addItem(ItemList.stone, 5, 0.5, true);
        world.createGroundItem(position, table.determineOutput());
    }
}
