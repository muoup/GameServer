package com.Game.WorldManagement;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.NPC.NPC;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemStack;
import com.Game.ItemData.DropTable;
import com.Game.Objects.GameObject;
import com.Game.Projectile.Projectile;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.Iteration;
import com.Game.Util.Other.IterationConditional;
import com.Game.Util.Other.Settings;
import com.Game.Util.Other.Timer;

import java.util.ArrayList;

public abstract class World {

    public final int id;

    public ArrayList<Player> players;
    public ArrayList<Enemy> enemies;
    public ArrayList<GameObject> objects;
    public ArrayList<Projectile> projectiles;
    public ArrayList<GroundItem> groundItems;
    public ArrayList<NPC> npcs;
    public ArrayList<Timer> itemSpawns;

    private String worldImage;

    public World(int id) {
        this.id = id;

        players = new ArrayList<>();
        enemies = new ArrayList<>();
        objects = new ArrayList<>();
        projectiles = new ArrayList<>();
        npcs = new ArrayList<>();
        groundItems = new ArrayList<>();
        itemSpawns = new ArrayList<>();
    }

    public abstract void worldCreation();

    public void update() {
        for (int i = 0; i < objects.size(); i++) {
            GameObject object = objects.get(i);
            object.update();
        }

        for (int i = 0; i < npcs.size(); i++) {
            NPC npc = npcs.get(i);
            npc.update();
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.updateEnemy();
        }

        for (int i = 0; i < projectiles.size(); i++) {
            Projectile projectile = projectiles.get(i);
            projectile.updateProjectile();
        }

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.update();
        }

        for (int i = 0; i < groundItems.size(); i++) {
            GroundItem item = groundItems.get(i);
            item.update();
        }

        for (int i = 0; i < itemSpawns.size(); i++) {
            Timer timer = itemSpawns.get(i);
            timer.update();
        }
    }

    public boolean empty() {
        return players.size() == 0;
    }

    protected void initImage(String image) {
        this.worldImage = image;
    }

    public void informPlayer(Player player) {
        Server.send(player, "wc", worldImage);

        sendObjects(player);
        sendNPCs(player);
        sendEnemies(player);
        sendGroundItems(player);
        sendOtherPlayers(player);
    }

    public void sendObjects(Player player) {
        for (GameObject object : objects) {
            Server.send(player, "ob", object.packetInfo());
        }
    }

    public void sendNPCs(Player player) {
        for (NPC npc : npcs) {
            Server.send(player,"ns", npc.packetInfo());
        }
    }

    public void sendEnemies(Player player) {
        for (Enemy enemy : enemies) {
            Server.send(player, "ne", enemy.packetInfo());
            enemy.informMovement(player);
        }
    }

    public void sendOtherPlayers(Player player) {
        for (Player otherPlayer : players) {
            if (otherPlayer != player) {
                Client.sendPlayerCreate(player, otherPlayer);
            }
        }
    }

    public void newEnemy(Enemy enemy) {
        enemies.add(enemy);

        for (Player p : players)
            Server.send(p, "ne", enemy.packetInfo());
    }

    public void createGroundItem(Vector2 position, ArrayList<ItemStack> drops) {
        for (GroundItem i : groundItems) {
            if (Vector2.distance(i.position, position) < Settings.mergeDistance) {
                i.addItems(drops);
                return;
            }
        }

        if (drops.size() == 0)
            return;

        new GroundItem(this, position, drops);
    }

    public void createGroundItem(Vector2 position, ItemStack drop) {
        for (GroundItem i : groundItems) {
            if (Vector2.distance(i.position, position) < Settings.mergeDistance) {
                i.addItem(drop);
                return;
            }
        }

        new GroundItem(this, position, drop);
    }

    public void sendGroundItemChange(GroundItem item) {
        for (Player player : players)
            Server.send(player, "gu", item.packetInfo());
    }

    public void removeGroundItem(GroundItem item) {
        groundItems.remove(item);

        for (Player player : players) {
            Server.send(player, "gr", item.randomToken);
        }
    }

    public void sendGroundItems(Player player) {
        for (GroundItem gItems : groundItems) {
            Server.send(player, "gi", gItems.packetInfo());
        }
    }

    public void sendGroundItem(GroundItem item) {
        groundItems.add(item);

        for (Player player : players)
            Server.send(player, "gi", item.packetInfo());
    }

    public void updateGroundItem(World world) {

    }

    public void removePlayer(Player player) {
        if (players == null) {
            System.err.println("rut roh");
            return;
        }

        player.cleanUpWorld();
        players.remove(player);
    }

    public void addPlayer(Player player) {
        if (players == null) {
            System.out.println("aut aoh");
            return;
        }

        players.add(player);

        for (Player p : players)
            Client.sendPlayerCreate(p, player);
    }

    protected void repeat(int times, Iteration runnable) {
        for (int i = 0; i < times; i++) {
            runnable.iterate(i);
        }
    }

    protected void repeat(int times, IterationConditional conditional, Iteration runnable) {
        for (int i = 0; i < times; i++) {
            if (conditional.shouldExecute(i))
                runnable.iterate(i);
            else
                times++;
        }
    }

    public String getToken() {
        return "w" + id;
    }

    public void addItemSpawn(ItemStack item, float x, float y) {
        itemSpawns.add(new Timer(Settings.groundItemDuration, true, () -> createGroundItem(new Vector2(x, y), item)));
    }

    public void addItemSpawn(DropTable table, float x, float y) {
        itemSpawns.add(new Timer(Settings.groundItemDuration, true, () -> createGroundItem(new Vector2(x, y), table.determineOutput())));
    }
}
