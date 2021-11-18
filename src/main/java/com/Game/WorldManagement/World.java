package com.Game.WorldManagement;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Enemy;
import com.Game.Entity.NPC.NPC;
import com.Game.Inventory.ItemStack;
import com.Game.Objects.GameObject;
import com.Game.Entity.Player.Player;
import com.Game.Projectile.Projectile;
import com.Game.Util.Math.Vector2;

import java.util.ArrayList;

public abstract class World {

    public final int id;

    public ArrayList<Player> players;
    public ArrayList<Enemy> enemies;
    public ArrayList<GameObject> objects;
    public ArrayList<Projectile> projectiles;
    public ArrayList<GroundItem> groundItems;
    public ArrayList<NPC> npcs;

    private String worldImage;

    public World(int id) {
        this.id = id;

        players = new ArrayList();
        enemies = new ArrayList();
        objects = new ArrayList();
        projectiles = new ArrayList();
        groundItems = new ArrayList();
        npcs = new ArrayList();

        worldCreation();
    }

    public abstract void worldCreation();

    public void update() {
        for (GameObject object : objects) {
            object.update();
        }
    }

    public boolean empty() {
        return players.size() == 0;
    }

    protected void initImage(String image) {
        this.worldImage = image;
    }

    public void createGroundItem(Vector2 position, ArrayList<ItemStack> items) {
        createGroundItem(new GroundItem(position, items));
    }

    public void createGroundItem(GroundItem groundItem) {
        groundItems.add(groundItem);
    }

    public void informPlayer(Player player) {
        Server.send(player, "wc", worldImage);

        sendObjects(player);
    }

    public void sendObjects(Player player) {
        System.out.println(objects.size());
        for (GameObject object : objects) {
            Server.send(player, "ob", object.packetInfo());
        }
    }
}
