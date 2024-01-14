package com.example.sm_project.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
ActivityRegisterBinding binding;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setVariable();
    }

    private void setVariable() {
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.emailText.getText().toString();
                String password = binding.passwordText.getText().toString();
                String login = binding.loginText.getText().toString();

                if (password.length() >= 6 && password.matches(".*\\d.*")
                        && password.matches(".*[A-Z].*")) {

                } else {
                    Toast.makeText(RegisterActivity.this, "Twoje hasło musi składać się z co najmniej 6 znaków, zawierać 1 cyfrę i 1 wielką literę",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
