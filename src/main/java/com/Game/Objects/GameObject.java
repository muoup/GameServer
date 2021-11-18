package com.Game.Objects;

import com.Game.ConnectionHandling.Client;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class GameObject {
    public Vector2 position;
    public ImageIdentifier image;

    protected boolean canInteract = true;

    protected World world;

    public float maxDistance;

    protected Vector2 scale;
    protected float maxTimer = 10f;

    public GameObject(World world, int x, int y) {
        this.position = new Vector2(x, y);
        this.world = world;

        world.objects.add(this);
    }

    public void update() {

    }

    public void loseFocus() {

    }

    public static void checkSingleInteract() {

    }

    public boolean onInteract(Player player) {
        return false;
    }

    public void initInteraction(Player player) {
        player.objectInteration = this;
        player.completionTime = getMillisTimer(player) + System.currentTimeMillis();

        Client.sendObjectInteraction(player, player.completionTime);
    }

    public int getMillisTimer(Player player) {
        return 0;
    }

    public void setScale(int x, int y) {
        this.scale = new Vector2(x, y);
    }

    public void setImage(String root) {
        this.image = ImageIdentifier.singleImage("/Objects/" + root);

        Client.sendObjectUpdate(this);
    }

    public World getWorld() {
        return world;
    }

    public String packetInfo() {
        return position + ":" + image;
    }
}
