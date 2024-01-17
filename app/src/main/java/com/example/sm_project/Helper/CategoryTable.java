package com.example.sm_project.Helper;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorytable")
public class CategoryTable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    String name, imagePath;

    public CategoryTable(int id, String name, String imagePath) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
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
}
