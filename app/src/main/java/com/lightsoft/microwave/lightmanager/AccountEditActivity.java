package com.lightsoft.microwave.lightmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.lightsoft.microwave.lightmanager.dbworks.Account;

import java.util.Date;
import java.util.GregorianCalendar;


public class AccountEditActivity extends Activity {
    DBHelper dbh;
    EditText nameEdit;
    EditText typeEdit;
    EditText currencyEdit;
    EditText balanceEdit;
    Account account;
    long id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        dbh = new DBHelper(this);
        account = new Account();
        nameEdit = (EditText) findViewById(R.id.ae_name);
        typeEdit = (EditText) findViewById(R.id.ae_type);
        currencyEdit = (EditText) findViewById(R.id.ae_currency);
        balanceEdit = (EditText) findViewById(R.id.ae_balance);

        Intent intent = getIntent();
        String action = intent.getAction();
        if(action != null && action.equals("ACTION_LM_EDIT")) {
            SQLiteDatabase db = dbh.getWritableDatabase();
            id = intent.getLongExtra("id", -1);
            if( id != -1 ) {
                Cursor c = db.query(Account.TABLE, new String[]{Account.ID, Account.ACCOUNTTYPE, Account.NAME, Account.CURRENCY, Account.BALANCE},
                        Account.ID + " = " + 1, null, null, null, null);
                if (!c.moveToFirst()) {
                    Log.e("myinfo", "AccountEdit editmode: record wasnt finded id=" + id);
                    return;
                }
                nameEdit.setText(c.getString(c.getColumnIndex(Account.NAME)));
                typeEdit.setText(c.getString(c.getColumnIndex(Account.ACCOUNTTYPE)));
                currencyEdit.setText(c.getString(c.getColumnIndex(Account.CURRENCY)));
                balanceEdit.setText(c.getString(c.getColumnIndex(Account.BALANCE)));
            }
        }
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
            if(this.id != -1)
                editRow();
            else
                putToDB();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {

        String type = typeEdit.getText().toString();
        String name = nameEdit.getText().toString();
        String currency = currencyEdit.getText().toString();
        int balance = Integer.parseInt(balanceEdit.getText().toString());
        if(id != -1)
            account.setId((int)id);
        account.setAccountType(type);
        account.setName(name);
        account.setCurrency(currency);
        account.setBalance(balance);
    }

    private void putToDB() {
        fetchData();
        account.insert(dbh.getWritableDatabase());
    }

    private void editRow() {
        fetchData();
        account.update(dbh.getWritableDatabase());
    }
}
