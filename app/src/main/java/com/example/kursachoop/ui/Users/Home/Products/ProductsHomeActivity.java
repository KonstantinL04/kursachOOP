package com.example.kursachoop.ui.Users.Home.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursachoop.Interface.ItemClickListener;
import com.example.kursachoop.Model.Product;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Users.Adapter.ProductAdapterHome;
import com.example.kursachoop.ui.Users.Cart.CartActivity;
import com.example.kursachoop.ui.Users.Home.Category.HomeActivity;
import com.example.kursachoop.ui.Users.Profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsHomeActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView recyclerViewProducts;
    private List<Product> productList;
    private ProductAdapterHome productAdapter;
    private DatabaseReference productsRef;
    private String categoryId, categoryName;
    private TextView categoryNameTxt;
    private BottomNavigationView nav;
    private static final String TAG = "ProductsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_home);

        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");
        if (categoryId == null || categoryName == null) {
            Toast.makeText(this, "Не удалось получить информацию о категории", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        categoryNameTxt = findViewById(R.id.products);
        categoryNameTxt.setText(categoryName);
        recyclerViewProducts = findViewById(R.id.recycler_products_admin);
        recyclerViewProducts.setHasFixedSize(true);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        productsRef = FirebaseDatabase.getInstance().getReference().child("products").child(categoryId);

        // Загружаем товары по категории
        loadProductsByCategory();

        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.homeActivity);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeActivity) {
                    item.setIcon(R.drawable.home_sel);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    return true;
                } else if (itemId == R.id.binActivity) {
                    item.setIcon(R.drawable.bin_sel);
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
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

    }

    private void loadProductsByCategory() {
        productList = new ArrayList<>();
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                    else {
                        Log.e(TAG, "Product is null");
                    }
                }
                productAdapter = new ProductAdapterHome(ProductsHomeActivity.this, productList);
                recyclerViewProducts.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProductsHomeActivity.this, "Ошибка загрузки товаров", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }
}