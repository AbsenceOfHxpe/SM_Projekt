    package com.example.sm_project.Helper;

    import android.content.Context;
    import android.util.Log;

    import androidx.annotation.NonNull;
    import androidx.room.Database;
    import androidx.room.Room;
    import androidx.room.RoomDatabase;
    import androidx.sqlite.db.SupportSQLiteDatabase;

    import com.example.sm_project.Dao.CategoryDao;
    import com.example.sm_project.Dao.DishDao;
    import com.example.sm_project.Dao.DishOrderCrossRefDao;
    import com.example.sm_project.Dao.OrderDao;
    import com.example.sm_project.Dao.RestaurantDao;
    import com.example.sm_project.Dao.RestaurantDishCrossRefDao;
    import com.example.sm_project.Dao.UserDao;

    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;

    @Database(entities = {UserTable.class,RestaurantTable.class,  DishTable.class,OrderTable.class, DishOrderCrossRef.class, RestaurantDishCrossRef.class, CategoryTable.class}, version = 18)
    public abstract class MyDataBase extends RoomDatabase {

        private static MyDataBase databaseInstance;

        static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();


        public abstract UserDao getDao();
        public abstract RestaurantDao getRestaurantDao();

        public abstract CategoryDao getCategoryDao();

        public abstract OrderDao getOrderDao();

        public abstract RestaurantDishCrossRefDao getRDCrossDao();

        public abstract DishOrderCrossRefDao getODCrossDao();

        public abstract DishDao getDishDao();






        public static MyDataBase getDatabase(final Context context) {


            if(databaseInstance == null) {
                databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                                MyDataBase.class, "Database_db")
                        .addCallback(roomDatabaseCallback)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return databaseInstance;
        }

        private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                databaseWriteExecutor.execute(() -> {





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