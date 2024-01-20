package com.example.sm_project.Activity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.example.sm_project.Dao.UserDao;
import com.example.sm_project.Helper.MyDataBase;
import com.example.sm_project.Helper.UserTable;
import com.example.sm_project.R;
import com.example.sm_project.databinding.ActivityRegisterBinding;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private MyDataBase myDB;
    private UserDao userDao;
    private boolean isUsernameAllowed = false;
    private boolean isEmailValid = false;
    private boolean isPasswordValid = false;
    private boolean isEmailTaken = false;

    private static final String CHANNEL_ID = "CHANEL_ID_NOTIFICATIONS";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = Room.databaseBuilder(this, MyDataBase.class, "Database_db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = myDB.getDao();

        setListeners();

        binding.registerBtn.setOnClickListener(v -> registerUser());
    }

    private void setListeners() {
        binding.loginText.addTextChangedListener(createSimpleTextWatcher(userName -> {
            isUsernameAllowed = !userDao.is_taken(userName);
        }));

        binding.emailText.addTextChangedListener(createSimpleTextWatcher(email -> {
            isEmailTaken = !userDao.isEmailTaken(email);
        }));

        binding.emailText.addTextChangedListener(createSimpleTextWatcher(email -> {
            isEmailValid = isValidEmail(email);
        }));

        binding.passwordText.addTextChangedListener(createSimpleTextWatcher(password -> {
            isPasswordValid = isValidPassword(password);
        }));

        binding.loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private TextWatcher createSimpleTextWatcher(SimpleTextChangeListener listener) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.onTextChanged(s.toString());
            }
        };
    }

    private void makeNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Zniżka powitalna")
                .setContentText("Dziękujemy za rejestrację. Otrzymujesz zniżkę 15% na pierwsze zamówienie." +
                        "Wpisz kod: 12345 i ciesz się pysznym jedzeniem w atrakcyjnej cenie!")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Dziękujemy za rejestrację. Otrzymujesz zniżkę 15% na pierwsze zamówienie. Wpisz kod: ABCDE i ciesz się pysznym jedzeniem w atrakcyjnej cenie!"));


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (checkSelfPermission(android.Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            // Posiadasz uprawnienia do wibracji, możesz wywołać notify
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
            // Brak uprawnień, możesz poprosić użytkownika o nie
            // Możesz to zrobić na przykład poprzez wyświetlenie okna dialogowego z prośbą o uprawnienia
           // requestPermissions(new String[]{android.Manifest.permission.VIBRATE}, REQUEST_VIBRATE_PERMISSION);
        }
    }

    private void registerUser() {
        String userName = binding.loginText.getText().toString();
        String email = binding.emailText.getText().toString();
        String password = binding.passwordText.getText().toString();

        if (isUsernameAllowed && isEmailValid && isPasswordValid && isEmailTaken) {
            UserTable userTable = new UserTable(0, userName, email, password);
            userDao.insertUser(userTable);

            showToast("Registration Successful");
            makeNotification();

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("userLogin", userName);
            startActivity(intent);
            finish();


        } else {
            if (!isUsernameAllowed) {
                showToast("Username Already Taken");
            }
            if (!isEmailValid) {
                showToast("Invalid Email");
            }
            if (!isPasswordValid) {
                showToast("Invalid Password");
            }
             if (!isEmailTaken) {
                showToast("Email Already Taken");
            }
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && email.contains("@");
    }


    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*\\d).{6,}$";
        return !TextUtils.isEmpty(password) && Pattern.compile(passwordPattern).matcher(password).matches();
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private interface SimpleTextChangeListener {
        void onTextChanged(String text);
    }
}
