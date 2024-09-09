package com.example.kursachoop.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursachoop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextView txtLogin;
    private Button btnRegister;
    private EditText usernameInput, phoneInput, passwordInput;
    private ProgressDialog loadingBar;
    private boolean isFormatting = false;
    private static final String PHONE_PREFIX = "+7 ";
    private static final int MAX_PHONE_LENGTH = 18; // Для формата +7 (999) 999-99-99
    private static final int MIN_PASSWORD_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtLogin = findViewById(R.id.reg_log_txt_sign_up);
        btnRegister = findViewById(R.id.reg_button_log);
        usernameInput = findViewById(R.id.reg_edTxt_nameLogin);
        phoneInput = findViewById(R.id.reg_edTxt_phone);
        passwordInput = findViewById(R.id.reg_edTxt_password);
        loadingBar = new ProgressDialog(this);

        // Добавляем TextWatcher к полю для телефона
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

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String username = usernameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Введите Ваше имя...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Введите номер телефона...", Toast.LENGTH_SHORT).show();
        }

        else if(phone.length() < MAX_PHONE_LENGTH ){
            Toast.makeText(this, "Введен некорректный номер телефона...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Придумайте пароль...", Toast.LENGTH_SHORT).show();
        }

        else if (password.length() < MIN_PASSWORD_LENGTH) {
            Toast.makeText(this, "Пароль должен содержать минимум 8 символов...", Toast.LENGTH_SHORT).show();
        }

        else {
            loadingBar.setTitle("Создание аккаунта");
            loadingBar.setMessage("Пожалуйста, подождите...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhone(username, phone, password);
        }
    }

    private void ValidatePhone(String username, String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone", phone);
                    userDataMap.put("name", username);
                    userDataMap.put("password", password);

                    RootRef.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Регистрация завершена!", Toast.LENGTH_SHORT).show();
                                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(loginIntent);
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Номер " + phone + " уже зарегистрирован!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
