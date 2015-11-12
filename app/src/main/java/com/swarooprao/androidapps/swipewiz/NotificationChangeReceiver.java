package com.swarooprao.androidapps.swipewiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationChangeReceiver extends BroadcastReceiver {
    public NotificationChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent service = new Intent(context, ShowNotificationIntentService.class);
        context.startService(service);
    }
}
