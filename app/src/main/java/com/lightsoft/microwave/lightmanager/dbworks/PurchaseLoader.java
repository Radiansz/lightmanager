package com.lightsoft.microwave.lightmanager.dbworks;

import android.content.Context;
import android.database.Cursor;
import android.content.AsyncTaskLoader;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lightsoft.microwave.lightmanager.DBHelper;

import java.util.Date;

/**
 * Created by lightwave on 30.07.15.
 */
public class PurchaseLoader extends  AsyncTaskLoader<Cursor> {

    Date minDate;
    Date maxDate;
    DBHelper dbh;

    public PurchaseLoader(Context context, Date minDate, Date maxDate){
        super(context);
        this.minDate = minDate;
        this.maxDate = maxDate;
        dbh = new DBHelper(getContext());
    }

    @Override
    protected void onStartLoading(){
        Log.v("myinfo", "PurchaseLoader.onStartLoading");
        forceLoad();
    }


    @Override
    public Cursor loadInBackground() {
        Log.i("myinfo", "PurchaseLoader.loadInBackground");
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor c = db.query("purchases", new String[]{"_id","purchasename", "place", "date", "total"}, "date > " + minDate.getTime() + " AND date <=" + maxDate.getTime(), null, null, null, "date DESC");
        return c;
    }
}
