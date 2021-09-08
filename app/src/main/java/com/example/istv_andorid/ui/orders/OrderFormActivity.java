package com.example.istv_andorid.ui.orders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.istv_andorid.R;
import com.example.istv_andorid.util.HttpUtil;
import com.example.istv_andorid.util.SharedPreferencesUtil;

import org.json.JSONObject;

public class OrderFormActivity extends AppCompatActivity {
    private Integer orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        final EditText editOrderStatus = findViewById(R.id.editOrderStatus);
        final EditText editDeliveryAddress = findViewById(R.id.editDeliveryAddress);
        final EditText editCompanyName = findViewById(R.id.editCompanyName);
        final EditText editPrice = findViewById(R.id.editPrice);
        Button button = findViewById(R.id.saveOrder);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getInt("order_id");
            editOrderStatus.setText(bundle.getString("order_status"));
            editDeliveryAddress.setText(bundle.getString("order_deliveryAddress"));
            editCompanyName.setText(bundle.getString("order_companyName"));
            editPrice.setText(bundle.getString("order_price"));
        }

        editCompanyName.setClickable(false);
        editCompanyName.setFocusable(false);

        editPrice.setClickable(false);
        editPrice.setFocusable(false);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;

                if (editOrderStatus.getText().toString().isEmpty()) {
                    editOrderStatus.setError("Status porudžbine ne sme biti prazan");
                    valid = false;
                } else {
                    editPrice.setError(null);
                }

                if (editDeliveryAddress.getText().toString().isEmpty()) {
                    editDeliveryAddress.setError("Adresa dostave ne sme biti prazan");
                    valid = false;
                } else {
                    editPrice.setError(null);
                }

                if(valid){
                    try{
                        JSONObject json = new JSONObject();
                        json.put("orderStatus", editOrderStatus.getText().toString());
                        json.put("deliveryAddress", editDeliveryAddress.getText().toString());
                        json.put("orderId", orderId);

                        HttpUtil.doPut("orders/dto", json.toString(), SharedPreferencesUtil.getSavedString(getApplicationContext(), "jwt"));

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
