package com.Game.Entity.NPC;


import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class NPC {

    // NOTE: Use id for comparing NPCs rather than comparing the objects themselves.
    public int id;

    protected ImageIdentifier image;
    public Vector2 position;
    public World world;

    public NPC(int id, World world, int x, int y) {
        this.id = id;

        position = new Vector2(x, y);
        image = null;

        world.npcs.add(this);
    }

    public void setImage(String imageName) {
        this.image = ImageIdentifier.singleImage("Entities/NPCs/" + imageName);
    }

    public void update() {
        move();
    }

    // When the npc is being interacted with, do so-and-so
    public void onInteract(Player player) {

    }

    // Update the NPC's position with a movement AI
    public void move() {

    }
}
