package com.example.kursachoop.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursachoop.Model.Admins;
import com.example.kursachoop.Model.Users;
import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Admin.Home.Category.AdminHomeActivity;
import com.example.kursachoop.ui.Users.Home.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
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
        String AdminPhoneKey = Paper.book().read(Prevalent.AdminPhoneKey);
        String AdminPasswordKey = Paper.book().read(Prevalent.AdminPasswordKey);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        parrentDbName = sharedPreferences.getString("parrentDbName", "");

        if(UserPhoneKey != null && UserPasswordKey != null) {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                ValidateUser(UserPhoneKey, UserPasswordKey);
                loadingBar.setTitle("Вход в аккаунт");
                loadingBar.setMessage("Пожалуйста, подождите...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
        else if(AdminPhoneKey != null && AdminPasswordKey != null){
            if(!TextUtils.isEmpty(AdminPhoneKey) && !TextUtils.isEmpty(AdminPasswordKey)){
                ValidateUser(AdminPhoneKey, AdminPasswordKey);
                loadingBar.setTitle("Вход в аккаунт");
                loadingBar.setMessage("Пожалуйста, подождите...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void ValidateUser(final String phone, final String password) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("Users");
        DatabaseReference adminsRef = rootRef.child("Admins");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(phone).exists()) {
                    Users usersData = snapshot.child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone) && usersData.getPassword().equals(password)) {
                        if (parrentDbName.equals("Users")){
                            // Пользователь успешно авторизован
                            Toast.makeText(MainActivity.this, "Успешная авторизация", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Пользователь с номером " + phone + " не существует", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Проверяем в узле "Admins"
                    adminsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(phone).exists()) {
                                Admins adminsData = snapshot.child(phone).getValue(Admins.class);

                                if (adminsData.getPhone().equals(phone) && adminsData.getPassword().equals(password)) {
                                    if (parrentDbName.equals("Admins")){
                                        // Пользователь успешно авторизован как администратор
                                        Toast.makeText(MainActivity.this, "Успешная авторизация", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(MainActivity.this, "Админа с номером " + phone + " не существует", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    loadingBar.dismiss();
                                    Toast.makeText(MainActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Пользователь с номером " + phone + " не существует", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
                Toast.makeText(MainActivity.this, "Ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}