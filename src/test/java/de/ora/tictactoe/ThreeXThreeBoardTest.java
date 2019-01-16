package de.ora.tictactoe;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class ThreeXThreeBoardTest {

    @Test
    public void basicChecks() {
        ThreeXThreeBoard board = new ThreeXThreeBoard();
        List<Coordinate> freeCells = board.freeCells();
        Assert.assertEquals(9, freeCells.size());

        Coordinate coordinate = pickRandomItem(freeCells);
        Assert.assertTrue(board.activePlayer() == Player.PLAYER1);
        boolean valid = board.set(coordinate.row, coordinate.column);
        Assert.assertTrue(valid);
        Assert.assertTrue(board.activePlayer() == Player.PLAYER2);

        freeCells = board.freeCells();
        Assert.assertEquals(8, freeCells.size());

        valid = board.set(coordinate.row, coordinate.column);
        Assert.assertFalse(valid);

    }

    @Test
    public void findWinnerCheck() {
        ThreeXThreeBoard board = new ThreeXThreeBoard();
        Assert.assertTrue(board.activePlayer() == Player.PLAYER1);
        board.set(0, 0);
        board.set(1, 0);
        Assert.assertEquals(Player.NONE, board.findWinner());
        board.set(1, 1);
        board.set(2, 0);
        board.set(2, 2);
        Assert.assertEquals(Player.PLAYER1, board.findWinner());
        System.out.println(board);

        board.reset();
        Assert.assertTrue(board.activePlayer() == Player.PLAYER1);
        board.set(0, 2);
        board.set(1, 0);
        Assert.assertEquals(Player.NONE, board.findWinner());
        board.set(1, 1);
        board.set(2, 2);
        board.set(2, 0);
        Assert.assertEquals(Player.PLAYER1, board.findWinner());
        System.out.println(board);


        board.reset();
        Assert.assertTrue(board.activePlayer() == Player.PLAYER1);
        board.set(0, 1);//1
        board.set(0, 0);
        Assert.assertEquals(Player.NONE, board.findWinner());
        board.set(1, 2);// 1
        board.set(1, 0);
        Assert.assertEquals(Player.NONE, board.findWinner());
        board.set(2, 0);// 1
        Assert.assertEquals(Player.NONE, board.findWinner());
        System.out.println(board);

        board.reset();
        Assert.assertTrue(board.activePlayer() == Player.PLAYER1);
        board.set(0, 1);//1
        board.set(0, 0);
        Assert.assertEquals(Player.NONE, board.findWinner());
        board.set(1, 1);// 1
        board.set(1, 0);
        Assert.assertEquals(Player.NONE, board.findWinner());
        board.set(2, 0);// 1
        Assert.assertEquals(Player.NONE, board.findWinner());
        System.out.println(board);

        board.reset();
        Assert.assertTrue(board.activePlayer() == Player.PLAYER1);
        board.set(0, 0);//1
        board.set(0, 1);//2
        board.set(0, 2);//1
        board.set(1, 0);//2
        board.set(1, 2);//1
        board.set(2, 1);//2
        board.set(2, 2);//1
        Assert.assertEquals(Player.PLAYER1, board.findWinner());
        System.out.println(board);

        board.reset();
        Assert.assertTrue(board.activePlayer() == Player.PLAYER1);
        board.set(0, 0);//1
        board.set(0, 1);//2
        board.set(0, 2);//1
        board.set(1, 0);//2
        board.set(1, 1);//1
        board.set(2, 0);//2
        board.set(2, 2);//1
        Assert.assertEquals(Player.PLAYER1, board.findWinner());
        System.out.println(board);
    }

    @Test
    public void testToFromKey() {
        Board board = new ThreeXThreeBoard();
        String key = board.asKey();
        Board createdBoard = Board.fromKey(key);

        Assert.assertEquals(board, createdBoard);
        Assert.assertEquals(board.activePlayer(), createdBoard.activePlayer());

        board = new ThreeXThreeBoard();
        board.board.initWith(3);
        key = board.asKey();
        createdBoard = Board.fromKey(key);

        Assert.assertEquals(board, createdBoard);
        Assert.assertEquals(board.activePlayer(), createdBoard.activePlayer());

        board = new ThreeXThreeBoard();
        board.set(new Coordinate(0, 0));
        key = board.asKey();
        createdBoard = Board.fromKey(key);

        Assert.assertEquals(board, createdBoard);
        Assert.assertEquals(board.activePlayer(), createdBoard.activePlayer());
    }

    public <T> T pickRandomItem(List<T> items) {
        return items.get(new Random().nextInt(items.size()));
    }
}