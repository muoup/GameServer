package com.Game.Items.Tool;

import com.Game.CustomInterfaces.HarpShop;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Other.RCOption;

public class WoodHarp extends Usable {
    private HarpShop shop;

    public WoodHarp(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
        setImage("wood_harp.png");
    }

    public void dataItemChange(ItemStack stack) {
        this.shop = new HarpShop(new ItemStack[] { new ItemStack(ItemList.empty, 0) }, 0.35f);

        stack.setOptions(new RCOption("Open Harp Shop", (player, index) -> player.enableShop(shop)));
    }
}
