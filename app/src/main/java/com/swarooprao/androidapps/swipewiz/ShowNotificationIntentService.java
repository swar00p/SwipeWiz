package com.swarooprao.androidapps.swipewiz;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.Duration;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ShowNotificationIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.swarooprao.androidapps.swipewiz.action.FOO";
    private static final String ACTION_BAZ = "com.swarooprao.androidapps.swipewiz.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.swarooprao.androidapps.swipewiz.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.swarooprao.androidapps.swipewiz.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ShowNotificationIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ShowNotificationIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public ShowNotificationIntentService() {
        super("ShowNotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int diffDays = -1, daysBetween = 0;
        String cardName, cardNumber;
        String bestCardName = "None", bestCardNumber;
        int bestBillDate;
        int curMonth, curYear, curDay;
        Calendar today, billDate;
        int billMonth, billYear, billDay;
        CardDBHelper dbHelper = new CardDBHelper(getApplicationContext());

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
                if (curDay > billDay) {
                    billMonth = curMonth + 1;
                    if (billMonth > 12) {
                        billMonth = 1;
                        billYear = curYear + 1;
                    }
                    billDate = Calendar.getInstance();
                    billDate.set(Calendar.YEAR, billYear);
                    billDate.set(Calendar.MONTH, billMonth);
                    billDate.set(Calendar.DAY_OF_MONTH, billDay);
                    daysBetween = 0;
                    today = Calendar.getInstance();
                    while (billDate.compareTo(today) < 0) {
                        billDate.add(Calendar.DAY_OF_MONTH, 1);
                        daysBetween++;
                    }
                    if (daysBetween > diffDays) {
                        diffDays = daysBetween;
                        bestCardName = cardName.toString();
                        bestCardNumber = cardNumber.toString();
                        bestBillDate = billDay;
                    }
                } else if (curDay < billDay) {
                    billMonth = curMonth;
                    billYear = curYear;
                    billDate = Calendar.getInstance();
                    billDate.set(Calendar.YEAR, billYear);
                    billDate.set(Calendar.MONTH, billMonth);
                    billDate.set(Calendar.DAY_OF_MONTH, billDay);
                    daysBetween = 0;
                    today = Calendar.getInstance();
                    while (today.compareTo(billDate) < 0) {
                        today.add(Calendar.DAY_OF_MONTH, 1);
                        daysBetween++;
                    }
                    if (daysBetween > diffDays) {
                        diffDays = daysBetween;
                        bestCardName = new String(cardName);
                        bestCardNumber = new String(cardNumber.toString());
                        bestBillDate = billDay;
                    }
                }
                cursor.moveToNext();
            }
            //Here, diffDays contains the max amount of days left to a billing date
            //bestCardName contains the best Card Name
            //bestCardNumber contains the best Card #
            //bestBillDate contains the "furthest" billing date
            Intent notificationIntent = new Intent(this, MainActivity.class);
            // use System.currentTimeMillis() to have a unique ID for the pending intent
            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), notificationIntent, 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_best_card)
                            .setContentTitle("Best card to use today: ")
                            .setContentText(bestCardName);
            mBuilder.setContentIntent(pIntent);
            // Sets an ID for the notification
            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
