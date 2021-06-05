
package com.project.project;

import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.UnsupportedOperationException;

public class ShakeListener implements SensorListener 
{
  public static int FORCE_THRESHOLD = 350;
  private static final int TIME_THRESHOLD = 100;
  private static final int SHAKE_TIMEOUT = 500;
  private static final int SHAKE_DURATION = 1000;
  private static final int SHAKE_COUNT = 5;

  private SensorManager mSensorMgr;
  private float mLastX=-1.0f, mLastY=-1.0f, mLastZ=-1.0f;
  private long mLastTime;
  private OnShakeListener mShakeListener;
  private Context mContext;
  private int mShakeCount = 0;
  private long mLastShake;
  private long mLastForce;

  public interface OnShakeListener
  {
    public void onShake(float x);
    public void onCamera(float y);
  }

  public ShakeListener(Context context) 
  { 
    mContext = context;
    resume();
  }

  public void setOnShakeListener(OnShakeListener listener)
  {
	     
	  mShakeListener = listener;
    
  }

  public void resume() {
	  
	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
	FORCE_THRESHOLD = Integer.parseInt(sp.getString("threshhold","850")); 
	
    mSensorMgr = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE); 
         
    if (mSensorMgr == null) { 
    	
      throw new UnsupportedOperationException("Sensors not supported");
      
    }
    boolean supported = mSensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
    if (!supported) {
      mSensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
      throw new UnsupportedOperationException("Accelerometer not supported");
    }    
  }      

  public void pause() {
    if (mSensorMgr != null) {
    	
      mSensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
      mSensorMgr = null;
      Toast.makeText(mContext, "disabled", 600).show();
    }   
                  
  }

  public void onAccuracyChanged(int sensor, int accuracy) { }

  public void onSensorChanged(int sensor, float[] values) 
  {
    if (sensor != SensorManager.SENSOR_ACCELEROMETER) return;
    long now = System.currentTimeMillis();

    if ((now - mLastForce) > SHAKE_TIMEOUT) {
    	
      mShakeCount = 0;
       
    }

    if ((now - mLastTime) > TIME_THRESHOLD) {
    	
      long diff = now - mLastTime;
      // threshhold caluclator
      float speed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
      
    //  System.out.println("the value is "+speed);
      
//      if(speed >= 1200 && speed < 1500 && (now - mLastShake > SHAKE_DURATION)){
//    	  
//    	  mLastShake = now;
//    	  if (mShakeListener != null) { 
//        	  
//             // mShakeListener.onCamera(speed);
//          	              
  //    	  mLastForce = now;
//      }          
      
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
      FORCE_THRESHOLD = Integer.parseInt(sp.getString("threshhold","125000"));       
      if (speed > FORCE_THRESHOLD) {
    	  Toast.makeText(mContext, "force value is "+FORCE_THRESHOLD + " speed is "+speed, 600).show();
        if ((now - mLastShake > SHAKE_DURATION)) {
        	
          mLastShake = now;
          mShakeCount = 0;
           
          if (mShakeListener != null) { 
        	       
            mShakeListener.onShake(speed);
        	     
          }    
        }    
        
        mLastForce = now;
      }
      mLastTime = now;
      mLastX = values[SensorManager.DATA_X];
      mLastY = values[SensorManager.DATA_Y];
      mLastZ = values[SensorManager.DATA_Z];
    }
  }

}
