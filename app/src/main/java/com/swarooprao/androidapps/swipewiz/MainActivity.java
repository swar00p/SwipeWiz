package com.swarooprao.androidapps.swipewiz;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    CardDBHelper dbHelper;
    boolean reloadSavedCards = false;
    private PendingIntent pendingIntent;
    String externalFontPath;
    Typeface fontLoaderTypeface;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Logger logger = Logger.getLogger("SwipeWiz");
        dbHelper = new CardDBHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        TextView textViewNewFont = new TextView(MainActivity.this);
        WindowManager.LayoutParams layoutparams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        textViewNewFont.setLayoutParams(layoutparams);
        textViewNewFont.setText("SwipeWiz");
        textViewNewFont.setTextColor(Color.rgb(132, 11, 30));
        textViewNewFont.setBackgroundColor(Color.rgb(8, 146, 208));
        textViewNewFont.setGravity(Gravity.LEFT);
        textViewNewFont.setGravity(Gravity.CENTER_VERTICAL);
        textViewNewFont.setPadding(0,0,0,0);
        textViewNewFont.setTextSize(20);
        textViewNewFont.setTypeface(typeface);

        // Assests folder font folder path
        //externalFontPath = "ExternalFonts/chopinscript.ttf";

        // Load Typeface font url String externalFontPath
       //fontLoaderTypeface = Typeface.createFromAsset(getAssets(), externalFontPath);

        //actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textViewNewFont);

        Intent serviceIntent = new Intent(this, AlarmCreationService.class);
        startService(serviceIntent);

        Button btnAddCard = (Button)findViewById(R.id.btnSaveCard);
        //final ListView lstSavedCardList = (ListView)findViewById(R.id.lstSavedCards);
        final EditText txtCardNumber = (EditText)findViewById(R.id.txtCardNum);
        final EditText txtBillDate = (EditText)findViewById(R.id.txtBillDate);
        final EditText txtCardName = (EditText)findViewById(R.id.txtCardName);
        //final TableLayout tblSavedCards = (TableLayout)findViewById(R.id.tblSavedCards);
        //final Drawable cellShape = getApplicationContext().getResources().getDrawable(R.drawable.cell_shape, null);
        //final int textColor = getApplicationContext().getResources().getColor(R.color.black, null);

        // Find ListView to populate
        ListView lvSavedCards = (ListView) findViewById(R.id.lstSavedCards);
        lvSavedCards.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            // Yes button clicked
                            //Toast.makeText(MainActivity.this, "Yes Clicked",
                                    //Toast.LENGTH_LONG).show();

                            dbHelper.deleteCard(id);
                            ListView lvSavedCards = (ListView) findViewById(R.id.lstSavedCards);
                            Cursor cursor = dbHelper.getCards();
                            // Setup cursor adapter using cursor from last step
                            SavedCardsCursorAdapter savedCardsAdapter = new SavedCardsCursorAdapter(getApplicationContext(), cursor, 0);
                            // Attach cursor adapter to the ListView
                            lvSavedCards.setAdapter(savedCardsAdapter);
                            // Switch to new cursor and update contents of ListView
                            savedCardsAdapter.changeCursor(cursor);
                            updateNotification ();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked
                            // do nothing
                            //Toast.makeText(MainActivity.this, "No Clicked",
                                    //Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Delete this card?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

                return true;
            }
        });
        Cursor cursor = dbHelper.getCards();
        // Setup cursor adapter using cursor from last step
        SavedCardsCursorAdapter savedCardsAdapter = new SavedCardsCursorAdapter(this, cursor, 0);
        // Attach cursor adapter to the ListView
        lvSavedCards.setAdapter(savedCardsAdapter);

        //final ArrayList<String> savedCardList = new ArrayList<String>();
        //final ArrayAdapter<String> savedCardListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, savedCardList);
        //lstSavedCardList.setAdapter(savedCardListAdapter);

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String cardNumber = txtCardNumber.getText().toString();
                txtCardNumber.setText("");
                final String billDate = txtBillDate.getText().toString();
                txtBillDate.setText("");
                final String cardName = txtCardName.getText().toString();
                txtCardName.setText("");

                if (cardNumber.trim().isEmpty() || billDate.trim().isEmpty() || cardName.trim().isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please provide details of card to be saved");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    //continueWithNextStep(v, cardName, cardNumber, billDate);
                                }
                            });

                    alertDialog.show();
                } else {
                    continueWithNextStep(v, cardName, cardNumber, billDate);
                }

                /*
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                long cardId = dbHelper.addCard(cardName, cardNumber, Integer.parseInt(billDate));

                //String listItem = cardName + " - " + cardNumber.substring(cardNumber.length()-4);

                // Find ListView to populate
                ListView lvSavedCards = (ListView) findViewById(R.id.lstSavedCards);
                Cursor cursor = dbHelper.getCards();
                // Setup cursor adapter using cursor from last step
                SavedCardsCursorAdapter savedCardsAdapter = new SavedCardsCursorAdapter(getApplicationContext(), cursor, 0);
                // Attach cursor adapter to the ListView
                lvSavedCards.setAdapter(savedCardsAdapter);
                // Switch to new cursor and update contents of ListView
                savedCardsAdapter.changeCursor(cursor);

                updateNotification();
                */
            }
        });
    }

    public void continueWithNextStep(View v, String cardName, String cardNumber, String billDate) {

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        long cardId = dbHelper.addCard(cardName, cardNumber, Integer.parseInt(billDate));

        //String listItem = cardName + " - " + cardNumber.substring(cardNumber.length()-4);

        // Find ListView to populate
        ListView lvSavedCards = (ListView) findViewById(R.id.lstSavedCards);
        Cursor cursor = dbHelper.getCards();
        // Setup cursor adapter using cursor from last step
        SavedCardsCursorAdapter savedCardsAdapter = new SavedCardsCursorAdapter(getApplicationContext(), cursor, 0);
        // Attach cursor adapter to the ListView
        lvSavedCards.setAdapter(savedCardsAdapter);
        // Switch to new cursor and update contents of ListView
        savedCardsAdapter.changeCursor(cursor);

        updateNotification();
    }

    private void updateNotification() {
        Context context = MainActivity.this.getApplicationContext();
        //Intent service = new Intent(context, ShowNotificationIntentService.class);
        //context.startService(service);

        Intent updateNotificationIntent = new Intent(context, AlarmBroadcastReceiver.class);
        context.sendBroadcast(updateNotificationIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //final TableLayout tblSavedCards = (TableLayout)findViewById(R.id.tblSavedCards);
        //if (tblSavedCards.getChildCount() <= 1) {
        if (reloadSavedCards) {
            //populateSavedCardsList();
            // Find ListView to populate
            ListView lvSavedCards = (ListView) findViewById(R.id.lstSavedCards);
            Cursor cursor = dbHelper.getCards();
            // Setup cursor adapter using cursor from last step
            SavedCardsCursorAdapter savedCardsAdapter = new SavedCardsCursorAdapter(getApplicationContext(), cursor, 0);
            // Attach cursor adapter to the ListView
            lvSavedCards.setAdapter(savedCardsAdapter);
            // Switch to new cursor and update contents of ListView
            savedCardsAdapter.changeCursor(cursor);

            //reloadSavedCards = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        reloadSavedCards = true;
    }

    /*
    @TargetApi(Build.VERSION_CODES.M)
    private void populateSavedCardsList () {
        String cardName, cardNumber;
        int billingDate;
        final EditText txtCardNumber = (EditText)findViewById(R.id.txtCardNum);
        final EditText txtBillDate = (EditText)findViewById(R.id.txtBillDate);
        final EditText txtCardName = (EditText)findViewById(R.id.txtCardName);
        final TableLayout tblSavedCards = (TableLayout)findViewById(R.id.tblSavedCards);
        final Drawable cellShape = getApplicationContext().getResources().getDrawable(R.drawable.cell_shape, null);
        final int textColor = getApplicationContext().getResources().getColor(R.color.black, null);
        Cursor cursor = dbHelper.getCards();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cardName = cursor.getString(1);
                cardNumber = cursor.getString(2);
                billingDate = cursor.getInt(3);
                String listItem = cardName + " - " + cardNumber.substring(cardNumber.length()-4);

                TableRow row= new TableRow(getApplicationContext());
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);

                TextView col1 = new TextView(getApplicationContext());
                col1.setBackground(cellShape);
                col1.setTextColor(textColor);
                col1.setText(listItem);
                col1.setLayoutParams(lp);

                TextView col2 = new TextView(getApplicationContext());
                col2.setBackground(cellShape);
                col2.setTextColor(textColor);
                col2.setText(String.valueOf(billingDate));
                col2.setLayoutParams(lp);

                row.addView(col1, 0);
                row.addView(col2, 1);

                tblSavedCards.addView(row);
                cursor.moveToNext();
            }
            cursor.close();
        }
    }
    */
}
