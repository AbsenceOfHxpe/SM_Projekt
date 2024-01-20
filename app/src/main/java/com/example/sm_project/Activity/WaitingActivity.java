package com.example.sm_project.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_project.R;

public class WaitingActivity extends AppCompatActivity {

    ImageView pic;
    Button btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        pic = findViewById(R.id.carPic);
        btn = findViewById(R.id.btn);



        ObjectAnimator animator = ObjectAnimator.ofFloat(
                pic,
                "translationX",
                -getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().widthPixels);

        animator.setInterpolator(new LinearInterpolator());

        animator.setDuration(5000);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                pic.setTranslationX(-getResources().getDisplayMetrics().widthPixels);

                animator.start();
            }
        });

        // Uruchom animację
        animator.start();


        btn.setOnClickListener(v -> {
            Intent intent = new Intent(WaitingActivity.this, MainActivity.class);
            startActivity(intent);

        });



    }

    private void animateImage() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_animation);

        pic.startAnimation(animation);
    }
}
