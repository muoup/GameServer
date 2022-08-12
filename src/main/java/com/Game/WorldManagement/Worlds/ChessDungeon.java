package com.Game.WorldManagement.Worlds;

import com.Game.Entity.Enemy.ChessDungeon.Bishop;
import com.Game.Entity.Enemy.ChessDungeon.Knight;
import com.Game.Entity.Enemy.ChessDungeon.Pawn;
import com.Game.Entity.Enemy.ChessDungeon.Rook;
import com.Game.Objects.AreaTeleporter.InvisibleTeleporter;
import com.Game.Objects.AreaTeleporter.Ladder;
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
        new Pawn(this, 902, 212, true);
        new Pawn(this, 998, 308, false);

        new Knight(this, 1678, 606, true);
        new Knight(this, 1774, 802, false);

        new Bishop(this, 604, 1438, true);
        new Bishop(this, 700, 1438, false);

        new Ladder(this, 1330, 1438, WorldHandler.queenLair, 725, 135);

        new Rook(this, 0, true);
        new Rook(this, 1, false);
        new Rook(this, 2, true);
        new Rook(this, 3, false);
    }
}
