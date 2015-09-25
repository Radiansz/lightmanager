package com.lightsoft.microwave.lightmanager.smshandle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.lightsoft.microwave.lightmanager.ListActivity;

/**
 * Created by lightwave on 31.07.15.
 */
public class SmsReciever extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final String NUMBER_TO_HANDLE = "900";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("myinfo", "Reciever: Message incoming");
        if( intent != null && intent.getAction() != null && ACTION.equalsIgnoreCase(intent.getAction())){
            Log.i("myinfo", "Reciever: Gathering extras");
            Object[] pdus = (Object[])intent.getExtras().get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for(int i=0; i<pdus.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            String body = "";
            boolean finded = false;
            for(int i=0; i<msgs.length; i++){
                Log.i("myinfo", "Reciever: Looking for intresting messages from " + i + " " + msgs[i].getDisplayOriginatingAddress());
                if(msgs[i].getDisplayOriginatingAddress().equals(NUMBER_TO_HANDLE)){
                    finded = true;
                    Log.i("myinfo", "Reciever: Message part is added. Body:" + msgs[i].getMessageBody());

                    body += msgs[i].getMessageBody();


                }
            }
            if(finded){
                Log.i("myinfo", "Reciever: Message is going to handle. Whole message:" + body);
                Intent smsHandleInt = new Intent("SMS_HANDLE", null, context, SmsHandler.class);
                smsHandleInt.putExtra("sms", body);
                context.startService(smsHandleInt);
            }

        }
    }
}
