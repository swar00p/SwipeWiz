package com.swarooprao.androidapps.swipewiz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmCreationService extends Service {
    public AlarmCreationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Send a one-time "immediate" trigger to update the notification before setting the alarm
        //that goes off at midnight
        Intent alarmBroadcastIntent = new Intent(this, AlarmBroadcastReceiver.class);
        getApplicationContext().sendBroadcast(alarmBroadcastIntent);

        //Set the alarm to go off at midnight if it's not already been set
        //Because this can be called from the Activity as well as the BootBroadcastReceiver
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
                new Intent(this, AlarmBroadcastReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(this, AlarmBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
            //int alarmType = AlarmManager.ELAPSED_REALTIME;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 1);
            int alarmType = AlarmManager.RTC_WAKEUP;
            long triggerTime = calendar.getTimeInMillis();
            long intervalTime = AlarmManager.INTERVAL_DAY;

            alarmManager.setInexactRepeating(alarmType, triggerTime, intervalTime, pendingIntent);
        }
        return START_STICKY;
    }
}
