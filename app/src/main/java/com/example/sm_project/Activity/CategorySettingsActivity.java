package com.example.sm_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.room.Room;

import com.example.sm_project.Dao.CategoryDao;
import com.example.sm_project.Helper.CategoryTable;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityCatManagmentBinding;
import com.example.sm_project.databinding.DialogAddCategoryBinding;
import com.example.sm_project.Actions; // Import enum Actions

import java.util.List;

public class CategorySettingsActivity extends AppCompatActivity {

    private ActivityCatManagmentBinding binding;
    private ImageView backBtn;
    private AppCompatButton addBtn, deleteBtn, editBtn;

    MyDataBase myDB;
    private CategoryDao categoryDao;

    private CategoryTable categoryToEdit; // Dodane pole do przechowywania obecnie edytowanej kategorii

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatManagmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backBtn = findViewById(R.id.backBtn);
        addBtn = findViewById(R.id.addBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        categoryDao = myDB.getCategoryDao();

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CategorySettingsActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        addBtn.setOnClickListener(v -> showCategoryDialog(Actions.ADD, null));
        deleteBtn.setOnClickListener(v -> showCategoryDialog(Actions.DELETE, null));
        editBtn.setOnClickListener(v -> {
            List<CategoryTable> categories = categoryDao.getAllCategoriesSync();

            if (!categories.isEmpty()) {
                // Stworzenie tablicy z nazwami kategorii
                String[] categoryNames = new String[categories.size()];
                for (int i = 0; i < categories.size(); i++) {
                    categoryNames[i] = categories.get(i).getName();
                }

                // Wybór kategorii przy użyciu AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Wybierz kategorię do edycji");
                builder.setItems(categoryNames, (dialog, which) -> {
                    CategoryTable categoryToEdit = categories.get(which);
                    showCategoryDialog(Actions.EDIT, categoryToEdit);
                });
                builder.show();
            } else {
                Toast.makeText(this, "Brak dostępnych kategorii do edycji", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCategoryDialog(Actions action, CategoryTable categoryToEdit) {
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
                if (categoryToEdit != null) {
                    this.categoryToEdit = categoryToEdit; // Przypisz obecnie edytowaną kategorię
                    dialogBinding.editCategoryName.setText(categoryToEdit.getName());
                }
                break;
            case DELETE:
                btnAction.setText("Usuń");
                break;
        }

        btnAction.setOnClickListener(view -> {
            String categoryName = dialogBinding.editCategoryName.getText().toString().trim();

            if (!categoryName.isEmpty()) {
                switch (action) {
                    case ADD:
                        if (categoryDao.isNameTaken(categoryName)) {
                            Toast.makeText(this, "Kategoria o nazwie " + categoryName + " już istnieje", Toast.LENGTH_SHORT).show();
                        } else {
                            CategoryTable newCategory = new CategoryTable();
                            newCategory.setName(categoryName);
                            long categoryId = categoryDao.insert(newCategory);

                            if (categoryId > 0) {
                                Toast.makeText(this, "Dodano kategorię: " + categoryName, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Błąd podczas dodawania kategorii", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case EDIT:
                        if (this.categoryToEdit != null) {
                            this.categoryToEdit.setName(categoryName);
                            categoryDao.update(this.categoryToEdit);
                            Toast.makeText(this, "Zaktualizowano kategorię: " + categoryName, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        break;
                    case DELETE:
                        int restaurantCount = categoryDao.getRestaurantCountForCategory(categoryName);

                        if (restaurantCount > 0) {
                            Toast.makeText(this, "Nie można usunąć kategorii. Istnieją powiązane restauracje.", Toast.LENGTH_SHORT).show();
                        } else {
                            int deletedRows = categoryDao.deleteByName(categoryName);

                            if (deletedRows > 0) {
                                Toast.makeText(this, "Usunięto kategorię: " + categoryName, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Brak kategorii o nazwie: " + categoryName, Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
            } else {
                Toast.makeText(this, "Podaj nazwę kategorii", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }
}
