package com.example.sm_project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_project.Domain.Foods;
import com.example.sm_project.Domain.Orders;
import com.example.sm_project.Domain.Restaurants;
import com.example.sm_project.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.viewholder> {

    ArrayList<Orders> listItem;


    @NonNull
    @Override
    public OrdersAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.viewholder holder, int position) {
        holder.title.setText(listItem.get(position).getTitle());
        holder.restaurant.setText(listItem.get(position).getName() + "");
        holder.date.setText(listItem.get(position).getDate()+"");
        holder.price.setText(listItem.get(position).getPrice()+"zł");


    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView title, restaurant, date, price;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTxt);
            restaurant = itemView.findViewById(R.id.restaurantTxt);
            date = itemView.findViewById(R.id.dateTxt);
            price = itemView.findViewById(R.id.priceTxt);
        }
    }
}
