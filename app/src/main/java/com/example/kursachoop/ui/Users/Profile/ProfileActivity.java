package com.example.kursachoop.ui.Users.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.LoginActivity;
import com.example.kursachoop.ui.MainActivity;
import com.example.kursachoop.ui.Users.Bin.BinActivity;
import com.example.kursachoop.ui.Users.Catalog.CatalogActivity;
import com.example.kursachoop.ui.Users.Home.HomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    private LinearLayout btn_settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        btnLogOut = (ImageView) findViewById(R.id.button);
        btn_settings = (LinearLayout) findViewById(R.id.btn_settings);
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.profileActivity);
//        TextView userNameTextView = findViewById(R.id.profile_name);
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
                } else if (itemId == R.id.catalogActivity) {
                    item.setIcon(R.drawable.catalog_sel);
                    startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
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

//        btnLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Paper.book().destroy();
//
//                Intent logOutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
//                startActivity(logOutIntent);
//            }
//        });


//        Picasso.get () .load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);


    }
}