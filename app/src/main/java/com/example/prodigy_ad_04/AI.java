package com.example.prodigy_ad_04;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI {
    public static int[] getBestMove(char[][] board) {
        // Win if possible
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ') {
                    board[i][j] = 'O';
                    if (new GameEngineCopy(board).checkWin('O')) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
        // Block if necessary
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ') {
                    board[i][j] = 'X';
                    if (new GameEngineCopy(board).checkWin('X')) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
        // Random move
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ') moves.add(new int[]{i, j});
        if (moves.isEmpty()) return null;
        return moves.get(new Random().nextInt(moves.size()));
    }

    // Helper class to avoid mutating original board
    static class GameEngineCopy extends GameEngine {
        public GameEngineCopy(char[][] src) {
            for (int i = 0; i < 3; i++)
                System.arraycopy(src[i], 0, board[i], 0, 3);
        }
    }
}