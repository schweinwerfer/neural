package de.ora.tictactoe.genetic;

import de.ora.tictactoe.Board;
import de.ora.tictactoe.Coordinate;
import de.ora.tictactoe.Player;

public class DynamicPlayingAgent extends PlayingAgent {
    public DynamicPlayingAgent() {
    }

    public DynamicPlayingAgent(String name) {
        super(name);
    }

    public DynamicPlayingAgent(PlayingAgent father, PlayingAgent mother) {
        super(father, mother);
    }

    @Override
    public Coordinate play(Player player, Board board) {
        int playedPiecesCount = board.getPlayedPiecesCount();
        if (player == Player.PLAYER1 && playedPiecesCount == 0) {
            return createRandomMove(board);
        } else if (player == Player.PLAYER2 && playedPiecesCount <= 1) {
            return createRandomMove(board);
        } else {
            return super.play(player, board);
        }
    }
}
