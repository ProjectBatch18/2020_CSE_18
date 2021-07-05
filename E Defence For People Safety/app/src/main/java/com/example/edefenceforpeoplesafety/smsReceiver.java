package com.example.smsReceiver;

import android.app.PendingIntent;
import android.content.context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

public class smsReceiver extends BroadcastReceiver {
    static MyTrackListener MTListener;
    public static int counter = 0;
    static MyLocationListener locationListener;
    static LocationManager locationManager;
    static LocationManager locationMgr;
    Context cntxt;
    private int currentFormat = 0;
    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        public void onError(MediaRecorder mr, int what, int extra) {
            Context context = smsReceiver.this.cntxt;
            Toast.makeText(context, "Error: " + what + ", " + extra, 0).show();
        }
    };
    
    
    SmsManager smanager;

    /* renamed from: sp */
    SharedPreferences f26sp;

    public void onReceive(Context context, Intent intent) {
        Context context2 = context;
        this.cntxt = context2;
        this.f26sp = PreferenceManager.getDefaultSharedPreferences(context);
        this.smanager = SmsManager.getDefault();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            char c = 0;
            int i = 0;
            while (i < msgs.length) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                incomingno = msgs[i].getOriginatingAddress();
                String body = msgs[i].getMessageBody().toString();
                Toast.makeText(this.cntxt, "sms received " + body, 1).show();
                String deviceId = ((TelephonyManager) this.cntxt.getSystemService("phone")).getDeviceId();
                
                if (body.equalsIgnoreCase("loc")) {
                    Criteria criteria = new Criteria();
                    locationManager = (LocationManager) context2.getSystemService("location");
                    String bestProvider = locationManager.getBestProvider(criteria, true);
                    Toast.makeText(context2, "" + bestProvider, 600000).show();
                    locationListener = new MyLocationListener();
                    locationManager.requestLocationUpdates(bestProvider, 0, 0.0f, locationListener);
                }
                
                if (body.equalsIgnoreCase("track")) {
                    Criteria criteria2 = new Criteria();
                    locationMgr = (LocationManager) context2.getSystemService("location");
                    String bestProvider2 = locationMgr.getBestProvider(criteria2, true);
                    Toast.makeText(this.cntxt, "" + bestProvider2, 600000).show();
                    MTListener = new MyTrackListener();
                    locationMgr.requestLocationUpdates(bestProvider2, 10000, 0.0f, MTListener);
                }
                if (body.contains("stop")) {
                    if (!(locationMgr == null || MTListener == null)) {
                        Toast.makeText(context2, "gps stopped", 6000000).show();
                        locationMgr.removeUpdates(MTListener);
                        locationMgr = null;
                        MTListener = null;
                    }
                } 
                
            }
        }
    }

    public void stopListening(LocationManager locationManager2) {
        if (locationManager2 != null) {
            try {
                if (locationListener != null) {
                    locationManager2.removeUpdates(locationListener);
                    locationListener = null;
                }
            } catch (Exception e) {
            }
        }
    }

    private class MyLocationListener implements LocationListener {
        private MyLocationListener() {
        }

        public void onLocationChanged(Location location) {
            String format = String.format("12345 New Location of your friend \n Longitude: %1$s \n Latitude: %2$s", new Object[]{Double.valueOf(location.getLongitude()), Double.valueOf(location.getLatitude())});
            Toast.makeText(smsReceiver.this.cntxt, "got loc", 6000).show();
            smsReceiver.this.stopListening(smsReceiver.locationManager);
            SmsManager smsManager = smsReceiver.this.smanager;
            String str = smsReceiver.incomingno;
            smsManager.sendTextMessage(str, (String) null, "loc1234 " + location.getLatitude() + "," + location.getLongitude(), (PendingIntent) null, (PendingIntent) null);
            String string = smsReceiver.this.f26sp.getString("Police No", (String) null);
        }

        public void onStatusChanged(String s, int i, Bundle b) {
        }

        public void onProviderDisabled(String s) {
        }

        public void onProviderEnabled(String s) {
        }
    }

    private class MyTrackListener implements LocationListener {
        private MyTrackListener() {
        }

        public void onLocationChanged(Location location) {
            String format = String.format("New Location of your friend \n Longitude: %1$s \n Latitude: %2$s", new Object[]{Double.valueOf(location.getLongitude()), Double.valueOf(location.getLatitude())});
            SmsManager smsManager = smsReceiver.this.smanager;
            String str = smsReceiver.incomingno;
            smsManager.sendTextMessage(str, (String) null, "loc1234 " + location.getLatitude() + "," + location.getLongitude(), (PendingIntent) null, (PendingIntent) null);
        }

        public void onStatusChanged(String s, int i, Bundle b) {
        }

        public void onProviderDisabled(String s) {
        }

        public void onProviderEnabled(String s) {
        }
    }

    

    public String getFilePath(String filename) {
        File file = new File("/sdcard/iot/");
        if (!file.exists()) {
            return "";
        }
        for (File f : file.listFiles()) {
            if (f.getName().toLowerCase().contains(filename)) {
                return f.getAbsolutePath();
            }
        }
        return "";
    }

    public void sendSMS(String data) {
        SmsManager smsManager = SmsManager.getDefault();
        String[] temp = data.split("\\#");
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < temp.length) {
                String[] info = temp[i2].split("\\,");
                smsManager.sendTextMessage(info[1], (String) null, "The person is in problem, need help", (PendingIntent) null, (PendingIntent) null);
                i = i2 + 1;
            } else {
                return;
            }
        }
    }
}
