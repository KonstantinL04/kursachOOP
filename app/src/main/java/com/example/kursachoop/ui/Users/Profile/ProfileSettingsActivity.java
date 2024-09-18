package com.example.kursachoop.ui.Users.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursachoop.R;
import com.example.kursachoop.ui.LoginActivity;
import com.example.kursachoop.ui.Users.Bin.BinActivity;
import com.example.kursachoop.ui.Users.Home.Category.HomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import io.paperdb.Paper;

public class ProfileSettingsActivity extends AppCompatActivity {
    private ImageView BtnBack;
    private RelativeLayout Btn_exit_settings;
    private BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Btn_exit_settings = (RelativeLayout) findViewById(R.id.exit_settings);
        BtnBack = (ImageView) findViewById(R.id.btn_back);
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.profileActivity);
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent(ProfileSettingsActivity.this, ProfileActivity.class);
                startActivity(closeIntent);
            }
        });

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeActivity) {
                    item.setIcon(R.drawable.home_sel);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.binActivity) {
                    item.setIcon(R.drawable.bin_sel);
                    startActivity(new Intent(getApplicationContext(), BinActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.profileActivity) {
                    item.setIcon(R.drawable.profile_sel);
//                    userNameTextView.setText(Prevalent.currentOnlineUser.getName());
                    return true;
                }
                return false;
            }
        });

        Btn_exit_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Intent logOutIntent = new Intent(ProfileSettingsActivity.this, LoginActivity.class);
                startActivity(logOutIntent);
            }
        });
    }
}
