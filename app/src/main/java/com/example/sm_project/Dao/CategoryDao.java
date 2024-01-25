package com.example.sm_project.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.sm_project.Domain.CategoryWithRestaurants;
import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.Helper.RestaurantTable;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categorytable")
    LiveData<List<CategoryTable>> getAllCategories();

    @Query("SELECT * FROM categorytable")
    List<CategoryTable> getAllCategoriesSync();

    @Insert
    long insert(CategoryTable category);

    @Update
    void update(CategoryTable category);

    @Delete
    void delete(CategoryTable category);

    @Query("DELETE FROM categorytable WHERE name = :categoryName")
    int deleteByName(String categoryName);


    @Query("SELECT EXISTS(SELECT * FROM categorytable WHERE name=:name)")
    boolean isNameTaken(String name);
    @Query("SELECT * FROM restauranttable WHERE categoryId = :categoryId")
    List<RestaurantTable> getRestaurantsByCategorySync(int categoryId);

    @Query("SELECT id FROM categorytable WHERE name = :categoryName")
    int getCategoryIdByName(String categoryName);

    @Query("SELECT COUNT(*) FROM restauranttable WHERE categoryId = (SELECT id FROM categorytable WHERE name = :categoryName)")
    int getRestaurantCountForCategory(String categoryName);

    @Transaction
    @Query("SELECT * FROM categorytable")
    LiveData<List<CategoryWithRestaurants>> getAllCategoriesWithRestaurants();


}

