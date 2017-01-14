package com.anudeepsamaiya.rangde.educorp;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by anudeepsamaiya on 14/1/17.
 */

public class NetworkManager {
    private final String BASE_URL = "http://hackerearth.0x10.info/api/educorp/";

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null)
            instance = new NetworkManager();
        return instance;
    }

    public EduCorpAuth getEduCorpAuthService() {
        return retrofit.create(EduCorpAuth.class);
    }

    public interface EduCorpAuth {

        @GET("auth")
        Call<JSONObject> registerUser(@QueryMap Map queryMap);

        @GET("auth")
        Call<JSONObject> loginUser(@QueryMap Map queryMap);
    }

}
