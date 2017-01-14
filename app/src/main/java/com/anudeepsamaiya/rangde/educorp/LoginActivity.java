package com.anudeepsamaiya.rangde.educorp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anudeepsamaiya.rangde.educorp.databinding.ActivityLoginBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

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
                                    Log.d(TAG, response.body().toString());
                                } else if (response.errorBody() != null)
                                    Log.d(TAG, response.errorBody().toString());
                            }

                            @Override
                            public void onFailure(Call<JSONObject> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
            }
            break;
            case R.id.btnRegister:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
