package com.example.sm_project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sm_project.Adapter.FoodListAdapter;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityOrdersBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ListFoodActivity extends AppCompatActivity implements FoodListAdapter.OnFoodClickListener {
    ActivityOrdersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_list_food);

        TextView restaurantNameTextView = findViewById(R.id.titleTxt);

        String restaurantName = getIntent().getStringExtra("nazwaRestauracji");
        int restaurantId = getIntent().getIntExtra("restaurantId", -1);


        Log.d("ListFoodActivity", "Received restaurantId: " + restaurantId);
        Log.d("ListFoodActivity", "Received restaurantName: " + restaurantName);

        restaurantNameTextView.setText(restaurantName);

        ArrayList<Foods> foodsList = new ArrayList<>();
       foodsList.add(new Foods(4.9, R.drawable.food, 50, "Pizza margherita", 20.0));
        foodsList.add(new Foods(4.8, R.drawable.food, 20, "Cheeseburger", 5.7));

        RecyclerView recyclerView = findViewById(R.id.FoodListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FoodListAdapter foodListAdapter = new FoodListAdapter(foodsList, this);
        recyclerView.setAdapter(foodListAdapter);

        setVariable();


        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.home){
                    Intent ordersIntent = new Intent(ListFoodActivity.this, MainActivity.class);
                    startActivity(ordersIntent);

                } else if(itemId == R.id.orders){
                    Intent ordersIntent = new Intent(ListFoodActivity.this, OrdersActivity.class);
                    startActivity(ordersIntent);

                }else if(itemId == R.id.search){
                    Intent ordersIntent = new Intent(ListFoodActivity.this, ListFoodActivity.class);
                    startActivity(ordersIntent);

                }
                return false;
            }
        });
    }
    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    @Override
    public void onFoodClick(String foodName, float price, int img) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("foodname", foodName);
        intent.putExtra("price", price);
        intent.putExtra("img", img);
        startActivity(intent);
    }
}
