package com.example.kursachoop.ui.Users.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.kursachoop.Model.Products;
import com.example.kursachoop.ViewHolder.ProductViewHolder;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Users.Bin.BinActivity;
import com.example.kursachoop.ui.Users.Catalog.CatalogActivity;
import com.example.kursachoop.ui.Users.Profile.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity  {
    private BottomNavigationView nav;
    DatabaseReference ProductsRef;
//    private RecyclerView recyclerViewUserHome;
//    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.homeActivity);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeActivity) {
                    item.setIcon(R.drawable.home_sel);
                    return true;
                } else if (itemId == R.id.catalogActivity) {
                    item.setIcon(R.drawable.catalog_sel);
                    startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.binActivity) {
                    item.setIcon(R.drawable.bin_sel);
                    startActivity(new Intent(getApplicationContext(), BinActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.profileActivity) {
                    item.setIcon(R.drawable.profile_sel);
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

//        recyclerViewUserHome = findViewById(R.id.recycler_menu);
//        recyclerViewUserHome.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerViewUserHome.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef.orderByChild("category").equalTo("процессор"), Products.class)
                        .build();
//                .setQuery(ProductsRef, Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {
                Picasso.get().load(model.getImage()).into(holder.imageView);
//                Glide.with(holder.itemView.getContext())
//                        .load(model.getImage())
//                        .into(holder.imageView);
                holder.txtProductName.setText(model.getPName());
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
//        recyclerViewUserHome.setAdapter(adapter);
//        adapter.startListening();
    }


}