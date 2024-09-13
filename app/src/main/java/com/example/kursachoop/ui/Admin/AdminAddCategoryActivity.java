package com.example.kursachoop.ui.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kursachoop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;

public class AdminAddCategoryActivity extends AppCompatActivity {
    private static final int GALLERY_PICK = 1;
    private ImageView selectProductImage;
    private EditText productName;
    private Button btnAddNewProduct;
    private Uri imageUri;
    private DatabaseReference categoriesRef;
    private StorageReference categoryImagesRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_category);

        // Инициализация Firebase
        categoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
        categoryImagesRef = FirebaseStorage.getInstance().getReference().child("Category Images");
        loadingBar = new ProgressDialog(this);

        // Инициализация элементов UI
        selectProductImage = findViewById(R.id.select_product_image);
        productName = findViewById(R.id.product_name);
        btnAddNewProduct = findViewById(R.id.btn_add_new_product);

        // Обработка нажатия на кнопку выбора изображения
        selectProductImage.setOnClickListener(view -> {
            openGallery();
        });

        // Обработка нажатия на кнопку добавления новой категории
        btnAddNewProduct.setOnClickListener(view -> {
            String categoryName = productName.getText().toString();

            if (imageUri == null) {
                Toast.makeText(this, "Пожалуйста, выберите изображение", Toast.LENGTH_SHORT).show();
            } else if (categoryName.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите название категории", Toast.LENGTH_SHORT).show();
            } else {
                uploadImageToStorage(categoryName);
            }
        });
    }

    // Метод для открытия галереи
    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            selectProductImage.setImageURI(imageUri); // Отображение выбранного изображения
        }
    }

    // Метод для загрузки изображения в Firebase Storage
    private void uploadImageToStorage(String categoryName) {
        loadingBar.setTitle("Добавление новой категории");
        loadingBar.setMessage("Пожалуйста, подождите, пока категория добавляется...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        String categoryId = categoriesRef.push().getKey(); // Генерируем уникальный ID категории
        StorageReference filePath = categoryImagesRef.child(categoryId + ".jpg");

        filePath.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                saveCategoryToDatabase(categoryId, categoryName, imageUrl);
            });
        }).addOnFailureListener(e -> {
            loadingBar.dismiss();
            Toast.makeText(this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
        });
    }

    // Метод для сохранения категории в Realtime Database
    private void saveCategoryToDatabase(String categoryId, String categoryName, String imageUrl) {
        HashMap<String, Object> categoryData = new HashMap<>();
        categoryData.put("id", categoryId);
        categoryData.put("name", categoryName);
        categoryData.put("imageUrl", imageUrl);

        categoriesRef.child(categoryId).updateChildren(categoryData)
                .addOnCompleteListener(task -> {
                    loadingBar.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Категория успешно добавлена", Toast.LENGTH_SHORT).show();
                        finish(); // Закрыть активность после успешного добавления
                    } else {
                        Toast.makeText(this, "Ошибка добавления категории", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

