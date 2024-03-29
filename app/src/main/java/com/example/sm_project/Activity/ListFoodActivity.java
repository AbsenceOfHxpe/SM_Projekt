package com.example.sm_project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sm_project.Adapter.FoodListAdapter;
import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.Helper.DishTable;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityOrdersBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ListFoodActivity extends AppCompatActivity implements FoodListAdapter.OnFoodClickListener {
    ActivityOrdersBinding binding;
    MyDataBase myDB;

    private RestaurantDao restaurantDao;
    private ProgressBar progressBar;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_list_food);
        backBtn = findViewById(R.id.backBtn);

        // Inicjalizacja restaurantDao (pamiętaj, że musisz mieć odpowiednią instancję bazy danych)
        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        restaurantDao = myDB.getRestaurantDao();
        progressBar = findViewById(R.id.progressBar);

        TextView restaurantNameTextView = findViewById(R.id.titleTxt);

        String restaurantName = getIntent().getStringExtra("nazwaRestauracji");
        int restaurantId = getIntent().getIntExtra("restaurantId", -1);

        Log.d("ListFoodActivity", "Received restaurantId: " + restaurantId);
        Log.d("ListFoodActivity", "Received restaurantName: " + restaurantName);

        restaurantNameTextView.setText(restaurantName);

        SharedPreferences preferences = getSharedPreferences("restaurant_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("restaurantId", restaurantId);
        editor.apply();
        Log.i("TAG","RestaurantId: "+ restaurantId + "");

        backBtn.setOnClickListener(v -> {
            if (hasDataInCart()) {
                showLeaveDialog();
            } else {
                finish();
            }
        });

        // Pobierz listę dań dla danej restauracji za pomocą DAO
        List<DishTable> dishesForRestaurant = restaurantDao.getDishesForRestaurant(restaurantId);

        // Przygotuj listę Foods na podstawie pobranych danych z bazy danych
        ArrayList<Foods> foodsList = new ArrayList<>();
        for (DishTable dish : dishesForRestaurant) {
            foodsList.add(new Foods(dish.getRating(), dish.getImagePath(), dish.getRating(), dish.getName(), dish.getPrice()));
        }
        Log.d("ListFoodActivity", "Dishes for restaurant: " + dishesForRestaurant.toString());
        progressBar.setVisibility(View.GONE);

        RecyclerView recyclerView = findViewById(R.id.FoodListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FoodListAdapter foodListAdapter = new FoodListAdapter(foodsList, this);
        recyclerView.setAdapter(foodListAdapter);

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
                } else if(itemId == R.id.search){
                    if (hasDataInCart()) {
                        showLeaveDialog();
                    } else {
                        Intent ordersIntent = new Intent(ListFoodActivity.this, ListFoodActivity.class);
                        startActivity(ordersIntent);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onFoodClick(String foodName, float price, int img) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("foodname", foodName);
        intent.putExtra("price", price);
        intent.putExtra("img", img);
        startActivity(intent);
    }

    private boolean hasDataInCart() {
        SharedPreferences cartPreferences = getSharedPreferences("cart_data", MODE_PRIVATE);
        return cartPreferences.contains("cart_list");
    }

    private void showLeaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Masz produkty w koszyku. Czy na pewno chcesz opuścić restaurację?");
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearOrderSharedPreferences();
                finish();
            }
        });
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void clearOrderSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("cart_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("cart_list");
        editor.apply();
    }
}
