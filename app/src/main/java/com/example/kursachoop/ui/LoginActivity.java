package com.example.kursachoop.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursachoop.Model.Admins;
import com.example.kursachoop.R;
import com.example.kursachoop.Model.Users;
import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.ui.Admin.AdminHomeActivity;
import com.example.kursachoop.ui.Users.Home.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private TextView txtRegister, txtRoot;
    private EditText phoneInput, passwordInput;
    private ProgressDialog loadingBar;
    private String parrentDbName = "Users";

    private CheckBox checkBoxRememberMe;
    private TextView AdminLink, NotAdminLink;
    private boolean isFormatting = false;
    private static final String PHONE_PREFIX = "+7 ";
    private static final int MAX_PHONE_LENGTH = 18; // Для формата +7 (999) 999-99-99
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingBar = new ProgressDialog(this);
        txtRoot = (TextView) findViewById(R.id.root_input);
        loginButton = (Button) findViewById(R.id.login_button_log);
        phoneInput = (EditText) findViewById(R.id.login_edTxt_phone);
        passwordInput = (EditText) findViewById(R.id.login_edTxt_password);
        txtRegister = (TextView) findViewById(R.id.log_reg_txt_sign_up);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.login_checkBox);
        Paper.init(this);

        AdminLink = (TextView) findViewById(R.id.panel_admin);
        NotAdminLink = (TextView) findViewById(R.id.not_panel_admin);

        phoneInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && phoneInput.getText().toString().isEmpty()) {
                    phoneInput.setText(PHONE_PREFIX);
                    phoneInput.setSelection(phoneInput.getText().length()); // Устанавливаем курсор в конец
                }
            }
        });

        phoneInput.addTextChangedListener(new TextWatcher() {
            private int lastLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Не требуется
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;

                isFormatting = true;

                // Ограничиваем длину номера
                if (s.length() > MAX_PHONE_LENGTH) {
                    s.delete(MAX_PHONE_LENGTH, s.length());
                    isFormatting = false;
                    return;
                }

                // Убираем все нецифровые символы, кроме первого символа "+"
                String input = s.toString().replaceAll("[^\\d]", "").replaceFirst("7", "");

                // Возвращаем префикс +7
                StringBuilder formatted = new StringBuilder(PHONE_PREFIX);

                if (input.length() > 0) {
                    formatted.append("(");
                    formatted.append(input.substring(0, Math.min(input.length(), 3)));
                }
                if (input.length() >= 3) {
                    formatted.append(") ");
                    formatted.append(input.substring(3, Math.min(input.length(), 6)));
                }
                if (input.length() >= 6) {
                    formatted.append("-");
                    formatted.append(input.substring(6, Math.min(input.length(), 8)));
                }
                if (input.length() >= 8) {
                    formatted.append("-");
                    formatted.append(input.substring(8, Math.min(input.length(), 10)));
                }

                // Сравниваем, был ли текст уменьшен (стерли ли символ)
                if (lastLength > s.length()) {
                    // Если символы были удалены, то передвигаем курсор корректно
                    int selectionPos = phoneInput.getSelectionStart();
                    if (selectionPos > 0 && s.length() > selectionPos && !Character.isDigit(s.charAt(selectionPos - 1))) {
                        phoneInput.setSelection(selectionPos - 1); // Корректировка позиции курсора
                    }
                } else {
                    // Если символы были добавлены
                    phoneInput.setText(formatted.toString());
                    phoneInput.setSelection(phoneInput.getText().length()); // Курсор в конце
                }

                isFormatting = false;
            }
        });

        txtRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rootIntent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(rootIntent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                loginButton.setText("Вход для админа");
                parrentDbName = "Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                loginButton.setText("Войти");
                parrentDbName = "Users";
            }
        });
    }

    private void loginUser() {
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Введите номер телефона...", Toast.LENGTH_SHORT).show();
        }
        else if(phone.length() < MAX_PHONE_LENGTH ){
            Toast.makeText(this, "Введен некорректный номер телефона...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Введите пароль...", Toast.LENGTH_SHORT).show();
        }

        else {
            loadingBar.setTitle("Вход в аккаунт");
            loadingBar.setMessage("Пожалуйста, подождите...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateUser(phone, password);

        }
    }

    private void ValidateUser(String phone, String password) {
        if(checkBoxRememberMe.isChecked() && parrentDbName.equals("Users")){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);

        }else if(checkBoxRememberMe.isChecked() && parrentDbName.equals("Admins")) {
            Paper.book().write(Prevalent.AdminPhoneKey, phone);
            Paper.book().write(Prevalent.AdminPasswordKey, password);
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("Users");
        DatabaseReference adminsRef = rootRef.child("Admins");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(phone).exists()) {
                    Users usersData = snapshot.child(phone).getValue(Users.class);

                    assert usersData != null;
                    if (usersData.getPhone().equals(phone) && usersData.getPassword().equals(password)) {
                        if (parrentDbName.equals("Users")){
                            Toast.makeText(LoginActivity.this, "Успешная авторизация", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("parrentDbName", parrentDbName);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Пользователь с номером " + phone + " не существует", Toast.LENGTH_SHORT).show();
                        }
                        // Пользователь успешно авторизован

                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Проверяем в узле "Admins"
                    adminsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(phone).exists()) {
                                Admins adminsData = snapshot.child(phone).getValue(Admins.class);

                                assert adminsData != null;
                                if (adminsData.getPhone().equals(phone) && adminsData.getPassword().equals(password)) {
                                    if (parrentDbName.equals("Admins")){
                                        // Пользователь успешно авторизован как администратор
                                        Toast.makeText(LoginActivity.this, "Успешная авторизация", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("parrentDbName", parrentDbName);
                                        editor.apply();

                                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(LoginActivity.this, "Админа с номером " + phone + " не существует", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    loadingBar.dismiss();
                                    Toast.makeText(LoginActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Админа с номером " + phone + " не существует", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, "Ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}