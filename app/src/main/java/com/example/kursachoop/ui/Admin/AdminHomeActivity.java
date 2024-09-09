package com.example.kursachoop.ui.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kursachoop.Interface.ItemClickListener;
import com.example.kursachoop.R;
import com.example.kursachoop.ui.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;


public class AdminHomeActivity extends AppCompatActivity implements ItemClickListener {
    private static final String TAG = "ADMIN";
    private RecyclerView recyclerViewAdminHome;
    private Button addCategoryButton;
    private DatabaseReference categoriesRef;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        recyclerViewAdminHome = findViewById(R.id.recycler_category_admin);
        recyclerViewAdminHome.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerViewAdminHome.setLayoutManager(layoutManager);
        addCategoryButton = findViewById(R.id.add_category);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminAddCategoryActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }
}