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

import com.bumptech.glide.Glide;
import com.example.sm_project.Domain.Category;
import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryTable> categoryList;
    private Context context;
    private ProgressBar progressBarCategory;
    private OnDataLoadedListener onDataLoadedListener;

    public CategoryAdapter(List<CategoryTable> categoryList) {
        this.categoryList = categoryList;
    }

    public interface OnDataLoadedListener {
        void onDataLoaded();
    }

    public void setOnDataLoadedListener(OnDataLoadedListener listener) {
        this.onDataLoadedListener = listener;
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
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTxt.setText(categoryList.get(position).getName() + "");

        Glide.with(context)
                .load(categoryList.get(position).getImgPath())
                .into(holder.img);

        // Po załadowaniu danych, wywołaj zdarzenie
        notifyDataLoaded();
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.catNameTxt);
            img = itemView.findViewById(R.id.imgCat);
        }
    }
}
