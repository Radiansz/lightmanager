package com.lightsoft.microwave.lightmanager.dbworks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lightwave on 06.08.15.
 */
public abstract class TableRow {

    public abstract long insertInDB(SQLiteDatabase db);
    public abstract long updateMatches(SQLiteDatabase db, TableRow pattern);
    public abstract long deleteMatches(SQLiteDatabase db);
    protected abstract ContentValues makeContentValues();
    protected abstract String makeWhere(TableRow pattern);
    protected abstract String[] makeWhereArgs(TableRow pattern);
}
