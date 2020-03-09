package com.Game.Inventory;

import com.Game.PseudoData.ImageIdentifier;

/**
    Contains all of the references of items, used for creating ItemStacks
 */
public enum ItemList {
    empty(new Item(0, "/", "/", ImageIdentifier.emptyImage(), 0)),
    bow(new BowWeapon(1, "Bow", "Get ready for the power of my bow!", ImageIdentifier.spriteSheet("bow_sheet", 0, 0), 170)),
    log(new com.Game.Items.RawResource.Log.Log(2, 1, ItemList.bow, "Log", "The remnants of a tree.", 1, 150)),
    arrow(new com.Game.Items.Ammo.ArrowItem(3, "arrow.png", "Arrow", "Some sharp arrows!", 100000, 10)),
    mapleBow(new com.Game.Items.Weapon.Superclasses.BowWeapon(4, 5, 50, "Maple Bow", "This is really gonna hurt.", 1, 375)),
    clownfish(new com.Game.Items.Consumables.Food.ClownFishFood(5, "clownfish.png", "Clownfish", "This looks quite tasty.", 1, 100)),
    bluefish(new com.Game.Items.Consumables.Food.BlueFishFood(6, "bluefish.png", "Bluefish", "This looks very tasty.", 1, 250)),
    mapleLog(new com.Game.Items.RawResource.Log.Log(7, 50, ItemList.mapleBow, "Maple Log", "A sticky log, sounds useful to me.", 1, 350)),
    gold(new com.Game.Items.RawResource.Coins(8, "gold_coin.png", "Coins", "[amt] shiny coins, good for trading..?", 100000000, -1)),
    woodHarp(new com.Game.Items.Tool.WoodHarp(9, "wood_harp.png", "Wooden Harp", "I can get some coins from this. (35% effective)", 1, -1)),
    rockHelmet(new com.Game.Items.Armor.Rock.ArmorPiece(10, com.Game.Items.Armor.Rock.ArmorType.helmet, 1, "Rock Helmet", "Someone went under a dock.", 1, 1000)),
    rockChestplate(new com.Game.Items.Armor.Rock.ArmorPiece(11, com.Game.Items.Armor.Rock.ArmorType.chestplate, 1, "Rock Chestplate", "And there they saw a rock.", 1, 5000)),
    rockLeggings(new com.Game.Items.Armor.Rock.ArmorPiece(12, com.Game.Items.Armor.Rock.ArmorType.leggings, 1, "Rock Leggings", "It wasn't a rock.", 1, 4000)),
    rockBoots(new com.Game.Items.Armor.Rock.ArmorPiece(13, com.Game.Items.Armor.Rock.ArmorType.boots, 1, "Rock Boots", "It was a rock lobster.", 1, 2000)),
    rockArrow(new com.Game.Items.Ammo.RockArrowItem(14, "rock_arrow.png", "Rock Arrow", "It was a rock lobster.", 100000, 25)),
    feather(new com.Game.Items.RawResource.Feather(15, "feather.png", "Feather", "Because steel is heavier than feathers.", 100000, 20)),
    arrowShaft(new com.Game.Items.RawResource.ArrowShaft(16, "arrow_shaft.png", "Arrow Shaft", "Not quite the orange kind.", 100000, 10)),
    stringItem(new com.Game.Items.RawResource.StringItem(17, "string.png", "String", "Where does the string come from?", 100000, 5)),
    bowString(new com.Game.Items.RawResource.BowString(18, "bow_string.png", "Bow String", "I can combine this with a bow stock.", 1, 20)),
    ashBow(new com.Game.Items.Weapon.Superclasses.BowWeapon(19, 1, 10, "Ash Bow", "Tier 10", 1, 100)),
    pineBow(new com.Game.Items.Weapon.Superclasses.BowWeapon(20, 2, 20, "Pine Bow", "Tier 20", 1, 100)),
    oakBow(new com.Game.Items.Weapon.Superclasses.BowWeapon(21, 3, 30, "Oak Bow", "Tier 30", 1, 100)),
    spruceBow(new com.Game.Items.Weapon.Superclasses.BowWeapon(22, 4, 40, "Spruce Bow", "Tier 40", 1, 100)),
    oakLog(new com.Game.Items.RawResource.Log.Log(23, 30, ItemList.oakBow, "Oak Wood", "Tier 30", 1, 100)),
    pineLog(new com.Game.Items.RawResource.Log.Log(24, 20, ItemList.pineBow, "Pine Wood", "Tier 20", 1, 100)),
    spruceLog(new com.Game.Items.RawResource.Log.Log(25, 40, ItemList.spruceBow, "Spruce Wood", "Tier 40", 1, 100)),
    ashLog(new com.Game.Items.RawResource.Log.Log(26, 10, ItemList.ashBow, "Ash Wood", "Tier 10", 1, 100)),
    seaWeed(new com.Game.Items.Consumables.Food.Seaweed(27, "sea_weed.png", "Sea Weed", "Tasty", 1, 100)),
    stone(new com.Game.Items.RawResource.Ore(28, 0, "Stone", "Tier 1", 1, 100)),
    copperOre(new com.Game.Items.RawResource.Ore(29, 2, "Copper Ore", "Tier 10", 1, 100)),
    tinOre(new com.Game.Items.RawResource.Ore(30, 1, "Tin Ore", "Tier 10", 1, 100)),
    ironOre(new com.Game.Items.RawResource.Ore(31, 3, "Iron Ore", "Tier 20", 1, 100)),
    goldOre(new com.Game.Items.RawResource.Ore(32, 4, "Gold Ore", "Tier 30", 1, 100)),
    crystalBow(new com.Game.Items.Weapon.Bows.CrystalBow(33, "Crystal Bow", "It's shines when I hold it sometimes.", 1, 100)),
    stoneDagger(new com.Game.Items.Weapon.Superclasses.DaggerWeapon(34, 0, 1, "Stone Dagger", "Dull, but effective.", 1, 100)),
    bronzeDagger(new com.Game.Items.Weapon.Superclasses.DaggerWeapon(35, 1, 10, "Bronze Dagger", "Dull, but effective.", 1, 100)),
    ironDagger(new com.Game.Items.Weapon.Superclasses.DaggerWeapon(36, 2, 20, "Iron Dagger", "Dull, but effective.", 1, 100)),
    goldDagger(new com.Game.Items.Weapon.Superclasses.DaggerWeapon(37, 3, 30, "Gold Dagger", "Dull, but effective.", 1, 100)),
    skyriteDagger(new com.Game.Items.Weapon.Superclasses.DaggerWeapon(38, 4, 40, "Skyrite Dagger", "Dull, but effective.", 1, 100)),
    parrotBird(new com.Game.Items.Misc.QuestItem(39, 0, "Bird", "I bet the Bird Watcher would like one of these.", 1, -1)),
    bronzeHelmet(new com.Game.Items.Armor.Rock.ArmorPiece(40, com.Game.Items.Armor.Rock.ArmorType.helmet, 10, "Bronze Helmet", "Someone went under a hopper.", 1, 1000)),
    bronzeChestplate(new com.Game.Items.Armor.Rock.ArmorPiece(41, com.Game.Items.Armor.Rock.ArmorType.chestplate, 10, "Bronze Chestplate", "And there they saw a copper.", 1, 5000)),
    bronzeLeggings(new com.Game.Items.Armor.Rock.ArmorPiece(42, com.Game.Items.Armor.Rock.ArmorType.leggings, 10, "Bronze Leggings", "It wasn't a copper.", 1, 4000)),
    bronzeBoots(new com.Game.Items.Armor.Rock.ArmorPiece(43, com.Game.Items.Armor.Rock.ArmorType.boots, 10, "Bronze Boots", "It was a copper lobster.", 1, 2000)),
    birdNest(new BirdNest(44, "bird_nest.png", "Bird Nest", "It appears that the bird left some goodies.", 1, -1)),
    fishBait(new Item(45, "fish_bait.png", "Fish Bait", "This will make me fish faster.", 25000000, 55)),
    kanunaNecklace(new com.Game.Items.Armor.Tribal.AmuletOfKanuna(46, "necklacePower.png", "Necklace of Kanuna", "Makes me a little bit stronger.", 1, 7500));

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

    public String itemName() {
        return item.getName();
    }

    public String examineText() {
        return item.getExamineText();
    }
}