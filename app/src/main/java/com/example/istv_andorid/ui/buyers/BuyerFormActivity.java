package com.example.istv_andorid.ui.buyers;

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

public class BuyerFormActivity extends AppCompatActivity {
    private Integer buyerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_form);

        final EditText editName = findViewById(R.id.editName);
        final EditText editSurname = findViewById(R.id.editSurname);
        final EditText editCompanyName = findViewById(R.id.editCompanyName);
        final EditText  editCity= findViewById(R.id.editCity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            buyerId = bundle.getInt("buyer_id");
            editName.setText(bundle.getString("buyer_name"));
            editSurname.setText(bundle.getString("buyer_surname"));
            editCompanyName.setText(bundle.getString("buyer_companyName"));
            editCity.setText(bundle.getString("buyer_city"));
        }
    }
}
