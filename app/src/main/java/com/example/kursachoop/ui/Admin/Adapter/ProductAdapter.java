package com.example.kursachoop.ui.Admin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursachoop.Model.Product;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Admin.Home.Products.AdminEditProductActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private String categoryId;

    public ProductAdapter(Context context, List<Product> productList, String categoryId) {
        this.context = context;
        this.productList = productList;
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_recycler, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        Picasso.get().load(product.getImage()).into(holder.image);

        // Удаление товара
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Удалить товар")
                    .setMessage("Вы уверены, что хотите удалить этот товар?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        // Вызов функции удаления
                        deleteProduct(product.getId(), position);
                    })
                    .setNegativeButton("Нет", null)
                    .show();
        });

        holder.editProductBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminEditProductActivity.class);
            intent.putExtra("productId", product.getId());
            intent.putExtra("categoryId", categoryId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image, deleteButton, editProductBtn;
        TextView productName, productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recImage);
            productName = itemView.findViewById(R.id.recPname);
            productPrice = itemView.findViewById(R.id.recPrice);
            deleteButton = itemView.findViewById(R.id.delProduct);
            editProductBtn = itemView.findViewById(R.id.editProduct);// Кнопка удаления товара
        }
    }

    private void deleteProduct(String productId, int position) {
        DatabaseReference productRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("products")
                .child(categoryId)  // Используем категорию
                .child(productId);  // Уникальный ID товара

        productRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Убираем товар из списка и уведомляем адаптер
                productList.remove(position);
                notifyItemRemoved(position);
                // Обновляем адаптер, если это необходимо
                if (productList.isEmpty()) {
                    notifyDataSetChanged(); // Обновление всего списка
                }
            } else {
                Toast.makeText(context, "Ошибка удаления товара", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
