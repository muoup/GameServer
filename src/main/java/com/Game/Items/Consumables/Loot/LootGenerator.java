package com.Game.Items.Consumables.Loot;

import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.ItemData.DropTable;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;

import java.util.ArrayList;

public class LootGenerator extends Item {
    protected DropTable table;

    public LootGenerator(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
        table = new DropTable();
    }

    public void OnClick(Player player, int index) {
        getItems().forEach(player.inventory::addItem);
        player.inventory.addAmount(index, -1);
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
