package com.example.istv_andorid.ui.products;

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
import com.example.istv_andorid.data.model.Product;
import com.example.istv_andorid.util.HttpUtil;
import com.example.istv_andorid.util.SharedPreferencesUtil;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> products;
    private Context context;

    public ProductAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        holder.name.setText(products.get(position).getFullName());
        holder.price.setText(products.get(position).getPrice() + " RSD");
        holder.code.setText(products.get(position).getProductCode());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView code;
        TextView name;
        TextView price;
        ImageButton delete;

        public ProductViewHolder(@NonNull final View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.product_code);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            delete = itemView.findViewById(R.id.product_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Product product = products.get(position);

                    boolean deleted = HttpUtil.doDelete("products/" + product.getId(),
                            SharedPreferencesUtil.getSavedString(context, "jwt"));

                    if (deleted) {
                        products.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, products.size());
                    } else {
                        Toast.makeText(context, "Greška prilikom brisanja", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Product product = products.get(position);

                    Intent intent = new Intent(context, ProductFormActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", product.getId());
                    bundle.putString("product_name", product.getFullName());
                    bundle.putString("product_price", product.getPrice());
                    bundle.putString("product_code", product.getProductCode());
                    bundle.putString("product_quantity", product.getStorageQuantity());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }
    }
}
