package com.example.kursachoop.ui.Users.Catalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.kursachoop.R;
import com.example.kursachoop.ui.Users.Bin.BinActivity;
import com.example.kursachoop.ui.Users.Home.HomeActivity;
import com.example.kursachoop.ui.Users.Profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CatalogActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.catalogActivity);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeActivity) {
                    item.setIcon(R.drawable.home_sel);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.catalogActivity) {
                    item.setIcon(R.drawable.catalog_sel);
                    return true;
                } else if (itemId == R.id.binActivity) {
                    item.setIcon(R.drawable.bin_sel);
                    startActivity(new Intent(getApplicationContext(), BinActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.profileActivity) {
                    item.setIcon(R.drawable.profile_sel);
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}