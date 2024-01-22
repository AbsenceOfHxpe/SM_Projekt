package com.example.sm_project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.sm_project.Adapter.CartAdapter;
import com.example.sm_project.Converter.DataConverter;
import com.example.sm_project.Dao.OrderDao;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.OrderTable;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityCartBinding;

import java.util.ArrayList;
import java.util.Date;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartListener {

    private ActivityCartBinding binding;
    private MyDataBase myDB;
    private OrderDao orderDao;
    private CartAdapter cartAdapter;

    private static final String USER_PREFERENCES_NAME = "user_preferences";
    private static final String USED_COUPON_KEY = "used_coupon";

    private ArrayList<Foods> cartItems;
    private int userId; // Dodane pole przechowujące userId

    private static final int DELIVERY_COST = 10;

    private TextView totalCartPriceTextView;
    private TextView deliveryPrice;
    private TextView servicePrice;
    private TextView totalSum;
    private EditText couponTxt;
    private AppCompatButton confirmBtn, couponBtn;

    private double discount = 0.0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        orderDao = myDB.getOrderDao();

        // Pobierz userId z SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        initViews();
        initRecyclerView();

        setListeners();

        updateCartSummary(calculateTotal(), discount);
    }

    private void initViews() {
        totalCartPriceTextView = findViewById(R.id.totalFeeTxt);
        deliveryPrice = findViewById(R.id.deliveryTxt);
        servicePrice = findViewById(R.id.serviceTxt);
        totalSum = findViewById(R.id.totalSumTxt);
        confirmBtn = findViewById(R.id.confirmBtn);
        couponTxt = findViewById(R.id.couponTxt);
        couponBtn = findViewById(R.id.couponBtn);
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.cardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String foodName = extras.getString("foodname");
            float price = extras.getFloat("price");
            int counter = extras.getInt("counter");
            int imagePath = extras.getInt("img");

            Foods food = new Foods(0.0, imagePath, 0, foodName, price);
            food.setNumberInCard(counter);
            cartItems.add(food);
        }

        cartAdapter = new CartAdapter(cartItems, this);
        recyclerView.setAdapter(cartAdapter);
    }

    private void setListeners() {
        confirmBtn.setOnClickListener(v -> {
            float totalAmount = calculateTotal();
            saveTotalAmountToDatabase(totalAmount);
            Intent intent = new Intent(CartActivity.this, WaitingActivity.class);
            startActivity(intent);
        });

        couponBtn.setOnClickListener(v -> applyCoupon());
    }

    private void saveTotalAmountToDatabase(double totalSum) {
        Date date = DataConverter.fromString("24.10.2033");

        OrderTable orderTable = new OrderTable(date, totalSum, userId, 2);
        orderDao.insert(orderTable);
    }

    private void disableCouponUsage() {
        couponTxt.setEnabled(false);
        couponBtn.setEnabled(false);
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

        deliveryPrice.setText(String.valueOf(DELIVERY_COST) + " zł");
        totalCartPriceTextView.setText(String.format("%.2f zł", total - discountAmount));
        servicePrice.setText(String.format("%.2f zł", serviceFee));
        totalSum.setText(String.format("%.2f zł", total - discountAmount + DELIVERY_COST + serviceFee));
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

        if (!checkIfCouponUsed()) {
            if (enteredCoupon.equals("12345")) {
                discount = 0.15;
                updateCartSummary(calculateTotal(), discount);
                Toast.makeText(this, "Wykorzystano kupon rabatowy", Toast.LENGTH_SHORT).show();
                updateCouponUsageStatus();
                disableCouponUsage();
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
