package com.example.sm_project.Helper;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorytable")
public class CategoryTable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    String name;
    int imgPath;
    public CategoryTable() {
    }

    public CategoryTable(String name, int imgPath) {
        this.name = name;
        this.imgPath = imgPath;
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

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }



}
