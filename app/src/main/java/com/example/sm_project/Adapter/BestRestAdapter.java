package com.example.sm_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_project.Domain.Restaurants;
import com.example.sm_project.R;

import java.util.ArrayList;

public class BestRestAdapter extends RecyclerView.Adapter<BestRestAdapter.viewholder> {

    private ArrayList<Restaurants> items;
    private Context context;
    private OnRestaurantClickListener clickListener;

    public BestRestAdapter(ArrayList<Restaurants> items, OnRestaurantClickListener clickListener) {
        this.items = items;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public BestRestAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_list_rest, parent, false);
        return new viewholder(inflate, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BestRestAdapter.viewholder holder, int position) {
        Restaurants restaurant = items.get(position);
        holder.titleTxt.setText(restaurant.getName());
        holder.img.setImageResource(restaurant.getImg());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTxt;
        ImageView img;
        OnRestaurantClickListener clickListener;

        public viewholder(@NonNull View itemView, OnRestaurantClickListener clickListener) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.restTitleTxt);
            img = itemView.findViewById(R.id.restImg);
            this.clickListener = clickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onRestaurantClick(items.get(getAdapterPosition()).getName());
        }
    }

    public interface OnRestaurantClickListener {
        void onRestaurantClick(String restaurantName);
    }
}
