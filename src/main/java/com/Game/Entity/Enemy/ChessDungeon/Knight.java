package com.Game.Entity.Enemy.ChessDungeon;

import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.ChessProjectile;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class Knight extends Enemy {
    public Knight(World world, int x, int y, boolean white) {
        super(world, x, y);

        this.name = "Knight";
        this.respawnTime = 10000;
        this.loseTargetTime = 7000;
        this.loseFocusDistance = 2000;
        this.speed = 250f;
        this.idleAI = AIType.none;
        this.targetAI = AIType::checkPoint;
        this.classLinked = true;

        setMaxHealth(400);
        setImage("ChessDungeon/" + (white ? "" : "black_") + "knight.png", 96,96);

        int multiplier = white ? 1 : -1;

        setRelativeCheckpoints(spawnPosition,
                new Vector2(96 * multiplier, 0),
                new Vector2(0, 192 * multiplier),
                new Vector2(-96 * multiplier, -192 * multiplier),
                new Vector2(0, 192 * multiplier),
                new Vector2(96 * multiplier, 0));

        addProjTimer(750, this::fire);
    }

    public void fire() {
        if (DeltaMath.randBool())
            new ChessProjectile(this, getPosition().addClone(0, 1), 300, 300)
                    .multiShot(22.5, 16, (i) -> i % 4 != 0);
        else
            new ChessProjectile(this, getPosition().addClone(1, 1), 300, 300)
                    .multiShot(22.5, 16, (i) -> i % 4 != 0);
    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.addItem(ItemList.gold, 500, 0.75);
        table.addItem(ItemList.stone, 5, 0.5, true);
        world.createGroundItem(position, table.determineOutput());
    }
}
