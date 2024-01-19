package com.example.sm_project.Extra;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sm_project.R;

public class ExitAppService extends Service {

    private static final String CHANNEL_ID = "CHANEL_ID_NOTIFICATIONS";
    private static final int NOTIFICATION_ID = 1;

    private BroadcastReceiver exitAppReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        exitAppReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sendNotification();
                stopSelf();
            }
        };

        // Zarejestruj odbiornik w momencie utworzenia usługi
        registerReceiver(exitAppReceiver, new IntentFilter("ExitApp"));
    }

    @Override
    public void onDestroy() {
        // Zatrzymaj serwis i zwolnij zasoby w momencie zniszczenia usługi
        unregisterReceiver(exitAppReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendNotification() {
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
                .setContentTitle("Wróć i dokończ zamówienie")
                .setContentText("Dokończ swoje zamówienie i ciesz się pysznym jedzeniem na dowóz!")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

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
}
