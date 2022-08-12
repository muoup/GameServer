package com.Game.Items.Misc;

import com.Game.ConnectionHandling.Client;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Other.SpriteSheet;

public class BugRelic extends Item {
    private static final long relicTime = 120000;
    private static SpriteSheet sheet = new SpriteSheet("Items/bug_relic.png", 48, 48);

    public BugRelic(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage(sheet.getCell(0, 0));
    }

    public void update(Player player, ItemStack stack, int index) {
        long elapsedTime = System.currentTimeMillis() - stack.getData();

        if (elapsedTime >= relicTime) {
            player.inventory.removeItem(stack);
            return;
        }

        if (!stack.hasReference("redness"))
            stack.pushReference("redness", 0);

        int newData = (int) (elapsedTime * 8 / relicTime);

        if (newData != (int) stack.getReference("redness")) {
            stack.pushReference("redness", newData);
            stack.setImage(sheet.getCell(newData, 0));
            Client.sendInventorySlot(player, index, stack);
        }
    }

    public void dataItemChange(ItemStack stack) {
        stack.preventBanking = true;
    }

    public void onDeath(Player player, ItemStack stack, int index) {
        player.inventory.removeItem(stack);
    }

    public void playerStatChange(ItemStack itemStack, Player player) {
        player.speedMult *= 0.65f;
        player.damageTakenMult *= 2;
    }
}
