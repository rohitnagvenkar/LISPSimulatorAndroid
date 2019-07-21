package com.rncorp.lispcompiler;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayDocumentationActivity extends Activity {
	String data;
	TextView textviewDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_documentation);
		textviewDisplay = (TextView)findViewById(R.id.textview_displayDocm);
		data = getIntent().getExtras().getString("data_docm");
		textviewDisplay.setText(data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.back, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_back)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
