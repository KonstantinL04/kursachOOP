package com.example.kursachoop.ui.Admin.Home.Products;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursachoop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryId, productId, image;
    private ImageView productImage;
    private EditText productName, productBrand, productDescription, productPrice, productAvailability;
    private Button btnAddNewProduct;
    private Uri imageUri;
    private ProgressDialog loadingBar;

    // Firebase Storage reference
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        // Получение ID выбранной категории
        categoryId = getIntent().getStringExtra("categoryId");

        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productBrand = findViewById(R.id.product_brand);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        productAvailability = findViewById(R.id.product_availability);
        btnAddNewProduct = findViewById(R.id.btn_add_new_product);
        loadingBar = new ProgressDialog(this);

        // Выбор изображения для товара
        productImage.setOnClickListener(v -> openGallery());

        // Обработчик нажатия кнопки "Добавить новый товар"
        btnAddNewProduct.setOnClickListener(v -> validateProductData());
    }

    // Открытие галереи для выбора изображения товара
    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            productImage.setImageURI(imageUri);
        }
    }

    // Валидация данных товара
    private void validateProductData() {
        String name = productName.getText().toString();
        String brand = productBrand.getText().toString();
        String description = productDescription.getText().toString();
        String price = productPrice.getText().toString();
        String availability = productAvailability.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Изображение товара обязательно.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name) || TextUtils.isEmpty(brand) ||
                TextUtils.isEmpty(description) || TextUtils.isEmpty(price) || TextUtils.isEmpty(availability)) {
            Toast.makeText(this, "Пожалуйста, заполните все поля.", Toast.LENGTH_SHORT).show();
        } else {
            storeProductInformation();
        }
    }

    // Загрузка изображения и сохранение информации о товаре
    private void storeProductInformation() {
        loadingBar.setTitle("Добавление нового товара");
        loadingBar.setMessage("Пожалуйста, подождите...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        productId = productsRef.push().getKey();
        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productId + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(e -> {
            Toast.makeText(AdminAddNewProductActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }).addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
            image = uri.toString();
            saveProductToDatabase();
        }));
    }

    // Сохранение информации о товаре в базу данных
    private void saveProductToDatabase() {
        String name = productName.getText().toString();
        String brand = productBrand.getText().toString();
        String description = productDescription.getText().toString();
        String price = productPrice.getText().toString();
        int availability = Integer.parseInt(productAvailability.getText().toString());

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("id", productId);
        productMap.put("name", name);
        productMap.put("brand", brand);
        productMap.put("description", description);
        productMap.put("price", price);
        productMap.put("availability", availability);
        productMap.put("image", image);

        productsRef.child(categoryId).child(productId).setValue(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminAddNewProductActivity.this, "Товар успешно добавлен.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            finish();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Ошибка: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }
}
