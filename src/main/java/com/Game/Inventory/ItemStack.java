package com.Game.Inventory;

import com.Game.ItemData.Requirement.ActionRequirement;
import com.Game.PseudoData.ImageIdentifier;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Physical stacks held in the inventory or other storage devices.
 */
public class ItemStack {

    // Essential
    public Item item;
    public int id, amount, data, equipStatus, worth;
    public ArrayList<String> options;
    public ActionRequirement requirement;
    public ImageIdentifier image;
    public String name, examineText;

    // Non-essential
    public float damageMultiplier = 0;
    public float defense = 0;
    public float speed = 0;

    public ItemStack(ItemList item, int amount) {
        this(item, amount, 0);
    }

    public ItemStack(Item item, int amount, int data) {
        this.id = item.getID();
        this.item = item;
        this.amount = amount;
        this.options = new ArrayList();
        this.requirement = ActionRequirement.none();
        this.image = item.getImage();
        this.name = item.getName();
        this.worth = item.worth;

        setData(data);
    }

    public ItemStack(ItemList item, int amount, int data) {
        this(item.getItem(), amount, data);
    }

    public ItemStack(int item, int amount, int data) {
        this(ItemList.values()[item], amount, data);
    }

    public static ItemStack empty() {
        return new ItemStack(ItemList.empty, 0, 0);
    }

    public boolean compare(ItemStack stack) {
        return getID() == stack.getID() &&
                getData() == stack.getData();
    }

    public ArrayList<String> getOptions() {
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

    public int getData() {
        return data;
    }

    public int getID() {
        return item.getID();
    }

    public ItemStack clone() {
        return new ItemStack(item, amount, data);
    }

    public void setData(int data) {
        this.data = data;
        item.setData(this);
    }

    public int getEquipStatus() {
        return item.getEquipStatus(data);
    }

    public int getMaxAmount() {
        return item.getMaxAmount();
    }

    public ItemList getItemList() {
        return item.getItemList();
    }

    public void setImage(ImageIdentifier image) {
        this.image = image;
    }

    public void setEquipStatus(int status) {
        this.equipStatus = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExamineText(String text) {
        this.examineText = text;
    }

    public void setOptions(String... options) {
         this.options.clear();

        this.options.addAll(Arrays.asList(options));
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String optionsString() {
        StringBuilder optionBuilder = new StringBuilder();

        for (String option : options) {
            optionBuilder.append(option + "\\");
        }

        return optionBuilder.toString();
    }

    public String getExamineText() {
        return examineText;
    }

    public int getWorth() {
        return worth * amount;
    }

    public ItemStack singleStack() {
        return new ItemStack(item, 1, data);
    }
}