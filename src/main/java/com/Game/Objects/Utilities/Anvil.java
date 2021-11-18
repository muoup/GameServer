package com.Game.Objects.Utilities;

import com.Game.ConnectionHandling.Client;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.InventoryManager;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Objects.UsableGameObject;
import com.Game.Skills.Skills;
import com.Game.WorldManagement.World;

import java.util.HashSet;

public class Anvil extends UsableGameObject {

    ItemList helmet = ItemList.empty;
    ItemList chestplate = ItemList.empty;
    ItemList leggings = ItemList.empty;
    ItemList boots = ItemList.empty;

    float experience = 0;

    public Anvil(World world, int x, int y) {
        super(world, x, y);
        setImage("anvil.png");

        options.add("Craft Helmet");
        options.add("Craft Chestplate");
        options.add("Craft Leggings");
        options.add("Craft Boots");
    }

    public void onOption(Player player, int option) {
        ItemStack opt;
        HashSet<String> messages = new HashSet();
        for (ItemStack stack : player.inventory.inventory) {
            ItemList itemList = stack.getItemList();

            switch (itemList) {
                case stone: // Stone (Ingot?)
                    helmet = ItemList.rockHelmet;
                    chestplate = ItemList.rockChestplate;
                    leggings = ItemList.rockLeggings;
                    boots = ItemList.rockBoots;
                    experience = 25f;
                    opt = createOption(option);
                    break;
                case copperOre: // Bronze Ingot
                    if (stack.getData() != 1)
                        continue;
                    helmet = ItemList.bronzeHelmet;
                    chestplate = ItemList.bronzeChestplate;
                    leggings = ItemList.bronzeLeggings;
                    boots = ItemList.bronzeBoots;
                    experience = 65f;
                    opt = createOption(option);
                    break;
                default:
                    helmet = ItemList.empty;
                    chestplate = ItemList.empty;
                    leggings = ItemList.empty;
                    boots = ItemList.empty;
                    experience = 0f;
                    continue;
            }

            if (player.inventory.getAmount(itemList, 1) >= opt.amount) {
                if (stack.requirement.meetsRequirement(player)) {
                    craftItem(player, opt, itemList);
                    return;
                } else {
                    messages.add("You need level " + stack.requirement.getLevelReq(Skills.SMITHING) + " Smithing to smith with " + stack.name);
                }
            } else {
                messages.add("You need " + opt.getAmount() + " " + stack.name + " to make this piece.");
            }
        }

        for (String message : messages)
            Client.sendMessage(player, message);
    }

    private void craftItem(Player player, ItemStack option, ItemList remove) {
        player.inventory.removeItem(new ItemStack(remove, option.getAmount(), 1));
        player.addItem(option.singleStack());
        player.addExperience(Skills.SMITHING, option.getAmount() * experience);
    }

    private ItemStack createOption(int option) {
        switch (option) {
            case 0:
                return new ItemStack(helmet, 3);
            case 1:
                return new ItemStack(chestplate, 6);
            case 2:
                return new ItemStack(leggings, 4);
            case 3:
                return new ItemStack(boots, 2);
            default:
                return null;
        }
    }
}
