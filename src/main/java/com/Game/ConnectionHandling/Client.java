package com.Game.ConnectionHandling;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Entity.Player.Player;
import com.Game.Objects.GameObject;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class Client {
    public static void sendSkill(Player player, int skill, boolean popup) {
        Server.send(player,"sk", skill, player.skills.levels[skill], player.skills.xp[skill], (popup) ? "p" : "np");
    }

    public static void sendInventorySlot(Player player, int slot, ItemStack item) {
        Server.send(player, "in", slot, item.getServerPacket());
    }

    public static void sendAccessorySlot(Player player, int slot, ItemStack item) {
        Server.send(player, "ac", slot, item.getServerPacket());
    }

    public static void sendQuest(Player player, int questID) {
        Server.send("07:" + questID + ":" + player.questData.getInfoForPacket(questID), player.getIpAddress(), player.getPort());
    }

    public static void sendMessage(Player player, String... messages) {
        for (String message : messages)
            Server.send(player, "me", formatText(player, message));
    }

    public static void playSound(Player player, String sound) {
        Server.send(player, "so" + sound);
    }

    public static void openGUI(Player player, String type) {
        Server.send(player, "ui", type);
    }

    public static void sendObjectUpdate(GameObject object) {
        for (Player player : object.getWorld().players) {
            Server.send(player, "ou", object.packetInfo());
        }
    }

    public static void sendObjectInteraction(Player player, long completionTime) {
        Server.send(player, "oi", completionTime);
    }

    public static void loseFocus(Player player) {
        Server.send(player, "lf");
    }

    public static void correctPosition(Player player, Vector2 pos) {
        Server.send(player, "cp", pos);
        player.sendMessage("You are moving to fast! This may be an issue with the server, but your movement speed may be illegitimate!");
    }

    public static void sendChoice(Player player, int npcID, String text, String... choices) {
        Object[] sendObjs = new Object[choices.length + 2];
        sendObjs[0] = npcID;
        sendObjs[1] = formatText(player, text);

        for (int i = 0; i < choices.length; i ++) {
            sendObjs[i + 2] = choices[i];
        }

        Server.send(player, "ch", sendObjs);
    }

    public static void sendText(Player player, String... text) {
        for (String message : text)
            Server.send(player, "te", formatText(player, message));
    }

    public static void clearTextBox(Player player) {
        Server.send(player, "cleartextbox");
    }

    public static void sendBankChange(Player player, Object... info) {
        Server.send(player, "bc", info);
    }

    public static void enemyUpdate(World world, int randomToken, String variable, Object newValue) {
        for (Player player : world.players)
            enemyUpdateToPlayer(player, randomToken, variable, newValue);
    }

    public static void enemyUpdateToPlayer(Player player, int randomToken, String variable, Object newValue) {
        if (newValue.toString().contains("null")) {
            System.err.println("Strange");
            System.err.printf("%s: %s\n", variable, newValue);
            return;
        }

        //System.out.println(player.username + "eu: " + randomToken + ", " + variable + ", " + newValue);
        Server.send(player, "eu", randomToken, variable, newValue);
    }

    public static void projectileSpawn(World world, Vector2 position, String imageToken, Vector2 direction, float speed, boolean friendly, int randomToken) {
        for (Player player : world.players) {
            Server.send(player, "ps", position, imageToken, direction, speed, friendly, randomToken);
        }
    }

    public static void projectileDestroy(World world, int randomToken) {
        for (Player player : world.players) {
            Server.send(player, "pd", randomToken);
        }
    }

    public static void hprojectileSpawn(World world, Vector2 position, String imageToken, float speed, String playerName, int randomToken) {
        for (Player player : world.players) {
            Server.send(player, "hs", position, imageToken, speed, "p_" + playerName, randomToken);
        }
    }

    public static void hprojectileSpawn(World world, Vector2 position, String imageToken, float speed, int enemyToken, int randomToken) {
        for (Player player : world.players) {
            Server.send(player, "hs", position, imageToken, speed, "e_" + enemyToken, randomToken);
        }
    }

    public static void sendHealth(Player player) {
        Server.send(player, "ph", player.health, player.maxHealth);
    }

    public static void informPlayerLeft(Player player, String username) {
        Server.send(player, "pl", username);
    }

    private static String formatText(Player player, String text) {
        String newText = text;
        newText = newText.replace("[name]", player.username);
        newText = newText.replace("[gold]", Integer.toString(player.inventory.itemCount(ItemList.gold, 0)));
        return newText;
    }
}
