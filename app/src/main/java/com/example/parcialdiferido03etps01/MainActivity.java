package com.example.parcialdiferido03etps01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.ic_carros);
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_item_1:
                Intent intent1 = new Intent(this, Clientes.class);
                startActivity(intent1);
                return true;
            case R.id.navigation_item_2:
                Intent intent2 = new Intent(this, Vehiculos.class);
                startActivity(intent2);
                return true;
            case R.id.navigation_item_3:
                Intent intent3 = new Intent(this, Vehiculos_Clientes.class);
                startActivity(intent3);
                return true;

        }
        return false;
    }
}

