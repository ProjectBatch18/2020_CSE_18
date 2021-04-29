package com.project.project;

import java.io.IOException;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

public class MyService extends Service {
	
	MyTrackListener MTListener;
	LocationManager locationMgr;    
	static String state="idle";    
	static boolean mBluezoneflag = false,mRedzoneflag = false;
	private TextToSpeech mTts;
	public MyService() {    
		
	}
          
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
         
	@Override
    public void onCreate() {
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
        
                       
    }

    @Override
    public void onStart(Intent intent, int startId) {
    	// For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
        mTts = new TextToSpeech(this, new OnInitListener() {
			
			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				if (status == TextToSpeech.SUCCESS) {
					      
			        int result = mTts.setLanguage(Locale.US);
			        if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
			        	Toast.makeText(getApplicationContext(), "", 60000).show();
			        //	mTts.speak("hello how are you", TextToSpeech.QUEUE_FLUSH, null);
			        }
			    }     
			}
		});
        
       final Criteria criteria = new Criteria();
        
        
        	
             
        state = "Active";
    	locationMgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    	MTListener = new MyTrackListener(); 
    	    
//    	if(!locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//    		
//    		locationMgr.requestLocationUpdates(
//             		LocationManager.NETWORK_PROVIDER, 
//             		5000,    
//             		0,   
//             		MTListener);   
//    	}else if(!locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
//    		locationMgr.requestLocationUpdates(
//             		LocationManager.GPS_PROVIDER, 
//             		0,        
//             		1,   
//             		MTListener);   
//    	}else{
    	          
    		final String bestProvider = locationMgr.getBestProvider(criteria, true);
    		Toast.makeText(getApplicationContext(), "Best Provider "+bestProvider, 6000000).show();
    		locationMgr.requestLocationUpdates(
             		bestProvider, 
             		10000,    
             		0,   
             		MTListener);   
    	//}  
	           
    }    
      
    @Override    
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        if(locationMgr!=null && MTListener!=null){
        	locationMgr.removeUpdates(MTListener);
        	locationMgr=null;
        	MTListener = null;
        }
    }    
            
	             
	private class MyTrackListener implements LocationListener {

		public void onLocationChanged(Location location) {
			  
			Toast.makeText(getApplicationContext(), "Got location", 600).show(); 
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyService.this);
			String mBluezone = sp.getString("bluezone",null);
	 	    String mRedzone = sp.getString("redzone",null);   
			      
	 	    if(mBluezone!=null ){
	 	    	String[] bTemp = mBluezone.split("\\$");
		 	    //String[] rTemp = mRedzone.split("\\$");
		 	      
		 	    if(bTemp.length==2){
		 	    	
		 	    	String[] nameloc = bTemp[0].split("\\#");
		 	    	String[] nameloc1 = bTemp[1].split("\\#");
		 	    	String[] bL1 = nameloc[0].split("\\,"); 
		 	    	String[] bL2 = nameloc1[0].split("\\,");
		 	    	 
		 	    	      
		 	    	                     
		 	    	Location mBluezoneLoc = new Location(LocationManager.NETWORK_PROVIDER);
		 	 	    Location mRedZoneLoc = new Location(LocationManager.NETWORK_PROVIDER);
		 	 	    Location mBluezoneLoc1 = new Location(LocationManager.NETWORK_PROVIDER);
		 	 	    Location mRedZoneLoc1 = new Location(LocationManager.NETWORK_PROVIDER);
		 	 	 
		 	 	    
		 	 	    
		 	 	    
		 	 	    
		 	 	    
		 	 	    double mbLat = Double.parseDouble(bL1[0]);
		 	 	    double mbLon = Double.parseDouble(bL1[1]);
		 	 	    
		 	 	    double mbLat1 = Double.parseDouble(bL2[0]);
		 	 	    double mbLon1 = Double.parseDouble(bL2[1]);
		 	 	          
		 	 	    mBluezoneLoc.setLatitude(mbLat);	 	
		 	 	    mBluezoneLoc.setLongitude(mbLon);  
		 	 	    
		 	 	    mBluezoneLoc1.setLatitude(mbLat1);	 	
		 	 	    mBluezoneLoc1.setLongitude(mbLon1);
		 	 	                 
		 	 	    double bDistance = location.distanceTo(mBluezoneLoc);
		 	 	   // double rDistance = location.distanceTo(mRedZoneLoc);
		 	 	    
		 	 	    double bDistance1 = location.distanceTo(mBluezoneLoc1);
		 	 	  //  double rDistance1 = location.distanceTo(mRedZoneLoc1);
		 	 	    
		 	 	    Toast.makeText(getApplicationContext(), "toll: "+bDistance,600).show();
		 	 	        
		 	 	  /*  if(rDistance>01&& rDistance<250 || rDistance1>01&& rDistance1<250 ){   
		 	 	    	if(!mRedzoneflag){
		 	 	    		Toast.makeText(getApplicationContext(), "redzone", 600000).show();
			 	 	    	//PlayAudio("/sdcard/iprob/redzone.ogg"); // Blue zone audio path   
			 	 	    	mRedzoneflag = true;
		 	 	    	}else{
		 	 	    		     
		 	 	    	}         
		 	 	    	             
		 	 	    }else */    
		 	 	                               
		 	 	    	if(bDistance>0 && bDistance<500 || bDistance1>0 && bDistance1<500){  
		 	 	    		if(!mBluezoneflag){
		 	 	    			Toast.makeText(getApplicationContext(), "bluezone", 600000).show();
		 	 	    			//PlayAudio("/sdcard/iprob/bluezone.ogg");// Danger zone audio path
		 	 	    			mTts.speak(nameloc[1]+" stop please get down.", TextToSpeech.QUEUE_FLUSH, null);
		 	 	    			mBluezoneflag = true;
		 	 	    			    
		 	 	    			int balance = Integer.parseInt(smsReceiver.BalanceWallet);
		 	 	    			if(Integer.parseInt(smsReceiver.BalanceWallet)>20){ 
		 	 	    				balance = balance - 20;       
		 	 	    				smsReceiver.BalanceWallet = Integer.toString(balance);   
		 	 	    				Toast.makeText(getApplicationContext(), "Found toll: "+smsReceiver.BalanceWallet, 600000).show();
		 	 	    			}else{          
		 	 	    				Toast.makeText(getApplicationContext(), "No sufficient balance", 6000000).show();
		 	 	    				SmsManager sm = SmsManager.getDefault();
//		 	 	    				sm.sendTextMessage("9008001899", null, "Insufficient funds to vehicle no KA 02 5502", null, null);
//		 	 	    				sm.sendTextMessage("8546953530", null, "Insufficient funds to vehicle no KA 02 5502", null, null);
		 	 	    			}
		 	 	    			     
		 	 	    			
		 	 	    		}else{   
		 	 	    		    
		 	 	    	}
		 	 	    	  
		 	 	    	
		 	 	    }         
		 	    }            
	 	    }   
	 	                   
		}         
                   
		public void onStatusChanged(String s, int i, Bundle b) {
			
		}

		public void onProviderDisabled(String s) {
		
		}

		public void onProviderEnabled(String s) {
		
		}
     
	}
	
	public void PlayAudio(String mAudioPath) {
		
		final MediaPlayer mp = new MediaPlayer();
		
		try {
			mp.setDataSource(mAudioPath);
			mp.prepare();
			mp.start(); 
			mp.setOnCompletionListener(new OnCompletionListener() {
				
				@Override   
				public void onCompletion(MediaPlayer mp1) {
					// TODO Auto-generated method stub    
					mp1.release();
					mp.release();
				}   
			});   
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block   
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
	}
}
