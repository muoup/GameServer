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
import com.Game.ConnectionHandling.Save.ItemMemory;
import com.Game.ConnectionHandling.security.Obfuscator;
import com.Game.ConnectionHandling.security.Password;
import com.Game.Entity.Entity;
import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.InventoryManager;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Objects.GameObject;
import com.Game.Questing.QuestList;
import com.Game.Skills.Skills;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;
import com.Game.WorldManagement.WorldHandler;

import java.net.InetAddress;
import java.util.ArrayList;

public class Player extends Entity {

    // Player Constants
    public final float playerMoveSpeed = 3;
    public final float playerInitialHealth = 100;

    // Connection Settings
    private InetAddress ipAddress;
    private int port;
    public int connected = 0;

    // Item Inventories Section
    public InventoryManager inventory;
    public AccessoriesManager accessory;
    public ArrayList<ItemMemory> bankItems;

    // Stats
    public QuestData questData;
    public Skills skills;
    public int maxHealth = 100;
    public float msMulti = 1;

    // Real-Time Data
    public int health = 100;
    public float timer = 1.0f;
    public float shootTimer = 0;

    // Player Properties
    public String username;

    // Skill Gather Timers
    public long completionTime = 0;
    public GameObject objectInteration = null;

    // Movement Verification
    public long lastMoveTime = 0;

    public Runnable[] options = new Runnable[0];

    /**
     * The Password, when set using {@link #setPassword(Password)}.
     * Typically, when set through this method, the password will be hashed.
     */
    public Password password;

    public Player(InetAddress ipAddress, int port) {
        super(null, Vector2.zero());
        this.ipAddress = ipAddress;
        this.port = port;
        bankItems = new ArrayList();

        initSkills();
    }

    public void initSkills() {
        this.skills = new Skills(this);
        this.username = "";
        this.password = new Password("", false, false);
        this.inventory = new InventoryManager(this);
        this.accessory = new AccessoriesManager(this);
        this.questData = new QuestData(this);
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
        world.players.remove(this);

        this.world = world;

        world.players.add(this);
        world.informPlayer(this);
    }

    public void sendMessage(String message) {
        Client.sendMessage(this, message);
    }

    public void addExperience(int skill, float amount) {
        skills.addExperience(skill, amount);
    }

    public boolean skillCompare(int index, int lvl) {
        return skills.levels[index] >= lvl;
    }

    public void teleport(int tx, int ty, World world) {
        this.position = new Vector2(tx, ty);
        this.world = world;
    }

    public void teleport(int tx, int ty, int worldID) {
        teleport(tx, ty, WorldHandler.getWorld(worldID));
    }

    public void teleport(int tx, int ty) {
        this.position = new Vector2(tx, ty);
    }

    public int getQuestData(int quest) {
        return questData.getData(quest);
    }

    public void setQuestData(int quest, int set) {
        questData.setData(quest, set);
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
        Client.openGUI(this, "bank");
    }

    public void addItem(ItemList item, int amount) {
        inventory.addItem(item, amount);
    }

    public void addItem(ItemStack stack) {
        inventory.addItem(stack);
    }

    public float getExperience(int skill) {
        return skills.xp[skill];
    }

    public void setExperience(int skill, float exp) {
        skills.xp[skill] = exp;
        skills.deltaLevel(skill, exp, false);
    }

    public World getRoom() {
        return null;
    }

    public void damage(float damage) {
        this.health -= damage;
    }

    public void playSound(String sound) {
        Client.playSound(this, sound);
    }

    public void setChoice(Runnable op1, Runnable op2, String... strings) {
        options = new Runnable[] {
                op1,
                op2
        };

        // TODO: Re-implement choice box.
    }

    public void interactWithObject() {
        for (GameObject object : world.objects) {
            if (Vector2.distance(object.position, position) < object.maxDistance) {
                object.onInteract(this);
                return;
            }
        }
    }

    public void loseFocus() {
        if (objectInteration == null)
            return;

        objectInteration.loseFocus();

        Client.loseFocus(this);
    }
}
