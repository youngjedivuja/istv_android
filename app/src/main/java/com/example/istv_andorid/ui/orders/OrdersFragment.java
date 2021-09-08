package com.example.istv_andorid.ui.orders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istv_andorid.R;
import com.example.istv_andorid.data.model.Buyer;
import com.example.istv_andorid.data.model.Order;
import com.example.istv_andorid.data.model.OrderProduct;
import com.example.istv_andorid.data.model.Product;
import com.example.istv_andorid.ui.buyers.BuyerAdapter;
import com.example.istv_andorid.ui.buyers.BuyerFragment;
import com.example.istv_andorid.util.HttpUtil;
import com.example.istv_andorid.util.SharedPreferencesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Order> orders;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView = root.findViewById(R.id.list_orders);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        updateList();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        orders = new OrdersFragment.ListOrderTask(getContext()).doInBackground();
        mAdapter = new OrderAdapter(orders, getContext());
        recyclerView.setAdapter(mAdapter);
    }

    class ListOrderTask extends AsyncTask<String, Void, ArrayList<Order>> {
        private Context context;

        public ListOrderTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Order> doInBackground(String... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            ArrayList<Order> list = new ArrayList<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                CollectionType collectionType = mapper.getTypeFactory()
                        .constructCollectionType(List.class, Order.class);
                InputStream response = HttpUtil.doGet("orders",
                        SharedPreferencesUtil.getSavedString(context, "jwt"));
                list = mapper.readValue(response, collectionType);
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Došlo je do greške!", Toast.LENGTH_LONG).show();
            }
            for (Order order : list) {
                ArrayList<OrderProduct> orderProducts = new ArrayList<>();

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    CollectionType collectionType = mapper.getTypeFactory()
                            .constructCollectionType(List.class, OrderProduct.class);
                    InputStream response = HttpUtil.doGet("orders/" + order.getId() + "/orderproducts",
                            SharedPreferencesUtil.getSavedString(context, "jwt"));
                    orderProducts = mapper.readValue(response, collectionType);
                    order.setOrderProducts(orderProducts);
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Došlo je do greške!", Toast.LENGTH_LONG).show();
                }
            }


            return list;
        }
    }
}
