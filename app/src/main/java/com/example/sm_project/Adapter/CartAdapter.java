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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Foods> listItem;

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        holder.title.setText(listItem.get(position).getTitle());
        holder.feeEachItem.setText(listItem.get(position).getPrice() + "zł");
        holder.totalEachItem.setText(listItem.get(position).getNumberInCard()+"zł" +
                (listItem.get(position).getNumberInCard()*listItem.get(position).getPrice()
                ));
        holder.num.setText(listItem.get(position).getNumberInCard()+"");
        Glide.with(holder.itemView.getContext())
                .load(listItem.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
        holder.plusItem.setOnClickListener(v -> {
          /*  managmentCart.plusNumberItem(listItem, position, new ChangeNumberItemsListener(){
                public void change() {

                }
            });   BAZA DANYCH   */
        });

        holder.minusItem.setOnClickListener(v -> {

        });




    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView title, feeEachItem, plusItem, minusItem, totalEachItem, num;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
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
}
