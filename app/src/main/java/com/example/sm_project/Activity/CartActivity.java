package com.example.sm_project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_project.Adapter.CartAdapter;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartListener {

    private static final String USER_PREFERENCES_NAME = "user_preferences";
    private static final String USED_COUPON_KEY = "used_coupon";

    private ArrayList<Foods> cartItems;
    private CartAdapter cartAdapter;

    private static int delivery = 10;

    private TextView totalCartPriceTextView;
    private TextView deliveryPrice;
    private TextView servicePrice;
    private TextView totalSum;
    private EditText couponTxt;
    private AppCompatButton confirmBtn;

    private ImageView backBtn;

    private double discount = 0.0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent exitAppIntent = new Intent("ExitApp");
        sendBroadcast(exitAppIntent);

        // Initialize your RecyclerView and set its layout manager
        RecyclerView recyclerView = findViewById(R.id.cardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the cartItems list with some example data
        cartItems = new ArrayList<>();

        // Check if there are extras in the Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Retrieve data from the Intent
            String foodName = extras.getString("foodname");
            float price = extras.getFloat("price");
            int counter = extras.getInt("counter");
            int imagePath = extras.getInt("img");

            // Create a Foods object with the retrieved data
            Foods food = new Foods(0.0, imagePath, 0, foodName, price);
            food.setNumberInCard(counter);
            cartItems.add(food);
        }

        cartAdapter = new CartAdapter(cartItems, this);
        recyclerView.setAdapter(cartAdapter);

        // Initialize TextViews
        totalCartPriceTextView = findViewById(R.id.totalFeeTxt);
        deliveryPrice = findViewById(R.id.deliveryTxt);
        servicePrice = findViewById(R.id.serviceTxt);
        totalSum = findViewById(R.id.totalSumTxt);
        confirmBtn = findViewById(R.id.confirmBtn);
        backBtn = findViewById(R.id.backBtn);
        couponTxt = findViewById(R.id.couponTxt);
        Button couponBtn = findViewById(R.id.couponBtn);


        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, DetailActivity.class);
            startActivity(intent);
        });


        couponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCoupon();
            }
        });

        if (checkIfCouponUsed()) {
            disableCouponUsage();
        }



        updateCartSummary(calculateTotal(), discount);
    }

    private void disableCouponUsage() {
        couponTxt.setEnabled(true);
        confirmBtn.setEnabled(true);
    }

    private boolean checkIfCouponUsed() {
        SharedPreferences userPreferences = getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE);
        return userPreferences.getBoolean(USED_COUPON_KEY, false);
    }

    private void updateCouponUsageStatus() {
        SharedPreferences userPreferences = getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putBoolean(USED_COUPON_KEY, true);
        editor.apply();
    }

    private void updateCartSummary(float total, double discount) {
        double serviceFee = 0.1 * total;
        double discountAmount = discount * total;

        // Ustawienie wartości w TextView
        deliveryPrice.setText(String.valueOf(delivery) + " zł");
        totalCartPriceTextView.setText(String.format("%.2f zł", total - discountAmount));
        servicePrice.setText(String.format("%.2f zł", serviceFee));
        totalSum.setText(String.format("%.2f zł", total - discountAmount + delivery + serviceFee));
    }

    private float calculateTotal() {
        float total = 0;
        for (Foods food : cartAdapter.getCartItems()) {
            total += (food.getPrice() * food.getNumberInCard());
        }
        return total;
    }

    private void applyCoupon() {
        String enteredCoupon = couponTxt.getText().toString();

        if (checkIfCouponUsed()) {
            disableCouponUsage();

            if (enteredCoupon.equals("12345")) {
                discount = 0.15;
                updateCartSummary(calculateTotal(), discount);
                Toast.makeText(this, "Wykorzystano kupon rabatowy", Toast.LENGTH_SHORT).show();

                // Zaktualizuj status wykorzystania kuponu
                updateCouponUsageStatus();

            } else {
                Toast.makeText(this, "Nieprawidłowy kupon rabatowy", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Kupon został już wcześniej wykorzystany", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemQuantityChanged(Foods food, boolean increase) {
        int currentQuantity = food.getNumberInCard();
        if (increase) {
            currentQuantity++;
        } else {
            if (currentQuantity > 0) {
                currentQuantity--;
            }
        }
        food.setNumberInCard(currentQuantity);
        cartAdapter.notifyDataSetChanged();
        updateCartSummary(calculateTotal(), discount);
    }
}
