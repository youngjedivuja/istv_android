package com.example.istv_andorid.ui.products;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.istv_andorid.R;

public class ProductFormActivity extends AppCompatActivity {
    private Integer productId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
    }
}
