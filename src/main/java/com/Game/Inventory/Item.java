package com.Game.Inventory;

import com.Game.Player.Player;
import com.Game.PseudoData.ImageIdentifier;

/**
 * Contains the functionality of an item, one instance of this is held in an ItemList
 */
public class Item {
    protected int equipStatus;
    protected double armor;
    protected float damageMulti;
    protected ItemRequirement requirement;

    public int id;
    public int worth;
    private String name, examineText;
    private ImageIdentifier image;

    public Item(int id, String name, String examineText, ImageIdentifier image, int worth) {
        this.id = id;
        this.worth = worth;
        this.name = name;
        this.examineText = examineText;
        this.image = image;
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

    public void onClick(Player player, int index) {

    }

    public void onOptionUse() {

    }
}
