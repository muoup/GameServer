package com.Game.Inventory;

import com.Game.BaseClass;
import com.Game.Entity.NPC.Shop;
import com.Game.Entity.Player.Player;
import com.Game.ItemData.Requirement.ActionRequirement;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Other.RCOption;
import com.Game.Util.Other.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Physical stacks held in the inventory or other storage devices.
 */
public class ItemStack extends BaseClass {

    public static final ItemStack shopBreakPoint = new ItemStack("Break Point");

    // Essential
    public Item item;
    public int id, amount, equipStatus, worth;
    public long data;
    public ArrayList<RCOption> options;
    public ActionRequirement requirement;
    public ImageIdentifier image;
    public String name, examineText;
    public boolean isStacked = false;

    // Special Variable Cases
    public boolean preventBanking = false;

    // Non-essential
    public float damage = 0, speed = 0, armor = 0;

    public ItemStack(ItemList item, int amount) {
        this(item, amount, 0);
    }

    public ItemStack(ItemList item, int amount, long data, int worth) {
        this(item, amount, data);
        this.worth = worth;
    }

    public ItemStack(String name) {
        this(ItemList.empty, 0, 0);
        this.name = name;
    }

    public ItemStack(Item item, int amount, long data) {
        this.id = item.getID();
        this.item = item;
        this.amount = amount;
        this.options = new ArrayList();
        this.requirement = ActionRequirement.none();
        this.image = item.getImage();
        this.name = item.getName();
        this.worth = item.worth;
        this.examineText = item.getExamineText();

        setData(data);
    }

    public ItemStack(Item item, int amount, long data, boolean stacked) {
        this.id = item.getID();
        this.item = item;
        this.amount = amount;
        this.options = new ArrayList();
        this.requirement = ActionRequirement.none();
        this.image = item.getImage();
        this.name = item.getName();
        this.worth = item.worth;
        this.examineText = item.getExamineText();
        this.isStacked = stacked;

        setData(data);
    }

    public ItemStack(ItemList item, int amount, long data) {
        this(item.getItem(), amount, data);
    }

    public ItemStack(ItemList item, int amount, long data, boolean stacked) {
        this(item.getItem(), amount, data, stacked);
    }

    public ItemStack(int item, int amount, long data) {
        this(ItemList.values()[item], amount, data);
    }

    public ItemStack(int item, int amount, long data, boolean stacked) {
        this(ItemList.values()[item], amount, data, stacked);
    }

    public static ItemStack empty() {
        return new ItemStack(ItemList.empty, 0, 0);
    }

    public boolean compare(ItemStack stack) {
        return getID() == stack.getID() &&
                getData() == stack.getData() &&
                isStacked == stack.isStacked;
    }

    public ArrayList<RCOption> getOptions() {
        return options;
    }

    public ImageIdentifier getImage() {
        return image;
    }

    public boolean hasAtleast(ItemStack minimum) {
        return amount > minimum.amount && compare(minimum);
    }

    public boolean hasAtleast(int amount) {
        return this.amount > amount;
    }

    public String getName() {
        return name;
    }

    public Item getItem() {
        return this.item;
    }

    public int getAmount() {
        return amount;
    }

    public long getData() {
        return data;
    }

    public int getID() {
        return item.getID();
    }

    public float getDamage() {
        return damage;
    }


    public float getSpeed() {
        return speed;
    }

    public float getArmor() {
        return armor;
    }

    public ItemStack clone() {
        return new ItemStack(item, amount, data, isStacked);
    }

    public void setData(long data) {
        this.data = data;

        setName(item.name);
        setImage(item.image);
        setEquipStatus(item.equipStatus);
        setWorth(item.worth);
        setRequirement(item.requirement);
        setArmor(item.armor);
        setDamage(item.damage);
        setSpeed(item.speed);

        item.dataItemChange(this);
    }

    public int getEquipStatus() {
        return equipStatus;
    }

    public ItemList getItemList() {
        return item.getItemList();
    }

    public void setImage(ImageIdentifier image) {
        this.image = image;
    }

    public void setRequirement(ActionRequirement requirement) {
        this.requirement = requirement;
    }

    public void setEquipStatus(int status) {
        this.equipStatus = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorth(int worth) {
        this.worth = worth;
    }

    public void setExamineText(String text) {
        this.examineText = text;
    }

    public void setOptions(ArrayList<RCOption> options) {
        this.options = options;
    }

    public void setOptions(RCOption... options) {
        this.options.clear();

        this.options.addAll(Arrays.asList(options));
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }

    public String optionsString() {
        if (isStacked)
            return "NO OPTIONS";

        StringBuilder optionBuilder = new StringBuilder();

        if (getEquipStatus() != -1) {
            optionBuilder.append("Equip" + "\n");
        }

        for (RCOption option : options) {
            optionBuilder.append(option.getOption() + "\n");
        }

        // if optionBuilder is unchanged, append "NO OPTIONS"
        if (optionBuilder.toString().equals("")) {
            optionBuilder.append("NO OPTIONS");
        }

        return optionBuilder.toString();
    }

    public String getExamineText() {
        if (!isStacked)
            return setUpExamine(examineText);
        else
            return "A stack of " + getAmount() + " " + getName();
    }

    public String getExamineTextAbstract() {
        String newExamine = examineText.replace("[amt]", examineText.indexOf("[amt]") == 0 ? "Some" : "some");

        return setUpExamine(newExamine);
    }

    public String setUpExamine(String text) {
        text = text.replace("[amt]", Integer.toString(getAmount()));

        return text;
    }

    public int getWorth() {
        return worth * amount;
    }

    public ItemStack singleStack() {
        return new ItemStack(item, 1, data);
    }

    public String getServerPacket() {
        return getName() + ";" + getAmount() + ";" + getImage().getToken() + ";" + optionsString() + ";" + getExamineText() + ";" + (int) (Settings.shopSellMultiplier * worth) + ";" + isStacked;
    }

    public void rightClick(Player player, int index, String name) {
        switch (name) {
            case "Examine":
                player.sendMessage(getExamineText());
                return;
            case "Drop":
                player.dropItem(index);
                return;
            case "Equip":
                item.equipItem(player, index);
                return;
        }

        if (isStacked)
            return;

        for (RCOption option : options) {
            if (option.getOption().equals(name)) {
                option.run(player, index);
            }
        }
    }

    public void click(Player player, int index) {
        if (player.shop.selectedShop != Shop.empty) {
            if (worth <= 0)
                player.sendMessage("This item cannot be sold!");
            else
                player.sendMessage("This item will sell for " + worth + " gold each.");
            return;
        }

        if (equipStatus != -1 && !isStacked) {
            item.equipItem(player, index);
        } else if (options.isEmpty() || isStacked) {
            player.sendMessage(getExamineText());
        } else {
            options.get(0).run(player, index);
        }
    }

    public int maxStack() {
        return isStackable() ? Integer.MAX_VALUE : 1;
    }

    public String toString() {
        return getID() + " " + getAmount() + " " + getData() + " " + isStacked;
    }

    public boolean compareIgnoreStack(ItemStack item) {
        return item.getID() == getID() && item.getData() == getData();
    }

    public void setStacked(boolean stacked) {
        isStacked = stacked && !item.stackable;
    }

    public boolean isStackable() {
        return item.stackable || isStacked;
    }

    public int getSingleValue() {
        return worth;
    }
}