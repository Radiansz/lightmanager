package com.lightsoft.microwave.lightmanager.dbworks;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by lightwave on 07.08.15.
 */
public class Purchase extends TableRow {

    public static final String TABLE = "purchases";

    public static final String PURCHASENAME = "purchasename";
    public static final String PLACE = "place";
    public static final String TOTAL = "total";
    public static final String CURRENCY = "currency";
    public static final String DATE = "date";
    public static final String COMMENT = "comment";
    public static final String ACCOUNTID = "accountid";


    String purchasename = null;
    String place = null;
    int total = -1;
    String currency = null;
    Date date = null;
    String comment = null;
    int accountid = -1;


    public void fetch(SQLiteDatabase db, int id) {
        Cursor c = db.query(TABLE, new String[]{ID, PURCHASENAME, PLACE, TOTAL, CURRENCY, DATE, COMMENT, ACCOUNTID}, ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if(c.moveToFirst()) {
            this.id = id;
            purchasename = c.getString(c.getColumnIndex(PURCHASENAME));
            place = c.getString(c.getColumnIndex(PLACE));
            total = c.getInt(c.getColumnIndex(TOTAL));
            comment = c.getString(c.getColumnIndex(COMMENT));
            date = new Date(c.getLong(c.getColumnIndex(DATE)));
            accountid = c.getInt(c.getColumnIndex(ACCOUNTID));
        }
    }


    @Override
    public long updateMatches(SQLiteDatabase db, TableRow pattern) {
        if(!(pattern instanceof Purchase))
            return -1;
        return  db.update(TABLE, makeContentValues(), makeWhere(pattern), makeWhereArgs(pattern) );

    }

    @Override
    public long deleteMatches(SQLiteDatabase db) {
        return db.delete(TABLE, makeWhere(this), makeWhereArgs(this));
    }

    @Override
    protected  ContentValues makeContentValues() {
        ContentValues cv = new ContentValues();
        if(purchasename != null)
            cv.put(PURCHASENAME, purchasename);
        if(place != null)
            cv.put(PLACE, place);
        if(total >= 0)
            cv.put(TOTAL, total);
        if(currency != null)
            cv.put(CURRENCY, currency);
        if(date != null)
            cv.put(DATE, date.getTime());
        if(comment != null)
            cv.put(COMMENT, comment);
        if(accountid >= 0)
            cv.put(ACCOUNTID, accountid);
        return cv;
    }

    @Override
    protected String makeWhere(TableRow pattern) {
        if(!(pattern instanceof Purchase))
            return null;
        Purchase temp = (Purchase) pattern;
        boolean isFirst = true;
        String where = "";
        if(temp.id < 0) {
            if (temp.purchasename != null) {
                where += PURCHASENAME + "= ?";
                isFirst = false;
            }
            if (temp.place != null){
                where += PLACE + "= ?";
                if(!isFirst)
                    where += " AND ";
                isFirst = false;
            }
            if (temp.accountid >= 0){
                where += ACCOUNTID + "= ?";
            }
        }
        else{
            where += ID + "= ?";
        }
        return  where;
    }

    @Override
    protected  String[] makeWhereArgs(TableRow pattern) {
        if(!(pattern instanceof Purchase))
            return null;
        Purchase temp = (Purchase) pattern;
        String[] args = new String[3];
        int i = 0;
        if(temp.id < 0) {
            if (temp.purchasename != null)
                args[i++] = temp.purchasename;
            if (temp.place != null)
                args[i++] = temp.place;
            if (temp.accountid >= 0){
                args[i++] = String.valueOf(temp.accountid);
            }
        }
        else
            args[i++] = String.valueOf(id);
        String[] retVal = new String[i];
        for(int j =0; j<i; j++)
            retVal[j] = args[j];
        return retVal;
    }

    @Override
    protected String table() {
        return TABLE;
    }




//***************************************************************
//            Getters and setters
//***************************************************************
    public void setId(int id) {
        this.id = id;
    }

    public void setPurchasename(String purchasename) {
        this.purchasename = purchasename;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public int getId() {
        return id;
    }

    public String getPurchasename() {
        return purchasename;
    }

    public String getPlace() {
        return place;
    }

    public int getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public int getAccountid() {
        return accountid;
    }
}
