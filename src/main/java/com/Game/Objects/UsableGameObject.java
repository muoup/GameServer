package com.Game.Objects;

import com.Game.WorldManagement.World;

import java.util.ArrayList;
import java.util.Arrays;

public class UsableGameObject extends GameObject {
    public ArrayList<String> options;

    public UsableGameObject(World world, int x, int y) {
        super(world, x, y);
        options = new ArrayList();
    }

    public void addOptions(String... opts) {
        options.addAll(Arrays.asList(opts));
    }

    public void onOption(int option) {

    }

    public void onRightClick() {

    }

    public void loseFocus() {

    }
}
