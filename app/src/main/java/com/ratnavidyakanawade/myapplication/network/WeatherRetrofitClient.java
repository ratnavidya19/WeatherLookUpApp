package com.ratnavidyakanawade.myapplication.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//singleton class for network calls
public class WeatherRetrofitClient {

    private static WeatherRetrofitClient instance = null;
    private CityApi myApi;

    private WeatherRetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CityApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(CityApi.class);
    }

    public static synchronized WeatherRetrofitClient getInstance() {
        if (instance == null) {
            instance = new WeatherRetrofitClient();
        }
        return instance;
    }

    public CityApi getData() {
        return myApi;
    }
}
