package com.example.kursachoop.ui.Users.Cart;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kursachoop.Model.Cart;
import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Users.Adapter .CheckoutAdapter;
import com.example.kursachoop.ui.Users.Home.Category.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private TextView userNameInput, userPhoneInput;
    private TextView totalOrderPriceText;
    private Button confirmOrderBtn;
    private RecyclerView recyclerCheckout;
    private CheckoutAdapter checkoutAdapter;

    private int totalPrice;
    private List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        String userPhone = Prevalent.currentOnlineUser.getPhone();
        String userName = Prevalent.currentOnlineUser.getName();
        // Инициализация элементов
        userNameInput = findViewById(R.id.name);
        userPhoneInput = findViewById(R.id.phone);
        totalOrderPriceText = findViewById(R.id.totalOrderPriceText);
        confirmOrderBtn = findViewById(R.id.confirmOrderBtn);
        recyclerCheckout = findViewById(R.id.recyclerCheck);

        userNameInput.setText(userName);
        userPhoneInput.setText(userPhone);

        // Получение данных из Intent
        totalPrice = getIntent().getIntExtra("totalPrice",0);
        cartList = (List<Cart>) getIntent().getSerializableExtra("cartList");

        // Заполнение RecyclerView
        recyclerCheckout.setHasFixedSize(true);
        recyclerCheckout.setLayoutManager(new LinearLayoutManager(this));
        checkoutAdapter = new CheckoutAdapter(this, cartList);
        recyclerCheckout.setAdapter(checkoutAdapter);

        // Заполнение данных активного пользователя
        userNameInput.setText(Prevalent.currentOnlineUser.getName());
        userPhoneInput.setText(Prevalent.currentOnlineUser.getPhone());
        totalOrderPriceText.setText("Общая сумма: " + totalPrice + "₽");

        // Подтверждение заказа
        confirmOrderBtn.setOnClickListener(v -> confirmOrder());
    }

    private void confirmOrder() {
        String userName = userNameInput.getText().toString();
        String userPhone = userPhoneInput.getText().toString();

        if (userName.isEmpty() || userPhone.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Сохранение заказа в Firebase
        saveOrderToFirebase(userName, userPhone, totalPrice);
        updateProductAvailability(cartList);
    }

    private void saveOrderToFirebase(String userName, String userPhone, int totalPrice) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        // Текущая дата и время
        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        // Уникальный ID заказа
        String orderID = saveCurrentDate + "_" + saveCurrentTime;

        // Map для заказа
        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderID", orderID);
        orderMap.put("userName", userName);
        orderMap.put("userPhone", userPhone);
        orderMap.put("totalPrice", totalPrice);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("status", "В обработке");

        // Сохранение в Firebase
        ordersRef.child(userPhone).child(orderID).updateChildren(orderMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Сохранение каждого товара
                saveCartItemsToOrder(userPhone, orderID);
            } else {
                Toast.makeText(this, "Ошибка оформления заказа", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveCartItemsToOrder(String userPhone, String orderID) {
        DatabaseReference orderItemsRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(userPhone).child(orderID).child("items");

        for (Cart cartItem : cartList) {
            HashMap<String, Object> itemMap = new HashMap<>();
            itemMap.put("id", cartItem.getId());
            itemMap.put("name", cartItem.getName());
            itemMap.put("price", cartItem.getPrice());
            itemMap.put("image", cartItem.getImage());
            itemMap.put("availableQuantity", cartItem.getAvailableQuantity());
            itemMap.put("quantity", cartItem.getQuantity());

            orderItemsRef.push().setValue(itemMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                }
            });
        }

        clearCart();
        Toast.makeText(this, "Ваш заказ успешно оформлен!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateProductAvailability(List<Cart> cartItems) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products");

        for (Cart cartItem : cartItems) {
            String productId = cartItem.getId();
            int quantityOrdered = cartItem.getQuantity();

            productRef.child(productId).child("availableQuantity").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String availableQuantityStr = snapshot.getValue(String.class);
                        int availableQuantity = Integer.parseInt(availableQuantityStr);
                        int newQuantity = availableQuantity - quantityOrdered;

                        // Обновляем количество в базе данных
                        productRef.child(productId).child("availableQuantity").setValue(String.valueOf(newQuantity));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(CheckoutActivity.this, "Ошибка при обновлении наличия", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void clearCart() {
        String userPhone = Prevalent.currentOnlineUser.getPhone();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(userPhone);
        cartRef.removeValue();
    }
}
