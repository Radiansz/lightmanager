package com.lightsoft.microwave.lightmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class AccountActivity extends Activity implements AdapterView.OnItemClickListener {

    DBHelper dbh;
    ListView lv;
    CursorAdapter adapter;


    private static final int DELETE_ELEM = 0;
    private static final int EDIT_ELEM = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        dbh = new DBHelper(this);
        lv = (ListView) findViewById(R.id.listView);
        String []from = new String[]{"name", "balance", "accountType"};
        int []to = {R.id.pe_main, R.id.pe_price, R.id.pe_extra};
        adapter = new SimpleCursorAdapter(this, R.layout.purchase_list_elem, null,from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);

    }

    @Override
    protected  void onResume(){
        super.onResume();
        refreshList();
    }

    private void refreshList(){
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor c = db.query("accounts", new String[]{"_id", "accountType", "name", "balance"}, null, null, null, null, null);
        adapter.swapCursor(c);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent ref;
        switch(id) {
            case R.id.new_task:
                //Intent broadcast = new Intent("android.provider.Telephony.SMS_RECEIVED");
                //this.sendBroadcast(broadcast);
                ref = new Intent(this, AccountEditActivity.class);
                startActivity(ref);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,EDIT_ELEM,0,R.string.edit);
        menu.add(0, DELETE_ELEM, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo ac = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch(item.getItemId()){
            case EDIT_ELEM:
                Log.i("myinfo", "id is = " + ac.id);
                Intent intent = new Intent("ACTION_LM_EDIT", null, this, AccountEditActivity.class);
                intent.putExtra("id", ac.id);
                startActivity(intent);
                break;
            case DELETE_ELEM:
                break;
        }
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
