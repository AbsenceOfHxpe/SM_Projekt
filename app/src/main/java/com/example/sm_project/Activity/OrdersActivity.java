package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.sm_project.Adapter.BestRestAdapter;
import com.example.sm_project.Adapter.OrdersAdapter;
import com.example.sm_project.Dao.CategoryDao;
import com.example.sm_project.Dao.OrderDao;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.OrderTable;
import com.example.sm_project.Helper.RestaurantTable;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityOrdersBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    ActivityOrdersBinding binding;

    MyDataBase myDB;
    private OrderDao orderDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        orderDao = myDB.getOrderDao();


        RecyclerView recyclerViewRest = findViewById(R.id.cardViewOrders);
        recyclerViewRest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<OrderTable> orderTables = orderDao.getAllOrdersSync();
        OrdersAdapter ordersAdapter = new OrdersAdapter(orderTables);
        recyclerViewRest.setAdapter(ordersAdapter);

        binding.backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
            startActivity(intent);
        });


        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.home){
                    Intent ordersIntent = new Intent(OrdersActivity.this, MainActivity.class);
                    startActivity(ordersIntent);

                } else if(itemId == R.id.orders){
                    Intent ordersIntent = new Intent(OrdersActivity.this, OrdersActivity.class);
                    startActivity(ordersIntent);

                }else if(itemId == R.id.search){
                    Intent ordersIntent = new Intent(OrdersActivity.this, ListFoodActivity.class);
                    startActivity(ordersIntent);

                }
                return false;
            }
        });
    }
}
