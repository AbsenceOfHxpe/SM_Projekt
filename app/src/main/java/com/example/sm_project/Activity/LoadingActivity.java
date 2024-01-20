package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.sm_project.Dao.CategoryDao;
import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Dao.UserDao;
import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.RestaurantTable;
import com.example.sm_project.Helper.UserTable;
import com.example.sm_project.R;

public class LoadingActivity  extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private ImageView pic;

    private MyDataBase myDB;
    private UserDao userDao;
    private RestaurantDao restaurantDao;
    private CategoryDao categoryDao;

    boolean isEmpty = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        pic = findViewById(R.id.img);
        animateImage();

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = myDB.getDao();
        categoryDao = myDB.getCategoryDao();
        restaurantDao = myDB.getRestaurantDao();
        userDao.doNothing();

        categoryDao.getAllCategories().observe(this, categories -> {
            if (categories == null || categories.isEmpty()) {
                categoryDao.insert(new CategoryTable("Pizza",R.drawable.btn_1));
                categoryDao.insert(new CategoryTable("Burger",R.drawable.btn_2));
                categoryDao.insert(new CategoryTable("Kurczaki",R.drawable.btn_3));
                categoryDao.insert(new CategoryTable("Sushi",R.drawable.btn_4));
                categoryDao.insert(new CategoryTable("Steki",R.drawable.btn_5));

            } else {

            }
        });

        restaurantDao.getAllRestaurants().observe(this, restaurants -> {
            if (restaurants == null || restaurants.isEmpty()) {
                restaurantDao.insert(new RestaurantTable("McDonalds",R.drawable.google,1));
                restaurantDao.insert(new RestaurantTable("KFC",R.drawable.btn_2,2));

            } else {

            }
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loadingIntent = new Intent(LoadingActivity.this, StartActivity.class);
                startActivity(loadingIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }

    private void animateImage() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_animation);
        pic.startAnimation(animation);
    }
}