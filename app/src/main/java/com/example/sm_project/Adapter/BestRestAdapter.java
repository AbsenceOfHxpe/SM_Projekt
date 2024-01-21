package com.example.sm_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_project.Helper.RestaurantTable;
import com.example.sm_project.R;

import java.util.List;

public class BestRestAdapter extends RecyclerView.Adapter<BestRestAdapter.ViewHolder> {

    private List<RestaurantTable> items;
    private Context context;
    private ProgressBar progressBarCategory;
    private OnRestaurantClickListener onRestaurantClickListener;
    private OnDataLoadedListener onDataLoadedListener;

    public BestRestAdapter(List<RestaurantTable> items) {
        this.items = items;
    }

    public interface OnRestaurantClickListener {
        void onRestaurantClick(RestaurantTable restaurant);
    }

    public void setOnRestaurantClickListener(OnRestaurantClickListener listener) {
        this.onRestaurantClickListener = listener;
    }

    public interface OnDataLoadedListener {
        void onDataLoaded();
    }

    public void setOnDataLoadedListener(OnDataLoadedListener listener) {
        this.onDataLoadedListener = listener;
    }

    private void notifyRestaurantClick(RestaurantTable restaurant) {
        if (onRestaurantClickListener != null) {
            onRestaurantClickListener.onRestaurantClick(restaurant);
        }
    }

    private void notifyDataLoaded() {
        if (onDataLoadedListener != null) {
            onDataLoadedListener.onDataLoaded();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_list_rest, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantTable restaurant = items.get(position);
        holder.titleTxt.setText(restaurant.getName());
        holder.img.setImageResource(restaurant.getImagePath());

        // Obsługa kliknięcia na restaurację
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyRestaurantClick(restaurant);
            }
        });

        // Po załadowaniu danych, wywołaj zdarzenie
        notifyDataLoaded();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTxt;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.restTitleTxt);
            img = itemView.findViewById(R.id.restImg);
        }
    }
}
