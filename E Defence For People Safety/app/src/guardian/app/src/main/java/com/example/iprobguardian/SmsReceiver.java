package com.project.guardian;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;


public class smsReceiver extends BroadcastReceiver {
    static int counter = 0;
    static String incomingno;
    


    static MediaPlayer f102mp;
    String TAG = "smsReceiver";
    Context cntxt;
    NotificationManager notificationManager;
    SmsManager smanager;
    SharedPreferences f103sp;

    public void onReceive(Context context, Intent intent) {
        Bundle bundle;
        Context context2 = context;
        this.cntxt = context2;
        this.f103sp = PreferenceManager.getDefaultSharedPreferences(context);
        this.smanager = SmsManager.getDefault();
        this.notificationManager = (NotificationManager) context2.getSystemService("notification");
        Bundle bundle2 = intent.getExtras();
        f102mp = new MediaPlayer();
        if (bundle2 != null) {
            Object[] pdus = (Object[]) bundle2.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            int i = 0;
            while (i < msgs.length) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                incomingno = msgs[i].getOriginatingAddress();
                String body = msgs[i].getMessageBody().toString();
                Toast.makeText(context2, "Sms received: " + body).show();
                Log.i(this.TAG, "SMS REceived: " + body);
                checkEmission(body);
                String body2 = body.replaceAll("\n", "");
                if (body2.contains(body2.contains("PROBLEM") || body2.contains("problem"))
                     {   
                        playVoice(body2);
                      }
            }
    }
       
    public void playVoice(String message) {
        Intent intentService = new Intent(this.cntxt, MyAlarmService.class);
        intentService.putExtra("text", message);
        this.cntxt.startService(intentService);
    }

    
