package com.lightsoft.microwave.lightmanager.visualize;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lightsoft.microwave.lightmanager.R;
import com.lightsoft.microwave.lightmanager.dbworks.Purchase;

/**
 * Created by lightwave on 21.09.15.
 */
public class PurchaseAdapter extends BaseAdapter {
    Context cnt;
    Cursor cursor;
    LayoutInflater inflater;
    int width;

    public PurchaseAdapter(Context cnt, Cursor cur, int width){
        this.cnt = cnt;
        this.cursor = cur;
        this.width = width;
        inflater = (LayoutInflater)cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();

    }

    @Override
    public Object getItem(int position) {

        return null;
    }


    @Override
    public long getItemId(int position) {
        cursor.move(position);
        return cursor.getInt(cursor.getColumnIndex(Purchase.ID));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(cursor == null)
            return null;
        if(!cursor.move(position))
            return null;
        View view = convertView;
        if(view == null)
            view = inflater.inflate(R.layout.static_list_elem, parent, false);

        ((TextView) view.findViewById(R.id.dataPlace1)).setText(cursor.getString(cursor.getColumnIndex(Purchase.PURCHASENAME)));
        ((TextView) view.findViewById(R.id.dataPlace2)).setText(cursor.getString(cursor.getColumnIndex(Purchase.PLACE)));
        ((TextView) view.findViewById(R.id.dataPlace3)).setText(cursor.getString(cursor.getColumnIndex(Purchase.TOTAL)));
        return view;

    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}
