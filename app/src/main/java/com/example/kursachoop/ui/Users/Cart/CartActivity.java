package com.example.kursachoop.ui.Users.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursachoop.Model.Cart;
import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.R;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kursachoop.ui.Users.Adapter.CartAdapter;
import com.example.kursachoop.ui.Users.Home.Category.HomeActivity;
import com.example.kursachoop.ui.Users.Profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    private RecyclerView recyclerViewCart;
    private DatabaseReference cartRef;
    private List<Cart> cartList;
    private CartAdapter cartAdapter;
    private TextView totalOrderPrice;
    private RelativeLayout checkoutButton;
    private int totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin);

        recyclerViewCart = findViewById(R.id.recyclerCart);
        recyclerViewCart.setHasFixedSize(true);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        totalOrderPrice = findViewById(R.id.qualityPrice);
        checkoutButton = findViewById(R.id.checkOutOrder);

        String userPhone = Prevalent.currentOnlineUser.getPhone();
        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(userPhone);


        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.binActivity);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeActivity) {
                    item.setIcon(R.drawable.home_sel);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.binActivity) {
                    item.setIcon(R.drawable.bin_sel);
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

        loadCartItems();

        checkoutButton.setOnClickListener(v -> proceedToCheckout());
    }

    private void loadCartItems() {
        cartList = new ArrayList<>();

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cart cartItem = dataSnapshot.getValue(Cart.class);

                    if (cartItem != null) {
                        cartList.add(cartItem);
                    }
                }

                cartAdapter = new CartAdapter(CartActivity.this, cartList);
                recyclerViewCart.setAdapter(cartAdapter);
                calculateTotalPrice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Ошибка при загрузке корзины", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateTotalPrice() {
        totalPrice = 0;  // Обнуляем общую сумму перед пересчетом

        for (Cart cart : cartList) {
            int itemPrice = Integer.parseInt(cart.getPrice());
            int itemQuantity = cart.getQuantity();
            totalPrice += itemPrice * itemQuantity;  // Считаем цену за каждый товар
        }

        // Обновляем текст общей суммы на экране
        totalOrderPrice.setText("Сумма заказа: " + totalPrice + "₽");
    }

    private void proceedToCheckout() {
        if (cartList.isEmpty()) {
            Toast.makeText(this, "Ваша корзина пуста", Toast.LENGTH_SHORT).show();
            return;
        }

        // Логика оформления заказа
        // Например, можно передать данные заказа в новое активити для подтверждения
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("totalPrice", totalPrice);  // Передаем общую сумму
        startActivity(intent);
    }

}