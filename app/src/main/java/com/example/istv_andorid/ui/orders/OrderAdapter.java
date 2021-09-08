package com.example.istv_andorid.ui.orders;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istv_andorid.R;
import com.example.istv_andorid.data.model.Order;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<Order> orders;
    private Context context;

    public OrderAdapter(ArrayList<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        holder.orderStatus.setText(orders.get(position).getOrderStatus());
        holder.buyer.setText(orders.get(position).getBuyerId().getCompanyName());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderStatus;
        TextView buyer;

        public OrderViewHolder(@NonNull final View itemView) {
            super(itemView);
            orderStatus = itemView.findViewById(R.id.order_status);
            buyer = itemView.findViewById(R.id.order_buyer);


            itemView.setOnClickListener(new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Order order = orders.get(position);

                    Intent intent = new Intent(context, OrderFormActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("order_id", order.getId());
                    bundle.putString("order_status", order.getOrderStatus());
                    bundle.putString("order_deliveryAddress", order.getDeliveryAddress());
                    bundle.putString("order_companyName", order.getBuyerId().getCompanyName());
                    Log.i("ORDERS: " , "" + order);
                    bundle.putString("order_price", String.valueOf(order.getOrderProducts().stream().mapToDouble(orderProduct -> Double.parseDouble(orderProduct.getProduct().getPrice()) * orderProduct.getQuantity()).sum()));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }
    }
}
