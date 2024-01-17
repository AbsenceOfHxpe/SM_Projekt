package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityOrdersBinding;

public class OrdersActivity extends AppCompatActivity {

    ActivityOrdersBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        binding.backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }
}
