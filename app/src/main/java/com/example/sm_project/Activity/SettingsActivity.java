package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityRegisterBinding;
import com.example.sm_project.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private ImageView backBtn;
    private AppCompatButton categorySet, restSet, dishSet, orderSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backBtn = findViewById(R.id.backBtn);
        categorySet = findViewById(R.id.catSet);
        restSet = findViewById(R.id.restSet);
        dishSet = findViewById(R.id.dishSet);
        orderSet = findViewById(R.id.ordersSet);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        categorySet.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, CategorySettingsActivity.class);
            startActivity(intent);
        });

        restSet.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, RestSettingsActivity.class);
            startActivity(intent);
        });
    }
}
