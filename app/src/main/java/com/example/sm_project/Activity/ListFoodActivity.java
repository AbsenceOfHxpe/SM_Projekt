package com.example.sm_project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sm_project.Adapter.FoodListAdapter;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.R;

import java.util.ArrayList;

public class ListFoodActivity extends AppCompatActivity implements FoodListAdapter.OnFoodClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);

        TextView restaurantNameTextView = findViewById(R.id.titleTxt);

        String restaurantName = getIntent().getStringExtra("nazwaRestauracji");

        restaurantNameTextView.setText(restaurantName);

        ArrayList<Foods> foodsList = new ArrayList<>();
       foodsList.add(new Foods(4.9, R.drawable.food, 50, "Pizza margherita", 20.0));
        foodsList.add(new Foods(4.8, R.drawable.food, 20, "Cheeseburger", 5.7));

        RecyclerView recyclerView = findViewById(R.id.FoodListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FoodListAdapter foodListAdapter = new FoodListAdapter(foodsList, this);
        recyclerView.setAdapter(foodListAdapter);
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
