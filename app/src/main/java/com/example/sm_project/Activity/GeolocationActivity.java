package com.example.sm_project.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
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
        if (savedInstanceState != null) {
            address.setText(savedInstanceState.getString(USER_ADDRESS_KEY));
            city.setText(savedInstanceState.getString(USER_CITY_KEY));
            country.setText(savedInstanceState.getString(USER_COUNTRY_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Zapisz dane, aby móc je przywrócić przy zmianie orientacji ekranu
        outState.putString(USER_ADDRESS_KEY, address.getText().toString());
        outState.putString(USER_CITY_KEY, city.getText().toString());
        outState.putString(USER_COUNTRY_KEY, country.getText().toString());
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(GeolocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(GeolocationActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
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

                                    Intent intent = new Intent(GeolocationActivity.this, MainActivity.class);
                                    intent.putExtra("userAddress", userAddress);
                                    intent.putExtra("userCity", userCity);
                                    intent.putExtra("userCountry", userCountry);
                                    //startActivity(intent);
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
        ActivityCompat.requestPermissions(GeolocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
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
