package com.czh.ios11rectprogress;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.cui.rectprogress.RectProgress;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RectProgress rectProgress_main = (RectProgress) findViewById(R.id.rectProgress_main);
        rectProgress_main.setChangedListener(new RectProgress.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int currentValue, int percent) {
                Log.d("Main", "==onProgressChanged: " + currentValue);
                Log.d("Main", "==percent: " + percent);
            }
        });
    }
}
