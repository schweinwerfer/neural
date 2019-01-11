package de.ora.tictactoe.genetic;

import de.ora.tictactoe.Board;
import de.ora.tictactoe.Coordinate;
import de.ora.tictactoe.Player;

public class InstantLoseFilter implements AgentPlayFilter {
    @Override
    public Coordinate filter(final Board board) {
        Player player = board.activePlayer();
        return board.findWinningMoveForPlayer(player == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1);
    }
}
