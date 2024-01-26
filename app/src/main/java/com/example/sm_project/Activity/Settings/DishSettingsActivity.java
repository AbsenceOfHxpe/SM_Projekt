package com.example.sm_project.Activity.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityDishManagmentBinding;
import com.example.sm_project.databinding.ActivityOrderManagmentBinding;

public class DishSettingsActivity extends AppCompatActivity {

    private ActivityDishManagmentBinding binding;

    private ImageView backBtn;
    private AppCompatButton addBtn, deleteBtn, editBtn;
    private TextView titleTxt;

    private MyDataBase myDB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDishManagmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();

    }

    private void initializeViews() {
        titleTxt = findViewById(R.id.titleTxt);
        titleTxt.setText(getString(R.string.settings_dish));

        backBtn = findViewById(R.id.backBtn);
        addBtn = findViewById(R.id.addBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        backBtn.setOnClickListener(v -> startActivity(new Intent(DishSettingsActivity.this, SettingsActivity.class)));
        //addBtn.setOnClickListener(v -> showRestaurantDialog(Actions.ADD, null));
        //deleteBtn.setOnClickListener(v -> showDeleteRestaurantListDialog());
        //editBtn.setOnClickListener(v -> showEditRestaurantListDialog());
    }
}
