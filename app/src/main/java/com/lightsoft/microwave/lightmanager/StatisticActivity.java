package com.lightsoft.microwave.lightmanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.lightsoft.microwave.lightmanager.dbworks.Purchase;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class StatisticActivity extends Activity implements View.OnClickListener {
    DBHelper dbh;
    ListView lv;
    CursorAdapter adapter;

    RadioButton yearRadio;
    RadioButton monthRadio;
    RadioButton weekRadio;
    RadioButton dayRadio;
    TextView totalText;

    Button refreshButton;
    long targetTime = -1;
    EditText amountEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        dbh = new DBHelper(this);
        lv = (ListView) findViewById(R.id.statdatalist);
        yearRadio = (RadioButton) findViewById(R.id.stat_year);
        monthRadio = (RadioButton) findViewById(R.id.stat_month);
        weekRadio = (RadioButton) findViewById(R.id.stat_week);
        dayRadio = (RadioButton) findViewById(R.id.stat_day);
        amountEdit = (EditText) findViewById(R.id.amount);
        totalText = (TextView) findViewById(R.id.stat_total);
        refreshButton = (Button) findViewById(R.id.stat_refresh);
        refreshButton.setOnClickListener(this);
        String []from = new String[]{"purchasename", "sum"};
        int []to = {R.id.stat_main, R.id.stat_left};
        adapter = new SimpleCursorAdapter(this, R.layout.purchase_list_elem2, null,from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(adapter);
        refreshList();

    }

    private long calcTargetTime(){

        return 0;
    }

    private void refreshList(){
        SQLiteDatabase db = dbh.getWritableDatabase();
        long curTime;
        GregorianCalendar calend = new GregorianCalendar();
        curTime = calend.getTimeInMillis();
        if(targetTime == -1) {
            calend.add(Calendar.YEAR, -1);
            targetTime = calend.getTimeInMillis();
        }
        Cursor cursor = db.query(Purchase.TABLE,
                new String[]{"sum(" + Purchase.TOTAL + ") as sum"},
                Purchase.DATE + " > " + targetTime + " AND " + Purchase.DATE + " < " + curTime,
                null,
                null,
                null,
                null,
                null
        );
        int total = -1;
        if(cursor.moveToFirst()){
            total = cursor.getInt(cursor.getColumnIndex("sum"));
        }
        totalText.setText(String.valueOf(total));
        Cursor statCursor = db.query(Purchase.TABLE,
                new String[]{"max(_id) as _id", " " + Purchase.PURCHASENAME, "(sum(" + Purchase.TOTAL + ")) as sum", " count(" + Purchase.PURCHASENAME + ") as extra"},
                Purchase.DATE + " > " + targetTime + " AND " + Purchase.DATE + " < " + curTime,
                null,
                Purchase.PURCHASENAME,
                null,
                "sum DESC",
                null
        );
        adapter.swapCursor(statCursor);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        GregorianCalendar calend = new GregorianCalendar();
        String s = amountEdit.getText().toString();
        int interval = Integer.parseInt(s);
        if(yearRadio.isChecked())
            calend.add(Calendar.YEAR, -1*interval);
        if(monthRadio.isChecked())
            calend.add(Calendar.MONTH, -1*interval);
        if(weekRadio.isChecked())
            calend.add(Calendar.DAY_OF_YEAR, -7*interval);
        if(dayRadio.isChecked())
            calend.add(Calendar.DAY_OF_YEAR, -1*interval);
        targetTime = calend.getTimeInMillis();
        refreshList();

    }
}
