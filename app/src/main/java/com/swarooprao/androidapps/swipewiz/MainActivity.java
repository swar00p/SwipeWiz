package com.swarooprao.androidapps.swipewiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddCard = (Button)findViewById(R.id.btnSaveCard);
        final ListView lstSavedCardList = (ListView)findViewById(R.id.lstSavedCards);
        final EditText txtCardNumber = (EditText)findViewById(R.id.txtCardNum);
        final EditText txtBillDate = (EditText)findViewById(R.id.txtBillDate);
        final ArrayList<String> savedCardList = new ArrayList<String>();
        final ArrayAdapter<String> savedCardListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, savedCardList);
        lstSavedCardList.setAdapter(savedCardListAdapter);

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNumber = txtCardNumber.getText().toString();
                String billDate = txtBillDate.getText().toString();
                String listItem = cardNumber + " | " + billDate;
                savedCardList.add(listItem);
                savedCardListAdapter.notifyDataSetChanged();
            }
        });

        lstSavedCardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                Toast.makeText(MainActivity.this, (String)a.getItemAtPosition(position), Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}
