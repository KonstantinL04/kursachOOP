package com.example.kursachoop.ui.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.kursachoop.R;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        categoryName = getIntent().getExtras().get("category").toString();

        Toast.makeText(this, "Выбрана категория " + categoryName, Toast.LENGTH_SHORT).show();
    }
}