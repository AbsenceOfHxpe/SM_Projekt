package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_project.Adapter.CartAdapter;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView totalCartPriceTextView;
    private TextView deliveryPrice;
    private TextView servicePrice;
    private TextView totalSum;

    private int delivery = 10;
    private double discount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent exitAppIntent = new Intent("ExitApp");
        sendBroadcast(exitAppIntent);

        // Pobierz dane z Intent
        String foodName = getIntent().getStringExtra("foodname");
        float total = getIntent().getFloatExtra("total", 0.1f);
        int img = getIntent().getIntExtra("img", 2);
        float totalEachItem = (float) getIntent().getDoubleExtra("price", 0.1f);
        int counter = getIntent().getIntExtra("counter", 0);

        // Przygotuj dane dla listy zakupów
        ArrayList<Foods> cartItems = new ArrayList<>();
        cartItems.add(new Foods(totalEachItem, img, 1, foodName, total));

        // Ustawienie RecyclerView
        recyclerView = findViewById(R.id.cardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicjalizacja i ustawienie adaptera
        cartAdapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(cartAdapter);

        totalCartPriceTextView = findViewById(R.id.totalFeeTxt);
        totalCartPriceTextView.setText(String.valueOf(total));

        deliveryPrice = findViewById(R.id.deliveryTxt);
        deliveryPrice.setText(String.valueOf(delivery));

        servicePrice = findViewById(R.id.serviceTxt);

        totalSum = findViewById(R.id.totalSumTxt);
        totalSum.setText(String.valueOf(total + delivery));

        Button couponBtn = findViewById(R.id.couponBtn);
        EditText couponTxt = findViewById(R.id.couponTxt);

        couponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCoupon = couponTxt.getText().toString();

                // Sprawdź kupon rabatowy
                if (enteredCoupon.equals("12345")) {
                    discount = 0.15;
                    updateCartSummary(total, discount);
                } else {

                }
            }
        });

        // Początkowe ustawienie sumy koszyka
        updateCartSummary(total, discount);
    }

    private void updateCartSummary(float total, double discount) {
        double serviceFee = 0.1 * total;
        double discountAmount = discount * total;

        // Ustawienie wartości w TextView
        totalCartPriceTextView.setText(String.valueOf(total - discountAmount + " zł"));
        deliveryPrice.setText(String.valueOf(delivery + " zł"));
        servicePrice.setText(String.valueOf(serviceFee) + " zł");
        totalSum.setText(String.valueOf((total - discountAmount + delivery + serviceFee) + " zł"));
    }
}
