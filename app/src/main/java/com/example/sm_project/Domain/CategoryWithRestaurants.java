package com.example.sm_project.Domain;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.Helper.RestaurantTable;

import java.util.List;

public class CategoryWithRestaurants {
    @Embedded
    public CategoryTable category;

    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = RestaurantTable.class)
    public List<RestaurantTable> restaurants;
}