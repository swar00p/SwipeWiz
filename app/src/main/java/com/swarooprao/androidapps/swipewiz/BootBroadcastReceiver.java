package com.swarooprao.androidapps.swipewiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent serviceIntent = new Intent(context, AlarmCreationService.class);
        context.startService(serviceIntent);
    }
}
