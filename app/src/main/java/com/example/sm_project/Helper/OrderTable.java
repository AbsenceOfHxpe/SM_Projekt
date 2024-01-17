package com.example.sm_project.Helper;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "ordertable", foreignKeys = @ForeignKey(entity = UserTable.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE))
public class OrderTable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;

    private double price;

    private int userId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
