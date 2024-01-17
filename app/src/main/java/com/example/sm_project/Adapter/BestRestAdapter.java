package com.example.sm_project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_project.Helper.RestaurantTable;
import com.example.sm_project.R;

import java.util.List;

public class BestRestAdapter extends RecyclerView.Adapter<BestRestAdapter.viewholder> {

    private List<RestaurantTable> restaurantList;

    public BestRestAdapter(List<RestaurantTable> restaurantList) {
        this.restaurantList = restaurantList;
    }
    public void setRestaurantList(List<RestaurantTable> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }



    public class viewholder extends RecyclerView.ViewHolder{
        TextView restTitleTxt;
        ImageView restImg;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            restImg = itemView.findViewById(R.id.restImg);
            restTitleTxt = itemView.findViewById(R.id.restTitleTxt);
        }
    }

    @NonNull
    @Override
    public BestRestAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_rest, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestRestAdapter.viewholder holder, int position) {
        RestaurantTable restaurant = restaurantList.get(position);

        // Ustawienie danych dla poszczególnego elementu
        holder.restTitleTxt.setText(restaurant.getName());
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }




}
