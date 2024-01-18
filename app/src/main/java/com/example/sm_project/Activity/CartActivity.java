package com.example.sm_project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sm_project.Adapter.CartAdapter;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Pobierz dane z Intent
        String foodName = getIntent().getStringExtra("foodname");
        float price = getIntent().getFloatExtra("price", 0.0f);
        int img = getIntent().getIntExtra("img", 2);

        // Przygotuj dane dla listy zakupów
        ArrayList<Foods> cartItems = new ArrayList<>();
        cartItems.add(new Foods(10, img, 1, foodName, price)); // Ustaw odpowiednie dane

        // Ustawienie RecyclerView
        recyclerView = findViewById(R.id.cardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicjalizacja i ustawienie adaptera
        cartAdapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(cartAdapter);

        // Wyświetlenie sumy koszyka
        TextView totalCartPriceTextView = findViewById(R.id.totalFeeTxt);
        float totalCartPrice = calculateTotalCartPrice(cartItems);
        totalCartPriceTextView.setText(String.valueOf(totalCartPrice) + " zł");
    }

    // Metoda do obliczania całkowitej wartości koszyka
    private float calculateTotalCartPrice(ArrayList<Foods> cartItems) {
        float total = 0.0f;
        for (Foods item : cartItems) {
            total += item.getNumberInCard() * item.getPrice();
        }
        return total;
    }
}
