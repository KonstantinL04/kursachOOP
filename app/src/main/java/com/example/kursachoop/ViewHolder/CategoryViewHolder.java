package com.example.kursachoop.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursachoop.Interface.ItemClickListener;
import com.example.kursachoop.R;
import com.google.android.material.imageview.ShapeableImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ShapeableImageView imageCategory;
    public TextView nameCategory;
    public ItemClickListener listenerCategory;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        imageCategory = itemView.findViewById(R.id.categoriesImage);
        nameCategory = itemView.findViewById(R.id.categoriesName);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener listenerCategory){
        this.listenerCategory = listenerCategory;
    }
    @Override
    public void onClick(View view) {
        listenerCategory.onClick(view, getAdapterPosition(), false);
    }
}
