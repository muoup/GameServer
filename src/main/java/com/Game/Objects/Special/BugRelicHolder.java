package com.Game.Objects.Special;

import com.Game.ConnectionHandling.Client;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.Objects.GameObject;
import com.Game.Util.Other.SpriteSheet;
import com.Game.WorldManagement.World;

public class BugRelicHolder extends GameObject {
    private long nextRelic = 0;
    private boolean hasRelic = true;

    private SpriteSheet sheet = new SpriteSheet("Objects/bug_relic.png", 64, 64);

    public BugRelicHolder(World world, int x, int y) {
        super(world, x, y);

        setImage(sheet.getCell(0, 0));
        setScale(64, 64);
    }

    public void update() {
        if (!hasRelic && System.currentTimeMillis() > nextRelic) {
            hasRelic = true;
            setImage(sheet.getCell(0, 0));
        }
    }

    public boolean onInteract(Player player) {
        if (hasRelic) {
            player.inventory.addItem(ItemList.bugRelic, 1, System.currentTimeMillis());
            setImage(sheet.getCell(1, 0));
            hasRelic = false;
            nextRelic = System.currentTimeMillis() + 120000;
            Client.sendText(player,
                    "You feel your heart grow shaky as you pick up the relic."
                    + " Your steps feel much slower and the matter that makes up the relic itself seems fragile."
                    + " You better be careful and get this back to the surface as soon as possible.");
        }

        return true;
    }
}
