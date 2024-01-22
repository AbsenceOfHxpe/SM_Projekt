package com.example.sm_project.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.sm_project.Adapter.BestRestAdapter;
import com.example.sm_project.Adapter.CategoryAdapter;
import com.example.sm_project.Converter.DataConverter;
import com.example.sm_project.Dao.CategoryDao;
import com.example.sm_project.Dao.OrderDao;
import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Dao.RestaurantDishCrossRefDao;
import com.example.sm_project.Domain.Restaurants;
import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.OrderTable;
import com.example.sm_project.Helper.RestaurantDishCrossRef;
import com.example.sm_project.Helper.RestaurantTable;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityMainBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Status;

import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity  {


    MyDataBase myDB;
    RestaurantDao RestaurantDao;
    private ActivityMainBinding binding;
    private Spinner combinedInfoTextView;

    private static final int SEARCH_RADIUS_METERS = 5000;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    private CategoryDao categoryDao;
    private RestaurantDao restaurantDao;
    private BestRestAdapter restaurantAdapter;

    private RestaurantDishCrossRefDao restaurantDishCrossRefDao;
    private OrderDao orderDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        categoryDao = myDB.getCategoryDao();
        restaurantDao = myDB.getRestaurantDao();
        orderDao = myDB.getOrderDao();
        restaurantDishCrossRefDao = myDB.getRDCrossDao();

        Date date = DataConverter.fromString("24.10.2023");



        orderDao.getAllOrders().observe(this, orderTables -> {
            if(orderTables == null || orderTables.isEmpty()){
                orderDao.insert(new OrderTable(date, 26.70, 1, 2));
                orderDao.insert(new OrderTable(date, 2222, 1, 2));
                orderDao.insert(new OrderTable(date, 103.40, 1, 1));
                orderDao.insert(new OrderTable(date, 30, 1, 1));
            }
        });


//                restaurantDishCrossRefDao.getAllRDCross().observe(this, categories -> {
//            if (categories == null || categories.isEmpty()) {
//                restaurantDishCrossRefDao.insert(new RestaurantDishCrossRef(1,1));
//                restaurantDishCrossRefDao.insert(new RestaurantDishCrossRef(1,2));
//            } else {
//
//            }
//        });





        RecyclerView recyclerViewRest = findViewById(R.id.bestRestView);
        recyclerViewRest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<RestaurantTable> restaurantTables = restaurantDao.getAllRestaurantsSync();
        BestRestAdapter RestAdapter = new BestRestAdapter(restaurantTables);
        recyclerViewRest.setAdapter(RestAdapter);
        restaurantAdapter = new BestRestAdapter(restaurantTables);
        recyclerViewRest.setAdapter(restaurantAdapter);

        restaurantAdapter.setOnRestaurantClickListener(new BestRestAdapter.OnRestaurantClickListener() {
            @Override
            public void onRestaurantClick(RestaurantTable restaurant) {
                // Po kliknięciu restauracji, przenieś się do ListFoodActivity
                Intent intent = new Intent(MainActivity.this, ListFoodActivity.class);
                intent.putExtra("restaurantId", restaurant.getId());
                intent.putExtra("nazwaRestauracji", restaurant.getName());
                startActivity(intent);
            }
        });

        restaurantAdapter.setOnDataLoadedListener(new BestRestAdapter.OnDataLoadedListener() {
            @Override
            public void onDataLoaded() {
                ProgressBar progressBarBestRest = findViewById(R.id.progressBarBestRest);
                progressBarBestRest.setVisibility(View.GONE);
            }
        });




        RecyclerView recyclerViewCat = findViewById(R.id.categoryView); // RecyclerView
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<CategoryTable> categoryTables = categoryDao.getAllCategoriesSync();
        CategoryAdapter adapterrr = new CategoryAdapter(categoryTables);
        recyclerViewCat.setAdapter(adapterrr);

        adapterrr.setOnDataLoadedListener(new CategoryAdapter.OnDataLoadedListener() {
        @Override
        public void onDataLoaded() {
            ProgressBar progressBarCategory = findViewById(R.id.progressBarCategory);
            // Gdy dane zostały załadowane, ukryj ProgressBar
            progressBarCategory.setVisibility(View.GONE);
        }
    });


        adapterrr.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(CategoryTable category) {
                String categoryName = category.getName();

                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("categoryName", categoryName);
                startActivity(intent);
            }
        });



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
            showCustomDialog(getString(R.string.logout_confirm));
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
                boolean isRestaurantOnList = isRestaurantOnList(restaurantTables, selectedRestaurantName);

                if (isRestaurantOnList) {
                    Intent intent = new Intent(MainActivity.this, ListFoodActivity.class);
                    intent.putExtra("nazwaRestauracji", selectedRestaurantName);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, R.string.invalid_restaurant, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.home){
                    Intent ordersIntent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(ordersIntent);

                } else if(itemId == R.id.orders){
                    Intent ordersIntent = new Intent(MainActivity.this, OrdersActivity.class);
                    startActivity(ordersIntent);

                }else if(itemId == R.id.search){
                    Intent ordersIntent = new Intent(MainActivity.this, ListFoodActivity.class);
                    startActivity(ordersIntent);

                }
                return false;
            }
        });

    }

        private boolean isRestaurantOnList (List < RestaurantTable > restaurantTables, String
        restaurantName){
            for (RestaurantTable restaurantTable : restaurantTables) {
                if (restaurantTable.getName().equalsIgnoreCase(restaurantName)) {
                    return true;
                }
            }
            return false;
        }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.botttom_nav_menu, menu);
        return true;
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
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}