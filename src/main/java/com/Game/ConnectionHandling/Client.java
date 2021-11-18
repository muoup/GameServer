package com.Game.ConnectionHandling;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Inventory.ItemStack;
import com.Game.Entity.Player.Player;
import com.Game.Objects.GameObject;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class Client {
    public static void changeItem(Player player, int index, ItemStack stack) {
        StringBuilder options = new StringBuilder();

        for (String s : stack.getOptions()) {
            options.append(s + ",");
        }

        options.delete(options.length() - 1, options.length() - 1);

        Server.send(player, "it", index, stack.getImage(), stack.getAmount(), options.toString());
    }

    public static void sendSkill(Player player, int skill) {
        Server.send(player,"sk", skill, player.skills.levels, player.skills.xp[skill]);
    }

    public static void sendInventorySlot(Player player, int slot, ItemStack item) {
        Server.send(player, "in", slot, item.getName(), item.getAmount(), item.getImage().getToken(), item.getExamineText(), item.optionsString());
    }

    public static void sendAccessorySlot(Player player, int slot, ItemStack item) {
        Server.send(player, "ac", slot, item.getName(), item.getAmount(), item.getImage().getToken(), item.getExamineText());
    }

    public static void sendMessage(Player player, String... message) {
        Server.send(player, "me", message);
    }

    public static void playSound(Player player, String sound) {
        Server.send(player, "so" + sound);
    }

    public static void openGUI(Player player, String bankType) {
        Server.send(player, "ui", bankType);
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

    public static void sendChoice(Player player, String text, String... choices) {
        Server.send(player, "ch", text, choices);
    }

    public static void sendText(Player player, String... text) {
        Server.send(player, "te", text);
    }

    public static void clearTextBox(Player player) {
        Server.send(player, "cleartextbox");
    }
}
