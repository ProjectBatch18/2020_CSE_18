package com.example.iprobguardian.MainStart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainStart extends Activity implements View.OnClickListener {
    String TAG = getClass().getSimpleName();
    Button loc;
    Button photo;
    Button emergencyinfo;
    Button nearestinfo;
    SmsManager sms = SmsManager.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);
        Button loc = (Button) findViewById(R.id.button7);
        loc.setVisibility(8);
        this.etPlacename.setVisibility(8);
        this.btnDirection.setVisibility(8);
        this.btnViewData.setVisibility(8);

        loc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                SmsManager.getDefault().sendTextMessage(MainStart.this.etPhone.getText().toString(), (String) null, "loc", (PendingIntent) null, (PendingIntent) null);
            }
        });
    }
    public void one(View v)
    {
        Intent i=new Intent(this,NearestInfo.class);
        startActivity(i);
    }
}

   
   

    


