package com.Game.ConnectionHandling;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Inventory.ItemStack;
import com.Game.Player.Player;

public class Client {
    public static void changeItem(Player player, int index, ItemStack stack) {
        StringBuilder options = new StringBuilder();

        for (String s : stack.getOptions()) {
            options.append(s + ",");
        }

        options.delete(options.length() - 1, options.length() - 1);

        Server.send("it" + index + ":" + stack.getImage() + ":" + options.toString(), player.getIpAddress(), player.getPort());
    }

    public static void sendSkill(Player player, int skill, int amount) {
        Server.send("sk" + skill + ":" + player.skills.levels + ":" + player.skills.xp[skill] + ":" + player.username, player.getIpAddress(), player.getPort());
    }

    public static void sendMessage(Player player, String message) {
        Server.send("me" + message, player);
    }
}
