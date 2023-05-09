package com.example.kursachoop.ui.Users.Bin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.kursachoop.R;
import com.example.kursachoop.ui.Users.Catalog.CatalogActivity;
import com.example.kursachoop.ui.Users.Home.HomeActivity;
import com.example.kursachoop.ui.Users.Profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BinActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin);

        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.binActivity);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeActivity) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.catalogActivity) {
                    startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.binActivity) {
                    return true;
                } else if (itemId == R.id.profileActivity) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}