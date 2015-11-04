package com.lightsoft.microwave.lightmanager.dbworks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by lightwave on 09.08.15.
 */
public class Income extends TableRow {


    public static final String TABLE = "income";
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String COMMENT = "comment";
    public static final String ACCOUNTID = "accountid";

    int amount = -1;
    Date date = null;
    String comment = null;
    int accountId = -1;




    @Override
    public long updateMatches(SQLiteDatabase db, TableRow pattern) {
        if(!(pattern instanceof Account))
            return -1;
        return  db.update(TABLE, makeContentValues(), makeWhere(pattern), makeWhereArgs(pattern) );

    }

    @Override
    public long deleteMatches(SQLiteDatabase db) {
        return  db.delete(TABLE, makeWhere(this), makeWhereArgs(this));
    }

    @Override
    protected ContentValues makeContentValues() {
        ContentValues cv = new ContentValues();
        if(amount >= 0)
            cv.put(AMOUNT, amount);
        if(date != null)
            cv.put(DATE, date.getTime());
        if(comment != null)
            cv.put(COMMENT, comment);
        if(accountId >= 0)
            cv.put(ACCOUNTID, accountId);
        return cv;
    }

    @Override
    protected String makeWhere(TableRow pattern) {
        if(!(pattern instanceof Income))
            return null;
        Income temp = (Income) pattern;
        boolean isFirst = true;
        String where = "";
        if(temp.id < 0) {
            if (temp.amount >= 0) {
                where += AMOUNT + "= ?";
                isFirst = false;
            }
            if (temp.date != null){
                where += DATE + "= ?";
                if(!isFirst)
                    where += " AND ";
                isFirst = false;
            }
            if (temp.accountId >= 0){
                where += ACCOUNTID + "= ?";
            }
        }
        else{
            where += ID + "= ?";
        }
        return where;
    }

    @Override
    protected String[] makeWhereArgs(TableRow pattern) {
        return new String[0];
    }

    @Override
    protected String table() {
        return TABLE;
    }

}
