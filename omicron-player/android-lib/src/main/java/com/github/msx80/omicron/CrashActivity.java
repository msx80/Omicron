package com.github.msx80.omicron;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.*;
import android.view.ViewGroup.LayoutParams;

public class CrashActivity extends Activity {

	TextView error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		
		TextView error = new TextView(this);
		
		error.setText(getIntent().getStringExtra("error"));
		
		ScrollView scroll = new ScrollView(this);
		scroll.setBackgroundColor(0xFFFFFFFF);
		scroll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		scroll.addView(error);
		
		setContentView(scroll);
	}
}