package com.example.sm_project.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityMainBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;

import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Spinner combinedInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        combinedInfoTextView = findViewById(R.id.locationSp);

        Intent intent = getIntent();
        String userAddress = intent.getStringExtra("userAddress");
        String userCity = intent.getStringExtra("userCity");
        String userCountry = intent.getStringExtra("userCountry");

        String combinedInfo =  userAddress + "\n" + userCity + "\n" + userCountry;

        ArrayList<String> list = new ArrayList<>();
        list.add(combinedInfo);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        combinedInfoTextView.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String savedUsername = preferences.getString("username", null);

        if (intent.hasExtra("userLogin") && savedUsername == null) {
            String userLogin = intent.getStringExtra("userLogin");
            saveUsername(userLogin);

            TextView loginTextView = findViewById(R.id.usernameTxt);
            loginTextView.setText(userLogin);
        } else if (savedUsername != null) {
            TextView loginTextView = findViewById(R.id.usernameTxt);
            loginTextView.setText(savedUsername);
        }

        binding.geoIcon.setOnClickListener(v -> {
            Intent intentGeo = new Intent(MainActivity.this, GeolocationActivity.class);
            startActivity(intentGeo);
        });

        binding.logoutBtn.setOnClickListener(v -> {
            showCustomDialog("Czy na pewno chcesz się wylogować?");
        });


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCEPM7C8Hx3XDlOFYSW2pjcCmtGCvjor4w");
        }

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setCountries("PL"); // Ogranicz wyniki do danego kraju (np. Polski)

        autocompleteFragment.setTypesFilter(Arrays.asList("restaurant"));


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Tutaj możesz obsłużyć wybrany obiekt Place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        LatLng bialystokLatLng = new LatLng(53.1325, 23.1688);
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(bialystokLatLng.latitude - 0.1, bialystokLatLng.longitude - 0.1),
                new LatLng(bialystokLatLng.latitude + 0.1, bialystokLatLng.longitude + 0.1)));

    }

    private void showCustomDialog(String message) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_dialog);

        TextView dialogMessage = dialog.findViewById(R.id.dialogMessage);
        dialogMessage.setText(message);

        Button dialogButton = dialog.findViewById(R.id.dialogButton);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (v.getId() == R.id.dialogButton) {
                    Intent intent3 = new Intent(MainActivity.this, StartActivity.class);
                    startActivity(intent3);
                }
            }
        });

        dialog.show();
    }

    private void saveUsername(String username) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }
}
