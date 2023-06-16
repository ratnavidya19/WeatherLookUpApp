package com.ratnavidyakanawade.myapplication.network;

import com.ratnavidyakanawade.myapplication.model.ThreeHourForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CityApi {

    String BASE_URL = "https://api.openweathermap.org/";
    String API_KEY = "65d00499677e59496ca2f318eb68c049";

    @GET("data/2.5/forecast?")
    Call<ThreeHourForecast> getCurrentWeatherData(@Query("q") String city, @Query("appid") String API_KEY);
}
