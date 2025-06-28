package com.example.prodigy_ad_04;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private final Button[][] buttons = new Button[3][3];
    private final GameEngine engine = new GameEngine();
    private boolean isPlayerX = true;
    private String gameMode;
    private final Handler[][] competitiveHandlers = new Handler[3][3];
    private final Handler aiMoveHandler = new Handler(); // Handler for AI move delay

    // Set/match logic
    private int totalSets; // Number of sets to play (e.g., 3, 5, 7)
    private int playerXSetsWon = 0;
    private int playerOSetsWon = 0;
    private int requiredSetsToWin; // e.g. 2 for best of 3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameMode = getIntent().getStringExtra("GAME_MODE");
        totalSets = getIntent().getIntExtra("GAME_SETS", 1);
        requiredSetsToWin = (totalSets / 2) + 1;

        initButtons();
    }

    private void initButtons() {
        int[][] ids = {
                { R.id.btn00, R.id.btn01, R.id.btn02 },
                { R.id.btn10, R.id.btn11, R.id.btn12 },
                { R.id.btn20, R.id.btn21, R.id.btn22 }
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = findViewById(ids[i][j]);
                final int x = i, y = j;
                buttons[i][j].setOnClickListener(v -> onCellClicked(x, y));
            }
        }
    }

    private void onCellClicked(int row, int col) {
        if (engine.board[row][col] != ' ' || engine.isGameOver()) return;

        char symbol = isPlayerX ? 'X' : 'O';
        engine.board[row][col] = symbol;
        buttons[row][col].setText(String.valueOf(symbol));

        if ("COMPETITIVE".equals(gameMode)) {
            Handler handler = new Handler();
            competitiveHandlers[row][col] = handler;
            handler.postDelayed(() -> {
                if (engine.board[row][col] == symbol && !engine.isGameOver()) {
                    engine.board[row][col] = ' ';
                    buttons[row][col].setText("");
                }
            }, 5000);
        }

        if (engine.checkWin(symbol)) {
            onGameFinished(symbol);
        } else if (engine.isDraw()) {
            onGameFinished('D');
        } else {
            isPlayerX = !isPlayerX;
            if ("VS_AI".equals(gameMode) && !isPlayerX) {
                delayedAIMove();
            }
        }
    }

    // Call after each game ends
    private void onGameFinished(char winner) {
        if (winner == 'X') {
            playerXSetsWon++;
            Toast.makeText(this, "Player X wins this set!", Toast.LENGTH_SHORT).show();
        } else if (winner == 'O') {
            playerOSetsWon++;
            Toast.makeText(this, "Player O wins this set!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        }
        checkMatchWinner();
    }

    // Check if anyone has won the match
    private void checkMatchWinner() {
        if (playerXSetsWon >= requiredSetsToWin) {
            showOverallWinner('X');
        } else if (playerOSetsWon >= requiredSetsToWin) {
            showOverallWinner('O');
        } else {
            resetBoardForNextSet();
        }
    }

    private void showOverallWinner(char winner) {
        String msg = (winner == 'X')
                ? "Player X wins the match!\nSets: " + playerXSetsWon + " - " + playerOSetsWon
                : "Player O wins the match!\nSets: " + playerOSetsWon + " - " + playerXSetsWon;
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        // Optionally, show a dialog or navigate back to main screen after a delay
        finish();
    }

    private void resetBoardForNextSet() {
        // Reset your game board UI and state for the next set/game
        engine.reset();
        isPlayerX = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        // If VS_AI mode and O starts, trigger AI move
        if ("VS_AI".equals(gameMode) && !isPlayerX) {
            delayedAIMove();
        }
    }

    private void delayedAIMove() {
        aiMoveHandler.postDelayed(this::aiMove, 1000);
    }

    private void aiMove() {
        int[] move = AI.getBestMove(engine.board);
        if (move != null) {
            onCellClicked(move[0], move[1]);
        }
    }
}