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

public class Rook extends Enemy {
    public Rook(World world, int index, boolean white) {
        super(world, 0, 0);

        this.name = "Rook";
        this.respawnTime = 10000;
        this.loseTargetTime = 7000;
        this.loseFocusDistance = 2000;
        this.speed = 375f;
        this.idleAI = AIType.none;
        this.targetAI = AIType::checkPoint;
        this.classLinked = true;

        setMaxHealth(350);
        setImage("ChessDungeon/" + (white ? "" : "black_") + "rook.png", 96,96);

        setRelativeCheckpoints(new Vector2(1698, 1724), new Vector2(288, 0),
                new Vector2(0, 288), new Vector2(-288, 0));

        setSpawnPosition(checkpoints[index]);

        reorderCheckpoints();

        addProjTimer(650, this::attack);
    }

    public void attack() {
        if (DeltaMath.randBool()) {
            new ChessProjectile(this, position.addClone(Vector2.up), 450, 650);
            new ChessProjectile(this, position.addClone(Vector2.down), 450, 650);
            new ChessProjectile(this, position.addClone(Vector2.left), 450, 650);
            new ChessProjectile(this, position.addClone(Vector2.right), 450, 650);
        } else {
            new ChessProjectile(this, position.addClone(Vector2.upleft), 450, 650);
            new ChessProjectile(this, position.addClone(Vector2.upright), 450, 650);
            new ChessProjectile(this, position.addClone(Vector2.downleft), 450, 650);
            new ChessProjectile(this, position.addClone(Vector2.downright), 450, 650);
        }
    }

    public void onTargetLost() {
        setMoveTo(spawnPosition);
    }

    public void update() {

    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.addItem(ItemList.gold, 500, 0.75);
        table.addItem(ItemList.stone, 5, 0.5, true);
        world.createGroundItem(position, table.determineOutput());
    }
}
