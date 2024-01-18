package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_project.R;

public class LoadingActivity  extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private ImageView pic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        pic = findViewById(R.id.img);
        animateImage();

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
