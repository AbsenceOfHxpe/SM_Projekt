package com.example.sm_project.Activity.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityDishManagmentBinding;

public class DishSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.sm_project.databinding.ActivityDishManagmentBinding binding = ActivityDishManagmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();

    }

    private void initializeViews() {
        TextView titleTxt = findViewById(R.id.titleTxt);
        titleTxt.setText(getString(R.string.settings_dish));

        ImageView backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> startActivity(new Intent(DishSettingsActivity.this, SettingsActivity.class)));
    }
}
