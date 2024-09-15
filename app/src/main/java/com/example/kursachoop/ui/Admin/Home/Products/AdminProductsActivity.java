package com.example.kursachoop.ui.Admin.Home.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursachoop.Interface.ItemClickListener;
import com.example.kursachoop.Model.Product;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Admin.Adapter.ProductAdapter;
import com.example.kursachoop.ui.Admin.Home.Category.AdminAddCategoryActivity;
import com.example.kursachoop.ui.Admin.Home.Category.AdminHomeActivity;
import com.example.kursachoop.ui.Admin.Profile.AdminProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdminProductsActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView recyclerViewProducts;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private DatabaseReference productsRef;
    private String categoryId, categoryName;
    private TextView categoryNameTxt;
    private BottomNavigationView nav;
    private Button addProductBtn;
    private static final String TAG = "AdminProductsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products);

        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");
        if (categoryId == null || categoryName == null) {
            Toast.makeText(this, "Не удалось получить информацию о категории", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        categoryNameTxt = findViewById(R.id.category);
        categoryNameTxt.setText(categoryName);
        addProductBtn = findViewById(R.id.add_product);
        recyclerViewProducts = findViewById(R.id.recycler_products_admin);
        recyclerViewProducts.setHasFixedSize(true);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        productsRef = FirebaseDatabase.getInstance().getReference().child("products").child(categoryId);

        // Загружаем товары по категории
        loadProductsByCategory();

        addProductBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminProductsActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("categoryId", categoryId);  // Передача ID категории
            startActivity(intent);
        });

        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.homeActivity);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeActivity) {
                    item.setIcon(R.drawable.home_sel);
                    return true;
                } else if (itemId == R.id.profileActivity) {
                    item.setIcon(R.drawable.profile_sel);
                    startActivity(new Intent(getApplicationContext(), AdminProfileActivity.class));
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
                }
                productAdapter = new ProductAdapter(AdminProductsActivity.this, productList);
                recyclerViewProducts.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminProductsActivity.this, "Ошибка загрузки товаров", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }
}