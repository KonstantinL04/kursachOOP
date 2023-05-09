package com.example.kursachoop.ui.Users.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursachoop.ui.LoginActivity;
import com.example.kursachoop.Model.Users;
import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.RegisterActivity;
import com.example.kursachoop.ui.Users.Bin.BinActivity;
import com.example.kursachoop.ui.Users.Catalog.CatalogActivity;
import com.example.kursachoop.ui.Users.Profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.homeActivity);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeActivity) {
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
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    public static class MainActivity extends AppCompatActivity   {
        private Button btnLogin;
        private TextView txtRegister;
        private ProgressDialog loadingBar;
        private String parrentDbName = "Users";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            loadingBar = new ProgressDialog(this);
            btnLogin = (Button) findViewById(R.id.main_button_log);
            txtRegister = (TextView) findViewById(R.id.main_txt_sign_up);

            Paper.init(this);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            });
            txtRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
            });

            String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
            String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

            if(UserPhoneKey != "" && UserPasswordKey != ""){
                if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                    ValidateUser(UserPhoneKey, UserPasswordKey);
                    loadingBar.setTitle("Вход в аккаунт");
                    loadingBar.setMessage("Пожалуйста, подождите...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                }
            }
        }

        private void ValidateUser(final String phone, final String password) {

            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(parrentDbName).child(phone).exists())
                    {
                        Users usersData = dataSnapshot.child(parrentDbName).child(phone).getValue(Users.class);

                        if(usersData.getPhone().equals(phone)){
                            if(usersData.getPassword().equals(password)){
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                            }
                            else {
                                loadingBar.dismiss();
                            }
                        }
                    }
                    else {
                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this, "Аккаунта с номером " + phone + " не существует!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}