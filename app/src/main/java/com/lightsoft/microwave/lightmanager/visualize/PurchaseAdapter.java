package com.lightsoft.microwave.lightmanager.visualize;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by lightwave on 21.09.15.
 */
public class PurchaseAdapter extends BaseAdapter {
    Context cnt;
    Cursor cursor;
    LayoutInflater inflater;
    int width;

    PurchaseAdapter(Context cnt, Cursor cur, int width){
        this.cnt = cnt;
        this.cursor = cur;
        this.width = width;
    }

    @Override
    public int getCount() {
        cnt.get
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
