package com.example.sm_project.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.example.sm_project.Dao.DishDao;
import com.example.sm_project.Dao.OrderDao;
import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Dao.UserDao;
import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.Helper.DishTable;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.OrderTable;
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

    private DishDao dishDao;

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
        dishDao = myDB.getDishDao();
        userDao.doNothing();



        userDao.getAllUsers().observe(this, users -> {
            if (users == null || users.isEmpty()) {
                userDao.insertUser(new UserTable("admin","Admin","admin"));
            } else {

            }
        });

            dishDao.getAllDishes().observe(this, dishes -> {
                if (dishes == null || dishes.isEmpty()) {
                    dishDao.insert(new DishTable("Chesseburger",R.drawable.chesseburger_mc,4.00,6.20));
                    dishDao.insert(new DishTable("Hamburger",R.drawable.hamburger_mc,3.60,5.50));
                    dishDao.insert(new DishTable("Big Mac",R.drawable.bigmac_mc,4.30,7.80));
                    dishDao.insert(new DishTable("McRoyal",R.drawable.mcroyal_mc,4.70,8.00));
                    dishDao.insert(new DishTable("McChicken",R.drawable.mcchicken_mc,4.70,8.00));
                    dishDao.insert(new DishTable("McWrap",R.drawable.mcwrap_mc,4.60,10.00));
                    dishDao.insert(new DishTable("Frytki",R.drawable.fries_mc,4.50,4.00));

                    dishDao.insert(new DishTable("Grander",R.drawable.grander_kfc,4.50,8.00));
                    dishDao.insert(new DishTable("Zinger",R.drawable.zinger_kfc,4.90,7.50));
                    dishDao.insert(new DishTable("Kubełek kurczaków",R.drawable.chickenbucket_kfc,4.30,25.50));
                    dishDao.insert(new DishTable("Nóżka kurczaka",R.drawable.chickendrumstick_kfc,3.20,2.00));
                    dishDao.insert(new DishTable("Skrzydełka",R.drawable.chickenwings_kfc,3.70,3.50));

                    dishDao.insert(new DishTable("Chesseburger",R.drawable.chesseburger_mc,3.70,4.0));
                    dishDao.insert(new DishTable("Whopper",R.drawable.whopper_bk,3.80,4.0));
                    dishDao.insert(new DishTable("Whopper Junior",R.drawable.whooperjr_bk,3.80,4.0));
                    dishDao.insert(new DishTable("Frytki",R.drawable.fries_bk,4.80,5.0));

                    dishDao.insert(new DishTable("Rolka z krewetką",R.drawable.shrimp_roll,4.80,10.0));
                    dishDao.insert(new DishTable("Sushi z łososiem",R.drawable.sushi_salmon,4.90,12.0));
                    dishDao.insert(new DishTable("Sushi z mango",R.drawable.mango_set,4.20,15.20));
                    dishDao.insert(new DishTable("California rolls",R.drawable.california_roll,5.0,12.5));

                    dishDao.insert(new DishTable("Barbecue",R.drawable.barbecue,4.80,30.0));
                    dishDao.insert(new DishTable("Pepperoni",R.drawable.pepperoni,5.0,25.0));
                    dishDao.insert(new DishTable("Z kiełbasą",R.drawable.sausagepizza,4.80,27.0));
                    dishDao.insert(new DishTable("Hawajska",R.drawable.hawaianpizza,4.80,28.0));
                    dishDao.insert(new DishTable("Z szynką",R.drawable.hampizza,4.3,26.0));
                    dishDao.insert(new DishTable("Margherita",R.drawable.margherita,4.00,20));

                    dishDao.insert(new DishTable("Z mozarellą", R.drawable.salatka_z_mozarella, 3.00, 25));
                    dishDao.insert(new DishTable("Z łososiem", R.drawable.salatka_z_lososiem, 4.00, 30));
                    dishDao.insert(new DishTable("Z falafelem", R.drawable.salatka_z_falafelem, 4.50, 26));
                    dishDao.insert(new DishTable("Z kurczakiem", R.drawable.salatka_z_kurczakiem, 4.70, 23));
                    dishDao.insert(new DishTable("Grecka", R.drawable.salatka_grecka, 3.90, 22));

                } else {

                }
            });


            categoryDao.getAllCategories().observe(this, categories -> {
                if (categories == null || categories.isEmpty()) {
                    categoryDao.insert(new CategoryTable("Pizza", R.drawable.btn_1));
                    categoryDao.insert(new CategoryTable("Burger",R.drawable.btn_2));
                    categoryDao.insert(new CategoryTable("Kurczaki",R.drawable.btn_3));
                    categoryDao.insert(new CategoryTable("Sushi",R.drawable.btn_4));
                    categoryDao.insert(new CategoryTable("Sałatki",R.drawable.btn_7));

                    restaurantDao.getAllRestaurants().observe(this, restaurants -> {
                        if (restaurants == null || restaurants.isEmpty()) {
                            restaurantDao.insert(new RestaurantTable("McDonald's",R.drawable.mcdonalds_logo,2));
                            restaurantDao.insert(new RestaurantTable("KFC",R.drawable.kfclogo,3));
                            restaurantDao.insert(new RestaurantTable("Burger King",R.drawable.burger_king_logo,2));
                            restaurantDao.insert(new RestaurantTable("Sushi Panda",R.drawable.sushi_logo,4));
                            restaurantDao.insert(new RestaurantTable("Pizza Hut",R.drawable.pizzahut_logo,1));
                            restaurantDao.insert(new RestaurantTable("Papa Johns Pizza",R.drawable.papajohns_logo,1));
                            restaurantDao.insert(new RestaurantTable("Pizza My Heart",R.drawable.pizzamyheart_logo,1));
                            //restaurantDao.insert(new RestaurantTable("Wendy's",R.drawable.wendys_logo,2));
                            restaurantDao.insert(new RestaurantTable("Chick-fil-A",R.drawable.chickfila_logo,3));
                            restaurantDao.insert(new RestaurantTable("Salad Story",R.drawable.salad_logo,5));
                           // restaurantDao.insert(new RestaurantTable("Subway",R.drawable.subway_logo,2));


                        }
                    });
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