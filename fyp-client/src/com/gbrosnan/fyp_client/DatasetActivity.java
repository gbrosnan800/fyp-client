package com.gbrosnan.fyp_client;

import java.util.ArrayList;
import java.util.List;

import com.gbrosnan.objects.ExerciseRaw;
import com.gbrosnan.objects.SensorSample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DatasetActivity extends Activity implements OnClickListener, SensorEventListener {

	
	Spinner dropdown;
	TextView status;
	private Button btnStart, btnStop, btnSend;
	EditText textServer, txtUsername, txtLiftWeight, txtReps;
	private SensorManager sensorManager;
	private Sensor sensor;
	private boolean started = false;
    ExerciseRaw exerciseRaw;
    String exerciseAsJsonString;
    private List<SensorSample> sensorData;
	
	
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
		textServer = (EditText) findViewById(R.id.txtDetect_ip);
		txtUsername = (EditText) findViewById(R.id.txtDataset_username);
		txtLiftWeight = (EditText) findViewById(R.id.txtDataset_weight);
		txtReps = (EditText) findViewById(R.id.txtDataset_reps);
		
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		
		btnStop.setEnabled(false);
        btnSend.setEnabled(false);		
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
					status.setText("valid!");
				}			
				break;
				
			case R.id.btnDataset_stop:
				
				break;
			
			case R.id.btnDataset_send:

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
   
   
}
