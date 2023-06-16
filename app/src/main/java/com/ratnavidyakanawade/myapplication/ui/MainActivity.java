package com.ratnavidyakanawade.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.material.textfield.TextInputEditText;
import com.ratnavidyakanawade.myapplication.R;
import com.ratnavidyakanawade.myapplication.reusables.CommonFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener{

    AutoCompleteTextView edt_search_city;
    Button btn_search;
    Context context;
    ImageView find_current_location;
    ProgressBar progress_current_location;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        context = this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        initializeUI();
    }

    private void initializeUI() {

        edt_search_city = findViewById(R.id.edt_search_city);
        btn_search = findViewById(R.id.btn_search);
        progress_current_location = findViewById(R.id.progress_current_location);
        find_current_location = findViewById(R.id.find_current_location);

        // Create an ArrayAdapter with city name suggestions
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, getCities());

        // Set the adapter to the AutoCompleteTextView
        edt_search_city.setAdapter(adapter);

        edt_search_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update the adapter with filtered city names
                find_current_location.setVisibility(View.VISIBLE);
                List<String> filteredCities = getCities(s.toString());
                adapter.clear();
                adapter.addAll(filteredCities);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_search_city.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    // Hide the keyboard when done button pressed
                    hideKeyboard();

                    return true;
                }
                return false;
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_search_city.getText().toString().trim().length() > 0) {
                    String city = edt_search_city.getText().toString().trim();

                    if (CommonFunctions.chkStatus(context)) {
                        //send the city value to next activity using intent
                        Intent i = new Intent(MainActivity.this, WeatherInfoActivity.class);
                        i.putExtra("City", city);
                        startActivity(i);
                    }
                    else {
                        CommonFunctions.displayToast(context, context.getResources().getString(R.string.network_connection));
                    }
                } else {
                    edt_search_city.requestFocus();
                    find_current_location.setVisibility(View.GONE);
                    edt_search_city.setError("Please enter a city name.");
                }

            }
        });

        //to get the current location of the user
        find_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for location permission
                if (hasLocationPermission()) {
                    // Permission granted, get the current location
                    progress_current_location.setVisibility(View.VISIBLE);
                    getCurrentLocation();
                } else {
                    // Request location permission
                    progress_current_location.setVisibility(View.GONE);
                    requestLocationPermission();
                }

            }
        });
    }

    // A method to provide a list of city names
    private List<String> getCities() {
        List<String> cities = new ArrayList<>();
        cities.add("Miami");
        cities.add("Tampa");
        cities.add("Durham");
        cities.add("Cary");
        cities.add("Saint Louis");
        cities.add("Charlotte");
        cities.add("Mumbai");
        cities.add("Mountain View");
        cities.add("Paris");
        cities.add("Austin");
        cities.add("Chicago");
        cities.add("Dallas");
        cities.add("San Jose");
        cities.add("San Francisco");
        cities.add("Los Angeles");

        return cities;
    }

    private List<String> getCities(String input) {
        List<String> filteredCities = new ArrayList<>();
        List<String> allCities = getCities(); // Retrieve the complete list of cities

        // Filter the city names based on the user's input
        for (String city : allCities) {
            if (city.toLowerCase().startsWith(input.toLowerCase())) {
                filteredCities.add(city);
            }
        }

        return filteredCities;
    }

    private boolean hasLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void getCurrentLocation() {
        if (hasLocationPermission()) {
            // Access the current location
            // Check if GPS provider is enabled
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                // Request location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) MainActivity.this);
            } else {
                // GPS provider is not enabled, show a message or prompt the user to enable GPS
                progress_current_location.setVisibility(View.GONE);
                Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
            }
        } else {
            progress_current_location.setVisibility(View.GONE);
            // Location permission denied
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_search_city.getWindowToken(), 0);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
                Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        try {
            addresses = geocoder. getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            edt_search_city.setText(city);
            progress_current_location.setVisibility(View.GONE);
            locationManager.removeUpdates(MainActivity.this);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}