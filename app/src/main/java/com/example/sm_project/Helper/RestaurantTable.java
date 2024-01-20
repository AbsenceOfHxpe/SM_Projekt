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


    private String name;

    private int categoryId,imagePath;

    public RestaurantTable(String name, int imagePath, int categoryId) {
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

    public int getImagePath() {
        return imagePath;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
