package com.example.mytravel;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class OfferAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final Context context;
    private final ArrayList<OfferData> list;

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public OfferAdapter(Context context, ArrayList<OfferData> list) {
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

        String imageID = list.get(position).getImageID();
        System.out.println(imageID);
        StorageReference storageRef = storage.getReferenceFromUrl("gs://fir-db-52ce4.appspot.com/images/" + imageID + ".jpg");
        long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> holder.offerImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));

        holder.offerTitle.setText(list.get(position).getTitle());
        holder.offerPrice.setText("Cena: " + list.get(position).getPrice() + " zł");

        holder.offerLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, OfferDetailActivity.class);
            intent.putExtra("Title", list.get(holder.getAdapterPosition()).getTitle());
            intent.putExtra("Price", list.get(holder.getAdapterPosition()).getPrice());
            intent.putExtra("Image", list.get(holder.getAdapterPosition()).getImageID());
            intent.putExtra("Rooms", list.get(holder.getAdapterPosition()).getRooms());
            intent.putExtra("DateOut", list.get(holder.getAdapterPosition()).getDateOut());
            intent.putExtra("DateIn", list.get(holder.getAdapterPosition()).getDateIn());
            intent.putExtra("OfferID", list.get(holder.getAdapterPosition()).getOfferID());
            intent.putExtra("AvailableRooms", list.get(holder.getAdapterPosition()).getAvailableRooms());
            intent.putExtra("Description", list.get(holder.getAdapterPosition()).getDescription());
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
    TextView offerTitle, offerPrice;
    ConstraintLayout offerLayout;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        offerImage = itemView.findViewById(R.id.offerImage);
        offerTitle = itemView.findViewById(R.id.offerTitle);
        offerPrice = itemView.findViewById(R.id.offerPrice);
        offerLayout = itemView.findViewById(R.id.offerLayout);
    }
}
