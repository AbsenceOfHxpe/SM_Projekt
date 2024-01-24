package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.sm_project.Adapter.BestRestAdapter;
import com.example.sm_project.Adapter.CategoryAdapter;
import com.example.sm_project.Adapter.OrdersAdapter;
import com.example.sm_project.Dao.CategoryDao;
import com.example.sm_project.Dao.OrderDao;
import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.OrderTable;
import com.example.sm_project.Helper.RestaurantTable;
import com.example.sm_project.R;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    MyDataBase myDB;
    private CategoryDao categoryDao;
    private RestaurantDao restaurantDao;


    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        String categoryName = getIntent().getStringExtra("categoryName");
        Log.d("CategoriesActivity", "Received category name: " + categoryName);
        TextView categoryNameTextView = findViewById(R.id.titleTxt);
        backBtn = findViewById(R.id.backBtn);
        categoryNameTextView.setText(categoryName);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
            startActivity(intent);
        });

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        categoryDao = myDB.getCategoryDao();
        restaurantDao = myDB.getRestaurantDao();


        int categoryId = categoryDao.getCategoryIdByName(categoryName);

        RecyclerView recyclerViewRest = findViewById(R.id.RestView);
        recyclerViewRest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<RestaurantTable> restaurantTables = categoryDao.getRestaurantsByCategorySync(categoryId);
        BestRestAdapter RestAdapter = new BestRestAdapter(restaurantTables);
        recyclerViewRest.setAdapter(RestAdapter);



        RestAdapter.setOnRestaurantClickListener(new BestRestAdapter.OnRestaurantClickListener() {
            @Override
            public void onRestaurantClick(RestaurantTable restaurant) {
                Intent intent = new Intent(CategoryActivity.this, ListFoodActivity.class);
                intent.putExtra("restaurantId", restaurant.getId());
                intent.putExtra("nazwaRestauracji", restaurant.getName());
                startActivity(intent);
            }
        });

        RestAdapter.setOnDataLoadedListener(new BestRestAdapter.OnDataLoadedListener() {
            @Override
            public void onDataLoaded() {
                ProgressBar progressBarCategory = findViewById(R.id.progressBar);
                progressBarCategory.setVisibility(View.GONE);
            }
        });

    }
}

