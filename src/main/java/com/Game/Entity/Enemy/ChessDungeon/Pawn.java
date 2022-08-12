package com.Game.Entity.Enemy.ChessDungeon;

import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.ChessProjectile;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class Pawn extends Enemy {
    private boolean white;
    private final float projSpeed = 450f;

    public Pawn(World world, int x, int y, boolean white) {
        super(world, x, y);

        this.name = "Pawn";
        this.respawnTime = 7500;
        this.loseTargetTime = 5000;
        this.loseFocusDistance = 2000;
        this.speed = 200f;
        this.idleAI = AIType.none;
        this.targetAI = AIType::checkPoint;
        this.white = white;
        this.classLinked = true;

        setMaxHealth(90);
        setImage("ChessDungeon/" + (white ? "" : "black_") + "pawn.png", 96,96);
        setCheckpoints(spawnPosition, spawnPosition.addClone(0, white ? 96 : -96));

        addProjTimer(500, () -> new ChessProjectile(this, white ? playerTarget.getPosition() : predict(projSpeed), projSpeed, 150));
    }

    public void onTargetLost() {
        setMoveTo(spawnPosition);
    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.addItem(ItemList.gold, 500, 0.75);
        table.addItem(ItemList.stone, 5, 0.5, true);
        world.createGroundItem(position, table.determineOutput());
    }
}
