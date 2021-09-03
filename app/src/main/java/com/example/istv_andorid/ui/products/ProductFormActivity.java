package com.example.istv_andorid.ui.products;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.istv_andorid.R;
import com.example.istv_andorid.util.HttpUtil;
import com.example.istv_andorid.util.SharedPreferencesUtil;

import org.json.JSONObject;

public class ProductFormActivity extends AppCompatActivity {
    private Integer productId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        final EditText editCode = findViewById(R.id.editCode);
        final EditText editName = findViewById(R.id.editName);
        final EditText editPrice = findViewById(R.id.editPrice);
        final EditText editQuantity = findViewById(R.id.editQuantity);
        Button button = findViewById(R.id.saveProduct);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getInt("product_id");
            Log.i("bundle: " , "" + bundle);
            editCode.setText(bundle.getString("product_code"));
            editName.setText(bundle.getString("product_name"));
            editPrice.setText(bundle.getString("product_price"));
            editQuantity.setText(bundle.getString("product_quantity"));
            button.setText("Izmeni");
        } else {
            button.setText("Dodaj");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;

                if (editName.getText().toString().isEmpty()) {
                    editName.setError("Naziv proizvoda ne sme biti prazan");
                    valid = false;
                } else {
                    editPrice.setError(null);
                }

                if (editCode.getText().toString().isEmpty()) {
                    editCode.setError("Naziv šifre ne sme biti prazan");
                    valid = false;
                } else {
                    editPrice.setError(null);
                }

                if (editPrice.getText().toString().isEmpty()) {
                    editPrice.setError("Cena proizvoda ne sme biti prazna");
                    valid = false;
                } else {
                    editPrice.setError(null);
                }

                if(valid){
                    try{
                        JSONObject json = new JSONObject();
                        json.put("productCode", editCode.getText().toString());
                        json.put("fullName", editName.getText().toString());
                        json.put("price", editPrice.getText().toString());
                        json.put("storageQuantity", editQuantity.getText().toString());

                        if (productId == null) {
                            HttpUtil.doPost("products/", json.toString(), SharedPreferencesUtil.getSavedString(getApplicationContext(), "jwt"));

                        } else {
                            json.put("productId", productId);
                            HttpUtil.doPut("products/", json.toString(), SharedPreferencesUtil.getSavedString(getApplicationContext(), "jwt"));

                        }
                        Toast.makeText(getApplicationContext(), "Uspešno sačuvano", Toast.LENGTH_SHORT).show();
                        Log.i("Info", "Info");
                        finish();


                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Greška prilikom čuvanja", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }
}
