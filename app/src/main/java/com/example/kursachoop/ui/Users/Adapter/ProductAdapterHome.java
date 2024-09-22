package com.example.kursachoop.ui.Users.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursachoop.Model.Product;
import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Admin.Home.Products.AdminEditProductActivity;
import com.example.kursachoop.ui.Users.Home.Products.CardProductActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class ProductAdapterHome extends RecyclerView.Adapter<ProductAdapterHome.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private DatabaseReference cartRef;
    private String userPhone = Prevalent.currentOnlineUser.getPhone();

    public ProductAdapterHome(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(userPhone);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice() + "₽");
        holder.productAvailability.setText(Integer.parseInt(product.getAvailability()) > 0
                ? "В наличии: " + product.getAvailability()
                : "Нет в наличии");
        Picasso.get().load(product.getImage()).into(holder.image);

        // Проверяем, есть ли продукт в корзине, и обновляем кнопку
        cartRef.child(product.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Если товар уже в корзине
                    holder.addToCartButton.setText("В корзине");
                    holder.addToCartButton.setBackgroundResource(R.drawable.btn_in_bin);
                    holder.addToCartButton.setEnabled(false);
                } else {
                    holder.addToCartButton.setText("Добавить в корзину");
                    holder.addToCartButton.setBackgroundResource(R.drawable.btn_buy);
                    holder.addToCartButton.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Ошибка проверки корзины", Toast.LENGTH_SHORT).show();
            }
        });
        holder.addToCartButton.setOnClickListener(v -> {
            addToCart(product);
        });

        // Добавляем обработчик клика для открытия карточки товара
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CardProductActivity.class);
            intent.putExtra("id", product.getId());
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("brand", product.getBrand());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("availability", product.getAvailability());
            intent.putExtra("image", product.getImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView productName, productPrice, productAvailability;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recImage);
            productName = itemView.findViewById(R.id.recPname);
            productPrice = itemView.findViewById(R.id.recPrice);
            productAvailability = itemView.findViewById(R.id.recAvailability);
            addToCartButton = itemView.findViewById(R.id.btn_add_bin);

        }
    }

    private void addToCart(Product product) {
        // Создаем структуру товара для добавления в корзину
        HashMap<String, Object> cartItem = new HashMap<>();
        cartItem.put("id", product.getId());
        cartItem.put("name", product.getName());
        cartItem.put("price", product.getPrice());
        cartItem.put("quantity", 1);  // Начальное количество — 1
        cartItem.put("availableQuantity", product.getAvailability());

        // Добавляем товар в корзину
        cartRef.child(product.getId()).setValue(cartItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Ошибка добавления в корзину", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
