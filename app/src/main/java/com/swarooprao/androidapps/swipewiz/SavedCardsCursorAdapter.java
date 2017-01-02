package com.swarooprao.androidapps.swipewiz;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by swarooprao on 11/9/15.
 */
public class SavedCardsCursorAdapter extends CursorAdapter {

    private int[] colors = new int[] { 0xAA0077BE, 0xAA0038A8 };

    public SavedCardsCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.savedcard_row, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int bgColor;
        int position = cursor.getPosition();

        if (position%2 == 0)
            bgColor = colors[0];
        else
            bgColor = colors[1];

        // Find fields to populate in inflated template
        TextView tvCardName = (TextView) view.findViewById(R.id.txtRowCardName);
        TextView tvBillDate = (TextView) view.findViewById(R.id.txtRowBillDate);
        // Extract properties from cursor
        String cardName = cursor.getString(cursor.getColumnIndexOrThrow("cardname"));
        String cardNumber = cursor.getString(cursor.getColumnIndexOrThrow("cardnumber"));
        int billDate = cursor.getInt(cursor.getColumnIndexOrThrow("billdate"));
        // Populate fields with extracted properties
        String listItem = cardName + " - " + cardNumber.substring(cardNumber.length()-4);
        tvCardName.setText(listItem);
        tvBillDate.setText(String.valueOf(billDate));
        tvCardName.setBackgroundColor(bgColor);
        tvBillDate.setBackgroundColor(bgColor);
    }
}
