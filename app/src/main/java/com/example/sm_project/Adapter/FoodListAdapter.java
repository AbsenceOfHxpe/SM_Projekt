package com.example.sm_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.R;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    private ArrayList<Foods> items;
    private Context context;
    private OnFoodClickListener onFoodClickListener;

    public FoodListAdapter(ArrayList<Foods> items, OnFoodClickListener onFoodClickListener) {
        this.items = items;
        this.onFoodClickListener = onFoodClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_list_food, parent, false);
        return new ViewHolder(inflate, onFoodClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTxt.setText(items.get(position).getTitle());
        holder.timeTxt.setText(items.get(position).getTimeValue() + "min");
        holder.rateTxt.setText(String.valueOf(items.get(position).getStar()));
        holder.priceTxt.setText(items.get(position).getPrice() + "zł");
        Glide.with(context)
                .load(items.get(position).getImagePath())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTxt, priceTxt, rateTxt, timeTxt;
        ImageView img;
        OnFoodClickListener onFoodClickListener;

        public ViewHolder(@NonNull View itemView, OnFoodClickListener onFoodClickListener) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            rateTxt = itemView.findViewById(R.id.rateTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            img = itemView.findViewById(R.id.img);
            this.onFoodClickListener = onFoodClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onFoodClickListener.onFoodClick(items.get(getAdapterPosition()).getTitle(),
                    (float) items.get(getAdapterPosition()).getPrice(),
                    (int) items.get(getAdapterPosition()).getImagePath());
        }
    }

    public interface OnFoodClickListener {
        void onFoodClick(String foodName, float price, int img);

    }
}
