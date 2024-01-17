package com.example.sm_project.Helper;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "dish_order_cross_ref",
        primaryKeys = {"dishId", "orderId"},
        foreignKeys = {
                @ForeignKey(entity = DishTable.class,
                        parentColumns = "id",
                        childColumns = "dishId"),
                @ForeignKey(entity = OrderTable.class,
                        parentColumns = "id",
                        childColumns = "orderId")
        })
public class DishOrderCrossRef {
    private int dishId;
    private int orderId;

    public DishOrderCrossRef(int dishId, int orderId) {
        this.dishId = dishId;
        this.orderId = orderId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
