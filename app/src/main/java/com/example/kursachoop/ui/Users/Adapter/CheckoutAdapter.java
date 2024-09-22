package com.example.kursachoop.ui.Users.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursachoop.Model.Cart;
import com.example.kursachoop.R;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private Context context;
    private List<Cart> cartList;

    public CheckoutAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cartItem = cartList.get(position);
        holder.productName.setText(cartItem.getName());
        holder.productQuantity.setText(cartItem.getQuantity() + "шт.");
        holder.productPrice.setText(Integer.parseInt(cartItem.getPrice()) * cartItem.getQuantity() + "₽");
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productQuantity, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.checkoutProductName);
            productQuantity = itemView.findViewById(R.id.checkoutProductQuantity);
            productPrice = itemView.findViewById(R.id.checkoutProductPrice);
        }
    }
}
