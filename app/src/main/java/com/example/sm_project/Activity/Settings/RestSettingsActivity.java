package com.example.sm_project.Activity.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.example.sm_project.databinding.ActivityRestManagmentBinding;
import com.example.sm_project.databinding.DialogAddCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class RestSettingsActivity extends AppCompatActivity {
    private ActivityRestManagmentBinding binding;
    private ImageView backBtn;
    private AppCompatButton addBtn, deleteBtn, editBtn;
    private TextView titleTxt;
    private EditText editCategoryName;

    private MyDataBase myDB;
    private RestaurantDao restaurantDao;
    private CategoryDao categoryDao;

    private RestaurantTable restaurantToEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestManagmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        restaurantDao = myDB.getRestaurantDao();
        categoryDao = myDB.getCategoryDao();
    }

    private void initializeViews() {
        titleTxt = findViewById(R.id.titleTxt);
        titleTxt.setText(getString(R.string.settings_restaurant));

        backBtn = findViewById(R.id.backBtn);
        addBtn = findViewById(R.id.addBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        backBtn.setOnClickListener(v -> startActivity(new Intent(RestSettingsActivity.this, SettingsActivity.class)));
        addBtn.setOnClickListener(v -> showRestaurantDialog(Actions.ADD, null));
        deleteBtn.setOnClickListener(v -> showDeleteRestaurantListDialog());
        editBtn.setOnClickListener(v -> showEditRestaurantListDialog());
    }

    private void showEditRestaurantListDialog() {
        List<RestaurantTable> restaurants = restaurantDao.getAllRestaurantsSync();

        if (!restaurants.isEmpty()) {
            showRestaurantListDialog(restaurants);
        } else {
            Toast.makeText(this, getString(R.string.no_restaurants_to_edit), Toast.LENGTH_SHORT).show();
        }
    }

    private void showRestaurantListDialog(List<RestaurantTable> restaurants) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_restaurant_to_edit));

        List<String> restaurantNames = getRestaurantNames(restaurants);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantNames);
        ListView listView = new ListView(this);
        listView.setAdapter(arrayAdapter);
        builder.setView(listView);

        AlertDialog dialog = builder.create();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            restaurantToEdit = restaurants.get(position);
            showRestaurantDialog(Actions.EDIT, restaurantToEdit);
            dialog.dismiss();
        });

        dialog.show();
    }

    private List<String> getRestaurantNames(List<RestaurantTable> restaurants) {
        List<String> restaurantNames = new ArrayList<>();
        for (RestaurantTable restaurant : restaurants) {
            restaurantNames.add(restaurant.getName());
        }
        return restaurantNames;
    }

    private void showDeleteRestaurantListDialog() {
        List<RestaurantTable> restaurants = restaurantDao.getAllRestaurantsSync();

        if (!restaurants.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.choose_restaurant_to_delete));

            List<String> restaurantNames = getRestaurantNames(restaurants);

            builder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantNames),
                    (dialog, which) -> {
                        RestaurantTable restaurantToDelete = restaurants.get(which);
                        restaurantDao.delete(restaurantToDelete);
                        Toast.makeText(this, getString(R.string.deleted_restaurant, restaurantToDelete.getName()), Toast.LENGTH_SHORT).show();
                    });

            builder.setNegativeButton(getString(R.string.cancel_button), (dialog, which) -> dialog.dismiss());

            builder.create().show();
        } else {
            Toast.makeText(this, getString(R.string.no_restaurants_to_delete), Toast.LENGTH_SHORT).show();
        }
    }

    private void showRestaurantDialog(Actions action, RestaurantTable restaurantToEdit) {
        DialogAddCategoryBinding dialogBinding = DialogAddCategoryBinding.inflate(LayoutInflater.from(this));
        View dialogView = dialogBinding.getRoot();

        editCategoryName = findViewById(R.id.editCategoryName);
        titleTxt.setText(getString(R.string.add_restaurant));

        AppCompatButton btnAction = dialogBinding.dialogButton;
        AppCompatButton btnCancel = dialogBinding.cancelButton;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        configureDialog(action, btnAction, dialogBinding);

        btnAction.setOnClickListener(view -> handleAction(action, dialogBinding, dialog));

        btnCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void configureDialog(Actions action, AppCompatButton btnAction, DialogAddCategoryBinding dialogBinding) {
        switch (action) {
            case ADD:
                btnAction.setText(getString(R.string.add));
                break;
            case EDIT:
                btnAction.setText(getString(R.string.edit));
                if (restaurantToEdit != null) {
                    this.restaurantToEdit = restaurantToEdit;
                    dialogBinding.editCategoryName.setText(restaurantToEdit.getName());
                }
                break;
            case DELETE:
                btnAction.setText(getString(R.string.delete));
                break;
        }
    }

    private void handleAction(Actions action, DialogAddCategoryBinding dialogBinding, AlertDialog dialog) {
        String restaurantName = dialogBinding.editCategoryName.getText().toString().trim();

        if (!restaurantName.isEmpty()) {
            switch (action) {
                case ADD:
                    handleAddAction(dialogBinding, restaurantName);
                    break;
                case EDIT:
                    handleEditAction(dialogBinding, restaurantName, dialog);
                    break;
                case DELETE:
                    handleDeleteAction(dialog);
                    break;
            }
        } else {
            Toast.makeText(this, getString(R.string.enter_restaurant_name), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleAddAction(DialogAddCategoryBinding dialogBinding, String restaurantName) {
        List<CategoryTable> categories = categoryDao.getAllCategoriesSync();

        if (categories.isEmpty()) {
            Toast.makeText(this, getString(R.string.add_categories_first), Toast.LENGTH_SHORT).show();
        } else {
            showCategorySelectionDialog(categories, restaurantName);
        }
    }

    private void showCategorySelectionDialog(List<CategoryTable> categories, String restaurantName) {
        String[] categoryNames = getCategoryNames(categories);

        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(this);
        categoryBuilder.setTitle(getString(R.string.choose_restaurant));
        categoryBuilder.setItems(categoryNames, (categoryDialog, categoryIndex) -> {
            CategoryTable selectedCategory = categories.get(categoryIndex);
            saveRestaurantToDatabase(restaurantName, selectedCategory);
        });

        categoryBuilder.show();
    }

    private String[] getCategoryNames(List<CategoryTable> categories) {
        String[] categoryNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
        }
        return categoryNames;
    }

    private void saveRestaurantToDatabase(String restaurantName, CategoryTable selectedCategory) {
        RestaurantTable newRestaurant = new RestaurantTable();
        newRestaurant.setName(restaurantName);
        newRestaurant.setCategoryId(selectedCategory.getId());

        long restaurantId = restaurantDao.insert(newRestaurant);

        if (restaurantId > 0) {
            Toast.makeText(this, getString(R.string.added_restaurant_to_category, restaurantName, selectedCategory.getName()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.error_adding_restaurant), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleEditAction(DialogAddCategoryBinding dialogBinding, String restaurantName, AlertDialog dialog) {
        if (this.restaurantToEdit != null) {
            this.restaurantToEdit.setName(restaurantName);
            restaurantDao.update(this.restaurantToEdit);
            Toast.makeText(this, getString(R.string.updated_restaurant, restaurantName), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void handleDeleteAction(AlertDialog dialog) {
        if (this.restaurantToEdit != null) {
            restaurantDao.delete(this.restaurantToEdit);
            Toast.makeText(this, getString(R.string.deleted_restaurant, this.restaurantToEdit.getName()), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
}
