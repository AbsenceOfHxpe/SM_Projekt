package com.example.sm_project.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sm_project.R;

public class StartActivity extends AppCompatActivity {
    ImageView img;
    Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        img = findViewById(R.id.img);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
       // animateImage();


        loginBtn.setOnClickListener(v -> {

        });


        registerBtn.setOnClickListener(v -> {

        });
    }

    /*private void animateImage() {
        // Wczytaj animację z pliku xml
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_animation);

        // Przypisz animację do obrazka
        img.startAnimation(animation);
    }*/
}