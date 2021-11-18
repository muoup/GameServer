package com.Game.Inventory;

import com.Game.ItemData.Requirement.ActionRequirement;
import com.Game.Entity.Player.Player;
import com.Game.Projectile.Projectile;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains the functionality of an item, one instance of this is held in an ItemList
 */
public class Item {
    protected double armor;
    protected float damageMulti;
    protected ActionRequirement requirement;
    protected boolean stackable;

    public int id, worth, equipStatus;
    public ImageIdentifier image;
    public String name, examineText;

    public static ItemStack emptyStack() {
        return new ItemStack(ItemList.empty, 0, 0);
    }

    public Item(int id, String name, String examineText, int worth, boolean stackable) {
        this.id = id;
        this.worth = worth;
        this.name = name;
        this.examineText = examineText;
        this.image = ImageIdentifier.emptyImage();
        this.stackable = stackable;
    }

    // For unspecial items like fish bait that don't need any special functionality as they exist as a middleman.
    public Item(int id, String image, String name, String examineText, int worth, boolean stackable) {
        this(id, name, examineText, worth, stackable);

        setImage(image);
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExamineText() {
        return examineText;
    }

    public String getImageToken() {
        return image.getToken();
    }

    public ImageIdentifier getImage() {
        return image;
    }

    public void setImage(ImageIdentifier image) {
        this.image = image;
    }

    public void setImage(String image) {
        this.image = ImageIdentifier.singleImage("Items/" + image);
    }

    public void ClickIdentities(Player player, int index) {
        //if (GUI.currentShop.empty()) {
        if (player.inventory.getStack(index).getEquipStatus() != -1) {
            equipItem(player, index);
        } else {
            player.inventory.useIndex = -1;
            onClick(player, index);
        }
//        } else {
//            ItemStack selected = InventoryManager.getStack(index);
//
//            ChatBox.sendMessage(selected.name + " will sell for " + (int) (selected.item.worth * 0.95f) + " coins each.");
//
//            if (selected.getAmount() > 1)
//                ChatBox.sendMessage("The whole stack is worth: " + (int) (selected.getWorth() * 0.95f) + " coins.");
//        }
    }

    public void onClick(Player player, int index) {

    }

    public void onOptionUse(Player player) {

    }

    public void examineItem(Player player, int index) {
        String text = examineText;
        text = text.replace("[amt]", Integer.toString(player.inventory.getStack(index).getAmount()));
        player.sendMessage(text);
    }

    public void RightClickIdentities(Player player, int index, int option) {
        if (option == 0) {
            ClickIdentities(player, index);
        }

//        if (option == RightClick.options.size() - 1)
//            examineItem(index);
//
//        if (option == RightClick.options.size() - 2)
//            dropItem(index);

        OnRightClick(player, index, option);
    }

    private void dropItem(Player player, int index) {
        ItemStack stack = player.inventory.getStack(index).clone();
        //TODO: GroundItem.createGroundItem(player.getPosition(), stack);

        player.inventory.setItem(index, ItemStack.empty());
    }

    public void OnRightClick(Player player, int index, int option) {

    }

    public void setData(ItemStack stack) {

    }

    public void useWeapon(Vector2 position, Vector2 direction) {

    }

    public Projectile createProjectile(Vector2 position, Vector2 direction, float damageMultiplier) {
        return null;
    }

    public void equipItem(Player player, int index) {
        ItemStack stack = player.inventory.getStack(index);
        ItemStack slotStack = player.accessory.getSlot(stack.getEquipStatus());

        if (!stack.requirement.meetsRequirement(player)) {
            player.sendMessage(stack.requirement.toString());
            return;
        }

        if (slotStack.getID() == stack.getID() && slotStack.getData() == stack.getData()) {
            int add = stack.getAmount();

            if (add > slotStack.getMaxAmount() - slotStack.getAmount())
                add = slotStack.getMaxAmount() - slotStack.getAmount();

            player.accessory.addAmount(stack.getEquipStatus(), add);
            player.inventory.addAmount(index, -add);

        } else {
            ItemStack invHolder = player.inventory.getStack(index).clone();
            player.inventory.setItem(index, player.accessory.getSlot(stack.getEquipStatus()));
            player.accessory.setSlot(stack.getEquipStatus(),
                    new ItemStack(invHolder.getItemList(), invHolder.getAmount(), invHolder.getData()));
        }
    }

    public void replaceInventory(Player player, int index, ItemStack item) {
        player.inventory.setItem(index, emptyStack());
        player.inventory.addItem(item);
    }

    public void use(int index) {

    }

    public boolean combine(Player player, int index, ItemList use, int maxUse, int newData) {
        int useAmount = Math.min(maxUse, player.inventory.itemCount(use));
        if (useAmount == 0) {
            player.sendMessage("You need some " + use.getItem().name + " to complete this action.");
            return false;
        }

        player.inventory.removeItem(use, useAmount);
        player.inventory.setData(index, newData);
        return true;
    }

    public int combine(Player player, int index, ItemList use, ItemList create, int amt) {
        int amount = player.inventory.itemCount(use);
        amount = Math.min(amt, amount);
        amount = Math.min(amount, player.inventory.getStack(index).getAmount());
        if (amount == 0) {
            player.sendMessage("You need some " + use.getItem().name + " to do this.");
            return 0;
        }

        player.inventory.removeItem(use, amount);
        player.inventory.removeItem(getItemList(), amount);
        player.inventory.addItem(create, amount);

        return amount;
    }

    public int combine(Player player, int index, ItemList use, ItemList create, int amt, int data) {
        int amount = player.inventory.itemCount(use);
        amount = Math.min(amt, amount);
        amount = Math.min(amount, player.inventory.getStack(index).getAmount());
        if (amount == 0) {
            player.sendMessage("You need some " + use.getItem().name + " to do this.");
            return 0;
        }

        player.inventory.removeItem(use, amount);
        player.inventory.removeItem(getItemList(), amount);
        int in = player.inventory.addItem(create, amount);
        player.inventory.setData(in, data);

        return amount;
    }

    public int convert(Player player, int amountPer, int maxCreate, ItemList create) {
        int use = player.inventory.itemCount(getItemList());
        int make = (int) DeltaMath.maxmin(use / amountPer, 0, maxCreate);
        if (make == 0) {
            player.sendMessage("You need " + amountPer + " " + name + " to make this.");
            return 0;
        }
        player.inventory.removeItem(getItemList(), make * amountPer);
        player.inventory.addItem(create, make);
        return amountPer;
    }

    public ItemList getItemList() {
        return ItemList.values()[id];
    }

    public ArrayList<String> createOptions(String... optionList) {
        return (ArrayList<String>) Arrays.asList(optionList);
    }

    public String getOptionText(int index, ItemStack stack) {
        if (stack.options.size() == 0) {
            return "Examine " + stack.getName();
        }

        return stack.options.get(index);
    }

    public int getMaxAmount() {
        return (stackable) ? Integer.MAX_VALUE : 1;
    }

    public int getEquipStatus(int data) {
        return equipStatus;
    }
}
