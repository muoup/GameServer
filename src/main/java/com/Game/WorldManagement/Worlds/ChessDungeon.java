package com.Game.WorldManagement.Worlds;

import com.Game.Entity.Enemy.ChessDungeon.Pawn;
import com.Game.Objects.AreaTeleporter.InvisibleTeleporter;
import com.Game.WorldManagement.World;
import com.Game.WorldManagement.WorldHandler;

public class ChessDungeon extends World {
    public ChessDungeon(int id) {
        super(id);
        initImage("chess_dungeon.png");
    }

    public void worldCreation() {
        // Teleporter to the Outside
        new InvisibleTeleporter(this, 135, 125, WorldHandler.main, 5507, 1643);

        // Chessboard Pieces
        new Pawn(this,502, 122, true);
        new Pawn(this,598, 122, false);
    }

    public void update() {

    }
}
