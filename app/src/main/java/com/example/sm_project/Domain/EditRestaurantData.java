package com.example.sm_project.Domain;

import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.Helper.RestaurantTable;

public class EditRestaurantData {
    private RestaurantTable restaurant;
    private CategoryTable category;

    public EditRestaurantData(RestaurantTable restaurant, CategoryTable category) {
        this.restaurant = restaurant;
        this.category = category;
    }

    public RestaurantTable getRestaurant() {
        return restaurant;
    }

    public CategoryTable getCategory() {
        return category;
    }
}
