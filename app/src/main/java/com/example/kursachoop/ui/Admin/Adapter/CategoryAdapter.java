package com.example.kursachoop.ui.Admin.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursachoop.Interface.ItemClickListener;
import com.example.kursachoop.Model.Category;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Admin.Home.Products.AdminProductsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categoryList;
    private ItemClickListener itemClickListener;

    public CategoryAdapter(Context context, List<Category> categoryList, ItemClickListener itemClickListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_list_category, parent, false);
        return new CategoryViewHolder(view);
    }

    // CategoryAdapter.java
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getName());
        Picasso.get().load(category.getImageUrl()).into(holder.categoryImage);
        // При клике на категорию запускаем экран с товарами
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminProductsActivity.class);
            intent.putExtra("categoryId", category.getId());
            intent.putExtra("categoryName", category.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImageAdmin);
            categoryName = itemView.findViewById(R.id.categoryNameAdmin);
        }
    }
}
