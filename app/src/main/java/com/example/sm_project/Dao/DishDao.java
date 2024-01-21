package com.example.sm_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sm_project.Helper.DishTable;
import com.example.sm_project.Helper.RestaurantTable;

import java.util.List;

@Dao
public interface DishDao {


    @Query("SELECT * FROM dishtable")
    LiveData<List<DishTable>> getAllDishes();

    @Insert
    long insert(DishTable dishTable);

    @Query("SELECT * FROM dishtable")
    List<DishTable> getAllDishesSync();


}
