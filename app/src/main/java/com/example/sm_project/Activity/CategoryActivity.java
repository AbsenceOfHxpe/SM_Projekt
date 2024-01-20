package com.example.sm_project.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_project.R;

public class CategoryActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        String categoryName = getIntent().getStringExtra("categoryName");
        Log.d("CategoriesActivity", "Received category name: " + categoryName);
        TextView categoryNameTextView = findViewById(R.id.titleTxt);
        categoryNameTextView.setText(categoryName);
    }


}
