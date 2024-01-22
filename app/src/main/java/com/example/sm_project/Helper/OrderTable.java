package com.example.sm_project.Helper;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "ordertable",
        foreignKeys = {
                @ForeignKey(entity = UserTable.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = RestaurantTable.class,
                        parentColumns = "id",
                        childColumns = "restaurantId",
                        onDelete = ForeignKey.CASCADE)
        })
public class OrderTable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;

    private double price;

    private int userId;
    private int restaurantId;

    public OrderTable(Date date, double price, int userId, int restaurantId) {
        this.date = date;
        this.price = price;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }
}
