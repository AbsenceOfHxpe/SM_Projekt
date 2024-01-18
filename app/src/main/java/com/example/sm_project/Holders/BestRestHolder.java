package com.example.sm_project.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_project.R;

public class BestRestHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    TextView nameView,emailView;

    public BestRestHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.restImg);
        nameView = itemView.findViewById(R.id.restTitleTxt);
        emailView = itemView.findViewById(R.id.restTxt);
    }


}