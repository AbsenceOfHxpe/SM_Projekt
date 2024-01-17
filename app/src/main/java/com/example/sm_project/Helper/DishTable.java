package com.example.sm_project.Helper;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "dishtable")
public class DishTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name, imagePath;

    private double rating,price;

    public DishTable(int id, String name, String imagePath, double rating, double price) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.rating = rating;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
