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
import com.example.sm_project.Dao.OrderDao;
import com.example.sm_project.Dao.RestaurantDao;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.OrderTable;
import com.example.sm_project.Helper.RestaurantTable;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityOrderManagmentBinding;
import com.example.sm_project.databinding.DialogAddCategoryBinding;
import com.example.sm_project.databinding.DialogOrderBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderSettingsActivity extends AppCompatActivity {
    private ActivityOrderManagmentBinding binding;

    private ImageView backBtn;
    private AppCompatButton addBtn, deleteBtn, editBtn;
    private TextView titleTxt;
    private MyDataBase myDB;
    private OrderDao orderDao;
    private RestaurantDao restaurantDao;
    private int selectedOrderPosition; // Added field to store the selected order position

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderManagmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        orderDao = myDB.getOrderDao();
        restaurantDao = myDB.getRestaurantDao();
    }

    private void initializeViews() {
        titleTxt = findViewById(R.id.titleTxt);
        titleTxt.setText(getString(R.string.settings_order));

        backBtn = findViewById(R.id.backBtn);
        addBtn = findViewById(R.id.addBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        backBtn.setOnClickListener(v -> startActivity(new Intent(OrderSettingsActivity.this, SettingsActivity.class)));
        deleteBtn.setOnClickListener(v -> showDeleteOrdersListDialog());
        editBtn.setOnClickListener(v -> showEditOrdersListDialog());
    }

    private void handleAction(Actions action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (action) {
            case EDIT:
                handleEditAction(builder);
                break;
            case DELETE:
                handleDeleteAction(builder);
                break;
        }
    }

    private void showDeleteOrdersListDialog() {
        List<OrderTable> orders = orderDao.getAllOrdersSync();

        if (!orders.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select order to delete");

            List<String> orderDetails = getOrdersDetails(orders);

            builder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderDetails),
                    (dialog, which) -> {
                        selectedOrderPosition = which; // Set the selected order position
                        handleAction(Actions.DELETE);
                    });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        } else {
            Toast.makeText(this, "No orders available for deleting", Toast.LENGTH_SHORT).show();
        }
    }


    private void handleDeleteAction(AlertDialog.Builder builder) {
        OrderTable orderToDelete = orderDao.getAllOrdersSync().get(selectedOrderPosition);
        orderDao.delete(orderToDelete);
        Toast.makeText(this, "Order deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private List<String> getOrdersDetails(List<OrderTable> orderTables) {
        List<String> orderDetails = new ArrayList<>();
        for (OrderTable order : orderTables) {
            String details = "Order ID: " + order.getId() +
                    "\nDate: " + order.getDate() +
                    "\nAmount: " + order.getPrice() +
                    "\nRestaurant: " + getRestaurantName(order.getRestaurantId()) +
                    "\n------------------------------------";
            orderDetails.add(details);
        }
        return orderDetails;
    }

    private String getRestaurantName(int restaurantId) {
        return restaurantDao.getRestaurantNameById(restaurantId);
    }

    private void showEditOrdersListDialog() {
        List<OrderTable> orders = orderDao.getAllOrdersSync();

        if (!orders.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select order to edit");

            List<String> orderNames = getOrdersDetails(orders);

            builder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderNames),
                    (dialog, which) -> {
                        selectedOrderPosition = which; // Set the selected order position
                        handleAction(Actions.EDIT);
                    });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        } else {
            Toast.makeText(this, "No orders available for editing", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOrdersDialog(Actions action, OrderTable orderTable) {
        DialogOrderBinding dialogBinding = DialogOrderBinding.inflate(LayoutInflater.from(this));
        View dialogView = dialogBinding.getRoot();

        AppCompatButton btnAction = dialogBinding.dialogButton;
        AppCompatButton btnCancel = dialogBinding.cancelButton;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

       // configureDialog(action, btnAction, dialogBinding);

       // btnAction.setOnClickListener(view -> handleAction(action, dialogBinding, dialog));

        btnCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void handleEditAction(AlertDialog.Builder builder) {
        List<OrderTable> orders = orderDao.getAllOrdersSync();

        if (!orders.isEmpty()) {
            builder.setTitle("Select order to edit");

            List<String> orderDetails = getOrdersDetails(orders);

            builder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderDetails),
                    (dialog, which) -> {
                        selectedOrderPosition = which; // Set the selected order position
                        showEditOrderDialog(orders.get(selectedOrderPosition));
                    });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        } else {
            Toast.makeText(this, "No orders available for editing", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditOrderDialog(OrderTable order) {
        DialogOrderBinding dialogBinding = DialogOrderBinding.inflate(LayoutInflater.from(this));
        View dialogView = dialogBinding.getRoot();

        EditText editRestaurantName = dialogBinding.editRestaurantName;
        EditText editOrderDate = dialogBinding.editOrderDate;
        EditText editOrderAmount = dialogBinding.editOrderAmount;
        AppCompatButton btnAction = dialogBinding.dialogButton;
        AppCompatButton btnCancel = dialogBinding.cancelButton;

        // Set initial values in the dialog
        editRestaurantName.setText(getRestaurantName(order.getRestaurantId()));
        editOrderDate.setText(formatDate(order.getDate())); // Use a method to format the Date
        editOrderAmount.setText(String.valueOf(order.getPrice()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        btnAction.setOnClickListener(view -> handleEditOrderAction(order, dialogBinding, dialog));
        btnCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void handleEditOrderAction(OrderTable order, DialogOrderBinding dialogBinding, AlertDialog dialog) {
        String editedOrderDate = dialogBinding.editOrderDate.getText().toString().trim();
        String editedOrderAmount = dialogBinding.editOrderAmount.getText().toString().trim();

        order.setDate(formatDate(editedOrderDate));
        order.setPrice(Double.parseDouble(editedOrderAmount));

        orderDao.update(order);

        dialog.dismiss();
        Toast.makeText(this, "Order updated successfully", Toast.LENGTH_SHORT).show();
    }

    private Date formatDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }


}
