package com.swarooprao.androidapps.swipewiz;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public AlarmBroadcastReceiver() {
    }


    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.swarooprao.androidapps.swipewiz.action.FOO";
    private static final String ACTION_BAZ = "com.swarooprao.androidapps.swipewiz.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.swarooprao.androidapps.swipewiz.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.swarooprao.androidapps.swipewiz.extra.PARAM2";

    @Override
    public void onReceive (Context context, Intent intent) {
        int diffDays = -1, daysBetween = 0;
        String cardName, cardNumber;
        String bestCardName = "None", bestCardNumber;
        int bestBillDate;
        int curMonth, curYear, curDay;
        Calendar today, billDate;
        int billMonth, billYear, billDay;
        CardDBHelper dbHelper = new CardDBHelper(context);

        if (intent != null) {
            Cursor cursor = dbHelper.getCards();
            today = Calendar.getInstance();
            curMonth = today.get(Calendar.MONTH);
            curYear = today.get(Calendar.YEAR);
            curDay = today.get(Calendar.DAY_OF_MONTH);
            billMonth = curMonth;
            billYear = curYear;
            billDay = curDay;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cardName = cursor.getString(1);
                cardNumber = cursor.getString(2);
                billDay = cursor.getInt(3);
                billDate = Calendar.getInstance();
                if (curDay > billDay) {
                    billMonth = curMonth + 1;
                    if (billMonth > 11) {
                        billMonth = 0;
                        billYear = curYear + 1;
                    }

                    billDate.set(Calendar.YEAR, billYear);
                    billDate.set(Calendar.MONTH, billMonth);
                    billDate.set(Calendar.DAY_OF_MONTH, billDay);
                } else if (curDay < billDay) {
                    billMonth = curMonth;
                    billYear = curYear;

                    billDate.set(Calendar.YEAR, billYear);
                    billDate.set(Calendar.MONTH, billMonth);
                    billDate.set(Calendar.DAY_OF_MONTH, billDay);
                }
                daysBetween = 0;
                today = Calendar.getInstance();
                while (today.compareTo(billDate) < 0) {
                    today.add(Calendar.DAY_OF_MONTH, 1);
                    daysBetween++;
                }
                if (daysBetween > diffDays) {
                    diffDays = daysBetween;
                    bestCardName = cardName;
                    bestCardNumber = cardNumber;
                    bestBillDate = billDay;
                }
                cursor.moveToNext();
            }
            //Here, diffDays contains the max amount of days left to a billing date
            //bestCardName contains the best Card Name
            //bestCardNumber contains the best Card #
            //bestBillDate contains the "furthest" billing date
            Intent notificationIntent = new Intent(context, MainActivity.class);
            // use System.currentTimeMillis() to have a unique ID for the pending intent
            PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_stat_18_512)
                            .setContentTitle("Best card to use today: ")
                            .setContentText(bestCardName);
            mBuilder.setContentIntent(pIntent);
            // Sets an ID for the notification
            int mNotificationId = 2262;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
            cursor.close();
        }
    }
}
