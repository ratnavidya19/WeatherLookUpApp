package com.ratnavidyakanawade.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ratnavidyakanawade.myapplication.adapter.WeatherAdapter;
import com.ratnavidyakanawade.myapplication.model.ThreeHourForecast;
import com.ratnavidyakanawade.myapplication.model.ThreeHourForecastWeather;
import com.ratnavidyakanawade.myapplication.model.Weather;
import com.ratnavidyakanawade.myapplication.network.CityApi;
import com.ratnavidyakanawade.myapplication.network.WeatherRetrofitClient;
import com.ratnavidyakanawade.myapplication.R;
import com.ratnavidyakanawade.myapplication.reusables.CommonFunctions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//This activity contains the list of weather data of selected city
public class WeatherInfoActivity extends AppCompatActivity {
    String city;
    Context context;
    RecyclerView recycler_weatherdata;
    ProgressBar data_progress;
    ArrayList<ThreeHourForecast> arrayCityDescription;
    WeatherAdapter weatherAdapter;
    ThreeHourForecastWeather threeHourForecastWeather;
    List<ThreeHourForecastWeather> array_weather;
    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        getSupportActionBar().hide();
        context = this;

        getInitializeUI();

    }

    private void getInitializeUI() {

        header = findViewById(R.id.header);
        data_progress = findViewById(R.id.data_progress);
        data_progress.setVisibility(View.VISIBLE);

        if (getIntent().getExtras() != null) {
            city = getIntent().getExtras().get("City").toString();
            header.setText(city);

        }

        arrayCityDescription = new ArrayList<>();
        array_weather = new ArrayList<>();
        recycler_weatherdata = findViewById(R.id.recycler_weatherdata);
        recycler_weatherdata.setHasFixedSize(true);
        recycler_weatherdata.setLayoutManager(new LinearLayoutManager(this));

        RelativeLayout img_back = (RelativeLayout) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherInfoActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        //check network connectivity status
        if (CommonFunctions.chkStatus(context)) {
            //api call
            getCurrentData();
        }else {
            data_progress.setVisibility(View.GONE);
            CommonFunctions.displayToast(context, context.getResources().getString(R.string.network_connection));
            finish();
        }

    }

    //this method gets data from api and set it to the Model class
    private void getCurrentData() {
        //Creating a call instance using our WeatherRetrofitClient
        Call<ThreeHourForecast> call = WeatherRetrofitClient.getInstance().getData().getCurrentWeatherData(city, CityApi.API_KEY);
        //to perform the API call we need to call the method enqueue()
        //We need to pass a Callback with enqueue method
        call.enqueue(new Callback<ThreeHourForecast>() {
            @Override
            public void onResponse(@NonNull Call<ThreeHourForecast> call, Response<ThreeHourForecast> response) {
                data_progress.setVisibility(View.GONE);

                if (response.code() == 200) {
                    ThreeHourForecast threeHourForecast = response.body();
                    arrayCityDescription.add(threeHourForecast);
                    for (int i = 0; i < response.body().getList().size(); i++) {
                        threeHourForecastWeather = threeHourForecast.getList().get(i);
                        array_weather.add(threeHourForecastWeather);

                    }

                    //set adapter
                    weatherAdapter = new WeatherAdapter(array_weather, arrayCityDescription, getApplicationContext());
                    recycler_weatherdata.setAdapter(weatherAdapter);
                    //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    finish();
                    CommonFunctions.displayToast(context, context.getResources().getString(R.string.incorrect_city));

                }
            }

            @Override
            public void onFailure(Call<ThreeHourForecast> call, Throwable t) {
                //handle error or failure cases here
                data_progress.setVisibility(View.GONE);
                finish();
                CommonFunctions.displayToast(context, t.getMessage());
            }


        });
    }
}