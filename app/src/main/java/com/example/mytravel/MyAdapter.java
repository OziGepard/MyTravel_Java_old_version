package com.example.mytravel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private ArrayList<DataClass> list;

    public MyAdapter(Context context, ArrayList<DataClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_layout_offer, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.offerText.setText(list.get(position).getDescription());
        holder.offerPrice.setText("Cena: " + list.get(position).getPrice());
        holder.offerImage.setImageBitmap(list.get(position).getImage());

        holder.offerLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, OfferDetailActivity.class);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder
{
    ImageView offerImage;
    TextView offerText, offerPrice;
    ConstraintLayout offerLayout;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        offerImage = itemView.findViewById(R.id.offerImage);
        offerText = itemView.findViewById(R.id.offerText);
        offerPrice = itemView.findViewById(R.id.offerPrice);
        offerLayout = itemView.findViewById(R.id.offerLayout);
    }
}
