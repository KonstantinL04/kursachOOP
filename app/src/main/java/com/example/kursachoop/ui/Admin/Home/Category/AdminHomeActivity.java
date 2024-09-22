package com.example.kursachoop.ui.Admin.Home.Category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kursachoop.Interface.ItemClickListener;
import com.example.kursachoop.Model.Category;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.Admin.Adapter.CategoryAdapter;
import com.example.kursachoop.ui.Admin.Profile.AdminProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdminHomeActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView recyclerViewAdminHome;
    private Button addCategoryButton;
    private DatabaseReference categoriesRef;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private BottomNavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        recyclerViewAdminHome = findViewById(R.id.recycler_category_admin);
        recyclerViewAdminHome.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewAdminHome.setLayoutManager(layoutManager);
        addCategoryButton = findViewById(R.id.add_category);
        categoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
        categoryAdapter = new CategoryAdapter(this, categoryList, this);
        recyclerViewAdminHome.setAdapter(categoryAdapter);

        loadCategories();

        addCategoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminAddCategoryActivity.class);
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

    private void loadCategories() {
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    if (category != null) {
                        categoryList.add(category);
                    }
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminHomeActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void deleteCategory(String categoryId, int position) {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("categories")
                .child(categoryId);

        categoryRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Убираем категорию из списка и уведомляем адаптер
                categoryList.remove(position);
                categoryAdapter.notifyItemRemoved(position);
                if (categoryList.isEmpty()) {
                    categoryAdapter.notifyDataSetChanged(); // Обновление всего списка, если он пуст
                }
            } else {
                Toast.makeText(AdminHomeActivity.this, "Ошибка удаления категории", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }

}