package com.prathameshmore.feedbackapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {


    private Context mContext;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        handler = new Handler();

        startHandler();
    }

    private void startHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(mContext,MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
        finish();
    }

}
