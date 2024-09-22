package com.example.kursachoop.ui.Users.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Users.Cart.CartActivity;
import com.example.kursachoop.ui.Users.Home.Category.HomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class ProfileActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    private LinearLayout btn_settings;
    private TextView userNameTextView, phoneTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userPhone = Prevalent.currentOnlineUser.getPhone();
        String userName = Prevalent.currentOnlineUser.getName();

        btn_settings = (LinearLayout) findViewById(R.id.btn_settings);
        userNameTextView = findViewById(R.id.profileName);
        phoneTextView = findViewById(R.id.profilePhone);
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.profileActivity);

        userNameTextView.setText(userName);
        phoneTextView.setText(userPhone);

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(ProfileActivity.this, ProfileSettingsActivity.class);
                startActivity(settingIntent);
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
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    finish();
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