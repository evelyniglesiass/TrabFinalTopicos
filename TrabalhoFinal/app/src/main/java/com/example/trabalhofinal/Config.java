package com.example.trabalhofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Config extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem i) {
                return selectedItemNavigationView(i);
            }
        });
    }

    private boolean selectedItemNavigationView(MenuItem item) {
        int i = item.getItemId();

        if (i == R.id.sair) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (i == R.id.config) {
            startActivity(new Intent(this, Config.class));
        } else if (i == R.id.deputados) {
            startActivity(new Intent(this, HomeDeputados.class));
        } else if (i == R.id.partidos) {
            startActivity(new Intent(this, HomePartidos.class));
        }

        return false;
    }
}