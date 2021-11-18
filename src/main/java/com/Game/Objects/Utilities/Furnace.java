package com.Game.Objects.Utilities;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.InventoryManager;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Objects.UsableGameObject;
import com.Game.Skills.Skills;
import com.Game.Util.Other.Settings;
import com.Game.WorldManagement.World;

import java.util.Timer;

public class Furnace extends UsableGameObject {
    private Runnable command;
    private Timer repeat;
    private boolean run = false;

    public Furnace(World world, int x, int y) {
        super(world, x, y);
        repeat = new Timer();

        setImage("Furnace.png");
        setScale(64, 84);
    }

    public void onRightClick(Player player) {
        options.clear();

        int stone = player.inventory.getAmount(ItemList.stone, 0);
        int copper = player.inventory.getAmount(ItemList.copperOre, 0);
        int tin = player.inventory.getAmount(ItemList.tinOre, 0);
        int iron = player.inventory.getAmount(ItemList.ironOre, 0);
        int gold = player.inventory.getAmount(ItemList.goldOre, 0);

        if (stone >= 1)
            options.add("Smelt Stone");

        if (copper >= 1 && tin >= 1)
            options.add("Smelt Bronze");
        else if ((copper >= 1 || tin >= 1))
            options.add("You must have both copper and tin to create bronze.");

        if (iron >= 1)
            options.add("Smelt Iron");

        if (gold >= 1)
            options.add("Smelt Stone");

        if (options.size() == 0)
            options.add("You need raw ores to smelt.");
    }

    public void loseFocus() {
        command = null;
        run = false;
        repeat.cancel();
        repeat.purge();
    }

    public void onOption(Player player, int option) {
        String action = options.get(option);
        boolean completeAction = true;
        switch (action) {
            case "Smelt Stone":
                command = () -> craft(player, ItemList.stone, 1, 10);
                break;

            case "Smelt Bronze":
                command = () -> smeltBronze(player);
                break;

            case "Smelt Iron":
                command = () -> craft(player, ItemList.ironOre, 1, 50);
                break;

            case "Smelt Gold":
                command = () -> craft(player, ItemList.goldOre, 1, 65);
                break;

            default:
                completeAction = false;
                break;
        }

        if (completeAction)
            command.run();
    }

    private void smeltBronze(Player player) {
        int slot = player.inventory.getIndexFirst(ItemList.copperOre, 0, ItemList.tinOre, 0);

        if (slot == -1) {
            repeat = new Timer();
            run = false;
            return;
        }

        ItemList remove = (player.inventory.getItem(slot).getItemList() == ItemList.copperOre) ?
                ItemList.tinOre : ItemList.copperOre;

        player.inventory.setItem(slot, new ItemStack(ItemList.copperOre, 1, 1));
        player.inventory.removeItem(remove, 1);
        player.addExperience(Skills.SMITHING, 45);

        repeat.schedule(Settings.wrap(command), 1250);
        run = true;
    }

    public void craft(Player player, ItemList ore, int data, int experience) {
        int slot = player.inventory.getIndex(ore.singleStack());

        if (slot == -1) {
            command = null;
            run = false;
            repeat = new Timer();
            return;
        }

        player.inventory.setData(slot, data);
        player.addExperience(Skills.SMITHING, experience);

        if (!run) {
            repeat = new Timer();
        }

        repeat.schedule(Settings.wrap(command), 2500);
        run = true;
    }
}
