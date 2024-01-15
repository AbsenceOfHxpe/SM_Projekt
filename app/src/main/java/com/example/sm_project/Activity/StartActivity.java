package com.example.sm_project.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sm_project.R;

public class StartActivity extends AppCompatActivity {
    ImageView pic;
    Button loginBtn, registerBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        pic = findViewById(R.id.pic);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        animateImage();


        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);

        });


        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivity(intent);

        });
    }

    private void animateImage() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_animation);

        pic.startAnimation(animation);
    }
}