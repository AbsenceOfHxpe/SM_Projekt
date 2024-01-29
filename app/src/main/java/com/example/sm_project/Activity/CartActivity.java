package com.example.sm_project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartListener {

    private ActivityCartBinding binding;
    private MyDataBase myDB;
    private OrderDao orderDao;
    private CartAdapter cartAdapter;

    private static final String CART_ITEMS_KEY = "cart_items";
    private static final String DISCOUNT_KEY = "discount";
    private static final String USER_COUPON_KEY_PREFIX = "used_coupon_";


    private static final String USER_PREFERENCES_NAME = "user_preferences";
    private static final String USED_COUPON_KEY = "used_coupon";

    private ArrayList<Foods> cartItems;
    private int userId;
    private int restaurantId;

    private static final int DELIVERY_COST = 10;

    private double cost = 0;

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

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences preferences2 = getSharedPreferences("restaurant_data", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        restaurantId = preferences2.getInt("restaurantId", -1);

        initViews();
        initRecyclerView();
        setListeners();

        if (savedInstanceState != null) {
            // Przywróć dane po obrocie ekranu
            cartItems = (ArrayList<Foods>) savedInstanceState.getSerializable(CART_ITEMS_KEY);
            discount = savedInstanceState.getDouble(DISCOUNT_KEY);
            updateCartSummary(calculateTotal(), discount);
        } else {
            // Załaduj dane z SharedPreferences
            if (cartItems == null) {
                cartItems = loadCartData();
            }
            discount = 0.0;
        }

        updateCartSummary(calculateTotal(), discount);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(CART_ITEMS_KEY, cartItems);
        outState.putDouble(DISCOUNT_KEY, discount);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Przywróć dane po obrocie ekranu
        cartItems = (ArrayList<Foods>) savedInstanceState.getSerializable(CART_ITEMS_KEY);
        discount = savedInstanceState.getDouble(DISCOUNT_KEY);
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

        cartItems = loadCartData();
        cartAdapter = new CartAdapter(new ArrayList<>(cartItems), this);
        recyclerView.setAdapter(cartAdapter);
    }

    private void setListeners() {
        confirmBtn.setOnClickListener(v -> {
            float totalAmount = calculateTotal();
            saveTotalAmountToDatabase(totalAmount);
            clearCartData();
            Intent intent = new Intent(CartActivity.this, WaitingActivity.class);
            startActivity(intent);
        });

        couponBtn.setOnClickListener(v -> applyCoupon());
    }

    private void saveTotalAmountToDatabase(double totalSum) {
        Date currentDate = DataConverter.getCurrentDate();
        OrderTable orderTable = new OrderTable(currentDate, cost, userId, restaurantId);
        orderDao.insert(orderTable);
    }

    private void disableCouponUsage() {
        couponTxt.setEnabled(false);
        couponBtn.setEnabled(false);
    }

    private String getUserCouponKey(int userId) {
        return USER_COUPON_KEY_PREFIX + userId;
    }
    private int getUserIdFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        return preferences.getInt("userId", -1);
    }

    private boolean checkIfCouponUsed() {
        int userId = getUserIdFromSharedPreferences();
        if (userId != -1) {
            SharedPreferences userPreferences = getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE);
            String userCouponKey = getUserCouponKey(userId);
            return userPreferences.getBoolean(userCouponKey, false);
        } else {
            return false;
        }
    }

    private void updateCouponUsageStatus() {
        int userId = getUserIdFromSharedPreferences();
        if (userId != -1) {
            SharedPreferences userPreferences = getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE);
            String userCouponKey = getUserCouponKey(userId);
            SharedPreferences.Editor editor = userPreferences.edit();
            editor.putBoolean(userCouponKey, true);
            editor.apply();
        }
    }


    private void updateCartSummary(float total, double discount) {
        double serviceFee = 0.1 * total;
        double discountAmount = discount * total;

        deliveryPrice.setText(String.valueOf(DELIVERY_COST) + " zł");
        totalCartPriceTextView.setText(String.format("%.2f zł", total - discountAmount));
        servicePrice.setText(String.format("%.2f zł", serviceFee));
        totalSum.setText(String.format("%.2f zł", total - discountAmount + DELIVERY_COST + serviceFee));
        cost = (total - discountAmount + DELIVERY_COST + serviceFee);
        double roundedCost = Math.round(cost * 100.0) / 100.0;
        cost = roundedCost;
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



    private void updateCartItemQuantity(Foods updatedFood) {
        for (int i = 0; i < cartItems.size(); i++) {
            Foods food = cartItems.get(i);
            if (food.getId() == updatedFood.getId()) {
                food.setNumberInCard(updatedFood.getNumberInCard());
                break;
            }
        }

        saveCartData(cartItems);
    }

    private ArrayList<Foods> loadCartData() {
        SharedPreferences preferences = getSharedPreferences("cart_data", MODE_PRIVATE);
        String jsonCart = preferences.getString("cart_list", "");

        Type type = new TypeToken<List<Foods>>() {
        }.getType();
        return new Gson().fromJson(jsonCart, type);
    }

    private void saveCartData(ArrayList<Foods> cartItems) {
        SharedPreferences preferences = getSharedPreferences("cart_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String jsonCart = gson.toJson(cartItems);

        editor.putString("cart_list", jsonCart);
        editor.apply();
    }

    private void clearCartData() {
        SharedPreferences preferences = getSharedPreferences("cart_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("cart_list");
        editor.apply();
    }

    @Override
    public void onItemQuantityChanged(Foods food, int newQuantity) {
        food.setNumberInCard(newQuantity);
        updateCartItemQuantity(food);
        cartAdapter.setCartItems(new ArrayList<>(cartItems));
        cartAdapter.notifyDataSetChanged();
        updateCartSummary(calculateTotal(), discount);
    }

}
