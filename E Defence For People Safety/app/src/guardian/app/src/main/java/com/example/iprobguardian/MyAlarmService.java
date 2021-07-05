package com.example.iprobguardian.MyAlarmService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Locale;

public class MyAlarmService extends Service implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    private TextToSpeech mTts;
    private String spokenText = "";

    
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        String data = intent.getStringExtra("text");
        this.spokenText = data + " " + data + " " + data + " " + data + " " + data + " " + data;
        this.mTts = new TextToSpeech(this, this);
    }

    public void onUtteranceCompleted(String utteranceId) {
        stopSelf();
    }

    
    public void onInit(int status) {
        int result;
        if (status == 0 && (result = this.mTts.setLanguage(Locale.US)) != -1 && result != -2) {
            this.mTts.speak(this.spokenText, 0, (HashMap) null);
        }
    }
}
