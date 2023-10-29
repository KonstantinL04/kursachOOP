package com.example.kursachoop.ui.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursachoop.Interface.ItemClickListener;
import com.example.kursachoop.Model.Catalog;
import com.example.kursachoop.Model.Category;
import com.example.kursachoop.Model.Products;
import com.example.kursachoop.R;
import com.example.kursachoop.ViewHolder.CatalogViewHolder;
import com.example.kursachoop.ViewHolder.CategoryViewHolder;
import com.example.kursachoop.ViewHolder.ProductViewHolder;
import com.example.kursachoop.ui.LoginActivity;
import com.example.kursachoop.ui.MainActivity;
import com.example.kursachoop.ui.Users.Profile.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;


public class AdminHomeActivity extends AppCompatActivity implements ItemClickListener {
    private static final String TAG = "ADMIN";
    private RecyclerView recyclerViewAdminHome;
    private Button addCategoryButton;
    private Button delCategoryButton, LogOutBtn;
    private DatabaseReference categoriesRef;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        categoriesRef = FirebaseDatabase.getInstance().getReference().child("Catalog");

        recyclerViewAdminHome = findViewById(R.id.recycler_menu_admin);
        recyclerViewAdminHome.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerViewAdminHome.setLayoutManager(layoutManager);
        LogOutBtn = findViewById(R.id.logout_button);
        addCategoryButton = findViewById(R.id.add_category);
        delCategoryButton = findViewById(R.id.del_category);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference newCategoryRef = categoriesRef.push(); // Создаем новый уникальный ключ
                String newCategoryId = newCategoryRef.getKey(); // Получаем ID новой категории

                // Создаем объект каталога с названием по умолчанию и установленным счетчиком в 0
                Catalog newCatalog = new Catalog(newCategoryId, "Название по умолчанию", 0);

                // Сохраняем новую запись в каталоге в Firebase Realtime Database
                newCategoryRef.setValue(newCatalog);

                // Выводим сообщение об успешном добавлении
                Toast.makeText(AdminHomeActivity.this, "Новая категория добавлена", Toast.LENGTH_SHORT).show();
            }
        });
        delCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Intent logOutIntent = new Intent(AdminHomeActivity.this, LoginActivity.class);
                startActivity(logOutIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Catalog> options =
                new FirebaseRecyclerOptions.Builder<Catalog>()
                        .setQuery(
                                categoriesRef.orderByChild("" +
                                        "Catalog"), Catalog.class)
                        .build();

        FirebaseRecyclerAdapter<Catalog, CatalogViewHolder> adapter = new FirebaseRecyclerAdapter<Catalog, CatalogViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CatalogViewHolder holder, int i, @NonNull Catalog model) {
                Log.d(TAG, "onBindViewHolder: " + model.getCategoryName());
                holder.catalogsName.setText(model.getCategoryName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminHomeActivity.this, AdminCategoryActivity.class);
                        intent.putExtra("categoryId", model.getCategoryId());
                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d(TAG, "onCreateViewHolder");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_catalog, parent, false);
                CatalogViewHolder holder = new CatalogViewHolder(view);
                return holder;
            }
        };

        recyclerViewAdminHome.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    public void onClick(View view, int position, boolean isLongClick) {
        Catalog catalog = (Catalog) view.getTag();
        Intent intent = new Intent(AdminHomeActivity.this, AdminCategoryActivity.class);
        intent.putExtra("categoryId", catalog.getCategoryId());
        startActivity(intent);
    }
}