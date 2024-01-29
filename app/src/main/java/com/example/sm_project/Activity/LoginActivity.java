package com.example.sm_project.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.sm_project.Dao.UserDao;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    MyDataBase myDB;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = myDB.getDao();

        if (savedInstanceState != null) {
            String userName = savedInstanceState.getString("userName");
            String password = savedInstanceState.getString("password");
            binding.loginText.setText(userName);
            binding.passwordText.setText(password);
        }

        binding.googleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                if (intent != null) {
                    startActivity(intent);
                } else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.google.com/")));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(LoginActivity.this, "Gmail is not installed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        binding.facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                if (intent != null) {
                    startActivity(intent);
                } else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/")));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(LoginActivity.this, "Facebook is not installed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });




        setVariable();

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.loginText.getText().toString();
                String password = binding.passwordText.getText().toString();

                if (userDao.login(userName, password)) {
                    int userId = userDao.getUserIdByLogin(userName);

                    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("userId", userId);
                    editor.apply();
                    Log.i("TAG", "Pokaz userID" + userId + "");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userLogin", userName); // Pass the login information
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.invalid_login, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userName", binding.loginText.getText().toString());
        outState.putString("password", binding.passwordText.getText().toString());
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
