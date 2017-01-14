package com.anudeepsamaiya.rangde.educorp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anudeepsamaiya.rangde.educorp.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    ActivityLoginBinding binding;
    SharedPreferences preferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin: {
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put("query", "login");
                queryParams.put("type", String.valueOf(1));
                queryParams.put("email", binding.etEmai.getText().toString());
                queryParams.put("password", binding.etpwd.getText().toString());

                NetworkManager.getInstance().getEduCorpAuthService()
                        .loginUser(queryParams)
                        .enqueue(new Callback<JSONObject>() {
                            @Override
                            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                if (response.isSuccessful()) {
                                    progressDialog.cancel();
                                    Log.d(TAG, response.body().toString());
                                    try {
                                        String api_key = response.body().getString("api_key");
                                        preferences.edit().putString("api_key", api_key).apply();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                } else if (response.errorBody() != null)
                                    Log.d(TAG, response.errorBody().toString());
                            }

                            @Override
                            public void onFailure(Call<JSONObject> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                progressDialog.show();
            }
            break;
            case R.id.btnRegister:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
