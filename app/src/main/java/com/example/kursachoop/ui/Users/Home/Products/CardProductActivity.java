package com.example.kursachoop.ui.Users.Home.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Users.Cart.CartActivity;
import com.example.kursachoop.ui.Users.Home.Category.HomeActivity;
import com.example.kursachoop.ui.Users.Profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

public class CardProductActivity extends AppCompatActivity {
    private ImageView productImage, backBtn;
    private TextView productName, productPrice, productAvailability, productDescription, productBrand;
    private Button addToCartButton;
    private BottomNavigationView nav;
    private boolean isInCart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_product);

        productName = findViewById(R.id.nameTxt);
        productPrice = findViewById(R.id.priceTxt);
        productImage = findViewById(R.id.imageProduct);
        productBrand = findViewById(R.id.brandTxt);
        productDescription = findViewById(R.id.descriptionTxt);
        productAvailability = findViewById(R.id.availabilityTxt);
        addToCartButton = findViewById(R.id.binBtn);

        // Получаем данные, переданные через Intent
        String productId = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String brand = getIntent().getStringExtra("brand");
        String description = getIntent().getStringExtra("description");
        String availability = getIntent().getStringExtra("availability");
        String imageUrl = getIntent().getStringExtra("image");
        String userPhone = Prevalent.currentOnlineUser.getPhone();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(userPhone);

        // Устанавливаем данные в элементы
        productName.setText(name);
        productPrice.setText(price + "₽");
        Picasso.get().load(imageUrl).into(productImage);
        productBrand.setText(brand);
        productDescription.setText(description);

        if(availability != null) {
            int availabilityInt = Integer.parseInt(availability);
            productAvailability.setText(availabilityInt > 0
                    ? "В наличии: " + availability
                    : "Нет в наличии");
        }

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.homeActivity);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeActivity) {
                    item.setIcon(R.drawable.home_sel);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    return true;
                } else if (itemId == R.id.binActivity) {
                    item.setIcon(R.drawable.bin_sel);
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.profileActivity) {
                    item.setIcon(R.drawable.profile_sel);
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

        cartRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Если товар уже в корзине
                    isInCart = true;
                    updateCartButtonState();  // Обновляем внешний вид кнопки
                } else {
                    isInCart = false;
                    updateCartButtonState();  // Обновляем внешний вид кнопки
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CardProductActivity.this, "Ошибка: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        addToCartButton.setOnClickListener(v -> {
            if (isInCart) {
                // Если товар уже в корзине, открываем активность корзины
                Intent intent = new Intent(CardProductActivity.this, CartActivity.class);
                startActivity(intent);
            } else {
                if(availability != null) {
                    int availabilityInt = Integer.parseInt(availability);
                    if (availabilityInt > 0) {
                        addToCart(productId, name, price, imageUrl,availabilityInt);
                    } else {
                        Toast.makeText(CardProductActivity.this, "Товара нет в наличии", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateCartButtonState() {
        if (isInCart) {
            // Изменяем текст и цвет кнопки, если товар уже в корзине
            addToCartButton.setText("В корзине");
            addToCartButton.setBackgroundResource(R.drawable.btn_in_bin);
        } else {
            addToCartButton.setText("Добавить в корзину");
            addToCartButton.setBackgroundResource(R.drawable.btn_buy);
        }
    }

    private void addToCart(String productId, String productName, String productPrice, String productImage ,int availableQuantity) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart");

        // Используем телефон текущего пользователя из Prevalent
        String userPhone = Prevalent.currentOnlineUser.getPhone();
        DatabaseReference userCartRef = cartRef.child(userPhone).child(productId);  // Создаем ссылку на корзину пользователя

        // Создаем структуру товара для добавления в корзину
        HashMap<String, Object> cartItem = new HashMap<>();
        cartItem.put("id", productId);
        cartItem.put("name", productName);
        cartItem.put("price", productPrice);
        cartItem.put("image", productImage);
        cartItem.put("quantity", 1);  // Начальное количество — 1
        cartItem.put("availableQuantity", availableQuantity);

        // Добавляем товар в корзину пользователя
        userCartRef.setValue(cartItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                isInCart = true;
                updateCartButtonState();  // Обновляем внешний вид кнопки
                Toast.makeText(CardProductActivity.this, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CardProductActivity.this, "Ошибка добавления в корзину", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
