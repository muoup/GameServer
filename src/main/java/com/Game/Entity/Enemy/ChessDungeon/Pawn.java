package com.Game.Entity.Enemy.ChessDungeon;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.PawnProjectile;
import com.Game.Projectile.Projectile;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.WorldManagement.GroundItem;
import com.Game.WorldManagement.World;
import com.sun.tools.javac.Main;

public class Pawn extends Enemy {
    public Pawn(World world, int x, int y, boolean white) {
        super(world, x, y);
        setMaxHealth(35);
        setImage("ChessDungeon/" + (white ? "" : "black_") + "pawn.png");
        this.name = "Chessboard Pawn";
        this.id = 35;
        this.respawnTimer = 25.0f;
        this.maxTarget = 4.5f;
        this.timer = 0.0f;
        this.passive = false;
        setScale(96, 96);
    }

    public void AI() {
        timer += Server.dTime();
        timer2 += Server.dTime();

        if (timer2 > 0.65f) {
            new PawnProjectile(this, playerTarget.getPosition(), 25.5f);
            timer2 = 0;
        }
    }

    public void passiveAI() {
        if (timer > 0.1f) {
            timer -= Server.dTime();
        } else if (timer < -0.1f) {
            timer += Server.dTime();
        } else if (timer != 0) {
            timer = 0;
        }
    }

    public void update() {
        position = spawnPosition.addClone(0, 48f + (float) Math.sin((timer - 3.14f / 2f) * 5) * 48f);
    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.add(ItemList.gold, 500, 0.75);
        table.add(ItemList.stone, 5, 0.5);
        world.createGroundItem(position, table.determineOutput());
    }
}
