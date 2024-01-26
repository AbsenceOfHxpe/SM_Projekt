package com.example.sm_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.Helper.OrderTable;

import java.util.List;


@Dao
public interface OrderDao {


    @Query("SELECT * FROM ordertable")
    LiveData<List<OrderTable>> getAllOrders();

    @Query("SELECT * FROM ordertable")
    List<OrderTable> getAllOrdersSync();

    @Query("SELECT * FROM OrderTable ORDER BY date DESC")
    List<OrderTable> getAllOrdersSyncDesc();

    @Insert
    long insert(OrderTable orderTable);
    @Delete
    void delete(OrderTable orderTable);

    @Update
    void update(OrderTable orderTable);

    @Query("UPDATE ordertable " +
            "SET restaurantId = :editedRestaurantId, date = :editedOrderDate, price = :editedOrderAmount " +
            "WHERE id = :orderId")
    void updateOrder(long orderId, long editedRestaurantId, String editedOrderDate, String editedOrderAmount);


    @Query("SELECT 1 FROM OrderTable LIMIT 1")
    int doNothing();



}
