package com.lightsoft.microwave.lightmanager.smshandle;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsMessage;
import android.util.Log;

import com.lightsoft.microwave.lightmanager.DBHelper;
import com.lightsoft.microwave.lightmanager.ListActivity;
import com.lightsoft.microwave.lightmanager.R;
import com.lightsoft.microwave.lightmanager.dbworks.TypeReplaceRule;
import com.lightsoft.microwave.lightmanager.dbworks.TypesProvider;

import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SmsHandler extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String SMS_HANDLE = "SMS_HANDLE";

    // TODO: Rename parameters
    private static final String SMS_PARAM = "sms";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSmsHandle(Context context, String param1, String param2) {

    }



    public SmsHandler() {
        super("SmsHandler");
    }

    private void showNotification(String text) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ListActivity.class), 0);
        Context context = getApplicationContext();
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("SmsHandler")
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notificationManager.notify(R.drawable.ic_launcher, notification);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        showNotification("SMS incoming");
        if (intent != null) {
            final String action = intent.getAction();
            if (SMS_HANDLE.equals(action)) {
                final String param1 = intent.getStringExtra(SMS_PARAM);

                handleActionSMSHandle(param1);
            }
        }
    }

    /**
     * Handle action SMS_HANDLE in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSMSHandle(String msg) {
        if(msg != null){
            int currencyIndex = msg.indexOf("р ");
            if(currencyIndex == -1) {
                Log.e("myinfo", "SMSHandle: Currency havent finded");
                return;
            }
            Log.i("myinfo", "SMSHandle: Currency have finded:" + currencyIndex);
            int sumIndex = msg.lastIndexOf(" ", currencyIndex);
            if(currencyIndex == -1) {
                Log.e("myinfo", "SMSHandle: Sum havent finded");
                return;
            }
            Log.i("myinfo", "SMSHandle: Sum have finded:" + sumIndex);
            float sum = Float.valueOf(msg.substring(sumIndex + 1, currencyIndex));
            Log.i("myinfo", "SMSHandle: Sum parsed:" + sum);
            int placeLIndex = msg.indexOf(" Баланс");
            Log.i("myinfo", "SMSHandle: Place have finded:" + placeLIndex);
            String place = msg.substring(currencyIndex+2, placeLIndex);
            Log.i("myinfo", "SMSHandle: Place gathered:" + place);
            writeInDB(place, (int)sum, msg);

        }
    }

    private void writeInDB(String place, int sum, String original){
        try {
            DBHelper dbh = new DBHelper(this);
            SQLiteDatabase db = dbh.getWritableDatabase();
            ContentValues cv = new ContentValues();
            TypesProvider tp = new TypesProvider(dbh, "purchasetypes");


            Cursor c = db.query(TypeReplaceRule.TABLE, new String[]{TypeReplaceRule.TYPE, TypeReplaceRule.BRAND}, " " + TypeReplaceRule.BRAND_RAW + " = ?", new String[]{place}, null, null, null);
            if(c.moveToFirst()){
                cv.put("purchasename", c.getString(c.getColumnIndex(TypeReplaceRule.TYPE)));
                tp.incrType(c.getString(c.getColumnIndex(TypeReplaceRule.TYPE)));
            }
            else{
                cv.put("purchasename", "Card parsed");
                tp.incrType("Card parsed");
            }
            cv.put("total", sum);
            cv.put("place", place);
            cv.put("date", new Date().getTime());
            cv.put("comment", original);
            cv.put("accountid", 0);
            db.insert("purchases", null, cv);
            dbh.close();
        }
        catch(Exception e){
            Log.e("myinfo", "SMSHandle: Database writing have failed");
            showNotification("Writing in the database have failed");
        }

    }

}
