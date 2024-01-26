package com.example.sm_project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
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

    private String foodName;
    private float price;
    private int imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView titleTextView = findViewById(R.id.titleTxt);
        TextView priceTextView = findViewById(R.id.priceTxt);
        ImageView imgView = findViewById(R.id.img);
        AppCompatButton addBtn = findViewById(R.id.addBtn);

        if (savedInstanceState == null) {
            // Jeśli to pierwsze utworzenie aktywności, ustaw dane z Intent
            foodName = getIntent().getStringExtra("foodname");
            price = getIntent().getFloatExtra("price", 0.1f);
            imagePath = getIntent().getIntExtra("img", 2);
        } else {
            // Jeśli to odtworzenie aktywności po zmianie orientacji, pobierz dane z zapisanego stanu
            foodName = savedInstanceState.getString("foodname");
            price = savedInstanceState.getFloat("price");
            imagePath = savedInstanceState.getInt("img");
            counter = savedInstanceState.getInt("counter", 1);
        }

        titleTextView.setText(foodName);
        priceTextView.setText(String.valueOf(price) + " zł");

        Glide.with(this)
                .load(imagePath)
                .into(imgView);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, CartActivity.class);
                intent.putExtra("foodname", foodName);
                intent.putExtra("price", price);
                intent.putExtra("total", price * counter);
                intent.putExtra("counter", counter);
                intent.putExtra("img", imagePath);

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Zachowaj dane przed zniszczeniem aktywności
        outState.putString("foodname", foodName);
        outState.putFloat("price", price);
        outState.putInt("img", imagePath);
        outState.putInt("counter", counter);
    }

    private void updateCounter() {
        TextView textView = findViewById(R.id.textView13);
        textView.setText(String.valueOf(counter));
        float total = counter * price;
        totalTextView.setText(String.valueOf(total) + " zł");
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void updateOrAddToCart(String foodName, float price, float total, int counter, int imagePath) {
        SharedPreferences preferences = getSharedPreferences("cart_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String jsonCart = preferences.getString("cart_list", "");
        Type type = new TypeToken<List<Foods>>() {}.getType();
        List<Foods> cartList = new Gson().fromJson(jsonCart, type);

        if (cartList == null) {
            cartList = new ArrayList<>();
        }

        boolean productExists = false;
        for (Foods existingFood : cartList) {
            if (existingFood.getTitle().equals(foodName)) {
                existingFood.setNumberInCard(existingFood.getNumberInCard() + counter);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            Foods newFood = new Foods(0.0, imagePath, 0, foodName, price);
            newFood.setNumberInCard(counter);
            cartList.add(newFood);
        }

        String updatedCart = new Gson().toJson(cartList);
        editor.putString("cart_list", updatedCart);
        editor.apply();
    }
}
