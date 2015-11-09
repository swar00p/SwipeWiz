package com.swarooprao.androidapps.swipewiz;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        final TableLayout tblSavedCards = (TableLayout)findViewById(R.id.tblSavedCards);
        final Drawable cellShape = getApplicationContext().getResources().getDrawable(R.drawable.cell_shape, null);
        final int textColor = getApplicationContext().getResources().getColor(R.color.black, null);

        //final ArrayList<String> savedCardList = new ArrayList<String>();
        //final ArrayAdapter<String> savedCardListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, savedCardList);
        //lstSavedCardList.setAdapter(savedCardListAdapter);

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNumber = txtCardNumber.getText().toString();
                String billDate = txtBillDate.getText().toString();
                String cardName = txtCardName.getText().toString();

                dbHelper.addCard(cardName, cardNumber, Integer.parseInt(billDate));

                String listItem = cardName + " - " + cardNumber.substring(cardNumber.length()-4);

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

                //populateSavedCardsList();
                //row.setLayoutParams(lp);
                //savedCardList.add(listItem);
                //savedCardListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateSavedCardsList();
    }

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
}
