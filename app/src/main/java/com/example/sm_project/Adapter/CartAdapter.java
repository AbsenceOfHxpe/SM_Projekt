package com.example.sm_project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ArrayList<Foods> cartItems;
    private CartListener cartListener;

    public CartAdapter(ArrayList<Foods> cartItems, CartListener cartListener) {
        this.cartItems = cartItems;
        this.cartListener = cartListener;
    }

    public void setCartItems(ArrayList<Foods> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods food = cartItems.get(position);

        holder.title.setText(food.getTitle());
        String formattedFeeEachItem = String.format("%.2f zł", (food.getNumberInCard() * food.getPrice()));
        holder.feeEachItem.setText(formattedFeeEachItem);
        String formattedTotalEachItem = String.format("%.2f zł", food.getPrice());
        holder.totalEachItem.setText(formattedTotalEachItem);

        holder.num.setText(String.valueOf(food.getNumberInCard()));

        Glide.with(holder.itemView.getContext())
                .load(food.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.plusItem.setOnClickListener(v -> {
            cartListener.onItemQuantityChanged(food, food.getNumberInCard() + 1);
        });

        holder.minusItem.setOnClickListener(v -> {
            cartListener.onItemQuantityChanged(food, Math.max(0, food.getNumberInCard() - 1));
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, plusItem, minusItem, totalEachItem, num;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            num = itemView.findViewById(R.id.numberItemTxt);
            pic = itemView.findViewById(R.id.img);
        }
    }

    public interface CartListener {
        void onItemQuantityChanged(Foods food, int newQuantity);
    }

    public ArrayList<Foods> getCartItems() {
        return cartItems;
    }
}

