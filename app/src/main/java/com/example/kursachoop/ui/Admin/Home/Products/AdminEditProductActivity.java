package com.example.kursachoop.ui.Admin.Home.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kursachoop.Model.Product;
import com.example.kursachoop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminEditProductActivity extends AppCompatActivity {
    private EditText productName, productBrand, productDescription, productPrice, productAvailability;
    private ImageView productImage;
    private Button btnUpdateProduct;
    private String productId, categoryId;
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);

        productName = findViewById(R.id.product_name);
        productBrand = findViewById(R.id.product_brand);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        productAvailability = findViewById(R.id.product_availability);
        productImage = findViewById(R.id.product_image);
        btnUpdateProduct = findViewById(R.id.btn_update_product);

        // Получение данных о товаре из Intent
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        categoryId = intent.getStringExtra("categoryId");

        productRef = FirebaseDatabase.getInstance().getReference().child("products").child(categoryId).child(productId);

        // Загрузка текущих данных о товаре
        loadProductDetails();

        btnUpdateProduct.setOnClickListener(view -> {
            updateProduct();
        });
    }

    private void loadProductDetails() {
        productRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Product product = task.getResult().getValue(Product.class);
                if (product != null) {
                    productName.setText(product.getName());
                    productBrand.setText(product.getBrand());
                    productDescription.setText(product.getDescription());
                    productPrice.setText(String.valueOf(product.getPrice()));
                    productAvailability.setText(product.getAvailability());
                    Picasso.get().load(product.getImage()).into(productImage);
                }
            } else {
                Toast.makeText(AdminEditProductActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProduct() {
        String name = productName.getText().toString().trim();
        String brand = productBrand.getText().toString().trim();
        String price = productPrice.getText().toString().trim();
        String description = productDescription.getText().toString().trim();
        String availability = productAvailability.getText().toString().trim();

        if (name.isEmpty() || brand.isEmpty() || description.isEmpty() || price.isEmpty() || availability.isEmpty()) {
            Toast.makeText(AdminEditProductActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
//            int availability = Integer.parseInt(availabilityStr);
            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String imageUrl = dataSnapshot.child("image").getValue(String.class);
                    Product product = new Product(productId, name, price, imageUrl, description, brand, availability );
                    productRef.setValue(product).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminEditProductActivity.this, "Товар обновлен успешно", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AdminEditProductActivity.this, "Ошибка обновления товара", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(AdminEditProductActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(AdminEditProductActivity.this, "Некорректное количество", Toast.LENGTH_SHORT).show();
        }
    }
}