/*
 * Copyright (c) 2019 Zachary Verlardi
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.Game.Entity.Player;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Init.Server;
import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.ConnectionHandling.security.Obfuscator;
import com.Game.ConnectionHandling.security.Password;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Entity;
import com.Game.Entity.NPC.NPC;
import com.Game.Entity.NPC.Shop;
import com.Game.Inventory.*;
import com.Game.Items.Weapon.Weapon;
import com.Game.Objects.GameObject;
import com.Game.Projectile.Fist;
import com.Game.Questing.QuestList;
import com.Game.Skills.Skills;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.*;
import com.Game.WorldManagement.GroundItem;
import com.Game.WorldManagement.World;
import com.Game.WorldManagement.WorldHandler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Optional;

public class Player extends Entity {

    // Player Constants
    public final float playerMoveSpeed = 225f;

    // Connection Settings
    private InetAddress ipAddress;
    private int port;
    public int connected = 0;

    // Item Inventories Section
    public InventoryManager inventory;
    public AccessoriesManager accessory;
    public BankingHandler banking;
    public ShopHandler shop;

    // Stats
    public QuestData questData;
    public Skills skills;
    public float maxHealth = 0;
    public float msMulti = 1;

    // Real-Time Data
    public float health = 0;
    public float healthRegen = 10;
    public long shootTimer = 0;
    public float armor, damageMult, defense, speedMult;
    public Vector2 lastKnownPosition;
    public Vector2 estimatedVelocity;

    // Player Properties
    public String username;

    // Timers
    public long completionTime = 0;
    public long velocityCheckTime = 0;
    public GameObject objectInteration = null;
    private Hashtable<String, Long> timers;

    // Movement Verification
    public long lastMoveTime = 0;

    public Runnable[] options = new Runnable[0];

    // On Action Lambdas
    public Perk onHit = null;
    public PlayerActionLoop playerLoop = null;
    public int playerLoopsRemaining = 0;

    /**
     * The Password, when set using {@link #setPassword(Password)}.
     * Typically, when set through this method, the password will be hashed.
     */
    public Password password;

    public Player(InetAddress ipAddress, int port) {
        super(null, Vector2.zero());
        this.ipAddress = ipAddress;
        this.port = port;
        this.timers = new Hashtable<>();
        banking = new BankingHandler(this);

        timers.put("healthRegen", System.currentTimeMillis() + 250);

        initSkills();
    }

    public void update() {
        if (System.currentTimeMillis() > velocityCheckTime) {
            velocityCheckTime = System.currentTimeMillis() + Settings.velocityCheckTime;
            Vector2 currentPosition = position.clone();

            if (lastKnownPosition != null)
                estimatedVelocity = Vector2.magnitudeDirection(lastKnownPosition, currentPosition).scale(playerMoveSpeed * (1 + speedMult));

            lastKnownPosition = currentPosition.clone();
        }

        if (playerLoop != null)
            playerLoop.update();

        // every when timers.get("healthRegen") is less than the current time, the player will regen health
        if (System.currentTimeMillis() > timers.get("healthRegen")) {
            timers.put("healthRegen", System.currentTimeMillis() + Settings.healInterval);
//            health = Math.min(health + healthRegen * Settings.healInterval, maxHealth);
            changeHealth((health < maxHealth) ? Math.min(healthRegen * Settings.healInterval / 1000, maxHealth - health) : 0);
        }
    }

    public void initSkills() {
        this.skills = new Skills(this);
        this.username = "";
        this.password = new Password("", false, false);
        this.inventory = new InventoryManager(this);
        this.accessory = new AccessoriesManager(this);
        this.questData = new QuestData(this);
        this.shop = new ShopHandler(this);
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String toString() {
        return "Address: " + ipAddress + "\nPort: " + port + "\nName: " + username;
    }

    public String getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void createPassword(String string) {
        Obfuscator obfuscator = new Obfuscator();
        password = new Password(obfuscator.hashPassword(string), true, true);
    }

    public void setPos(int x, int y) {
        Vector2 newPosition = new Vector2(x, y);

        Server.send(this, "pc", x, y);

        if (Vector2.distance(position, newPosition) > (System.currentTimeMillis() - lastMoveTime) * playerMoveSpeed * msMulti * 2.0) {
            Client.correctPosition(this, position);
        }

        this.position = newPosition;
    }

    public void setWorld(World world) {
        velocityCheckTime = 0;
        estimatedVelocity = null;
        lastKnownPosition = null;

        if (world == null) {
            System.err.println("world is null!");
            return;
        }

        if (this.world != null) {
            this.world.removePlayer(this);

            for (Player player : this.world.players) {
                Client.informPlayerLeft(player, username);
            }
        }

        this.world = world;

        world.informPlayer(this);
        world.addPlayer(this);
    }

    public void sendMessage(String message) {
        Client.sendMessage(this, message);
    }

    public float getExperience(int skill) {
        return skills.xp[skill];
    }

    public void setExperience(int skill, float exp) {
        skills.setExperience(skill, exp);
    }

    public boolean addExperience(int skill, float amount) {
        skills.addExperience(skill, amount);
        return true;
    }

    public boolean skillCompare(int index, int lvl) {
        return skills.levels[index] >= lvl;
    }

    public void teleport(int tx, int ty, World world) {
        setPos(tx, ty);
        setWorld(world);
    }

    public void teleport(int tx, int ty, int worldID) {
        teleport(tx, ty, WorldHandler.getWorld(worldID));
    }

    public void teleport(int tx, int ty) {
        setPos(tx, ty);
    }

    public int getQuestData(int quest) {
        return questData.getData(quest);
    }

    public void setQuestData(int quest, int set) {
        questData.setData(quest, set);
        Client.sendQuest(this, quest);
    }

    public void changeQuestData(int quest, int set) {
        questData.changeData(quest, set);
    }

    public boolean isQuestComplete(int quest) {
        return QuestList.getIndex(quest).isComplete(this);
    }

    public int getLevel(int skill) {
        return skills.levels[skill];
    }

    public boolean timerComplete() {
        return System.currentTimeMillis() > completionTime;
    }

    public void resetInteraction() {
        this.objectInteration = null;
        this.completionTime = 0;
    }

    public void showBank() {
        banking.setOpen(true);
        Client.openGUI(this, "bankon");
    }

    public void hideBank() {
        banking.setOpen(false);
        Client.openGUI(this, "bankoff");
    }

    private void hideShop() {
        shop.selectedShop = Shop.empty;
        Client.openGUI(this, "shopoff");
    }

    public int addItem(ItemList item, int amount) {
        return inventory.addItem(item, amount);
    }

    public int addItem(ItemStack stack) {
        return inventory.addItem(stack);
    }

    public int addItem(ItemStack stack, int amount) {
        ItemStack newStack = stack.clone();
        newStack.amount = amount;

        return inventory.addItem(newStack);
    }

    public void setItem(int slot, int amount) {
        inventory.setItemAmount(slot, amount);
    }

    public void removeItem(int slot, int amount) {
        ItemStack type = inventory.getStack(slot).clone();
        removeItem(type.getItemList(), type.getData(), amount);
    }

    public void removeItem(ItemStack stack, int amount) {
        removeItem(stack.getItemList(), stack.getData(), amount);
    }

    public void removeItem(ItemList type, int data, int amount) {
        ItemStack[] itemStacks = inventory.inventory;

        for (int i = 0; i < itemStacks.length; i++) {
            ItemStack item = itemStacks[i];
            if (item.getItemList() == type && item.getData() == data) {
                int subAmount = Math.min(amount, item.getAmount());
                amount -= subAmount;

                inventory.changeItemAmount(i, -subAmount);
            }

            if (amount <= 0)
                break;
        }
    }

    public void damage(float damage) {
        // set damage to what is returned from the onhit lamda
        if (onHit != null)
            damage -= onHit.action(damage);

        changeHealth(-damage * getDamageReduction());
    }

    public float getDamageReduction() {
        return 1 / (armor / 250f + 1);
    }

    public void playSound(String sound) {
        Client.playSound(this, sound);
    }

    public void setChoice(Runnable op1, Runnable op2, String... strings) {
        options = new Runnable[] {
                op1,
                op2
        };
    }

    public void interact() {
        if (world == null)
            return;

        for (NPC npc : world.npcs) {
            if (Vector2.distance(npc.position, position) < SaveSettings.npcInteractRange) {
                npc.onInteract(this);
                return;
            }
        }

        for (GameObject object : world.objects) {
            if (Vector2.distance(object.position, position) < object.maxDistance) {
                object.onInteract(this);
                return;
            }
        }
    }

    public void loseFocus() {
        endPlayerLoop();

        if (banking.isOpen())
            hideBank();

        if (shop.selectedShop != Shop.empty)
            hideShop();

        if (objectInteration == null)
            return;

        objectInteration.loseFocus(this);

        Client.loseFocus(this);
    }

    public void deposit(int inventoryIndex, int amount) {
        if (!banking.isOpen()) {
            sendMessage("Uh oh! Error alert. Please reopen the bank.");
        }

        banking.depositFromInventory(inventoryIndex, amount);
    }

    public void withdraw(int bankIndex, int amount, boolean inStack) {
        if (!banking.isOpen()) {
            sendMessage("Uh oh! Error alert. Please reopen the bank.");
        }

        banking.withdrawItem(bankIndex, amount, inStack);
    }

    public void handleBankRequest(String[] index) {
        String request = index[1];
        int hover = Integer.parseInt(index[2]);
        int amount = Integer.parseInt(index[3]);
        boolean isStacked = Boolean.parseBoolean(index[4]);

        switch (request.trim()) {
            case "deposit":
                deposit(hover, amount);
                break;
            case "withdraw":
                withdraw(hover, amount, isStacked);
                break;
            default:
                System.err.println("Unknown banking request " + request);
        }
    }

    public void addBankItem(ItemStack itemStack) {
        banking.addBankItem(itemStack);
    }

    public void swapSlots(int index1, int index2) {
        if (index1 < 0 || index1 >= banking.items.size() || index2 < 0 || index2 >= banking.items.size())
            return;

        ItemStack holder = inventory.inventory[index1].clone();
        inventory.setItem(index1, inventory.getStack(index2));
        inventory.setItem(index2, holder);
    }

    public void swapBankSlots(int index1, int index2) {
        // check if index1 or index is out of bounds
        if (index1 < 0 || index1 >= banking.items.size() || index2 < 0 || index2 >= banking.items.size())
            return;

        ItemStack holder = banking.items.get(index1).clone();
        banking.setBankItem(index1, banking.getStack(index2));
        banking.setBankItem(index2, holder);
    }

    public void rightClick(int index, String name) {
        inventory.getStack(index).rightClick(this, index, name);
    }

    public void click(int index) {
        inventory.getStack(index).click(this, index);
    }

    public void unequip(int index) {
        accessory.unequip(this, index);
    }

    public void enableShop(Shop shop) {
        this.shop.selectedShop = shop;

        Server.send(this, "ui", "shop", shop.getShopVerb(), shop.getInventoryVerb());

        shop.sendItems(this);
    }

    public void shoot(Vector2 shootPos) {
        if (System.currentTimeMillis() < shootTimer)
            return;

        ItemStack item = accessory.getSlot(AccessoriesManager.WEAPON_SLOT);

        if (item.getID() == 0) {
            new Fist(this, shootPos);
            return;
        }

        if (!(item.item instanceof Weapon))
            return;

        Weapon weapon = (Weapon) item.item;

        weapon.useWeapon(this, shootPos);
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;

        Client.sendHealth(this);
    }

    public void changeHealth(float healAmount) {
        health += healAmount;

        if (health > maxHealth) {
            health -= healAmount;
        }

        if (health < 0) {
            health = maxHealth;

            cleanUpAfterDeath();

            setWorld(WorldHandler.getWorld(WorldHandler.main));
            setPos(SaveSettings.startX, SaveSettings.startY);
            sendMessage("Oh no! You have died.");
        }

        Client.sendHealth(this);
    }

    public void setHealth(float health) {
        this.health = health;

        if (health < 0) {
            this.health = maxHealth;

            cleanUpAfterDeath();

            setPos(SaveSettings.startX, SaveSettings.startY);
            setWorld(WorldHandler.getWorld(WorldHandler.main));
            sendMessage("Oh no! You have died.");
        }

        Client.sendHealth(this);
    }

    private void cleanUpAfterDeath() {
        ArrayList<Enemy> enemies = world.enemies;
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);

            if (enemy.playerTarget == null)
                continue;

            if (enemy.playerTarget.username.equalsIgnoreCase(username)) {
                enemy.loseTarget();
            }
        }
    }

    public void pickUp(String message) {
        String[] parts = message.split(";");

        int token = Integer.parseInt(parts[1]);
        int index = Integer.parseInt(parts[2]);

        for (GroundItem groundItem : world.groundItems) {
            if (groundItem.randomToken == token) {
                if (groundItem.stack.size() < index - 1)
                    continue;

                int removed = addItem(groundItem.stack.get(index));

                groundItem.changeAmount(index, -removed);
                return;
            }
        }
    }

    public boolean hasItem(ItemList item, int data) {
        return inventory.itemCount(item, data) > 0;
    }

    public void changeSprite(String code) {
        Server.send(this, "sc" + code);
    }

    public void resetAnimation() {
        changeSprite("idle");
    }

    public Long getTimer(String key) {
        if (timers.containsKey(key))
            return timers.get(key);

        return 0L;
    }

    public void setTimer(String key, long timeFromNow) {
        timers.put(key, System.currentTimeMillis() + timeFromNow);
    }

    public void setOnHit(Perk onHit) {
        this.onHit = onHit;
    }

    public void createPlayerLoop(PlayerAction action, long delay) {
        playerLoop = new PlayerActionLoop(this, action, delay);
    }

    public void createPlayerLoop(PlayerActionLoop actionLoop) {
        playerLoop = actionLoop;
    }

    public void setLoopTime(long delay) {
        playerLoop.setLoopTime(delay);
    }

    public void endPlayerLoop() {
        playerLoop = null;
    }

    public InventoryManager getInventory() {
        return inventory;
    }

    public void dropItem(int index) {
        if (inventory.getItem(index).worth == -1) {
            sendMessage("You can't drop that.");
        }

        world.createGroundItem(position, inventory.getStack(index));
        inventory.setItem(index, ItemStack.empty());
    }

//    public float getDamageMultiplier() {
//        float mult = 1;
//
//        for (ItemStack item : accessory.accessories) {
//            mult += item.getDamage();
//        }
//
//        return mult;
//    }
}
