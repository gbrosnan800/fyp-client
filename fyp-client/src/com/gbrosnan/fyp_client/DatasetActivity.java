package com.gbrosnan.fyp_client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.gbrosnan.objects.ExerciseJsonSerializer;
import com.gbrosnan.objects.ExerciseRaw;
import com.gbrosnan.objects.SensorSample;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DatasetActivity extends Activity implements OnClickListener, SensorEventListener {
	
	Spinner dropdown;
	TextView status;
	private Button btnStart, btnStop, btnSend;
	EditText txtIpAddress, txtUsername, txtLiftWeight, txtReps;
	private SensorManager sensorManager;
	private Sensor sensor;
	private boolean started = false;
    ExerciseRaw exerciseRaw;
    String exerciseAsJsonString;
    private List<SensorSample> sensorData;
    PowerManager pm;
    PowerManager.WakeLock wl;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_dataset);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
			.permitAll().build();
			StrictMode.setThreadPolicy(policy);
	    }
		
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); 
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorData = new ArrayList<SensorSample>();
		
		dropdown = (Spinner) findViewById(R.id.dataset_spinner);
		String[] exercises = new String[]{"bicep_curl", "lat_raise", "shoulder_shrug", "back_fly"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exercises);
		dropdown.setAdapter(adapter);

		btnStart = (Button) findViewById(R.id.btnDataset_start);
		btnStop = (Button) findViewById(R.id.btnDataset_stop);
		btnSend = (Button) findViewById(R.id.btnDataset_send);
		status = (TextView) findViewById(R.id.lblDataset_status);
		txtIpAddress = (EditText) findViewById(R.id.txtDataset_ip);
		txtUsername = (EditText) findViewById(R.id.txtDataset_username);
		txtLiftWeight = (EditText) findViewById(R.id.txtDataset_weight);
		txtReps = (EditText) findViewById(R.id.txtDataset_reps);
		
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		
		btnStop.setEnabled(false);
        btnSend.setEnabled(false);	
        
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dataset, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
		  
    @Override
    public void onSensorChanged(SensorEvent event) {
    	
        if (started) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            long timestamp = System.currentTimeMillis();
            SensorSample sample = new SensorSample(timestamp, x, y, z);
            sensorData.add(sample);                            
        }	
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
   @Override
    protected void onPause() {
        super.onPause();
        if (started == true) {
            sensorManager.unregisterListener(this);
        }
    }
    
   @Override
   public void onClick(View v) {
   	
		switch (v.getId()) {
		
			case R.id.btnDataset_start:
				if(isInputValid()) {
					status.setText("Go after beep!");
					btnStart.setEnabled(false);
		            btnStop.setEnabled(false);
		            btnSend.setEnabled(false);
		            sensorData = new ArrayList<SensorSample>();
					started = true;
		            Handler handler = new Handler();
		            handler.postDelayed(new Runnable() {
		            	public void run() {
		            		wl.acquire();
		            		startSensor();
		            		btnStop.setEnabled(true);
		            	}            	
		            }, 4000);
				}			
				break;
				
			case R.id.btnDataset_stop:
				wl.release();
	            btnStart.setEnabled(true);
	            btnStop.setEnabled(false);
	            btnSend.setEnabled(true);
	            started = false;
	            sensorManager.unregisterListener(this);            	            
	            status.setText("Number of sensor samples: " + sensorData.size());
				break;
			
			case R.id.btnDataset_send:				
				createNewExerciseObject();	
				status.setText("Object created - sending to server...");
				uploadDataToServer();
				break;
				
			default:
				break;
				
		}
   }
   
   private boolean isInputValid() {
	   
	   String repNumInput = txtReps.getText().toString();
	   String weightInput = txtLiftWeight.getText().toString();
	   
	   if(isNumeric(weightInput) && isInteger(repNumInput) ) {
		   return true;
	   }
	   else {
		   status.setText("Weight must be numeric - Reps must be int!");
		   return false;
	   }
   }
   
   // Taken from StackOverflow == http://stackoverflow.com/questions/14206768/how-to-check-if-a-string-is-numeric
   private boolean isNumeric(String str)
   {
     return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
   }
   
   private boolean isInteger(String str) {
	   try {
		   Integer.parseInt(str);
		   return true;
		 } catch (NumberFormatException  e) {
		   return false;
		 }  
   }
   
   private void startSensor() { 
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);	
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
   }
   
   private void createNewExerciseObject() {
   	
	String username = txtUsername.getText().toString();
   	String exerciseName = dropdown.getSelectedItem().toString();
   	double weight = Double.parseDouble(txtLiftWeight.getText().toString());
   	int repCount = Integer.parseInt(txtReps.getText().toString());
   	Date date = getNewDate();
   	exerciseRaw = new ExerciseRaw(0, "dataset", username, exerciseName, weight, repCount, date, sensorData);
   
	Gson gson = new GsonBuilder().registerTypeAdapter(ExerciseRaw.class, new ExerciseJsonSerializer()).create();
	exerciseAsJsonString = gson.toJson(exerciseRaw);   	
   }
   
   public Date getNewDate() {
	   Calendar cal = Calendar.getInstance();
	   return cal.getTime();    	
   }
   
   private void uploadDataToServer() {
	   	   
	   String ipAdddress = txtIpAddress.getText().toString();	   
	   String uri = "http://" + ipAdddress  + "/fyp-server/rest/datasetitem";

	   HttpClient httpclient = new DefaultHttpClient();
	   HttpPost httppost = new HttpPost(uri);
	   String responseString = "";	   
	   try {
		   	InputStream jsonStream = new ByteArrayInputStream(exerciseAsJsonString.getBytes());
		   	InputStreamEntity reqEntity = new InputStreamEntity(jsonStream, -1);
	   		reqEntity.setContentType("binary/octet-stream");       	
	   		reqEntity.setChunked(true); // Send in multiple parts if needed
	   		httppost.setEntity(reqEntity);
	 	    HttpResponse response = httpclient.execute(httppost);	 	    
	 	    StringBuilder sb = new StringBuilder();
	 	    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
	 	    String line = null;
	 	    while ((line = reader.readLine()) != null) {
	 	        sb.append(line);
	 	    }
	 	    responseString = sb.toString();	   	 	    
	 	    status.setText(responseString);
	 	    
	    } catch (NoHttpResponseException e) {
			status.setText(e.toString());		
	    } catch (ConnectionClosedException e) {
			status.setText(e.toString());
		} catch (ConnectionPoolTimeoutException e) {
			status.setText(e.toString());
		} catch (IOException e) {
			status.setText(e.toString());
		} catch (Exception e) {
			status.setText(e.toString());
		} 

   }


   
   
}
