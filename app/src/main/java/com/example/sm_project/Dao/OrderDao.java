package com.example.sm_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.Helper.OrderTable;

import java.util.List;


@Dao
public interface OrderDao {

    @Query("SELECT * FROM ordertable")
    LiveData<List<OrderTable>> getAllOrders();

    @Query("SELECT * FROM ordertable")
    List<OrderTable> getAllOrdersSync();

    @Insert
    long insert(OrderTable orderTable);

    @Query("SELECT 1 FROM OrderTable LIMIT 1")
    int doNothing();



}
