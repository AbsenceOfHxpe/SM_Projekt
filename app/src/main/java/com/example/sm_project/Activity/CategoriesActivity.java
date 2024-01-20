package com.example.sm_project.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_project.R;

public class CategoriesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        String categoryName = getIntent().getStringExtra("categoryName");
        TextView categoryNameTextView = findViewById(R.id.titleTxt);
        categoryNameTextView.setText(categoryName);


    }
}
