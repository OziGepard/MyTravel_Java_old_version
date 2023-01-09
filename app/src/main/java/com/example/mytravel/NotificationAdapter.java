package com.example.mytravel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {

    private final Context context;
    private final ArrayList<NotificationData> list;
    private final CollectionReference notRef;

    public NotificationAdapter(Context context, ArrayList<NotificationData> list) {
        this.context = context;
        this.list = list;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        notRef = db.collection("notifications");
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_layout_notification, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        String title = list.get(holder.getAdapterPosition()).getTitle();
        String date = "Data wiadomości: " + list.get(holder.getAdapterPosition()).getDate();
        String message = list.get(holder.getAdapterPosition()).getMessage();
        String ID = list.get(holder.getAdapterPosition()).getNotificationID();
        String isRead = list.get(holder.getAdapterPosition()).getIsRead();

        holder.notificationTitle.setText(title);
        if(isRead.equals("true"))
        {
            holder.notificationTitle.setTypeface(null, Typeface.NORMAL);
        }
        holder.notificationDate.setText(date);
        holder.notificationDelete.setImageResource(R.drawable.ic_baseline_delete_24);

        holder.notificationTitle.setOnClickListener(view -> {
            notRef.document(ID).update("is_read", "true");
            Intent intent = new Intent(context, NotificationDetailActivity.class);
            intent.putExtra("Message", message);
            context.startActivity(intent);

        });

        holder.notificationDelete.setOnClickListener(view -> {
            notRef.document(ID).delete();
            list.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class NotificationHolder extends RecyclerView.ViewHolder
{
    TextView notificationTitle, notificationDate;
    ImageView notificationDelete;

    public NotificationHolder(@NonNull View itemView) {
        super(itemView);

        notificationDate = itemView.findViewById(R.id.notificationDate);
        notificationTitle = itemView.findViewById(R.id.notificationTitle);
        notificationDelete = itemView.findViewById(R.id.notificationDelete);
    }
}
