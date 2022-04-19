package com.Game.Util.Other;

import com.Game.Entity.Player.Player;

public class RCOption {
    private String option;
    private ItemAction action;

    public RCOption(String option, ItemAction action) {
        this.option = option;
        this.action = action;
    }

    public String getOption() {
        return option;
    }

    public void run(Player player, int index) {
        action.run(player, index);
    }
}
