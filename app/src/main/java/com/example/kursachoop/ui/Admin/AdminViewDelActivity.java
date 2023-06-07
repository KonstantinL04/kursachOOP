package com.example.kursachoop.ui.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kursachoop.R;

public class AdminViewDelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_del);


    }
    private static class Products extends RecyclerView.ViewHolder{
        TextView Pname;
        Button btnDel;
        public Products(View itemView){
            super (itemView);
//            Pname = (TextView) itemView.findViewById(R.id.)
        }
    }
}