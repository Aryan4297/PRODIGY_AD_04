package com.example.prodigy_ad_04;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStandard = findViewById(R.id.btnStandard);
        Spinner spinnerStandardSets = findViewById(R.id.spinnerStandardSets);
        btnStandard.setOnClickListener(v -> {
            int sets = Integer.parseInt(spinnerStandardSets.getSelectedItem().toString());
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("GAME_MODE", "STANDARD");
            intent.putExtra("GAME_SETS", sets);
            startActivity(intent);
        });

        Button btnCompetitive = findViewById(R.id.btnCompetitive);
        Spinner spinnerCompetitiveSets = findViewById(R.id.spinnerCompetitiveSets);
        btnCompetitive.setOnClickListener(v -> {
            int sets = Integer.parseInt(spinnerCompetitiveSets.getSelectedItem().toString());
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("GAME_MODE", "COMPETITIVE");
            intent.putExtra("GAME_SETS", sets);
            startActivity(intent);
        });

        Button btnVsAI = findViewById(R.id.btnVsAI);
        Spinner spinnerVsAISets = findViewById(R.id.spinnerVsAISets);
        btnVsAI.setOnClickListener(v -> {
            int sets = Integer.parseInt(spinnerVsAISets.getSelectedItem().toString());
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("GAME_MODE", "VS_AI");
            intent.putExtra("GAME_SETS", sets);
            startActivity(intent);
        });
    }
}