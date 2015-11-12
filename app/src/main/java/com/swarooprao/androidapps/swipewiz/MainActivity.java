package com.swarooprao.androidapps.swipewiz;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CardDBHelper dbHelper;
    boolean reloadSavedCards = false;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new CardDBHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddCard = (Button)findViewById(R.id.btnSaveCard);
        //final ListView lstSavedCardList = (ListView)findViewById(R.id.lstSavedCards);
        final EditText txtCardNumber = (EditText)findViewById(R.id.txtCardNum);
        final EditText txtBillDate = (EditText)findViewById(R.id.txtBillDate);
        final EditText txtCardName = (EditText)findViewById(R.id.txtCardName);
        //final TableLayout tblSavedCards = (TableLayout)findViewById(R.id.tblSavedCards);
        //final Drawable cellShape = getApplicationContext().getResources().getDrawable(R.drawable.cell_shape, null);
        final int textColor = getApplicationContext().getResources().getColor(R.color.black, null);

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

            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
            public void onClick(View v) {
                String cardNumber = txtCardNumber.getText().toString();
                txtCardNumber.setText("");
                String billDate = txtBillDate.getText().toString();
                txtBillDate.setText("");
                String cardName = txtCardName.getText().toString();
                txtCardName.setText("");

                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
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
                /*
                TableRow row= new TableRow(getApplicationContext());
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);

                TextView col1 = new TextView(getApplicationContext());
                col1.setBackground(cellShape);
                col1.setTextColor(textColor);
                col1.setText(listItem);
                col1.setLayoutParams(lp);

                TextView col2 = new TextView(getApplicationContext());
                col2.setBackground(cellShape);
                col2.setTextColor(textColor);
                col2.setText(String.valueOf(billDate));
                col2.setLayoutParams(lp);

                row.addView(col1, 0);
                row.addView(col2, 1);

                tblSavedCards.addView(row);
                */

                //populateSavedCardsList();
                //row.setLayoutParams(lp);
                //savedCardList.add(listItem);
                //savedCardListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateNotification() {
        Context context = MainActivity.this.getApplicationContext();
        Intent service = new Intent(context, ShowNotificationIntentService.class);
        context.startService(service);
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
