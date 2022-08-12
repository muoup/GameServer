package com.Game.Entity.NPC;


import com.Game.ConnectionHandling.Client;
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

    public NPC(World world, int x, int y) {
        this.world = world;
        this.id = getID();

        position = new Vector2(x, y);
        image = null;

        world.npcs.add(this);
    }

    public static void choose(Player chooser, int npcID, String choice) {
        World playerWorld = chooser.getWorld();

        for (NPC npc : playerWorld.npcs) {
            if (npc.id == npcID) {
                npc.choiceChosen(chooser, choice);
            }
        }
    }

    private int getID() {
        int id;

        do {
            id = (int) (Math.random() * Integer.MAX_VALUE);
        } while (IDExists(id));

        return id;
    }

    private boolean IDExists(int id) {
        for (NPC npc : world.npcs)
            if (npc.id == id)
                return true;
        return false;
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

    public String packetInfo() {
        return position + ":" + image.getToken();
    }

    public void choiceChosen(Player player, String message) { }
}
