package de.ora.tictactoe.genetic;

import de.ora.tictactoe.Board;
import de.ora.tictactoe.Coordinate;

public interface AgentPlayFilter {
    Coordinate filter(final Board board);
}
