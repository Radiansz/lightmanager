package com.lightsoft.microwave.lightmanager;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Microwave on 09.02.2015.
 */
public class DBHelper  extends SQLiteOpenHelper {

    SQLiteDatabase db = null;

    public DBHelper(Context c){
        super(c, "lightPurchDB", null, c.getResources().getInteger(R.integer.dbversion));
    }

    @Override
    public SQLiteDatabase getWritableDatabase(){
        if(db == null)
            db = super.getWritableDatabase();
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i("myinfo", "Database is creating");
        try {
            db.execSQL("CREATE TABLE purchasetypes ("
                    + "purchasetype TEXT PRIMARY KEY,"
                    + "count INTEGER"
                    + ");");
            db.execSQL("CREATE TABLE goodstypes ("
                    + "goodstype TEXT PRIMARY KEY,"
                    + "count INTEGER"
                    + ");");
            db.execSQL("CREATE TABLE purchases ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "purchasename TEXT,"
                    + "place TEXT,"
                    + "total INTEGER,"
                    + "currency TEXT(3),"
                    + "date INT8,"
                    + "comment TEXT,"
                    + "accountid INTEGER,"
                    + "FOREIGN KEY(purchasename) REFERENCES purchasetypes(purchasetype),"
                    + "FOREIGN KEY(accountid) REFERENCES accounts(_id)"
                    + ");");

            db.execSQL("CREATE TABLE goods ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "goodsgroup TEXT,"
                    + "brand TEXT,"
                    + "price INTEGER,"
                    + "amount INTEGER,"
                    + "comment TEXT,"
                    + "purchaseid INTEGER,"
                    + "FOREIGN KEY(purchaseid) REFERENCES purchases(_id) ON UPDATE CASCADE,"
                    + "FOREIGN KEY(goodsgroup) REFERENCES goodstypes(goodstype) ON UPDATE CASCADE"
                    + ");");
            db.execSQL("CREATE TABLE accounts ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "accountType TEXT,"
                    + "name TEXT,"
                    + "balance INTEGER,"
                    + "currency TEXT(3),"
                    + "comment TEXT"
                    + ");");
            db.execSQL("CREATE TABLE income ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "amount INTEGER,"
                    + "date INT8,"
                    + "comment TEXT,"
                    + "accountid INTEGER,"
                    + "FOREIGN KEY(accountid) REFERENCES accounts(_id) ON UPDATE CASCADE"
                    + ");");
            db.execSQL("CREATE TABLE type_replace_rules ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "type TEXT,"
                    + "brand_raw TEXT,"
                    + "brand TEXT,"
                    + "flags INTEGER"
                    + ");");
        }
        catch (Exception e){
            Log.e("myinfo",e.getMessage());
            throw e;
        }
        ContentValues cv = new ContentValues();
        cv.put("accountType", "cash");
        cv.put("balance", 0);
        cv.put("currency", "rub");
        cv.put("comment", "");
        db.insert("accounts", null, cv);
        Date d = new Date();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVers, int newVers){ // TODO: Переписать адекватно
        if(newVers == 2 && oldVers == 1){
            db.execSQL("ALTER TABLE accounts ADD name TEXT AFTER accountType");
        }
        if(newVers == 3 && oldVers == 1 ){
            db.execSQL("ALTER TABLE accounts ADD name TEXT AFTER accountType");
            db.execSQL("CREATE TABLE type_replace_rules ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "type TEXT,"
                    + "brand_raw TEXT,"
                    + "brand TEXT,"
                    + "flags INTEGER"
                    + ");");
        }
        if(newVers == 3 && oldVers == 2 ){
            db.execSQL("CREATE TABLE type_replace_rules ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "type TEXT,"
                    + "brand_raw TEXT,"
                    + "brand TEXT,"
                    + "flags INTEGER"
                    + ");");
        }
    }

    void open(){
        db = this.getWritableDatabase();
    }

// ------------------------------------- Work with purchases -----------------------------------------------

