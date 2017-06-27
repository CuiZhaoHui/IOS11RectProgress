package com.czh.ios11rectprogress;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cui.rectprogress.RectProgress;

public class MainActivity extends Activity {

    private RectProgress rectProgress_main;
    private RectProgress rectProgress_main1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rectProgress_main = (RectProgress) findViewById(R.id.rectProgress_main);
        rectProgress_main1 = (RectProgress) findViewById(R.id.rectProgress_main1);
        rectProgress_main.setChangedListener(new RectProgress.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int currentValue, int percent) {
                Log.d("Main", "==onProgressChanged: " + currentValue);
                Log.d("Main", "==percent: " + percent);
            }
        });
    }

    public void setProgress(View v){
        rectProgress_main.setProgress((int) (Math.random() * 100));
        rectProgress_main1.setProgress((int) (Math.random() * 100));
    }

}
