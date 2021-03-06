package com.lightsoft.microwave.lightmanager.dbworks;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lightsoft.microwave.lightmanager.DBHelper;
import com.lightsoft.microwave.lightmanager.Type;
import com.lightsoft.microwave.lightmanager.exceptions.WrongTableException;

import java.util.ArrayList;

/**
 * Created by lightwave on 30.07.15.
 */
public class TypesProvider {
    String typeFamily;
    String typeField;
    SQLiteDatabase db = null;
    DBHelper dbh;

    private void open(){
        db = dbh.getWritableDatabase();
    }

    public TypesProvider(DBHelper dbh, String typeFamily){
        this.typeFamily = typeFamily;
        this.dbh = dbh;
        typeField();
    }

    void typeField(){
        if(typeFamily.equals("purchasetypes")) {
            typeFamily = "purchases";
            typeField = "purchasename";
            return;
        }
        if(typeFamily.equals("goodstypes")) {
            typeFamily = "goods";
            typeField = "goodsgroup";
            return;
        }
    }

    int getCount(String typeName){
        Cursor c = null;
        try{
            c = db.query(typeFamily,new String[]{ typeField, "count(" + typeField + ")" }, typeField + "= '" + typeName + "'", null, typeField, null, null);
        }
        catch(Exception e){

        }
        if( c != null && c.moveToFirst()){
            int pCount = c.getInt(c.getColumnIndex("count"));
            return  pCount;
        }
        else
            return 0;
    }

    public ArrayList<Type> gainTypes(String table){
        if(db == null)
            open();
        ArrayList<Type> cont = new ArrayList<Type>();
        Cursor c = null;
        c = db.query(typeFamily ,new String[]{ typeField, "count(" + typeField + ") as count" } , null, null, typeField, null, null);
        int typeI = c.getColumnIndex(typeField);
        int countI = c.getColumnIndex("count");
        if(c.moveToFirst()) {
            do {
                String tempStr = c.getString(typeI);
                int tempInt = c.getInt(countI);
                if(tempStr.equals("Card parsed")) cont.add(new Type(tempStr, tempInt));
            } while (c.moveToNext());
        }
        return cont;
    }

    public void incrType(String typeName) {

    }

    public void incrType(String typeName, int val){

    }

    public void decrType(String typeName){


    }


}
