package com.example.sm_project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sm_project.Fragments.Map_Fragment;
import com.example.sm_project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeolocationActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView country, city, address;
    private Button locationBtn;
    private static final int REQUEST_CODE = 100;
    private static final String USER_ADDRESS_KEY = "user_address";
    private static final String USER_CITY_KEY = "user_city";
    private static final String USER_COUNTRY_KEY = "user_country";
    private static final String LATITUDE_KEY = "latitude";
    private static final String LONGITUDE_KEY = "longitude";
    private static final String MARKER_LATITUDE_KEY = "marker_latitude";
    private static final String MARKER_LONGITUDE_KEY = "marker_longitude";
    private static final int DELAY_MILLISECONDS = 5000;
    private static final String SHARED_PREFERENCES_NAME = "user_location";
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);
        Fragment fragment = new Map_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        address = findViewById(R.id.userAdressTxt);
        city = findViewById(R.id.userCityTxt);
        country = findViewById(R.id.userCountryTxt);

        locationBtn = findViewById(R.id.getLocationBtn);
        locationBtn.setOnClickListener(v -> getLastLocation());

        // Przywróć dane, jeśli zostały zapisane
        restoreSavedLocation();
        // Przywróć lokalizację markera
        restoreMarkerLocation();
    }

    private void restoreSavedLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        String savedAddress = sharedPreferences.getString(USER_ADDRESS_KEY, "");
        String savedCity = sharedPreferences.getString(USER_CITY_KEY, "");
        String savedCountry = sharedPreferences.getString(USER_COUNTRY_KEY, "");
        double savedLatitude = Double.longBitsToDouble(sharedPreferences.getLong(LATITUDE_KEY, Double.doubleToLongBits(0.0)));
        double savedLongitude = Double.longBitsToDouble(sharedPreferences.getLong(LONGITUDE_KEY, Double.doubleToLongBits(0.0)));

        address.setText(savedAddress);
        city.setText(savedCity);
        country.setText(savedCountry);
        currentLatitude = savedLatitude;
        currentLongitude = savedLongitude;

        // Aktualizuj mapę w fragmencie
        Map_Fragment mapFragment = (Map_Fragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (mapFragment != null) {
            mapFragment.setMarker(savedLatitude, savedLongitude);
        }
    }

    private void saveLocationToSharedPreferences(String userAddress, String userCity, String userCountry, double latitude, double longitude) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ADDRESS_KEY, userAddress);
        editor.putString(USER_CITY_KEY, userCity);
        editor.putString(USER_COUNTRY_KEY, userCountry);
        editor.putLong(LATITUDE_KEY, Double.doubleToRawLongBits(latitude));
        editor.putLong(LONGITUDE_KEY, Double.doubleToRawLongBits(longitude));
        editor.apply();
    }

    private void saveMarkerLocationToSharedPreferences(double latitude, double longitude) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(MARKER_LATITUDE_KEY, Double.doubleToRawLongBits(latitude));
        editor.putLong(MARKER_LONGITUDE_KEY, Double.doubleToRawLongBits(longitude));
        editor.apply();
    }

    private void restoreMarkerLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        double savedMarkerLatitude = Double.longBitsToDouble(sharedPreferences.getLong(MARKER_LATITUDE_KEY, Double.doubleToLongBits(0.0)));
        double savedMarkerLongitude = Double.longBitsToDouble(sharedPreferences.getLong(MARKER_LONGITUDE_KEY, Double.doubleToLongBits(0.0)));

        Log.d("GeolocationActivity", "Restoring marker location: Latitude = " + savedMarkerLatitude + ", Longitude = " + savedMarkerLongitude);

        // Aktualizuj mapę w fragmencie
        Map_Fragment mapFragment = (Map_Fragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (mapFragment != null) {
            mapFragment.setMarker(savedMarkerLatitude, savedMarkerLongitude);
        }
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(GeolocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(GeolocationActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    currentLatitude = location.getLatitude();
                                    currentLongitude = location.getLongitude();
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    String userAddress = addresses.get(0).getThoroughfare() + " " + addresses.get(0).getSubThoroughfare();
                                    String userCity = addresses.get(0).getLocality();
                                    String userCountry = addresses.get(0).getCountryName();

                                    address.setText(userAddress);
                                    city.setText(userCity);
                                    country.setText(userCountry);

                                    // Aktualizuj mapę w fragmencie
                                    Map_Fragment mapFragment = (Map_Fragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                                    if (mapFragment != null) {
                                        mapFragment.setMarker(location.getLatitude(), location.getLongitude());
                                    }

                                    // Zapisz lokalizację w SharedPreferences
                                    saveLocationToSharedPreferences(userAddress, userCity, userCountry, currentLatitude, currentLongitude);

                                    // Zapisz lokalizację markera w SharedPreferences
                                    saveMarkerLocationToSharedPreferences(currentLatitude, currentLongitude);

                                    // Przejdź do innego widoku
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(GeolocationActivity.this, MainActivity.class);
                                            intent.putExtra("userAddress", userAddress);
                                            intent.putExtra("userCity", userCity);
                                            intent.putExtra("userCountry", userCountry);
                                            intent.putExtra("latitude", currentLatitude);
                                            intent.putExtra("longitude", currentLongitude);
                                            startActivity(intent);
                                        }
                                    }, DELAY_MILLISECONDS);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(GeolocationActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    Toast.makeText(this, "Odmowa dostępu do lokalizacji", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
