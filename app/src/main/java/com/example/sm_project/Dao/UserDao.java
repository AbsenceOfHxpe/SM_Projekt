package com.example.sm_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sm_project.Helper.DishTable;
import com.example.sm_project.Helper.UserTable;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(UserTable userTable);

    @Query("SELECT * FROM UserTable")
    LiveData<List<UserTable>> getAllUsers();

    @Query("SELECT EXISTS(SELECT * from UserTable where username=:username)")
    boolean is_taken(String username);

    @Query("SELECT EXISTS(SELECT * FROM UserTable WHERE email=:email)")
    boolean isEmailTaken(String email);

    @Query("SELECT EXISTS (SELECT * FROM UserTable where username=:username AND password=:password)")
    boolean login(String username, String password);

    @Query("SELECT 1 FROM UserTable LIMIT 1")
    int doNothing();

}
