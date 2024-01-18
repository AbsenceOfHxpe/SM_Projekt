package com.example.sm_project.Helper;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.sm_project.Dao.CategoryDao;
import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Dao.UserDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UserTable.class,RestaurantTable.class,  DishTable.class,OrderTable.class, DishOrderCrossRef.class, RestaurantDishCrossRef.class, CategoryTable.class}, version = 8)
public abstract class MyDataBase extends RoomDatabase {

    private static MyDataBase databaseInstance;

    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();


    public abstract UserDao getDao();
    public abstract RestaurantDao getRestaurantDao();

    public abstract CategoryDao getCategoryDao();



    public static MyDataBase getDatabase(final Context context) {


        if(databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MyDataBase.class, "my_database")
                    .addCallback(roomDatabaseCallback)
                    .build();
        }
        return databaseInstance;
    }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {

                CategoryDao categoryDao = databaseInstance.getCategoryDao();
                CategoryTable category1 = new CategoryTable("Kategoria 1");
                long categoryId1 = categoryDao.insert(category1);
                Log.d("Database", "Inserted category 1 with ID: " + categoryId1);

                CategoryTable category2 = new CategoryTable("Kategoria 2");
                long categoryId2 = categoryDao.insert(category2);
                Log.d("Database", "Inserted category 2 with ID: " + categoryId2);

                RestaurantDao dao = databaseInstance.getRestaurantDao();
                RestaurantTable restaurant = new RestaurantTable( "Restauracja 1", "444", (int) categoryId1);
                dao.insert(restaurant);
                Log.d("Database", "Inserted category 2 with ID: " + restaurant);
                RestaurantTable restaurant2= new RestaurantTable( "Restauracja 2","43ed", (int) categoryId2);
                dao.insert(restaurant2);
                Log.d("Database", "Inserted category 2 with ID: " + restaurant2);



            });
        }
    };

    public static MyDataBase getDatabaseInstance() {
        return databaseInstance;
    }

    public static void setDatabaseInstance(MyDataBase databaseInstance) {
        MyDataBase.databaseInstance = databaseInstance;
    }
}