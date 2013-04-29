package cs378.cyberphys.tankcontroller;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	private Socket socket;
	private String serverIpAddress = "192.168.1.65";
	private int port = 8000;
	
	private int testUpInt = -1;
	private int testDownInt = -1;
	private int testLeftInt = -1;
	private int testRightInt = -1;
	private int testFireInt = -1;
	private static String logtag = "TANK CONTROLLER";
	private static String piIP;
	public static Client piClient;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
		StrictMode.setThreadPolicy(policy);
		
		piClient = new Client();
		piClient.writeMessage("FIRST MESSAGE RECEIVED!!!!");
		piClient = new Client();
		piClient.writeMessage("SECOND MESSAGE RECIEVED!!!!");
		piClient = new Client();
		piClient.writeMessage("THIRD MESSAGE RECEIVED!!!!");
		
		final ImageButton buttonUp = (ImageButton) findViewById(R.id.upArrow);
		final ImageButton buttonDown = (ImageButton) findViewById(R.id.downArrow);
		final ImageButton buttonLeft = (ImageButton) findViewById(R.id.leftArrow);
		final ImageButton buttonRight = (ImageButton) findViewById(R.id.rightArrow);
		final ImageButton buttonFire = (ImageButton) findViewById(R.id.fireButton);
	
		/* touch handler for all buttons */
		OnTouchListener buttonListener = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int act = event.getAction();
				switch (v.getId()) {
					case R.id.upArrow:{
						if (act == MotionEvent.ACTION_DOWN) {
							upRunnable.run();
							piClient = new Client();
							piClient.writeMessage("UP BUTTON PRESSED!!!!");
							Log.d(logtag, "Up pressed");
						} else if (act == MotionEvent.ACTION_UP) {
							handler.removeCallbacks(upRunnable);
							Log.d(logtag, "Up released, was held for " + testUpInt + " seconds");
							testUpInt = -1;
						}
						break;
					} case R.id.downArrow:{
						if (act == MotionEvent.ACTION_DOWN) {
							downRunnable.run();
							Log.d(logtag, "Down pressed");
						} else if (act == MotionEvent.ACTION_UP){
							handler.removeCallbacks(downRunnable);
							Log.d(logtag, "Down released, was held for " + testDownInt + " seconds");
							testDownInt = -1;
						}
						break;
					} case R.id.leftArrow:{
						if (act == MotionEvent.ACTION_DOWN) {
							leftRunnable.run();
							Log.d(logtag, "Left pressed");
						} else if (act == MotionEvent.ACTION_UP){
							handler.removeCallbacks(leftRunnable);
							Log.d(logtag, "Left released, was held for " + testLeftInt + " seconds");
							testLeftInt = -1;
						}
						break;
					} case R.id.rightArrow:{
						if (act == MotionEvent.ACTION_DOWN) {
							rightRunnable.run();
							Log.d(logtag, "Right pressed");
						} else if (act == MotionEvent.ACTION_UP){
							handler.removeCallbacks(rightRunnable);
							Log.d(logtag, "Right released, was held for " + testRightInt + " seconds");
							testRightInt = -1;
						}
						break;
					} case R.id.fireButton:{
						if (act == MotionEvent.ACTION_DOWN) {
							fireRunnable.run();
							
							Log.d(logtag, "Fire pressed");
						} else if (act == MotionEvent.ACTION_UP){
							handler.removeCallbacks(fireRunnable);
							Log.d(logtag, "Fire released, was held for " + testFireInt + " seconds");
							testFireInt = -1;
						}
						break;
					}
				}
			return true;
			}
		};
		
		buttonUp.setOnTouchListener(buttonListener);
		buttonDown.setOnTouchListener(buttonListener);
		buttonLeft.setOnTouchListener(buttonListener);
		buttonRight.setOnTouchListener(buttonListener);
		buttonFire.setOnTouchListener(buttonListener);
		
		piClient.closeConnection();
		
	}
	
	private Handler handler = new Handler();

	private Runnable upRunnable = new Runnable() {
		public void run() {
			testUpInt++;
			piClient = new Client();
			piClient.writeMessage("UP");
			handler.postDelayed(this, 1000);
		}
	};
	
	private Runnable downRunnable = new Runnable() {
		public void run() {
			testDownInt++;
			piClient = new Client();
			piClient.writeMessage("DOWN");
			handler.postDelayed(this, 1000);
		}
	};
	
	private Runnable leftRunnable = new Runnable() {
		public void run() {
			testLeftInt++;
			piClient = new Client();
			piClient.writeMessage("LEFT");
			handler.postDelayed(this, 1000);
		}
	};
	
	private Runnable rightRunnable = new Runnable() {
		public void run() {
			testRightInt++;
			piClient = new Client();
			piClient.writeMessage("RIGHT");
			handler.postDelayed(this, 1000);
		}
	};
	
	private Runnable fireRunnable = new Runnable() {
		public void run() {
			testFireInt++;
			piClient = new Client();
			piClient.writeMessage("FIRE!");
			handler.postDelayed(this, 3000);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* uses text input to manually store pi IP */
	public void setPiIP(View view) {
		EditText editText = (EditText) findViewById(R.id.url_text);
		piIP = editText.getText().toString();
	}
};