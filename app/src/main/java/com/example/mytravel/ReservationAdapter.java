package com.example.mytravel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationHolder> {

    private final Context context;
    private final ArrayList<ReservationData> list;
    private final CollectionReference resRef;
    private final CollectionReference offerRef;

    public ReservationAdapter(Context context, ArrayList<ReservationData> list) {
        this.context = context;
        this.list = list;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.resRef = db.collection("reservations");
        this.offerRef = db.collection("offers");
    }

    @NonNull
    @Override
    public ReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_layout_reservations, parent, false);
        return new ReservationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationHolder holder, int position) {

        int rooms = list.get(holder.getAdapterPosition()).getRooms();
        int price = list.get(holder.getAdapterPosition()).getPrice();
        String offerTitle = list.get(holder.getAdapterPosition()).getTitle();
        String isConfirmed = list.get(holder.getAdapterPosition()).getIsConfirmed();

        holder.reservationTitle.setText(offerTitle);
        holder.reservationInfo.setText("Ilość pokoi: " + rooms + ", Cena: " + price);

        if(isConfirmed.equals("true") || isConfirmed.equals("false"))
        {
            holder.reservationConfirm.setVisibility(View.INVISIBLE);
            holder.reservationCancel.setVisibility(View.INVISIBLE);
            holder.reservationDecision.setVisibility(View.VISIBLE);
            if(isConfirmed.equals("true"))
            {
                holder.reservationDecision.setImageResource(R.drawable.ic_baseline_check_circle_24);
            }
            else
            {
                holder.reservationDecision.setImageResource(R.drawable.ic_baseline_cancel_24);
            }
        }

        holder.reservationConfirm.setOnClickListener(view -> {
            String reservationID = list.get(holder.getAdapterPosition()).getReservationID();
            resRef.document(reservationID).update("is_confirmed", true);
            Toast.makeText(context, "Rezerwacja została potwierdzona!", Toast.LENGTH_SHORT).show();
            holder.reservationConfirm.setVisibility(View.INVISIBLE);
            holder.reservationCancel.setVisibility(View.INVISIBLE);
            holder.reservationDecision.setImageResource(R.drawable.ic_baseline_check_circle_24);
            holder.reservationDecision.setVisibility(View.VISIBLE);
        });
        holder.reservationCancel.setOnClickListener(view -> {
            String reservationID = list.get(holder.getAdapterPosition()).getReservationID();
            resRef.document(reservationID).update("is_confirmed", false);
            Toast.makeText(context, "Rezerwacja została odwołana!", Toast.LENGTH_SHORT).show();
            holder.reservationConfirm.setVisibility(View.INVISIBLE);
            holder.reservationCancel.setVisibility(View.INVISIBLE);
            holder.reservationDecision.setImageResource(R.drawable.ic_baseline_cancel_24);
            holder.reservationDecision.setVisibility(View.VISIBLE);

            resRef.document(reservationID).get().addOnCompleteListener(task -> {
                DocumentSnapshot document = task.getResult();
                String email = document.get("user_email").toString();
                String offerID = document.get("offer_ID").toString();

                cancelNotification(offerTitle, email);

                offerRef.document(offerID).get().addOnCompleteListener(task1 -> {
                    DocumentSnapshot documentUpdate = task1.getResult();
                    int availableRooms = Integer.parseInt(documentUpdate.get("available_rooms").toString());

                    offerRef.document(offerID).update("available_rooms", availableRooms + rooms);
                    resRef.document(reservationID).delete();
                });
            });
        });


    }

    private void cancelNotification(String offerTitle, String email) {
        NotificationActivity notificationActivity = new NotificationActivity();

        String title = context.getString(R.string.offer_cancel_title);
        String message = context.getString(R.string.offer_cancel_notification_p1) + offerTitle + context.getString(R.string.offer_cancel_notification_p2);

        notificationActivity.createNotification(title, message, email);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}
class ReservationHolder extends RecyclerView.ViewHolder
{
    TextView reservationTitle, reservationInfo;
    ImageView reservationConfirm, reservationCancel, reservationDecision;

    public ReservationHolder(@NonNull View itemView) {
        super(itemView);

        reservationTitle = itemView.findViewById(R.id.reservationTitle);
        reservationInfo = itemView.findViewById(R.id.reservationInfo);
        reservationConfirm = itemView.findViewById(R.id.reservationConfirm);
        reservationCancel = itemView.findViewById(R.id.reservationCancel);
        reservationDecision = itemView.findViewById(R.id.reservationDecision);

    }
}
