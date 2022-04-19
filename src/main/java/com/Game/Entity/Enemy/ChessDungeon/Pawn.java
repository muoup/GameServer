package com.Game.Entity.Enemy.ChessDungeon;

import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.WorldManagement.World;

public class Pawn extends Enemy {
    public Pawn(World world, int x, int y, boolean white) {
        super(world, x, y);
        setMaxHealth(35);
        setImage("ChessDungeon/" + (white ? "" : "black_") + "pawn.png", 96 ,96);
        this.name = "Chessboard Pawn";
        this.id = 35;
        this.respawnTime = 25000;
        this.targetLostTime = 4500;
        this.passive = false;
        this.speed = 0;
    }

    public void AI() {

    }

    public void update() {

    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.addItem(ItemList.gold, 500, 0.75);
        table.addItem(ItemList.stone, 5, 0.5);
        world.createGroundItem(position, table.determineOutput());
    }
}
