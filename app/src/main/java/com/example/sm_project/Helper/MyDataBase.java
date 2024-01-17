package com.example.sm_project.Helper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Dao.UserDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UserTable.class,RestaurantTable.class,  DishTable.class,OrderTable.class, DishOrderCrossRef.class, RestaurantDishCrossRef.class, CategoryTable.class}, version = 6)
public abstract class MyDataBase extends RoomDatabase {

    private static MyDataBase databaseInstance;

    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();


    public abstract UserDao getDao();
    public abstract RestaurantDao getRestaurantDao();


    public static MyDataBase getDatabase(final Context context) {
        if(databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MyDataBase.class, "restaurant_database")
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
                RestaurantDao dao = databaseInstance.getRestaurantDao();
                RestaurantTable restaurant = new RestaurantTable( "Restauracja 1", "",1);
                dao.insert(restaurant);
                RestaurantTable restaurant2= new RestaurantTable( "Restauracja 2","",2);
                dao.insert(restaurant2);
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
