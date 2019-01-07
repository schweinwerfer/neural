package de.ora.tictactoe.genetic;

import de.ora.tictactoe.Board;
import de.ora.tictactoe.Coordinate;
import de.ora.tictactoe.GameResult;
import de.ora.tictactoe.Player;

import java.util.*;

public class PlayingAgent {
    private String name;
    private int generation;
    private int score = 0;
    private Map<String, List<Coordinate>> moves = new HashMap<>();
    private Random rnd = new Random();
    private int winCnt = 0;
    private int loseCnt = 0;
    private int drawCnt = 0;

    public PlayingAgent(String name) {
        this.name = name;
        this.generation = 1;
    }

    public PlayingAgent(final PlayingAgent father, final PlayingAgent mother) {
        this.generation = ((father.generation + mother.generation) / 2) + 1;
        this.name = Math.abs((father.name + mother.name).hashCode()) + "-" + this.generation;

        int fatherMoveCnt = father.moves.size();
        int motherMoveCnt = mother.moves.size();

        if (fatherMoveCnt >= motherMoveCnt) {
//            this.moves.putAll(pickRandomlyFrom(mother.moves, motherMoveCnt / 2));
            this.moves.putAll(pickLinearFrom(mother.moves, motherMoveCnt / 2));
            fillWithMissingMoves(this.moves, father.moves);
        } else {
//            this.moves.putAll(pickRandomlyFrom(father.moves, fatherMoveCnt / 2));
            this.moves.putAll(pickLinearFrom(father.moves, fatherMoveCnt / 2));
            fillWithMissingMoves(this.moves, mother.moves);
        }

        if (rnd.nextInt(100) % 42 == 0) {
//            System.out.println("xxx mutation");
            Set<String> keysSet = this.moves.keySet();
            List<String> keys = new ArrayList<String>();
            keys.addAll(keysSet);
            this.moves.remove(keys.get(rnd.nextInt(keys.size())));
        }

    }

    public Coordinate play(final Player player, final Board board) {
        if (board.activePlayer() != player) {
            throw new IllegalArgumentException("Player " + player + " is not active");
        }

        final String key = board.asKey();
        List<Coordinate> possibleMoves = moves.get(key);
        if (possibleMoves == null) {
            possibleMoves = new ArrayList<>();
            moves.put(key, possibleMoves);
        }

        if (possibleMoves.isEmpty()) {
            List<Coordinate> freeCells = board.freeCells();
            if (freeCells.size() > 0) {
                // choose randomly one move
                possibleMoves.add(freeCells.get(rnd.nextInt(freeCells.size())));
            } else {
                throw new IllegalArgumentException("No free cell left on board for move!");
            }
        }

        return possibleMoves.get(0);
    }

    public void feedback(final GameResult result, int score) {
        switch (result) {
            case WON:
                this.winCnt++;
                break;
            case LOST:
                this.loseCnt++;
                break;
            case DRAW:
                this.drawCnt++;
                break;
        }
        this.score += score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getDetailedScore() {
        StringBuilder sb = new StringBuilder();
        sb.append(score).append(" (w: ").append(winCnt).append(", l: ").append(loseCnt).append(", d: ").append(drawCnt).append(")");
        return sb.toString();
    }

    public void resetScore() {
        this.score = 0;
        this.winCnt = 0;
        this.loseCnt = 0;
        this.drawCnt = 0;
    }

    private void fillWithMissingMoves(Map<String, List<Coordinate>> kidsMoves, Map<String, List<Coordinate>> parentMoves) {
        int parentMovesSize = parentMoves.size();
        int kidsMovesSize = kidsMoves.size();

        if (parentMovesSize <= kidsMovesSize) {
            return;
        }

        while (kidsMoves.size() < parentMovesSize) {
            for (Map.Entry<String, List<Coordinate>> entry : parentMoves.entrySet()) {
                if (!kidsMoves.containsKey(entry.getKey())) {
                    kidsMoves.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private Map<String, List<Coordinate>> pickRandomlyFrom(Map<String, List<Coordinate>> map, int count) {
        Map<String, List<Coordinate>> result = new HashMap<>();

        List<Map.Entry<String, List<Coordinate>>> entries = new ArrayList<>(map.entrySet());

        while (result.size() < count) {
            Map.Entry<String, List<Coordinate>> picked = entries.get(rnd.nextInt(entries.size()));
            if (!result.containsKey(picked.getKey())) {
                result.put(picked.getKey(), picked.getValue());
            }
        }

        return result;
    }

    private Map<String, List<Coordinate>> pickLinearFrom(Map<String, List<Coordinate>> map, int count) {
        Map<String, List<Coordinate>> result = new HashMap<>();

        List<Map.Entry<String, List<Coordinate>>> entries = new ArrayList<>(map.entrySet());

        for (int i = 0; i < count; i++) {
            Map.Entry<String, List<Coordinate>> picked = entries.get(i);
            if (!result.containsKey(picked.getKey())) {
                result.put(picked.getKey(), picked.getValue());
            }
        }

        return result;
    }

    public int getGeneration() {
        return generation;
    }
}
