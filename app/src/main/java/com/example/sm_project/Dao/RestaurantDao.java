package com.example.sm_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sm_project.Helper.RestaurantTable;

import java.util.List;

@Dao
public interface RestaurantDao {
//    @Query("SELECT EXISTS(SELECT * from restauranttable where name=:name)")
//    void search(String name);

    @Query("SELECT * FROM restauranttable")
    LiveData<List<RestaurantTable>> getAllRestaurants();

    @Insert
    void insert(RestaurantTable restaurant);
}