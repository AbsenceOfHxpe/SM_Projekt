package com.example.sm_project.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    @Override
    protected void onPostCreate( Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setVariable();
    }

    private void setVariable() {
        binding.registerBtn.setOnClickListener(v -> {
            String email = binding.emailText.getText().toString();
            String password = binding.passwordText.getText().toString();
            String login = binding.loginText.getText().toString();

            if (password.length() < 6 || !password.matches(".*\\d.*") || !password.matches(".*[A-Z].*")) {
                showCustomDialog("Twoje hasło musi składać się z co najmniej 6 znaków, zawierać 1 cyfrę i 1 wielką literę");
                return;
            }

            else if (!email.contains("@")) {
                showCustomDialog("Nieprawidłowy format adresu email");
                return;
            }

            else if((password.length() < 6 || !password.matches(".*\\d.*") && (!email.contains("@")) )){
                showCustomDialog("Nieprawidłowy format adresu email \n Twoje hasło musi składać się z co najmniej 6 znaków, zawierać 1 cyfrę i 1 wielką literę");
                return;

            }

            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });


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
