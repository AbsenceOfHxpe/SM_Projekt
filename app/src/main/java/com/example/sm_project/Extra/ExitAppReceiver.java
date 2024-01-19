package com.example.sm_project.Extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ExitAppReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, ExitAppService.class);
        context.startService(serviceIntent);

    }
}
