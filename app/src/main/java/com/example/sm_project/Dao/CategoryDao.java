package com.example.sm_project.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sm_project.Helper.CategoryTable;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categorytable")
    LiveData<List<CategoryTable>> getAllCategories();

    @Insert
    long insert(CategoryTable category);
}
