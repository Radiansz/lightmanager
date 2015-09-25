package com.lightsoft.microwave.lightmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;

import com.lightsoft.microwave.lightmanager.dbworks.TypesProvider;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;


public class NewPurchase extends Activity implements View.OnClickListener, TextView.OnEditorActionListener {

    DBHelper dbh;
    EditText nameEdit;
    EditText detailsEdit;
    EditText dateEdit;
    EditText priceEdit;
    ArrayList<Type> types;
    long id;
    boolean isEdit = false;
    String oldPName;
    boolean neFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_purchase);
        dbh = new DBHelper(this);
        dbh.getWritableDatabase();
        dbh.getWritableDatabase();
        nameEdit = (EditText) findViewById(R.id.purchaseNameEdit);
        detailsEdit = (EditText) findViewById(R.id.detailsEdit);
        dateEdit = (EditText) findViewById(R.id.dateEdit);
        priceEdit = (EditText) findViewById(R.id.totalPriceEdit);
        nameEdit.setOnEditorActionListener(this);
        types = dbh.gainTypes("purchasetypes");
        nameEdit.addTextChangedListener(new InputAssister(nameEdit, types));
        Intent intent = getIntent();
        String action = intent.getAction();
        if(action != null && action.equals("ACTION_LM_EDIT")){
            id = intent.getLongExtra("id", -1);
            isEdit = true;
            SQLiteDatabase db = dbh.getWritableDatabase();
            if( id!=-1 ) {
                Cursor c = db.query("purchases", new String[]{"_id", "purchasename", "date", "total", "comment"}, "_id = "+ id, null, null, null, null);
                c.moveToFirst();
                oldPName = c.getString(c.getColumnIndex("purchasename"));
                nameEdit.setText(oldPName);
                detailsEdit.setText(c.getString(c.getColumnIndex("comment")));
                priceEdit.setText(c.getString(c.getColumnIndex("total")));
                Date date = new Date(c.getLong(c.getColumnIndex("date")));
                GregorianCalendar calend = new GregorianCalendar();
                calend.setTime(date);
                dateEdit.setText(calend.get(GregorianCalendar.DAY_OF_MONTH)+ "/" + (calend.get(GregorianCalendar.MONTH) + 1) +"/" + calend.get(GregorianCalendar.YEAR) );
            }
        }
        else{
            GregorianCalendar calend = new GregorianCalendar();
            dateEdit.setText(calend.get(GregorianCalendar.DAY_OF_MONTH)+ "/" + (calend.get(GregorianCalendar.MONTH) + 1) + "/" + calend.get(GregorianCalendar.YEAR));
        }

    }


    public void onClick(View v)
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_purchase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.accept) {
            if(isEdit)
                editRow();
            else
                putToDB();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void putToDB(){

        String purchtype = nameEdit.getText().toString();
        ContentValues cv;
        TypesProvider tp = new TypesProvider(dbh, "purchasetypes");
        tp.incrType(purchtype);

        //dbh.incrType("purchasetypes", purchtype);

        String details = detailsEdit.getText().toString();
        int price = Integer.parseInt(priceEdit.getText().toString());
        String tempDate = dateEdit.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(tempDate);
        }
        catch (Exception e){
            date = new Date();
        }
        GregorianCalendar calendar = new GregorianCalendar(), calendar1 = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, calendar1.get(GregorianCalendar.HOUR_OF_DAY));
        calendar.set(GregorianCalendar.MINUTE, calendar1.get(GregorianCalendar.MINUTE));
        date = calendar.getTime();

        cv = new ContentValues();
        cv.put("purchasename", purchtype);
        cv.put("total", price);
        cv.put("place", "default");
        cv.put("date", date.getTime());
        cv.put("comment", details);
        cv.put("accountid", 0);

        dbh.addPurchase(cv);
    }


    void editRow(){
        String pType = nameEdit.getText().toString();
        int price = Integer.parseInt(priceEdit.getText().toString());
        String details = detailsEdit.getText().toString();
        String tempDate = dateEdit.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(tempDate);
        }
        catch (Exception e) {
            date = new Date();
        }
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(!pType.equals(oldPName)){
            Log.i("myinf", "Type has been changed");
            dbh.decrType("purchasetypes", oldPName);
            dbh.incrType("purchasetypes",pType);
        }
        cv.put("purchasename", pType);
        cv.put("comment", details);
        cv.put("date", date.getTime());
        cv.put("total", price);
        dbh.updatePurchase(id, cv);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
