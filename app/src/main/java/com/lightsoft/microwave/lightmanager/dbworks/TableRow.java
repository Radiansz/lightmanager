package com.lightsoft.microwave.lightmanager.dbworks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lightwave on 06.08.15.
 */
public abstract class TableRow {

    public static final String ID = "_id";
    int id = -1;

    public long insertOrUpdate(SQLiteDatabase db) {
        if(id == -1)
            return insert(db);
        else
            return update(db);
    }

    public long insert(SQLiteDatabase db) {
        return  db.insert(table(), null, makeContentValues());
    }

    public long update(SQLiteDatabase db) {
        return db.update(table(), makeContentValues(), "_id = " + id, null);
    }
    public abstract long updateMatches(SQLiteDatabase db, TableRow pattern);
    public abstract long deleteMatches(SQLiteDatabase db);
    protected abstract ContentValues makeContentValues();
    protected abstract String makeWhere(TableRow pattern);
    protected abstract String[] makeWhereArgs(TableRow pattern);

    protected abstract String table();
}
