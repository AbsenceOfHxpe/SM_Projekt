
package com.example.sm_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sm_project.Helper.RestaurantDishCrossRef;

import java.util.List;
@Dao
public interface RestaurantDishCrossRefDao {
    @Insert
    void insert(RestaurantDishCrossRef crossRef);

    @Query("SELECT * FROM restaurant_dish_cross_ref")
    LiveData<List<RestaurantDishCrossRef>> getAllRDCross();
    @Delete
    void delete(RestaurantDishCrossRef crossRef);

    @Query("SELECT * FROM restaurant_dish_cross_ref WHERE restaurantId = :restaurantId")
    List<RestaurantDishCrossRef> getProductsForRestaurantSync(long restaurantId);

    @Query("SELECT * FROM restaurant_dish_cross_ref")
    List<RestaurantDishCrossRef> getAllRDCrossSync();
}