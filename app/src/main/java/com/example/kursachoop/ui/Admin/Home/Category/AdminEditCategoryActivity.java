package com.example.kursachoop.ui.Admin.Home.Category;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kursachoop.Model.Category;
import com.example.kursachoop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminEditCategoryActivity extends AppCompatActivity {
    private EditText categoryName;
    private ImageView categoryImage;
    private Button btnUpdateCategory;
    private String categoryId;
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_category);

        categoryName = findViewById(R.id.category_name);
        categoryImage = findViewById(R.id.category_image);
        btnUpdateCategory = findViewById(R.id.btn_update_category);

        // Получение данных о товаре из Intent
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");

        productRef = FirebaseDatabase.getInstance().getReference().child("categories").child(categoryId);

        // Загрузка текущих данных о товаре
        loadCategoryDetails();

        btnUpdateCategory.setOnClickListener(view -> {
            updateCategory();
        });
    }

    private void loadCategoryDetails() {
        productRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Category category = task.getResult().getValue(Category.class);
                if (category != null) {
                    categoryName.setText(category.getName());
                    Picasso.get().load(category.getImageUrl()).into(categoryImage);
                }
            } else {
                Toast.makeText(AdminEditCategoryActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCategory() {
        String name = categoryName.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText( AdminEditCategoryActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                Category category = new Category(categoryId,name, imageUrl);
                productRef.setValue(category).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminEditCategoryActivity.this, "Категория обновлена успешно", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AdminEditCategoryActivity.this, "Ошибка обновления категории", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminEditCategoryActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        });
    }
}