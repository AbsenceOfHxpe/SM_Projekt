package com.example.sm_project.Domain;

import java.util.List;

public class Restaurants {
        private String name;
        int img;

    private List<Foods> foodsList;

    public Restaurants(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public List<Foods> getFoodsList() {
        return foodsList;
    }

    public void setFoodsList(List<Foods> foodsList) {
        this.foodsList = foodsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void addFood(Foods food) {
        foodsList.add(food);
    }


}
