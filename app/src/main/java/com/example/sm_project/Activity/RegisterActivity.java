package com.example.sm_project.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.sm_project.Dao.UserDao;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.UserTable;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    MyDataBase myDB;
    UserDao userDao;
    public static boolean isAllowed = false;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myDB = Room.databaseBuilder(this, MyDataBase.class, "usertable")
                        .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = myDB.getDao();

        setVariable();

        binding.loginText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userName=s.toString();
                if(userDao.is_taken(userName)){
                    isAllowed = false;
                    Toast.makeText(RegisterActivity.this, "Already Taken", Toast.LENGTH_SHORT).show();

                } else{
                    isAllowed=true;
                }

            }
        });
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllowed){
                    UserTable userTable = new UserTable(0,binding.loginText.getText().toString(),
                           binding.emailText.getText().toString() ,
                            binding.passwordText.getText().toString());
                    userDao.insertUser(userTable);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(RegisterActivity.this, "Username Already Taken", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setVariable() {


        binding.loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void showCustomDialog(String message) {
        final Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.setContentView(R.layout.custom_dialog);

        TextView dialogMessage = dialog.findViewById(R.id.dialogMessage);
        dialogMessage.setText(message);

        Button dialogButton = dialog.findViewById(R.id.dialogButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
