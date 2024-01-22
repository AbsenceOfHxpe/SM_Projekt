package com.example.sm_project.Helper;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "dishtable")
public class DishTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private int imagePath,rating;

    private double price;

    public DishTable( String name, int imagePath, int rating, double price) {
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

    public int getImagePath() {
        return imagePath;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
