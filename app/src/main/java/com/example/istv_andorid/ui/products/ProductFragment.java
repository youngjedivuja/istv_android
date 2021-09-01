package com.example.istv_andorid.ui.products;

import android.content.Context;
import android.content.Intent;
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
import com.example.istv_andorid.data.model.Product;
import com.example.istv_andorid.util.HttpUtil;
import com.example.istv_andorid.util.SharedPreferencesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton addButton;
    private ArrayList<Product> products;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product, container, false);
        recyclerView = root.findViewById(R.id.list_products);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);

        addButton = root.findViewById(R.id.addButton);

        /*addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CenovnikFormActivity.class));
            }
        });*/

        updateList();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        products = new ListProductTask(getContext()).doInBackground();
        mAdapter = new ProductAdapter(products, getContext());
        recyclerView.setAdapter(mAdapter);
    }

    class ListProductTask extends AsyncTask<String, Void, ArrayList<Product>>{
        private Context context;

        public ListProductTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Product> doInBackground(String... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            ArrayList<Product> list = new ArrayList<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                CollectionType collectionType = mapper.getTypeFactory()
                        .constructCollectionType(List.class, Product.class);
                InputStream response = HttpUtil.doGet("products/",
                        SharedPreferencesUtil.getSavedString(context, "jwt"));
                list = mapper.readValue(response, collectionType);
                System.err.println("ABD " + list);
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Došlo je do greške!", Toast.LENGTH_LONG).show();
            }
            return list;
        }
    }
}
