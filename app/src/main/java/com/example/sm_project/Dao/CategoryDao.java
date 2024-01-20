package com.example.sm_project.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

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

    @Query("SELECT EXISTS(SELECT * FROM categorytable WHERE name=:name)")
    boolean isNameTaken(String name);

    @Query("SELECT id FROM categorytable WHERE name = :categoryName")
    int getCategoryIdByName(String categoryName);


}

