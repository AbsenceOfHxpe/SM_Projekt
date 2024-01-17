package com.example.sm_project.Helper;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "restaurant_dish_cross_ref",
        primaryKeys = {"restaurantId", "dishId"},
        foreignKeys = {
                @ForeignKey(entity = RestaurantTable.class,
                        parentColumns = "id",
                        childColumns = "restaurantId"),
                @ForeignKey(entity = DishTable.class,
                        parentColumns = "id",
                        childColumns = "dishId")
        })
public class RestaurantDishCrossRef {
    private int restaurantId;
    private int dishId;

    public RestaurantDishCrossRef(int restaurantId, int dishId) {
        this.restaurantId = restaurantId;
        this.dishId = dishId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }
}