package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.room.Room;

import com.example.sm_project.Actions;
import com.example.sm_project.Dao.CategoryDao;
import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.RestaurantTable;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityCatManagmentBinding;
import com.example.sm_project.databinding.ActivityRestManagmentBinding;
import com.example.sm_project.databinding.DialogAddCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class RestSettingsActivity extends AppCompatActivity {
    private ActivityRestManagmentBinding binding;
    private ImageView backBtn;
    private AppCompatButton addBtn, deleteBtn, editBtn;
    private TextView titleTxt;

    MyDataBase myDB;
    private RestaurantDao restaurantDao;
    private CategoryDao categoryDao;

    private RestaurantTable restaurantToEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestManagmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        titleTxt = findViewById(R.id.titleTxt);
        titleTxt.setText(getString(R.string.settings_restaurant));

        backBtn = findViewById(R.id.backBtn);
        addBtn = findViewById(R.id.addBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RestSettingsActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        addBtn.setOnClickListener(v -> showRestaurantDialog(Actions.ADD, null));
        deleteBtn.setOnClickListener(v -> showDeleteRestaurantListDialog());
        editBtn.setOnClickListener(v -> {
            List<RestaurantTable> restaurants = restaurantDao.getAllRestaurantsSync();

            if (!restaurants.isEmpty()) {
                showRestaurantListDialog(restaurants);
            } else {
                Toast.makeText(this, "Brak dostępnych restauracji do edycji", Toast.LENGTH_SHORT).show();
            }
        });

        // Inicjalizacja bazy danych
        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        restaurantDao = myDB.getRestaurantDao();
        categoryDao = myDB.getCategoryDao();
    }

    private void showRestaurantListDialog(List<RestaurantTable> restaurants) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wybierz restaurację do edycji");

        List<String> restaurantNames = new ArrayList<>();
        for (RestaurantTable restaurant : restaurants) {
            restaurantNames.add(restaurant.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantNames);
        ListView listView = new ListView(this);
        listView.setAdapter(arrayAdapter);
        builder.setView(listView);

        AlertDialog dialog = builder.create();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            RestaurantTable restaurantToEdit = restaurants.get(position);
            showRestaurantDialog(Actions.EDIT, restaurantToEdit);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showRestaurantDialog(Actions action, RestaurantTable restaurantToEdit) {
        DialogAddCategoryBinding dialogBinding = DialogAddCategoryBinding.inflate(LayoutInflater.from(this));
        View dialogView = dialogBinding.getRoot();

        AppCompatButton btnAction = dialogBinding.dialogButton;
        AppCompatButton btnCancel = dialogBinding.cancelButton;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        switch (action) {
            case ADD:
                btnAction.setText("Dodaj");
                break;
            case EDIT:
                btnAction.setText("Edytuj");
                if (restaurantToEdit != null) {
                    this.restaurantToEdit = restaurantToEdit;
                    dialogBinding.editCategoryName.setText(restaurantToEdit.getName());
                }
                break;
            case DELETE:
                btnAction.setText("Usuń");
                if (this.restaurantToEdit != null) {
                    restaurantDao.delete(this.restaurantToEdit);
                    Toast.makeText(this, "Usunięto restaurację " + this.restaurantToEdit.getName(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                break;
        }

        btnAction.setOnClickListener(view -> {
            String restaurantName = dialogBinding.editCategoryName.getText().toString().trim();

            if (!restaurantName.isEmpty()) {
                switch (action) {
                    case ADD:
                        List<CategoryTable> categories = categoryDao.getAllCategoriesSync();

                        if (categories.isEmpty()) {
                            Toast.makeText(this, "Dodaj najpierw kategorie", Toast.LENGTH_SHORT).show();
                        } else {
                            String[] categoryNames = new String[categories.size()];
                            for (int i = 0; i < categories.size(); i++) {
                                categoryNames[i] = categories.get(i).getName();
                            }

                            AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(this);
                            categoryBuilder.setTitle("Wybierz kategorię");
                            categoryBuilder.setItems(categoryNames, (categoryDialog, categoryIndex) -> {
                                CategoryTable selectedCategory = categories.get(categoryIndex);

                                RestaurantTable newRestaurant = new RestaurantTable();
                                newRestaurant.setName(restaurantName);
                                newRestaurant.setCategoryId(selectedCategory.getId());

                                long restaurantId = restaurantDao.insert(newRestaurant);

                                if (restaurantId > 0) {
                                    Toast.makeText(this, "Dodano restaurację: " + restaurantName + " do kategorii: " + selectedCategory.getName(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(this, "Błąd podczas dodawania restauracji", Toast.LENGTH_SHORT).show();
                                }
                            });

                            categoryBuilder.show();
                        }
                        break;
                    case EDIT:
                        if (this.restaurantToEdit != null) {
                            this.restaurantToEdit.setName(restaurantName);
                            restaurantDao.update(this.restaurantToEdit);
                            Toast.makeText(this, "Zaktualizowano restaurację: " + restaurantName, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        break;
                    case DELETE:
                        if (this.restaurantToEdit != null) {
                            restaurantDao.delete(this.restaurantToEdit);
                            Toast.makeText(this, "Usunięto restaurację " + this.restaurantToEdit.getName(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        break;
                }
            } else {
                Toast.makeText(this, "Podaj nazwę restauracji", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }


    private void showDeleteRestaurantListDialog() {
        List<RestaurantTable> restaurants = restaurantDao.getAllRestaurantsSync();

        if (!restaurants.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wybierz restaurację do usunięcia");

            List<String> restaurantNames = new ArrayList<>();
            for (RestaurantTable restaurant : restaurants) {
                restaurantNames.add(restaurant.getName());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantNames);
            builder.setAdapter(arrayAdapter, (dialog, which) -> {
                RestaurantTable restaurantToDelete = restaurants.get(which);
                restaurantDao.delete(restaurantToDelete);
                Toast.makeText(this, "Usunięto restaurację: " + restaurantToDelete.getName(), Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss());

            AlertDialog deleteDialog = builder.create();
            deleteDialog.show();
        } else {
            Toast.makeText(this, "Brak dostępnych restauracji do usunięcia", Toast.LENGTH_SHORT).show();
        }
    }
}
