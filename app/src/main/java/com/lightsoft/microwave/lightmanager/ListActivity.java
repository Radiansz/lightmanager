package com.lightsoft.microwave.lightmanager;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;
import android.util.Log;

import com.lightsoft.microwave.lightmanager.dbworks.PurchaseLoader;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


import java.util.zip.Inflater;


public class ListActivity extends Activity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    DBHelper dbh;
    ListView lv;
    TextView costsSumV;
    CursorAdapter adapter;

    private static final int DELETE_ELEM = 0;
    private static final int EDIT_ELEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        dbh = new DBHelper(this);
        lv = (ListView) findViewById(R.id.purchList);
        costsSumV = (TextView) findViewById(R.id.costs_sum_v);
        lv.setCacheColorHint(Color.GREEN);
        lv.setOnItemClickListener(this);
        registerForContextMenu(lv);
        String []from = new String[]{"purchasename", "total", "place"};
        int []to = {R.id.pe_main, R.id.pe_price, R.id.pe_extra};
        adapter = new SimpleCursorAdapter(this, R.layout.purchase_list_elem, null,from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(adapter);
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("myinf" , "" + id);
    }



    @Override
    protected  void onResume(){
        super.onResume();
        refreshList();
        refreshCostsSum();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,EDIT_ELEM,0,R.string.edit);
        menu.add(0, DELETE_ELEM, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterContextMenuInfo ac = (AdapterContextMenuInfo)item.getMenuInfo();
        switch(item.getItemId()){
            case EDIT_ELEM:
                Intent intent = new Intent("ACTION_LM_EDIT", null, this, NewPurchase.class);
                intent.putExtra("id", ac.id);
                startActivity(intent);
                break;
            case DELETE_ELEM:

                String pName = dbh.getPurchaseName(ac.id);

                dbh.decrType("purchasetypes", pName);
                dbh.deletePurchase(ac.id);

                refreshList();
                refreshCostsSum();
                break;
        }
        return false;
    }
    void showAllNotes(Cursor notes, String s){
        notes.moveToFirst();
        int ind = notes.getColumnIndex(s), i = 0;
        Log.i("notesInfo","Note " + i + " = " + notes.getString(ind));
        while(notes.moveToNext()){
            i++;
            Log.i("notesInfo","Note " + i + " = " + notes.getString(ind));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent ref;
        switch(id)
        {
            case R.id.new_task:
                //Intent broadcast = new Intent("android.provider.Telephony.SMS_RECEIVED");
                //this.sendBroadcast(broadcast);
                ref = new Intent(this, NewPurchase.class);
                startActivity(ref);
                return true;
            case R.id.temp_picker_set:
                ref = new Intent(this, TemplatePickerActivity.class);
                startActivity(ref);
                return true;
            case R.id.edit_rules_set:
                ref = new Intent(this, RuleEditorActivity.class);
                startActivity(ref);
                return true;
            case R.id.statistic:
                ref = new Intent(this, StatisticActivity.class);
                startActivity(ref);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    // -----------------------------
    void refreshList(){
        /*Cursor c = dbh.getPurchases();
        String []from = new String[]{"purchasename", "total"};
        int []to = {R.id.pe_main, R.id.pe_price};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.purchase_list_elem, c,from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(adapter);*/
    }

    void refreshCostsSum(){
        long curDate,prevDate;
        GregorianCalendar calend = new GregorianCalendar();
        curDate = calend.getTimeInMillis();
        calend.add(Calendar.DAY_OF_YEAR, -14 );
        prevDate = calend.getTimeInMillis();

        costsSumV.setText(String.valueOf(dbh.sumPurchase(prevDate,curDate)));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> ldr = null;
        if( id==0 )
            ldr = new PurchaseLoader(this, new Date(0), new Date() );
        return ldr;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            boolean b = data.moveToFirst();
            if(b) {
                String s = data.getString(data.getColumnIndex("purchasename"));
                adapter.swapCursor(data);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
