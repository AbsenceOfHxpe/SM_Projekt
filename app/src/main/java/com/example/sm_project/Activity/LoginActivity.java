package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.sm_project.Dao.UserDao;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityLoginBinding;
import com.example.sm_project.databinding.ActivityRegisterBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    MyDataBase myDB;
    UserDao userDao;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        if (intent.hasExtra("userLogin")) {
            String userLogin = intent.getStringExtra("userLogin");
        }



        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myDB = Room.databaseBuilder(this, MyDataBase.class, "usertable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = myDB.getDao();

        binding.googleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Gmail is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setVariable();

        binding.googleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Gmail is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.loginText.getText().toString();
                String password = binding.passwordText.getText().toString();

                if(userDao.login(userName,password)){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userLogin", userName); // Pass the login information
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.invalid_login, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setVariable() {
        binding.loginBtn.setOnClickListener(v -> {
            String password = binding.passwordText.getText().toString();
            String login = binding.loginText.getText().toString();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userLogin", login);
            startActivity(intent);
        });


        binding.registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
