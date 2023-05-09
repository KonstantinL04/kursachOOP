package com.example.kursachoop.ui.Users.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.kursachoop.R;
import com.example.kursachoop.ui.Users.Bin.BinActivity;
import com.example.kursachoop.ui.Users.Catalog.CatalogActivity;
import com.example.kursachoop.ui.Users.Home.HomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    private Button btnLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnLogOut = (Button) findViewById(R.id.button);

        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.profileActivity);

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
                    startActivity(new Intent(getApplicationContext(), BinActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.profileActivity) {
                    return true;
                }
                return false;
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Intent logOutIntent = new Intent(ProfileActivity.this, HomeActivity.MainActivity.class);
                startActivity(logOutIntent);
            }
        });


    }
}