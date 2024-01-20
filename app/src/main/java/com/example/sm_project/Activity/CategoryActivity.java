package com.example.sm_project.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.sm_project.Adapter.BestRestAdapter;
import com.example.sm_project.Dao.CategoryDao;
import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.RestaurantTable;
import com.example.sm_project.R;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    MyDataBase myDB;
    private CategoryDao categoryDao;
    private RestaurantDao restaurantDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        String categoryName = getIntent().getStringExtra("categoryName");
        Log.d("CategoriesActivity", "Received category name: " + categoryName);
        TextView categoryNameTextView = findViewById(R.id.titleTxt);
        categoryNameTextView.setText(categoryName);

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        categoryDao = myDB.getCategoryDao();
        restaurantDao = myDB.getRestaurantDao();

        int categoryId = categoryDao.getCategoryIdByName(categoryName);

        RecyclerView recyclerViewRest = findViewById(R.id.RestView);
        recyclerViewRest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Użyj zaktualizowanego zapytania, aby pobrać restauracje dla danej kategorii
        List<RestaurantTable> restaurantTables = categoryDao.getRestaurantsByCategorySync(categoryId);
        BestRestAdapter RestAdapter = new BestRestAdapter(restaurantTables);
        recyclerViewRest.setAdapter(RestAdapter);
    }
}

