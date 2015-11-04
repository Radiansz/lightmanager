package com.lightsoft.microwave.lightmanager.dbworks;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lightwave on 09.08.15.
 */
public class Account extends TableRow {

    public static final String TABLE = "accounts";
    public static final String ACCOUNTTYPE = "accountType";
    public static final String NAME = "name";
    public static final String BALANCE = "balance";
    public static final String CURRENCY = "currency";
    public static final String COMMENT = "comment";

    String accountType = null;
    String name = null;
    int balance = -1;
    String currency = null;
    String comment = null;

    public Account(){

    }

    public Account(ContentValues cv){
        id = cv.getAsInteger(ID);
        accountType = cv.getAsString(ACCOUNTTYPE);
        name = cv.getAsString(NAME);
        currency = cv.getAsString(CURRENCY);
        balance = cv.getAsInteger(BALANCE);
    }

    public boolean fetch(SQLiteDatabase db, int id) {
        Cursor c = db.query(TABLE, new String[]{ID, ACCOUNTTYPE, NAME, BALANCE, CURRENCY, COMMENT}, ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if(c.moveToFirst()) {
            this.id = id;
            accountType = c.getString(c.getColumnIndex(ACCOUNTTYPE));
            name = c.getString(c.getColumnIndex(NAME));
            currency = c.getString(c.getColumnIndex(CURRENCY));
            comment = c.getString(c.getColumnIndex(COMMENT));
            balance = c.getInt(c.getColumnIndex(BALANCE));
            return true;
        }
        return false;
    }

    public void income(SQLiteDatabase db, int value) {
        balance += value;
        update(db);
    }

    public void outcome(SQLiteDatabase db,int value) {
        balance -= value;
        update(db);
    }

    @Override
    public long updateMatches(SQLiteDatabase db, TableRow pattern) {
        if(!(pattern instanceof Account))
            return -1;
        return  db.update(TABLE, makeContentValues(), makeWhere(pattern), makeWhereArgs(pattern));

    }

    @Override
    public long deleteMatches(SQLiteDatabase db) {
        return db.delete(TABLE, makeWhere(this), makeWhereArgs(this));
    }

    @Override
    protected ContentValues makeContentValues() {
        ContentValues cv = new ContentValues();
        if(accountType != null)
            cv.put(ACCOUNTTYPE, accountType);
        if(name != null)
            cv.put(NAME, name);
        if(balance >= 0)
            cv.put(BALANCE, balance);
        if(currency != null)
            cv.put(CURRENCY, currency);
        if(comment != null)
            cv.put(COMMENT, comment);
        return cv;
    }

    @Override
    protected String makeWhere(TableRow pattern) {
        if(!(pattern instanceof Account))
            return null;
        Account temp = (Account) pattern;
        boolean isFirst = true;
        String where = "";
        if(temp.id < 0) {
            if (temp.accountType != null) {
                where += ACCOUNTTYPE + "= ?";
                isFirst = false;
            }
            if (temp.name != null){
                where += NAME + "= ?";
                if(!isFirst)
                    where += " AND ";
                isFirst = false;
            }
            if (temp.balance >= 0){
                where += BALANCE + "= ?";
            }
        }
        else{
            where += ID + "= ?";
        }
        return where;
    }

    @Override
    protected String[] makeWhereArgs(TableRow pattern) {
        if(!(pattern instanceof Account))
            return null;
        Account temp = (Account) pattern;
        String[] args = new String[3];
        int i = 0;
        if(temp.id < 0) {
            if (temp.accountType != null)
                args[i++] = temp.accountType;
            if (temp.name != null)
                args[i++] = temp.name;
            if (temp.balance >= 0){
                args[i++] = String.valueOf(temp.balance);
            }
        }
        else
            args[0] = String.valueOf(id);
        return args;
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

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getAccountType() {
        return accountType;
    }

    public int getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public String getComment() {
        return comment;
    }
}
