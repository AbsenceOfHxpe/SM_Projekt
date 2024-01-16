package com.example.sm_project.Activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_project.Adapter.BestFoodAdapter;
import com.example.sm_project.Adapter.BestRestAdapter;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.Domain.Restaurants;
import com.example.sm_project.Helper.DataBaseHelper;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityMainBinding;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DataBaseHelper(this);

        initLocation();
        initTime();
        initPrice();
        initBestRestaurant();
    }

    private void initBestRestaurant() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        binding.progressBarBestRest.setVisibility(View.VISIBLE);
        ArrayList<Restaurants> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM YourTableName WHERE BestRestaurant = 1", null);

        int nameIndex = cursor.getColumnIndex("name");

        if (nameIndex != -1){
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(nameIndex);

                Restaurants restaurants = new Restaurants(name);
                list.add(restaurants);
            } while (cursor.moveToNext());
        }
        }

        cursor.close();
        db.close();

        if (list.size() > 0) {
            binding.bestRestView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
            RecyclerView.Adapter adapter = new BestRestAdapter(list);
            binding.bestRestView.setAdapter(adapter);
        }

        binding.progressBarBestRest.setVisibility(View.GONE);
    }



    private void initLocation() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Location> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM Location", null);

        int latitudeIndex = cursor.getColumnIndex("latitude");
        int longitudeIndex = cursor.getColumnIndex("longitude");

        if (latitudeIndex != -1 && longitudeIndex != -1) {
            while (cursor.moveToNext()) {
                double latitude = cursor.getDouble(latitudeIndex);
                double longitude = cursor.getDouble(longitudeIndex);

                Location location = new Location("");
                location.setLatitude(latitude);
                location.setLongitude(longitude);

                list.add(location);
            }
        }

        cursor.close();
        db.close();

        ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.locationSp.setAdapter(adapter);
    }

    private void initTime() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Time> timeList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM Time", null);

        int timeIndex = cursor.getColumnIndex("time_column"); // Zmień na nazwę rzeczywistej kolumny w bazie danych

        if (timeIndex != -1) {
            while (cursor.moveToNext()) {
                long timeMillis = cursor.getLong(timeIndex);

                Time time = new Time(timeMillis);

                timeList.add(time);
            }
        }

        cursor.close();
        db.close();

        ArrayAdapter<Time> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, timeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.timeSp.setAdapter(adapter);
    }

    private void initPrice() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<BigDecimal> priceList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM Price", null);

        int priceIndex = cursor.getColumnIndex("price_column");

        if (priceIndex != -1) {
            while (cursor.moveToNext()) {
                double priceValue = cursor.getDouble(priceIndex);


                BigDecimal price = BigDecimal.valueOf(priceValue);

                priceList.add(price);
            }
        }

        cursor.close();
        db.close();

        ArrayAdapter<BigDecimal> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, priceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.timeSp.setAdapter(adapter);
    }
}
