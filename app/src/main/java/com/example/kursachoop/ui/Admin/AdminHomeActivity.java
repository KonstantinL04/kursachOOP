package com.example.kursachoop.ui.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kursachoop.Model.Products;
import com.example.kursachoop.R;
import com.example.kursachoop.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class AdminHomeActivity extends AppCompatActivity {
    private Button addProduct, delProduct;
    DatabaseReference ProductsRef;
    private RecyclerView recyclerViewAdminHome;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("процессор");
        addProduct = findViewById(R.id.add_product);
        delProduct = findViewById(R.id.del_product);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProductIntent = new Intent(AdminHomeActivity.this, AdminCategoryActivity.class);
                startActivity(addProductIntent);
            }
        });
        delProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent delProductIntent = new Intent(AdminHomeActivity.this, AdminDelProductActivity.class);
                startActivity(delProductIntent);
            }
        });
        recyclerViewAdminHome = findViewById(R.id.recycler_menu_admin);
        recyclerViewAdminHome.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewAdminHome.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.txtProductName.setText(model.getCategory());
                holder.txtProductPrice.setText(model.getPrice());
            }
            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerViewAdminHome.setAdapter(adapter);
        adapter.startListening();
    }

}