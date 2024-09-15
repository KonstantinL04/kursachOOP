package com.example.kursachoop.ui.Admin.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursachoop.R;
import com.example.kursachoop.ui.Admin.Home.Category.AdminHomeActivity;
import com.example.kursachoop.ui.LoginActivity;
import com.example.kursachoop.ui.Users.Profile.ProfileSettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import io.paperdb.Paper;

public class AdminProfileActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    private RelativeLayout exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        exit = findViewById(R.id.exit);
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.profileActivity);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Intent logOutIntent = new Intent(AdminProfileActivity.this, LoginActivity.class);
                startActivity(logOutIntent);
            }
        });
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeActivity) {
                    item.setIcon(R.drawable.home_sel);
                    startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                    return true;
                } else if (itemId == R.id.profileActivity) {
                    item.setIcon(R.drawable.profile_sel);
                    return true;
                }
                return false;
            }
        });
    }
}
