package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityOrdersBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrdersActivity extends AppCompatActivity {

    ActivityOrdersBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
            startActivity(intent);
        });


        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.home){
                    Intent ordersIntent = new Intent(OrdersActivity.this, MainActivity.class);
                    startActivity(ordersIntent);

                } else if(itemId == R.id.orders){
                    Intent ordersIntent = new Intent(OrdersActivity.this, OrdersActivity.class);
                    startActivity(ordersIntent);

                }else if(itemId == R.id.search){
                    Intent ordersIntent = new Intent(OrdersActivity.this, ListFoodActivity.class);
                    startActivity(ordersIntent);

                }
                return false;
            }
        });
    }
}
