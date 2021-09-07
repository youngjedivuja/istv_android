package com.example.istv_andorid.ui.buyers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istv_andorid.R;
import com.example.istv_andorid.data.model.Buyer;

import java.util.ArrayList;

public class BuyerAdapter extends RecyclerView.Adapter<BuyerAdapter.BuyerViewHolder> {

    private ArrayList<Buyer> buyers;
    private Context context;



    public BuyerAdapter(ArrayList<Buyer> buyers, Context context) {
        this.buyers = buyers;
        this.context = context;
    }

    @NonNull
    @Override
    public BuyerAdapter.BuyerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_item_layout, parent, false);
        return new BuyerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyerAdapter.BuyerViewHolder holder, int position) {
        holder.username.setText(buyers.get(position).getUserId().getUsername());
        holder.companyName.setText(buyers.get(position).getCompanyName());
        holder.city.setText(buyers.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return buyers.size();
    }


    public class BuyerViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView companyName;
        TextView city;

        public BuyerViewHolder(@NonNull final View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.buyer_username);
            companyName = itemView.findViewById(R.id.buyer_companyName);
            city = itemView.findViewById(R.id.buyer_city);


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Buyer buyer = buyers.get(position);

                    Intent intent = new Intent(context, BuyerFormActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("buyer_id", buyer.getId());
                    bundle.putString("buyer_name", buyer.getUserId().getPersonId().getName());
                    bundle.putString("buyer_surname", buyer.getUserId().getPersonId().getSurname());
                    bundle.putString("buyer_companyName", buyer.getCompanyName());
                    bundle.putString("buyer_city", buyer.getCity());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }
    }
}
