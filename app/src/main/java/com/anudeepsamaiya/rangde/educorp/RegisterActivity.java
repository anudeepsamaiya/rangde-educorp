package com.anudeepsamaiya.rangde.educorp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.anudeepsamaiya.rangde.educorp.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    ActivityRegisterBinding binding;

    int accountType = -1;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);

        Spinner spinner = binding.spinnerAccountType;
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister: {
                if (accountType == -1l) {
                    Snackbar.make(view, "Select an account Type", Snackbar.LENGTH_SHORT).show();
                }

                Map<String, String> queryParams = new HashMap<>();
                queryParams.put("query", "register");
                queryParams.put("name", binding.etName.getText().toString());
                queryParams.put("email", binding.etEmai.getText().toString());
                queryParams.put("password", binding.etpwd.getText().toString());
                queryParams.put("type", String.valueOf(accountType));

                NetworkManager.getInstance().getEduCorpAuthService()
                        .loginUser(queryParams)
                        .enqueue(new Callback<JSONObject>() {
                            @Override
                            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                if (response.isSuccessful()) {
                                    progressDialog.cancel();
                                    Log.d(TAG, response.body().toString());
//                                    try {
//                                        if (response.body().getString("status").equalsIgnoreCase("success"))
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
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

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view,
                               int position, long l) {
        switch (adapterView.getItemAtPosition(position).toString().trim()) {
            case "Parent":
                accountType = 0;
                break;
            case "Tutor":
                accountType = 1;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
