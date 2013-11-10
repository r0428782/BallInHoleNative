package com.example.ballinhole;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.SensorEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button startButton;
	private Button pauseButton;
	private Button stopButton;

	private TextView timerValue;

	private long startTime = 0L;

	private Handler customHandler = new Handler();

	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	
	BallView mBallView = null;
	int mScrWidth, mScrHeight;
    Timer mTmr = null;
    TimerTask mTsk = null;
    android.graphics.PointF mBallPos, mBallSpd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ball_in_hole);
		
		timerValue = (TextView) findViewById(R.id.timer);
		/*final android.widget.FrameLayout main = (android.widget.FrameLayout) findViewById(R.id.speelveld);
		 mScrWidth = findViewById(R.id.speelveld).getWidth(); 
		 mScrHeight = findViewById(R.id.speelveld).getHeight();
		 mBallPos = new android.graphics.PointF();
		 mBallSpd = new android.graphics.PointF();

		 mBallPos.x = mScrWidth/2; 
		 mBallPos.y = mScrHeight/2; 
		 mBallSpd.x = 0;
		 mBallSpd.y = 0; 
		 
		 mBallView = new BallView(this,mBallPos.x,mBallPos.y,5);
		 main.addView(mBallView);
		 mBallView.invalidate();*/
	    }
	
	public Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            secs = secs % 60;
            //int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText(String.format("%02d", secs)
            		//+ ":" + String.format("%03d", milliseconds)
            		);
            customHandler.postDelayed(this, 0);
        }
    };
	
    /*public void onSensorChanged(SensorEvent event){
    	ball.changeSpeedX(-event.values[0]);
        ball.changeSpeedY(event.values[1]);
     }*/
    
	public void play(View view) {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
	}
	public void pause(View view) {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);	
	}	
	public void stop(View view) {
		timeSwapBuff = 0;
		customHandler.removeCallbacks(updateTimerThread);
	}
	
	@Override
	public void onPause() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);	
	    super.onPause();
	}
	public void onResume() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
	    super.onResume();
	}
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    android.os.Process.killProcess(android.os.Process.myPid());
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}
}
