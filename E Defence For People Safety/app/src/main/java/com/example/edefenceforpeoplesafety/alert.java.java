package com.alert;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class alert extends Activity {

    
    boolean x = false;
    SharedPreferences sp;
    static public ShakeListener mShaker;
    SmsManager smanager;
    LocationManager locationManager;
    MyLocationListener locationListener;
    TextView tv, tv1;
    Handler handler = new Handler();
    static alert ib;
    Button btnDict;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);


        String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.i(TAG, "AndroidId: " + " " + androidID);




        

        ib = this;
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        String data = sp.getString("threshold",null);
        smanager = SmsManager.getDefault();

       smanager.sendTextMessage("9483440215", null, "X", null, null);
        Button button = (Button) findViewById(R.id.button);
      




        

        l
    public void Enable() {

        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {

            public void onShake(float speed) {
                mShaker.pause();

                tv1.setText("Force on the acceleremeter is "+speed);
                Toast.makeText(getApplicationContext(), "this is onshake",
                        60000).show();

                System.out.println("onshake");
                vibe.vibrate(200);



                alertDialogtwoButton();

                handler = new Handler();
                handler.postDelayed(runnable, 15000);

                 Intent intent = new
                Intent(IProbActivity.this,OneShotPreviewActivity.class);
                startActivity(intent);

            }

           
        });
    }


    
    AlertDialog alertDialog;

    @SuppressWarnings("deprecation")
    public void alertDialogtwoButton() {

        alertDialog = new AlertDialog.Builder(VehicleTheft.this).create();
        alertDialog.setTitle("Warning!");
        alertDialog.setMessage("Really emergency????");
        alertDialog.setButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                handler.removeCallbacks(runnable);
                sendSMS();
            }
        });
        alertDialog.setButton2("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                handler.removeCallbacks(runnable);

            }
        });
        alertDialog.show();
    }

    public void sendSMS() {


        String FirstNo = sp.getString("First No", null);
        String SecondNo = sp.getString("Second No", null);
        String PoliceNo = sp.getString("Third No", null);

        


        smanager.sendTextMessage(FirstNo, null, "The person is in problem, need help", null, null);
        smanager.sendTextMessage(SecondNo, null, "The person is in problem, need help", null, null);
        smanager.sendTextMessage(PoliceNo, null, "The person is in problem, need help", null, null);



        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + FirstNo));
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

   

        
    });
    }

    

  

    
        
   

    public void fetchCurrentLocation() {
        final Criteria criteria = new Criteria();
        final LocationManager locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final String bestProvider = locationMgr.getBestProvider(criteria, true);

        Toast.makeText(VehicleTheft.this, "" + bestProvider, 600000).show();
        locationMgr.requestLocationUpdates(
                bestProvider,
                0,
                0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        locationMgr.removeUpdates(this);
                        Utils.showToast(getApplicationContext(), "Found current location");
                        tvLocation.setText("CurrentLocation lat and long: " + location.getLatitude() + "," + location.getLongitude());
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });
    }


    
                        }
                    });
                }

            }
        }.start();
    }

   
        

      

        
    }

    

    



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.showToast(getApplicationContext(), "Onactivity result");
        switch (requestCode) {
            case RESULT_SPEECH: {


                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String voice = text.get(0);

                    for (int i = 0; i < text.size(); i++) {
                        if (text.get(i).equalsIgnoreCase("danger")) {
                            sendSMS();
                            break;
                        }
                    }


                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q="+voice));
                    startActivity(intent);

                }
            }
        }
    }


    
}