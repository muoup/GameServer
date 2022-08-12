package com.Game.Inventory;

import com.Game.Items.Ammo.*;
import com.Game.Items.Armor.Rock.*;
import com.Game.Items.Armor.Tribal.AmuletOfKanuna;
import com.Game.Items.Consumables.Food.*;
import com.Game.Items.Consumables.Loot.BirdNest;
import com.Game.Items.Consumables.Loot.GradedFish;
import com.Game.Items.Misc.BugRelic;
import com.Game.Items.Misc.QuestItem;
import com.Game.Items.Perks.ChickenShield;
import com.Game.Items.RawResource.*;
import com.Game.Items.RawResource.Log.*;
import com.Game.Items.Tool.FishBait;
import com.Game.Items.Tool.WoodHarp;
import com.Game.Items.Weapon.Bows.CrystalBow;
import com.Game.Items.Weapon.Superclasses.*;

/**
    Contains all of the references of items, used for creating ItemStacks
 */
public enum ItemList {
    empty(new Item(0, "/", "/", 0, false)),
    bow(new BowWeapon(1, 0, 1, "Unstrung Bow", "Get ready for the power of my bow!", 170, false)),
    log(new Log(2, 1, ItemList.bow, "Log", "The remnants of a tree.", 150, false)),
    arrow(new ArrowItem(3, "Arrow", "Some sharp arrows!", 10, true)),
    mapleBow(new BowWeapon(4, 5, 50, "Maple Bow", "This is really gonna hurt.", 100, false)),
    clownfish(new ClownFishFood(5, "Clownfish", "This looks quite tasty.", 100, false)),
    bluefish(new BlueFishFood(6, "Bluefish", "This looks very tasty.", 250, false)),
    mapleLog(new Log(7, 50, ItemList.mapleBow, "Maple Log", "A sticky log, sounds useful to me.", 350, false)),
    gold(new Coins(8, "Coins", "[amt] shiny coins, good for trading..?", -1, true)),
    woodHarp(new WoodHarp(9, "Wooden Harp", "I can get some coins from this. (35% effective)", -1, false)),
    rockHelmet(new ArmorPiece(10, ArmorType.helmet, 0, "Rock Helmet", "Someone went under a dock.", 1000, false)),
    rockChestplate(new ArmorPiece(11, ArmorType.chestplate, 0, "Rock Chestplate", "And there they saw a rock.", 5000, false)),
    rockLeggings(new ArmorPiece(12, ArmorType.leggings, 0, "Rock Leggings", "It wasn't a rock.", 4000, false)),
    rockBoots(new ArmorPiece(13, ArmorType.boots, 0, "Rock Boots", "It was a rock lobster.", 2000, false)),
    rockArrow(new RockArrowItem(14,"Rock Arrow", "A stick with a rock on the end. A bit barbaric.", 25, true)),
    feather(new Feather(15,"Feather", "Because steel is heavier than feathers.", 20, true)),
    arrowShaft(new ArrowShaft(16, "Arrow Shaft", "Not quite the orange kind.", 10, true)),
    stringItem(new StringItem(17, "String", "Where does the string come from?", 5, false)),
    bowString(new BowString(18, "Bow String", "I can combine this with a bow stock.", 20, false)),
    ashBow(new BowWeapon(19, 1, 10, "Unstrung Ash Bow", "Tier 10", 100, false)),
    pineBow(new BowWeapon(20, 2, 20, "Unstrung Pine Bow", "Tier 20", 100, false)),
    oakBow(new BowWeapon(21, 3, 30, "Unstrung Oak Bow", "Tier 30", 100, false)),
    spruceBow(new BowWeapon(22, 4, 40, "Unstrung Spruce Bow", "Tier 40",100, false)),
    oakLog(new Log(23, 30, ItemList.oakBow, "Oak Wood", "Tier 30", 100, false)),
    pineLog(new Log(24, 20, ItemList.pineBow, "Pine Wood", "Tier 20", 100, false)),
    spruceLog(new Log(25, 40, ItemList.spruceBow, "Spruce Wood", "Tier 40",100, false)),
    ashLog(new Log(26, 10, ItemList.ashBow, "Ash Wood", "Tier 10",100, false)),
    seaWeed(new Seaweed(27, "Sea Weed", "Tasty",100, false)),
    stone(new Ore(28, 0, "Stone", "Tier 1",100, false)),
    tinOre(new Ore(29, 1, "Tin Ore", "Tier 10",100, false)),
    copperOre(new Ore(30, 2, "Copper Ore", "Tier 10",100, false)),
    ironOre(new Ore(31, 3, "Iron Ore", "Tier 20", 100, false)),
    goldOre(new Ore(32, 4, "Gold Ore", "Tier 30", 100, false)),
    crystalBow(new CrystalBow(33, "Crystal Bow", "It's shines when I hold it sometimes.", 100, false)),
    parrotBird(new QuestItem(34, 0, "Bird", "I bet the Bird Watcher would like one of these.", -1, false)),
    copperHelmet(new ArmorPiece(35, ArmorType.helmet, 1, "Copper Helmet", "Someone went under a hopper.", 1000, false)),
    copperChestplate(new ArmorPiece(36, ArmorType.chestplate, 1, "Copper Chestplate", "And there they saw a copper.", 5000, false)),
    copperLeggings(new ArmorPiece(37, ArmorType.leggings, 1, "Copper Leggings", "It wasn't a copper.",4000, false)),
    copperBoots(new ArmorPiece(38, ArmorType.boots, 1, "Copper Boots", "It was a copper lobster.",2000, false)),
    birdNest(new BirdNest(39,"Bird Nest", "It appears that the bird left some goodies.",-1, false)),
    fishBait(new FishBait(40,"Fish Bait", "This will make me fish faster.", 55, true)),
    kanunaNecklace(new AmuletOfKanuna(41, "Necklace of Kanuna", "Makes me a little bit stronger.",7500, false)),
    chickenShield(new ChickenShield(42, "Chicken Shield", "I can use this to protect myself.", 1000, false)),
    rainbowFish(new RainbowFish(43, "Rainbow Fish", "A sweet fruity snack. Unsure how it got in the waters.", 150, false)),
    tinHelmet(new ArmorPiece(44, ArmorType.helmet, 2, "Tin Helmet", "Tin Helmet", 1000, false)),
    tinChestplate(new ArmorPiece(45, ArmorType.chestplate, 2, "Tin Chestplate", "Tin Chestplate", 5000, false)),
    tinLeggings(new ArmorPiece(46, ArmorType.leggings, 2, "Tin Leggings", "Tin Leggings",4000, false)),
    tinBoots(new ArmorPiece(47, ArmorType.boots, 2, "Tin Boots", "Tin Boots",2000, false)),
    goldHelmet(new ArmorPiece(48, ArmorType.helmet, 3, "Tin Helmet", "Gold Helmet", 1000, false)),
    goldChestplate(new ArmorPiece(49, ArmorType.chestplate, 3, "Tin Chestplate", "Gold Chestplate", 5000, false)),
    goldLeggings(new ArmorPiece(50, ArmorType.leggings, 3, "Tin Leggings", "Gold Leggings",4000, false)),
    goldBoots(new ArmorPiece(51, ArmorType.boots, 3, "Tin Boots", "Gold Boots",2000, false)),
    bugRelic(new BugRelic(52, "Bug Relic", "A relic of the creepies down.",-1, false)),
    gradedFish(new GradedFish(53, "Graded Fish", "A fish worth some money.",100, false));

    private Item item;

    ItemList(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public int getID() {
        return item.getID();
    }

    public String getImage() {
        return item.getImageToken();
    }

    public String examineText() {
        return item.getExamineText();
    }

    public ItemStack singleStack() {
        return new ItemStack(this, 1, 0);
    }
}