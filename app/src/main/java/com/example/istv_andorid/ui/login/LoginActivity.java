package com.example.istv_andorid.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.istv_andorid.MainActivity;
import com.example.istv_andorid.R;
import com.example.istv_andorid.data.model.LoggedInUser;
import com.example.istv_andorid.util.HttpUtil;
import com.example.istv_andorid.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        checkAuthenticated();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    onLoginSuccess();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.setResultValue(new LoginTask(getApplicationContext())
                        .doInBackground(usernameEditText.getText().toString(),
                                passwordEditText.getText().toString()));
            }
        });
    }

    private void showLoginFailed(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    private void onLoginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void checkAuthenticated() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.getString("jwt", "").isEmpty()) {
            onLoginSuccess();
            finish();
        }
    }

    class LoginTask extends AsyncTask<String, Void, LoginResult> {

        private Context context;

        public LoginTask(Context context) {
            this.context = context;
        }

        @Override
        protected LoginResult doInBackground(String... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            LoginResult result;
            try {
                JSONObject body = new JSONObject();
                body.put("username", params[0]);
                body.put("password", params[1]);

                JSONObject response = new JSONObject(HttpUtil.doPost("auth/login", body.toString(), null));

                LoggedInUser user = new LoggedInUser(response.getString("token"), params[0]);

                result = new LoginResult(new LoggedInUserView(params[0]));

                SharedPreferencesUtil.saveString(context, "jwt", user.getToken());
                SharedPreferencesUtil.saveString(context, "username", user.getUsername());
            } catch (JSONException e) {
                result = new LoginResult("Netačni korisnički podaci");
            } catch (Exception e) {
                e.printStackTrace();
                result = new LoginResult("Greška prilikom logovanja");
            }
            return result;
        }
    }

}
