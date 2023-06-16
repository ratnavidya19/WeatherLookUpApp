package com.ratnavidyakanawade.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ratnavidyakanawade.myapplication.model.ThreeHourForecast;
import com.ratnavidyakanawade.myapplication.R;
import com.ratnavidyakanawade.myapplication.model.ThreeHourForecastWeather;
import com.ratnavidyakanawade.myapplication.model.Weather;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private List<ThreeHourForecastWeather> array_weatherList;
    private Context context;
    private static int currentPosition = 0;

    public WeatherAdapter(List<ThreeHourForecastWeather> array_weatherList, ArrayList<ThreeHourForecast> arrayCityDescription, Context context) {
        this.array_weatherList = array_weatherList;
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_weather, parent, false);
        return new WeatherViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        ThreeHourForecastWeather threeHourForecastWeather = array_weatherList.get(position);
        holder.txt_weather.setText(threeHourForecastWeather.getWeather().get(0).getMain());
        double temperatureSI = threeHourForecastWeather.getMain().getTemp();
        double temperatureCelsius = temperatureSI - 273.15;
        temperatureCelsius = Math.round(temperatureCelsius * 100) / 100;
        holder.txt_temperature.setText(""+ (int) temperatureCelsius+"°C");
        holder.txt_temp.setText(""+ (int) temperatureCelsius+"°C");

        // Create a SimpleDateFormat instance to parse the date and time
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault());

        try {
            // Parse the API response string into a Date object
            Date date = apiDateFormat.parse(threeHourForecastWeather.getDtTxt());

            // Create a new SimpleDateFormat instance to format the date and time separately
            SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm aa", Locale.getDefault());

            // Format the date and time components
            String dateStr = DateFormat.getDateInstance(DateFormat.LONG).format(date);
            String timeStr = timeFormatter.format(date);

            holder.txt_date.setText(dateStr);
            holder.txt_time.setText(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parse exception if the API response string format is incorrect
        }

        if(threeHourForecastWeather.getWeather().get(0).getMain().equalsIgnoreCase("Clouds"))
        {
            holder.image_weather.setImageDrawable(context.getResources().getDrawable(R.drawable.cloudy));
            holder.img_weatherIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.cloudy));

        }
        else if(threeHourForecastWeather.getWeather().get(0).getMain().equalsIgnoreCase("Clear")){
            holder.image_weather.setImageDrawable(context.getResources().getDrawable(R.drawable.clear));
            holder.img_weatherIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.clear));

        }
        else {
            holder.image_weather.setImageDrawable(context.getResources().getDrawable(R.drawable.rain));
            holder.img_weatherIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.rain));

        }
        double tempFeelsLikeSI = threeHourForecastWeather.getMain().getFeels_like();
        double tempFeelsLikeCelsius = tempFeelsLikeSI - 273.15;
        tempFeelsLikeCelsius = Math.round(tempFeelsLikeCelsius * 100) / 100;
        holder.txt_feelsLike.setText("Feels like: "+ (int) tempFeelsLikeCelsius+"°C");
        holder.txt_humidity.setText(""+threeHourForecastWeather.getMain().getHumidity());
        float windspeed = (threeHourForecastWeather.getWind().getSpeed() * 3600) / 1000;
        holder.txt_windspeed.setText(""+(int)windspeed +" km/h");
        holder.txt_weatherMain.setText(threeHourForecastWeather.getWeather().get(0).getMain());
        holder.txt_weatherDesc.setText(" - "+threeHourForecastWeather.getWeather().get(0).getDescription());
        holder.txt_visibility.setText(""+(int) (threeHourForecastWeather.getVisibility() * 0.000621)+" mi");

        holder.linear_details.setVisibility(View.GONE);

        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

            //toggling visibility
            holder.linear_details.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.linear_details.startAnimation(slideDown);
        }

        holder.card_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.linear_details.getVisibility() == View.VISIBLE){


                    holder.linear_details.setVisibility(View.GONE);
                }
                else {

                    holder.linear_details.setVisibility(View.VISIBLE);
                    //getting the position of the item to expand it
                    currentPosition = position;

                    //reloading the list
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_weatherList.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        ImageView image_weather, img_weatherIcon, img_humidity, img_windspeed;
        CardView card_weather;
        LinearLayout linear_details;
        TextView txt_date, txt_time, txt_temperature, txt_weather, txt_city, txt_temp, txt_feelsLike, txt_humidity,
                txt_windspeed, txt_weatherMain, txt_weatherDesc, img_visibility, txt_visibility;
        WeatherViewHolder(View itemView) {
            super(itemView);

            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_temperature = itemView.findViewById(R.id.txt_temperature);
            txt_weather = itemView.findViewById(R.id.txt_weather);
            image_weather = itemView.findViewById(R.id.image_weather);
            card_weather = itemView.findViewById(R.id.card_weather);
            img_weatherIcon = itemView.findViewById(R.id.img_weatherIcon);
            img_windspeed = itemView.findViewById(R.id.img_windspeed);
            txt_temp = itemView.findViewById(R.id.txt_temp);
            txt_feelsLike = itemView.findViewById(R.id.txt_feelsLike);
            txt_humidity = itemView.findViewById(R.id.txt_humidity);
            txt_windspeed = itemView.findViewById(R.id.txt_windspeed);
            txt_weatherMain = itemView.findViewById(R.id.txt_weatherMain);
            txt_weatherDesc = itemView.findViewById(R.id.txt_weatherDesc);
            txt_visibility = itemView.findViewById(R.id.txt_visibility);
            linear_details = itemView.findViewById(R.id.linear_details);

        }
    }
}
