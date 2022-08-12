package com.Game.Entity.Enemy;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.WorldManagement.World;

public class Chicken extends Enemy {
    public Chicken(World world) {
        super(world, 0, 0);
        this.respawnTime = 5000;
        this.loseTargetTime = 2000;
        this.name = "Chicken";
        this.speed = 80f;
        this.idleAI = AIType::passiveBoundaryWalk;

        setImage("chicken.png", 64, 64);
        setMaxHealth(10);
        setBounds(3513, 1279,
                4143, 1912);

        this.spawnPosition = bounds.randomPoint();
        setPosition(spawnPosition);

        //addPassiveTimer(1000, this::chickenTest);
    }

    public void update() {

    }

    public void chickenTest() {
        if (moveTo == null || movement == null)
            return;

        for (Player player : world.players) {
            Server.send(player, "cc", position, moveTo, movement);
        }
    }

    public void handleDrops() {
        DropTable dropTable = new DropTable();
        dropTable.addItem(ItemList.feather, 1, 6, 1);
        world.createGroundItem(position, dropTable.determineOutput());
    }
}
