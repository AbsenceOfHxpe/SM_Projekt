package com.example.sm_project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityDetailBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private TextView totalTextView;
    private int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
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
                intent.putExtra("price", price);
                intent.putExtra("total", price * counter);
                intent.putExtra("counter", counter);
                intent.putExtra("img", imagePath);

                // Zapisz dane w SharedPreferences
                updateOrAddToCart(foodName, price, price * counter, counter, imagePath);

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

    private void updateOrAddToCart(String foodName, float price, float total, int counter, int imagePath) {
        SharedPreferences preferences = getSharedPreferences("cart_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Odczytaj obecną listę produktów
        String jsonCart = preferences.getString("cart_list", "");
        Type type = new TypeToken<List<Foods>>() {}.getType();
        List<Foods> cartList = new Gson().fromJson(jsonCart, type);

        if (cartList == null) {
            cartList = new ArrayList<>();
        }

        // Sprawdź, czy produkt już istnieje na liście
        boolean productExists = false;
        for (Foods existingFood : cartList) {
            if (existingFood.getTitle().equals(foodName)) {
                // Produkt istnieje, zwiększ ilość
                existingFood.setNumberInCard(existingFood.getNumberInCard() + counter);
                productExists = true;
                break;
            }
        }

        // Jeśli produkt nie istnieje, dodaj go do listy
        if (!productExists) {
            Foods newFood = new Foods(0.0, imagePath, 0, foodName, price);
            newFood.setNumberInCard(counter);
            cartList.add(newFood);
        }

        // Zapisz zaktualizowaną listę produktów
        String updatedCart = new Gson().toJson(cartList);
        editor.putString("cart_list", updatedCart);

        editor.apply();
    }
}
