package com.example.sm_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.sm_project.Domain.EditRestaurantData;
import com.example.sm_project.Helper.DishTable;
import com.example.sm_project.Helper.RestaurantTable;
//import com.example.sm_project.Helper.RestaurantWithDishes;

import java.util.List;

@Dao
public interface RestaurantDao {
//    @Query("SELECT EXISTS(SELECT * from restauranttable where name=:name)")
//    void search(String name);

    @Query("SELECT * FROM restauranttable")
    LiveData<List<RestaurantTable>> getAllRestaurants();

    @Insert
    long insert(RestaurantTable restaurant);

    @Delete
    void delete(RestaurantTable restaurant);

    @Update
    void update(RestaurantTable restaurant);

    @Query("DELETE FROM restauranttable WHERE id = :restaurantId")
    void deleteById(int restaurantId);

    @Transaction
    @Query("UPDATE restauranttable SET name = :newName WHERE id = :restaurantId")
    void updateName(int restaurantId, String newName);

    @Query("SELECT * FROM restauranttable")
    List<RestaurantTable> getAllRestaurantsSync();

    @Query("SELECT * FROM restauranttable WHERE categoryId = :categoryId")
    LiveData<List<RestaurantTable>> getRestaurantsByCategory(int categoryId);

    @Query("SELECT name FROM restauranttable WHERE id = :restaurantId")
    String getRestaurantNameById(int restaurantId);

    @Query("SELECT id FROM restauranttable WHERE name = :restaurantName")
    int getRestaurantIdByName(String restaurantName);

    @Query("SELECT dishtable.* FROM dishtable " +
            "INNER JOIN restaurant_dish_cross_ref ON dishtable.id = restaurant_dish_cross_ref.dishId " +
            "WHERE restaurant_dish_cross_ref.restaurantId = :restaurantId")
    List<DishTable> getDishesForRestaurant(int restaurantId);

    @Transaction
    @Query("UPDATE restauranttable SET name = :newName WHERE id = :restaurantId")
    void update(int restaurantId, String newName);






}

