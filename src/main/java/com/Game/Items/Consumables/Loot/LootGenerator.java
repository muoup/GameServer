package com.Game.Items.Consumables.Loot;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.Items.DropTable;
import com.Game.Items.Item;
import com.Game.Items.ItemStack;

import java.util.ArrayList;

public class LootGenerator extends Item {
    protected DropTable table;

    public LootGenerator(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        table = new DropTable();
    }

    public void OnClick(int index) {
        getItems().forEach(InventoryManager::addItem);
        InventoryManager.addAmount(index, -1);
    }

    protected void setTable(DropTable table) {
        this.table = table;
    }

    protected void resetTable() {
        table.wipe();
    }

    public ArrayList<ItemStack> getItems() {
        return table.determineOutput();
    }
}
