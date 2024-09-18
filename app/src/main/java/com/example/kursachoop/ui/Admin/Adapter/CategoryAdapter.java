package com.example.kursachoop.ui.Admin.Adapter;
import android.app.AlertDialog;
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
import com.example.kursachoop.ui.Admin.Home.Category.AdminEditCategoryActivity;
import com.example.kursachoop.ui.Admin.Home.Category.AdminHomeActivity;
import com.example.kursachoop.ui.Admin.Home.Products.AdminProductsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categoryList;
    private ItemClickListener itemClickListener;
    private String categoryId;

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

        holder.editCategoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminEditCategoryActivity.class);
            intent.putExtra("categoryId", category.getId());
            context.startActivity(intent);
        });

        holder.deleteCategoryBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Удалить категорию")
                    .setMessage("Вы уверены, что хотите удалить эту категорию?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        // Вызов функции удаления
                        if (context instanceof AdminHomeActivity) {
                            ((AdminHomeActivity) context).deleteCategory(category.getId(), position);
                        }
                    })
                    .setNegativeButton("Нет", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage, editCategoryBtn, deleteCategoryBtn;
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImageAdmin);
            categoryName = itemView.findViewById(R.id.categoryNameAdmin);
            editCategoryBtn = itemView.findViewById(R.id.editCategory);
            deleteCategoryBtn = itemView.findViewById(R.id.delCategory);
        }
    }
}
