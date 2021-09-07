package com.example.istv_andorid.ui.buyers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istv_andorid.R;
import com.example.istv_andorid.data.model.Buyer;
import com.example.istv_andorid.util.HttpUtil;
import com.example.istv_andorid.util.SharedPreferencesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BuyerFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Buyer> buyers;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buyer, container, false);
        recyclerView = root.findViewById(R.id.list_buyers);
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
        buyers = new BuyerFragment.ListBuyerTask(getContext()).doInBackground();
        mAdapter = new BuyerAdapter(buyers, getContext());
        recyclerView.setAdapter(mAdapter);
    }

    class ListBuyerTask extends AsyncTask<String, Void, ArrayList<Buyer>> {
        private Context context;

        public ListBuyerTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Buyer> doInBackground(String... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            ArrayList<Buyer> list = new ArrayList<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                CollectionType collectionType = mapper.getTypeFactory()
                        .constructCollectionType(List.class, Buyer.class);
                InputStream response = HttpUtil.doGet("buyers",
                        SharedPreferencesUtil.getSavedString(context, "jwt"));
                list = mapper.readValue(response, collectionType);
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Došlo je do greške!", Toast.LENGTH_LONG).show();
            }
            return list;
        }
    }
}
