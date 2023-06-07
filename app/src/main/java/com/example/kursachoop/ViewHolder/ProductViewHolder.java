package com.example.kursachoop.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kursachoop.Interface.ItemClickListener;
import com.example.kursachoop.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, txtProductPrice;
    public ImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(View itemView){
        super(itemView);

        imageView = itemView.findViewById(R.id.recImage);
        txtProductName = itemView.findViewById(R.id.recPname);
        txtProductPrice = itemView.findViewById(R.id.recPrice);
    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
