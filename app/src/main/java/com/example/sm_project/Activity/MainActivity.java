package com.example.sm_project.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.sm_project.Adapter.BestRestAdapter;
import com.example.sm_project.Adapter.CategoryAdapter;
import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Dao.UserDao;
import com.example.sm_project.Domain.Category;
import com.example.sm_project.Domain.Foods;
import com.example.sm_project.Domain.Restaurants;
import com.example.sm_project.Helper.MyDataBase;
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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;

import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;


public class MainActivity extends AppCompatActivity implements BestRestAdapter.OnRestaurantClickListener {


    MyDataBase myDB;
    RestaurantDao RestaurantDao;
    private ActivityMainBinding binding;
    private Spinner combinedInfoTextView;

    private static final int SEARCH_RADIUS_METERS = 5000;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ArrayList<Restaurants> restaurantsList = new ArrayList<>();
        Restaurants johnWick = new Restaurants("Jollibee", R.drawable.google);


        restaurantsList.add(johnWick);

        Restaurants mcdonalds = new Restaurants("McDonald's", R.drawable.food);
        restaurantsList.add(mcdonalds);


        // Inicjalizuj RecyclerView
        RecyclerView recyclerView = findViewById(R.id.bestRestView); // Zastąp yourRecyclerViewId identyfikatorem swojego RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Utwórz adapter
        BestRestAdapter adapterr = new BestRestAdapter(restaurantsList, this);

        // Ustaw adapter na RecyclerView
        recyclerView.setAdapter(adapterr);

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(R.drawable.btn_1, "Pizza"));
        categories.add(new Category(R.drawable.btn_2, "Burger"));
        categories.add(new Category(R.drawable.btn_3, "Burger"));
        categories.add(new Category(R.drawable.btn_4, "Burger"));


        RecyclerView recyclerViewCat = findViewById(R.id.categoryView); // RecyclerView
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        CategoryAdapter adapterrr = new CategoryAdapter(categories);

        recyclerViewCat.setAdapter(adapterrr);


        // Utwórz adapter i przypisz go do RecyclerView
       /* BestRestAdapter restaurantAdapter = new BestRestAdapter(new ArrayList<>());
        bestRestView.setAdapter(restaurantAdapter);

        myDB = Room.databaseBuilder(this, MyDataBase.class, "restauranttable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        myDB.getDatabase(getApplicationContext());  // Inicjalizacja myDB przed użyciem

        RestaurantDao restaurantDao = myDB.getRestaurantDao();
        restaurantDao.getAllRestaurants().observe(this, restaurantList -> {
            // Aktualizuj dane w adapterze
            restaurantAdapter.setRestaurantList(restaurantList);
            restaurantAdapter.notifyDataSetChanged();

            if (!restaurantList.isEmpty()) {
                bestRestView.setAdapter(restaurantAdapter);
            }
        });
*/


        combinedInfoTextView = findViewById(R.id.locationSp);

        Intent intent = getIntent();
        String userAddress = intent.getStringExtra("userAddress");
        String userCity = intent.getStringExtra("userCity");
        String userCountry = intent.getStringExtra("userCountry");
        double latitude = intent.getDoubleExtra("latitude", 0.0);
        double longitude = intent.getDoubleExtra("longitude", 0.0);


        String combinedInfo = userAddress + "\n" + userCity + "\n" + userCountry;

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


        autocompleteFragment.setTypesFilter(Arrays.asList("restaurant"));


        LatLng bialystokLatLng = new LatLng(latitude, longitude);
        autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(
                new LatLng(bialystokLatLng.latitude - 0.1, bialystokLatLng.longitude - 0.1),
                new LatLng(bialystokLatLng.latitude + 0.1, bialystokLatLng.longitude + 0.1)));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                // Sprawdzamy, czy wybrane miejsce jest na liście restauracji
                String selectedRestaurantName = place.getName();
                boolean isRestaurantOnList = isRestaurantOnList(restaurantsList, selectedRestaurantName);

                if (isRestaurantOnList) {
                    // Przeniesienie do ListFoodActivity
                    Intent intent = new Intent(MainActivity.this, ListFoodActivity.class);
                    intent.putExtra("nazwaRestauracji", selectedRestaurantName);
                    startActivity(intent);
                } else {
                    // Komunikat informujący, że restauracja nie jest na liście
                    Toast.makeText(MainActivity.this, "Restauracja nie jest na Twojej liście", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

// Metoda sprawdzająca, czy restauracja jest na liście
        private boolean isRestaurantOnList (List < Restaurants > restaurantsList, String
        restaurantName){
            for (Restaurants restaurant : restaurantsList) {
                if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                    return true;
                }
            }
            return false;
        }





    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.botttom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.profile) {
            Log.d("MainActivity", "Clicked on Profile");
            startActivity(new Intent(this, StartActivity.class));
            return true;
        } else if (itemId == R.id.search) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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
                    clearUsername();
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

    private void clearUsername() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("username");
        editor.apply();
    }

    @Override
    public void onRestaurantClick(String restaurantName) {
        Intent intent = new Intent(this, ListFoodActivity.class);
        intent.putExtra("nazwaRestauracji", restaurantName);
        startActivity(intent);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}