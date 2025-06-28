package com.example.prodigy_ad_04;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private Button[][] buttons = new Button[3][3];
    private GameEngine engine = new GameEngine();
    private boolean isPlayerX = true;
    private String gameMode;
    private Handler[][] competitiveHandlers = new Handler[3][3];
    private Handler aiMoveHandler = new Handler(); // Handler for AI move delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameMode = getIntent().getStringExtra("GAME_MODE");
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
        if (engine.board[row][col] != ' ') return;

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
            Toast.makeText(this, symbol + " wins!", Toast.LENGTH_LONG).show();
            finish();
        } else if (engine.isDraw()) {
            Toast.makeText(this, "Draw!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            isPlayerX = !isPlayerX;
            if ("VS_AI".equals(gameMode) && !isPlayerX) {
                delayedAIMove(); // <-- use delayed move
            }
        }
    }

    private void delayedAIMove() {
        aiMoveHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                aiMove();
            }
        }, 1000); // 1000 milliseconds = 1 second delay
    }

    private void aiMove() {
        int[] move = AI.getBestMove(engine.board);
        if (move != null) {
            onCellClicked(move[0], move[1]);
        }
    }
}