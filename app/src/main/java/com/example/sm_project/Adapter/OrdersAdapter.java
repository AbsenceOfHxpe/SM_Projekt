package com.example.sm_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_project.Helper.OrderTable;
import com.example.sm_project.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.viewholder> {

    private List<OrderTable> listItem;
    private Context context;

    public OrdersAdapter(List<OrderTable> listItem) {
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public OrdersAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.viewholder holder, int position) {
        holder.date.setText(listItem.get(position).getDate() + "");
        holder.price.setText(listItem.get(position).getPrice() + "zł");
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView date, price;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.dateTxt);
            price = itemView.findViewById(R.id.priceTxt);
        }
    }
}
