package com.lightsoft.microwave.lightmanager.dbworks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lightwave on 10.09.15.
 */
public class TypeReplaceRule extends  TableRow {

    public static final String TABLE = "type_replace_rules";
    public static final String TYPE = "type";
    public static final String BRAND_RAW = "brand_raw";
    public static final String BRAND = "brand";
    public static final String FLAGS = "flags";

    int id = -1;
    String type = null;
    String brandRaw = null;
    String brand = null;
    int flags = -1; // TODO: Заменить на UNION с именноваными значениями





    @Override
    public long updateMatches(SQLiteDatabase db, TableRow pattern) {
        if(!(pattern instanceof Purchase))
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
        if(type != null)
            cv.put(TYPE, type);
        if(brandRaw != null)
            cv.put(BRAND_RAW, brandRaw);
        if(brand != null)
            cv.put(BRAND, brand);
        if(flags != -1)
            cv.put(FLAGS, flags);
        return cv;
    }

    @Override
    protected String makeWhere(TableRow pattern) {
        if(!(pattern instanceof TypeReplaceRule))
            return null;
        TypeReplaceRule temp = (TypeReplaceRule) pattern;
        boolean isFirst = true;
        String where = "";
        if(temp.id < 0){
            if(temp.type != null){
                where += TYPE + "= ?";
                isFirst = false;
            }
            if(temp.brandRaw != null){
                where += BRAND_RAW + "= ?";
                if(!isFirst) where += " AND ";
                isFirst = false;
            }
            if(temp.brand != null){
                where += BRAND + "= ?";
                if(!isFirst) where += " AND ";
                isFirst = false;
            }
            if(temp.flags >= 0){
                where += FLAGS+ "= ?";
            }
        }
        else{
            where += ID + "= ?";
        }
        return where;
    }


    @Override
    protected String[] makeWhereArgs(TableRow pattern) {
        if(!(pattern instanceof TypeReplaceRule))
            return null;
        TypeReplaceRule temp = (TypeReplaceRule) pattern;
        boolean isFirst = true;
        String[] args = new String[4];
        int i = 0;
        if(temp.id < 0){
            if(temp.type != null){
                args[i++] = temp.type;
            }
            if(temp.brandRaw != null)
                args[i++] = temp.brandRaw;
            if(temp.brand != null)
                args[i++] = temp.brand;
            if(temp.flags >= 0)
                args[i++] = String.valueOf(temp.type);
        }
        else
            args[i++] = String.valueOf(id);
        String[] result = new String[i];
        for(int j = 0; j<i; j++) result[j] = args[j];
        return result;
    }

    @Override
    protected String table() {
        return TABLE;
    }

    /**
     *
     * Getters and setters
     *
     */

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getBrandRaw() {
        return brandRaw;
    }

    public String getBrand() {
        return brand;
    }

    public int getFlags() {
        return flags;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBrandRaw(String brandRaw) {
        this.brandRaw = brandRaw;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }
}
