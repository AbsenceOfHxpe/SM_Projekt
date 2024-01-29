package com.example.sm_project.Activity.Settings;

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

    private ImageView backBtn;
    private AppCompatButton addBtn;
    private AppCompatButton deleteBtn;
    private AppCompatButton editBtn;

    private CategoryDao categoryDao;
    private CategoryTable categoryToEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.sm_project.databinding.ActivityCatManagmentBinding binding = ActivityCatManagmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();

        MyDataBase myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        categoryDao = myDB.getCategoryDao();

        setClickListeners();
    }

    private void initializeViews() {
        backBtn = findViewById(R.id.backBtn);
        addBtn = findViewById(R.id.addBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
    }

    private void setClickListeners() {
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CategorySettingsActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        addBtn.setOnClickListener(v -> showCategoryDialog(Actions.ADD, null));
        deleteBtn.setOnClickListener(v -> showDeleteCategoryDialog());
        editBtn.setOnClickListener(v -> showEditCategoryDialog());
    }

    private void showEditCategoryDialog() {
        List<CategoryTable> categories = categoryDao.getAllCategoriesSync();

        if (!categories.isEmpty()) {
            showCategorySelectionDialog(categories);
        } else {
            Toast.makeText(this, R.string.no_categories_to_edit, Toast.LENGTH_SHORT).show();
        }
    }

    private void showCategorySelectionDialog(List<CategoryTable> categories) {
        String[] categoryNames = getCategoryNames(categories);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_cat);
        builder.setItems(categoryNames, (dialog, which) -> {
            CategoryTable categoryToEdit = categories.get(which);
            showCategoryDialog(Actions.EDIT, categoryToEdit);
        });
        builder.show();
    }

    private String[] getCategoryNames(List<CategoryTable> categories) {
        String[] categoryNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
        }
        return categoryNames;
    }

    private void showCategoryDialog(Actions action, CategoryTable categoryToEdit) {


        DialogAddCategoryBinding dialogBinding = DialogAddCategoryBinding.inflate(LayoutInflater.from(this));
        View dialogView = dialogBinding.getRoot();

        AppCompatButton btnAction = dialogBinding.dialogButton;
        AppCompatButton btnCancel = dialogBinding.cancelButton;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        configureDialogForAction(action, dialogBinding, categoryToEdit);

        btnAction.setOnClickListener(view -> handleAction(action, dialogBinding, dialog));
        btnCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }




    private void showDeleteCategoryDialog() {
        List<CategoryTable> categories = categoryDao.getAllCategoriesSync();

        if (!categories.isEmpty()) {
            showCategorySelectionDialogForDelete(categories);
        } else {
            Toast.makeText(this, R.string.no_categories_to_delete, Toast.LENGTH_SHORT).show();
        }
    }

    private void showCategorySelectionDialogForDelete(List<CategoryTable> categories) {
        String[] categoryNames = getCategoryNames(categories);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_cat_to_delete);
        builder.setItems(categoryNames, (dialog, which) -> {
            CategoryTable categoryToDelete = categories.get(which);
            handleDeleteAction(categoryToDelete.getName(), null);
        });
        builder.show();
    }



    private void configureDialogForAction(Actions action, DialogAddCategoryBinding dialogBinding, CategoryTable categoryToEdit) {
        switch (action) {
            case ADD:
                configureAddDialog(dialogBinding);
                break;
            case EDIT:
                configureEditDialog(dialogBinding, categoryToEdit);
                break;
            case DELETE:
                configureDeleteDialog(dialogBinding);
                break;
        }
    }

    private void configureAddDialog(DialogAddCategoryBinding dialogBinding) {
        dialogBinding.dialogButton.setText(R.string.add);
    }

    private void configureEditDialog(DialogAddCategoryBinding dialogBinding, CategoryTable categoryToEdit) {
        dialogBinding.dialogButton.setText(R.string.edit);
        if (categoryToEdit != null) {
            this.categoryToEdit = categoryToEdit;
            dialogBinding.editCategoryName.setText(categoryToEdit.getName());
        }
    }

    private void configureDeleteDialog(DialogAddCategoryBinding dialogBinding) {
        dialogBinding.dialogButton.setText(R.string.delete);
    }

    private void handleAction(Actions action, DialogAddCategoryBinding dialogBinding, AlertDialog dialog) {
        String categoryName = dialogBinding.editCategoryName.getText().toString().trim();

        if (!categoryName.isEmpty()) {
            switch (action) {
                case ADD:
                    handleAddAction(categoryName, dialog);
                    break;
                case EDIT:
                    handleEditAction(categoryName, dialog);
                    break;
                case DELETE:
                    handleDeleteAction(categoryName, dialog);
                    break;
            }
        } else {
            Toast.makeText(this, R.string.enter_category_name, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleAddAction(String categoryName, AlertDialog dialog) {
        if (categoryDao.isNameTaken(categoryName)) {
            String message = getString(R.string.category_already_exists, categoryName);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            CategoryTable newCategory = new CategoryTable();
            newCategory.setName(categoryName);
            long categoryId = categoryDao.insert(newCategory);

            if (categoryId > 0) {
                String message = getString(R.string.category_added, categoryName);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, R.string.error_adding_category, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleEditAction(String categoryName, AlertDialog dialog) {
        if (this.categoryToEdit != null) {
            this.categoryToEdit.setName(categoryName);
            categoryDao.update(this.categoryToEdit);
            String message = getString(R.string.category_updated, categoryName);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void handleDeleteAction(String categoryName, AlertDialog dialog) {
        int restaurantCount = categoryDao.getRestaurantCountForCategory(categoryName);

        if (restaurantCount > 0) {
            Toast.makeText(this, R.string.cannot_delete_category, Toast.LENGTH_SHORT).show();
        } else {
            int deletedRows = categoryDao.deleteByName(categoryName);

            if (deletedRows > 0) {
                String message = getString(R.string.category_deleted, categoryName);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (dialog != null) {
                    dialog.dismiss();
                }
            } else {
                String message = getString(R.string.no_category_with_name, categoryName);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }


}