package com.example.sm_project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private TextView totalTextView;
    private int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());

        TextView titleTextView = findViewById(R.id.titleTxt);
        TextView priceTextView = findViewById(R.id.priceTxt);
        ImageView imgView = findViewById(R.id.img);
        AppCompatButton addBtn = findViewById(R.id.addBtn);

        String foodName = getIntent().getStringExtra("foodname");
        float price = getIntent().getFloatExtra("price", 0.1f);
        int imagePath = getIntent().getIntExtra("img", 2);

        titleTextView.setText(foodName);
        priceTextView.setText(String.valueOf(price) + " zł");

        Glide.with(this)
                .load(imagePath)
                .into(imgView);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Przygotuj dane do przekazania
                Intent intent = new Intent(DetailActivity.this, CartActivity.class);
                intent.putExtra("foodname", foodName);
                intent.putExtra("price", price*counter);

                intent.putExtra("img", imagePath);

                // Uruchom nową aktywność
                startActivity(intent);
            }
        });

        TextView plusButton = findViewById(R.id.plusBtn);
        TextView minusButton = findViewById(R.id.minusBtn);
        totalTextView = findViewById(R.id.totalTxt);

        setVariable();
        updateCounter();
        plusButton.setOnClickListener(v -> {
            counter++;
            updateCounter();
        });

        minusButton.setOnClickListener(v -> {
            if (counter > 0) {
                counter--;
                updateCounter();
            }
        });
    }

    private void updateCounter() {
        TextView textView = findViewById(R.id.textView13);
        textView.setText(String.valueOf(counter));
        float total = counter * getIntent().getFloatExtra("price", 0.0f);
        totalTextView.setText(String.valueOf(total) + " zł");
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
