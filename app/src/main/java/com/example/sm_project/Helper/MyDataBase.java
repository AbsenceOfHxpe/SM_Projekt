package com.example.sm_project.Helper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.sm_project.Dao.UserDao;

@Database(entities = {UserTable.class}, version = 1)
public abstract class MyDataBase extends RoomDatabase {
    public abstract UserDao getDao();
}
