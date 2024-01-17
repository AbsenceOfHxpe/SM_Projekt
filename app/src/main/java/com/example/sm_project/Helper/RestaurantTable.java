package com.example.sm_project.Helper;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(tableName = "restauranttable", foreignKeys = @ForeignKey(entity = CategoryTable.class,
        parentColumns = "id",
        childColumns = "categoryId",
        onDelete = ForeignKey.CASCADE))
public class RestaurantTable {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private String name,imagePath;

    private int categoryId;

    public RestaurantTable(String name, String imagePath, int categoryId) {
        this.name=name;
        this.imagePath=imagePath;
        this.categoryId=categoryId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
