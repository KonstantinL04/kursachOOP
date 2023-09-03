package com.example.kursachoop.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursachoop.Interface.ItemClickListener;
import com.example.kursachoop.R;

public class CatalogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView catalogsName;
    public ItemClickListener listenerCatalog;
    public CatalogViewHolder(@NonNull View itemView) {
        super(itemView);

        catalogsName = itemView.findViewById(R.id.catalogsName);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener listenerCategory){
        this.listenerCatalog = listenerCatalog;
    }
    @Override
    public void onClick(View view) {
        listenerCatalog.onClick(view, getAdapterPosition(), false);
    }
}
