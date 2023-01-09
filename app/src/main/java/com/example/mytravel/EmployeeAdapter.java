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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeHolder> {

    private final Context context;
    private final ArrayList<EmployeeData> list;
    private final CollectionReference offerRef;

    public EmployeeAdapter(Context context, ArrayList<EmployeeData> list) {
        this.context = context;
        this.list = list;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        offerRef = db.collection("offers");
    }

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_layout_employee, parent, false);
        return new EmployeeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeHolder holder, int position) {
        String title = list.get(holder.getAdapterPosition()).getTitle();
        String ID = list.get(holder.getAdapterPosition()).getID();

        holder.offerTitleEmployee.setText(title);
        holder.deleteImage.setImageResource(R.drawable.ic_baseline_delete_24);

        holder.deleteImage.setOnClickListener(view -> offerRef.document(ID)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Oferta usunięta z bazy!", Toast.LENGTH_SHORT).show();
                    list.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }));

        System.out.println(list.get(holder.getAdapterPosition()));
    }


    @Override
    public int getItemCount() {

        return list.size();
    }

}
class EmployeeHolder extends RecyclerView.ViewHolder
{
    TextView offerTitleEmployee;
    ImageView deleteImage;

    public EmployeeHolder(@NonNull View itemView) {
        super(itemView);

        offerTitleEmployee = itemView.findViewById(R.id.offerTitleEmployee);
        deleteImage = itemView.findViewById(R.id.deleteImageEmployee);

    }
}
