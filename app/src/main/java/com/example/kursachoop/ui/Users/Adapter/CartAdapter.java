package com.example.kursachoop.ui.Users.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kursachoop.ui.Users.Cart.CartActivity;
import com.example.kursachoop.Model.Cart;
import com.example.kursachoop.Prevalent.Prevalent;
import com.example.kursachoop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context mContext;
    private List<Cart> cartList;
    private DatabaseReference cartRef;

    public CartAdapter(Context mContext, List<Cart> cartList) {
        this.mContext = mContext;
        this.cartList = cartList;
        String userPhone = Prevalent.currentOnlineUser.getPhone();
        this.cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(userPhone);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.productName.setText(cart.getName());
        holder.productPrice.setText(cart.getPrice() + "₽");
        Picasso.get().load(cart.getImage()).into(holder.productImage);
        holder.productQuantity.setText(String.valueOf(cart.getQuantity()));
        holder.productAvailability.setText("В наличии: " + cart.getAvailableQuantity());

        holder.increaseQuantityBtn.setOnClickListener(v -> {
            if (cart.getQuantity() < Integer.parseInt(cart.getAvailableQuantity())) {
                int newQuantity = cart.getQuantity() + 1;
                updateQuantity(cart, newQuantity);
                holder.productQuantity.setText(String.valueOf(newQuantity));
            } else {
                Toast.makeText(mContext, "Больше нельзя добавить", Toast.LENGTH_SHORT).show();
            }
        });

        holder.decreaseQuantityBtn.setOnClickListener(v -> {
            if (cart.getQuantity() > 1) {
                int newQuantity = cart.getQuantity() - 1;
                updateQuantity(cart, newQuantity);
                holder.productQuantity.setText(String.valueOf(newQuantity));
            } else {
                removeCartItem(cart);
            }
        });
    }



    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice, productQuantity, productAvailability;
        public ImageView productImage;
        public ImageView increaseQuantityBtn, decreaseQuantityBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.priceTxt);
            productQuantity = itemView.findViewById(R.id.countProduct);
            productImage = itemView.findViewById(R.id.productImage);
            increaseQuantityBtn = itemView.findViewById(R.id.addProduct);
            decreaseQuantityBtn = itemView.findViewById(R.id.delProduct);
            productAvailability = itemView.findViewById(R.id.availabilityTxt);
        }
    }

    private void updateQuantity(Cart cart, int newQuantity) {
        cartRef.child(cart.getId()).child("quantity").setValue(newQuantity).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
//                Toast.makeText(mContext, "Количество обновлено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Ошибка обновления", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeCartItem(Cart cart) {
        cartRef.child(cart.getId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cartList.remove(cart);
                notifyDataSetChanged();
            } else {
                Toast.makeText(mContext, "Ошибка при удалении товара", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
