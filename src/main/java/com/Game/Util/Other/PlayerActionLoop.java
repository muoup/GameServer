package com.Game.Util.Other;

import com.Game.Entity.Player.Player;

public class PlayerActionLoop {
    private Player player;
    private PlayerAction playerAction;
    private long loopTime;
    private long nextLoopTime;

    public PlayerActionLoop(Player player, PlayerAction playerAction, long loopTime) {
        this.player = player;
        this.playerAction = playerAction;
        this.loopTime = loopTime;
        this.nextLoopTime = System.currentTimeMillis() + loopTime;
    }

    public void update() {
        if (System.currentTimeMillis() >= nextLoopTime) {
            nextLoopTime = System.currentTimeMillis() + loopTime;
            if (!playerAction.onAction(player)) {
                player.endPlayerLoop();
            }
        }
    }

    protected void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    public PlayerAction getPlayerAction() {
        return playerAction;
    }

    public long getLoopTime() {
        return loopTime;
    }

    public void setLoopTime(long loopTime) {
        this.loopTime = loopTime;
    }
}
