package com.example.sm_project.Helper;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "restaurant_dish_cross_ref",
        foreignKeys = {
                @ForeignKey(entity = RestaurantTable.class,
                        parentColumns = "id",
                        childColumns = "restaurantId"),
                @ForeignKey(entity = DishTable.class,
                        parentColumns = "id",
                        childColumns = "dishId")
        })
public class RestaurantDishCrossRef {

    @PrimaryKey(autoGenerate = true)
    private int crossRefId;
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

    public int getCrossRefId() {
        return crossRefId;
    }

    public void setCrossRefId(int crossRefId) {
        this.crossRefId = crossRefId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }
}
