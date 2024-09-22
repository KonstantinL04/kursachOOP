package com.example.kursachoop.ui.Admin.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Admin.Home.Category.AdminHomeActivity;
import com.example.kursachoop.ui.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import io.paperdb.Paper;

public class AdminProfileActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    private RelativeLayout exit;
    private TextView adminsNameTextView, adminsphoneTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        exit = findViewById(R.id.exit);
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.profileActivity);

        String adminsPhone = Prevalent.currentOnlineAdmins.getPhone();
        String adminsName = Prevalent.currentOnlineAdmins.getName();

        adminsNameTextView = findViewById(R.id.profileNameAdmin);
        adminsphoneTextView = findViewById(R.id.profilePhoneAdmin);

        adminsNameTextView.setText(adminsName);
        adminsphoneTextView.setText(adminsPhone);

        exit.setOnClickListener(v -> {
            Paper.book().destroy();
            Intent logOutIntent = new Intent(AdminProfileActivity.this, LoginActivity.class);
            startActivity(logOutIntent);
        });

        nav.setOnItemSelectedListener(item -> {
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
        });
    }
}
