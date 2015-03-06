package com.gbrosnan.fyp_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button dataset, demoDetect, demoFeedback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		
		dataset = (Button) findViewById(R.id.btnDataset);
		demoDetect = (Button) findViewById(R.id.btnDemoExDetect);
		demoFeedback = (Button) findViewById(R.id.btnDemoFeedback);
		
		dataset.setOnClickListener(this);
		demoDetect.setOnClickListener(this);
		demoFeedback.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	public void onClick(View v) {
		
		switch (v.getId()) {
		
			case R.id.btnDataset:
				
				Intent intent = new Intent(this, DatasetActivity.class);
				startActivity(intent);
				break;
				
			case R.id.btnDemoExDetect:
				
				break;
			
			case R.id.btnDemoFeedback:
				
				break;
				
			default:
				break;
				
		}
		
	}
	

	
}
