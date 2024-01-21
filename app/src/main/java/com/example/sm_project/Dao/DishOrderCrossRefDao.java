

package com.example.sm_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sm_project.Helper.DishOrderCrossRef;

import java.util.List;
@Dao
public interface DishOrderCrossRefDao {
    @Insert
    void insert(DishOrderCrossRef crossRef);

    @Query("SELECT * FROM dish_order_cross_ref")
    LiveData<List<DishOrderCrossRef>> getAllDOCross();

    @Query("SELECT * FROM dish_order_cross_ref")
    List<DishOrderCrossRef> getAllDOCrossSync();
}