    public int sumPurchase(long first, long last){
        if(db == null)
            open();

        Cursor c = db.query("purchases", new String[]{"date", "sum(total) as costssum"},"date > " + first + " AND date <=" + last , null, null, null, null);
        c.moveToFirst();
        int result = c.getInt(c.getColumnIndex("costssum"));
        return  result;
    }

    public void addPurchase(ContentValues cv){
        if(db == null)
            open();

        db.insert("purchases", null, cv);
    }

    public void deletePurchase(long id){
        if(db == null)
            open();
        db.delete("purchases", "_id = " + id, null);
    }

    public void updatePurchase(long id, ContentValues cv){
        if(db == null)
            open();
        db.update("purchases", cv, "_id = " + id, null);
    }

    public String getPurchaseName(long id){
        if(db == null)
            open();
        Cursor c = db.query("purchases", new String[]{"_id", "purchasename"}, "_id = " + id, null, null, null, null);
        c.moveToFirst();
        String pName = c.getString(c.getColumnIndex("purchasename"));
        return pName;
    }

    public Cursor getPurchases(){
        return getPurchases(0, new Date().getTime());
    }

    public Cursor getPurchases(long beginDate, long endDate){
        if(db == null)
            open();
        Cursor c = db.query("purchases", new String[]{"_id","purchasename", "date", "total"}, "date > " + beginDate + " AND date <=" + endDate, null, null, null, "date DESC");
        return c;
    }



// ------------------------------- Work with types -------------------------------------------
// To TypesProv
    String typeField(String table){
        String typeField = null;
        if(table.equals("purchasetypes"))
            typeField = "purchasetype";
        if(table.equals("goodstypes"))
            typeField = "goodstype";
        return typeField;
    }
    // To TypesProv
    public ArrayList<Type> gainTypes(String table){
        if(db == null)
            open();
        String typeField = typeField(table);
        if(typeField == null){
            Log.e("myinf","Wrong table name");
            return null;
        }
        ArrayList<Type> cont = new ArrayList<Type>();
        Cursor c = null;
        c = db.query(table ,new String[]{ typeField, "count" }, null, null, null, null, null);
        int typeI = c.getColumnIndex(typeField);
        int countI = c.getColumnIndex("count");
        if(c.moveToFirst()) {
            do {
                String tempStr = c.getString(typeI);
                int tempInt = c.getInt(countI);
                cont.add(new Type(tempStr, tempInt));
            } while (c.moveToNext());
        }
        return cont;
    }
    // To TypesProv
    public void incrType(String table, String typeName){
        if(db == null)
            open();
        String typeField = typeField(table);
        if(typeField == null){
            Log.e("myinf","Wrong table name");
            return;
        }
        Cursor c = null;
        ContentValues cv = new ContentValues();
        try{
            c = db.query(table,new String[]{ typeField, "count" }, typeField + "= '" + typeName + "'", null, null, null, null);
        }
        catch(Exception e){

        }

        if( c != null && c.moveToFirst()){
            c.moveToFirst();
            cv.put(typeField, typeName);
            int pCount = c.getInt(c.getColumnIndex("count"));
            cv.put("count", pCount++);
            db.update(table, cv, typeField+"= '" + typeName + "'", null);
        }
        else{
            cv.put(typeField, typeName);
            cv.put("count", 1);
            db.insert(table, null, cv);
        }
    }
    // To TypesProv
    public void decrType(String table, String typeName){
        if(db == null)
            open();
        String typeField = typeField(table);
        if(typeField == null){
            Log.e("myinf","Wrong table name");
            return;
        }
        Cursor c = null;
        ContentValues cv = new ContentValues();
        try{
            c = db.query(table,new String[]{ typeField, "count" }, typeField + "= '" + typeName + "'", null, null, null, null);
        }
        catch(Exception e){

        }
        if( c != null && c.moveToFirst()) {
            c.moveToFirst();
            cv.put(typeField, typeName);
            int pCount = c.getInt(c.getColumnIndex("count"));
            pCount = pCount <= 0 ? 0 : pCount - 1;
            cv.put("count", pCount);
            db.update(table, cv, typeField +"= '" + typeName + "'", null);
        }
    }

}
