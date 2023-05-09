package com.example.kursachoop.ui.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kursachoop.R;

public class AdminCategoryActivity extends AppCompatActivity {
    private Button btnAddFullpc, btnAddpc, btnAddCPU, btnAddMotherbrd,
            btnAddGPU, btnAddRam, btnAddCooler, btnAddHDD, btnAddPower,
            btnAddSoundcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        init();

        btnAddFullpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "готовые сборки");
                startActivity(intent);
            }
        });

        btnAddpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "корпус");
                startActivity(intent);
            }
        });

        btnAddCPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "процессор");
                startActivity(intent);
            }
        });

        btnAddMotherbrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "материнская плата");
                startActivity(intent);
            }
        });

        btnAddGPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "видеокарта");
                startActivity(intent);
            }
        });

        btnAddRam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "оперативная память");
                startActivity(intent);
            }
        });

        btnAddCooler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "система охлаждения");
                startActivity(intent);
            }
        });

        btnAddHDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "хранение данных");
                startActivity(intent);
            }
        });

        btnAddPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "блок питания");
                startActivity(intent);
            }
        });

        btnAddSoundcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "звуковая карта");
                startActivity(intent);
            }
        });

    }
    private void init(){
        btnAddFullpc = (Button) findViewById(R.id.btnAddFullpc);
        btnAddpc = (Button) findViewById(R.id.btnAddpc);
        btnAddCPU = (Button) findViewById(R.id.btnAddCPU);
        btnAddMotherbrd = (Button) findViewById(R.id.btnAddMotherbrd);
        btnAddGPU = (Button) findViewById(R.id.btnAddGPU);
        btnAddRam = (Button) findViewById(R.id.btnAddRam);
        btnAddCooler = (Button) findViewById(R.id.btnAddCooler);
        btnAddHDD = (Button) findViewById(R.id.btnAddHDD);
        btnAddPower = (Button) findViewById(R.id.btnAddPower);
        btnAddSoundcard = (Button) findViewById(R.id.btnAddSoundcard);
    }
}