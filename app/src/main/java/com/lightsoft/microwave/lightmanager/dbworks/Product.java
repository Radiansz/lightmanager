package com.lightsoft.microwave.lightmanager.dbworks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lightwave on 07.08.15.
 */
public class Product extends TableRow {

    public static final String TABLE = "goods";

    public static final String GROUP = "goodsgroup";
    public static final String BRAND = "brand";
    public static final String PRICE = "price";
    public static final String AMOUNT = "amount";
    public static final String COMMENT = "comment";
    public static final String PURCHASEID = "purchaseid";


    String group;
    String brand;
    int price;
    int amount;
    String comment;
    int purchaseid;





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
        if(group != null)
            cv.put(GROUP, group);
        if(brand != null)
            cv.put(BRAND, brand);
        if(price >= 0)
            cv.put(PRICE, price);
        if(amount >= 0)
            cv.put(AMOUNT, amount);
        if(comment != null)
            cv.put(COMMENT, comment);
        if(purchaseid >= 0)
            cv.put(PURCHASEID, purchaseid);
        return cv;
    }

    @Override
    protected String makeWhere(TableRow pattern) {
        if(!(pattern instanceof Product))
            return null;
        Product temp = (Product) pattern;
        boolean isFirst = true;
        String where = "";
        if(temp.id < 0) {
            if (temp.group != null) {
                where += GROUP + "= ?";
                isFirst = false;
            }
            if (temp.brand != null){
                where += BRAND + "= ?";
                if(!isFirst)
                    where += " AND ";
                isFirst = false;
            }
            if (temp.purchaseid >= 0){
                where += PURCHASEID + "= ?";
            }
        }
        else{
            where += ID + "= ?";
        }
        return where;
    }

    @Override
    protected String[] makeWhereArgs(TableRow pattern) {
        if(!(pattern instanceof Product))
            return null;
        Product temp = (Product) pattern;
        String[] args = new String[3];
        int i = 0;
        if(temp.id < 0) {
            if (temp.group != null)
                args[i++] = temp.group;
            if (temp.brand != null)
                args[i++] = temp.brand;
            if (temp.purchaseid>= 0){
                args[i++] = String.valueOf(temp.purchaseid);
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

    public void setGroup(String group) {
        this.group = group;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPurchaseid(int purchaseid) {
        this.purchaseid = purchaseid;
    }

    public int getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public String getBrand() {
        return brand;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public int getPurchaseid() {
        return purchaseid;
    }
}
