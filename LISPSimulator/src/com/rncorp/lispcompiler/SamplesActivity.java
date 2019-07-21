package com.rncorp.lispcompiler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SamplesActivity extends Activity {
	ListView listview;
	Button buttonShow;
	EditText textviewOpenFile;
	String[] values;
	ArrayList<String> txtFilesAL = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_samples);
		listview = (ListView) findViewById(R.id.listView_samples);
		buttonShow = (Button) findViewById(R.id.button_samples);
		textviewOpenFile = (EditText) findViewById(R.id.editText_samples);
		assetsTovalues("");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(SamplesActivity.this, android.R.layout.simple_list_item_1, values);
		listview.setAdapter(adapter);
		
		buttonShow.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.button_samples)
				{
					assetsTovalues(textviewOpenFile.getText().toString());
					ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SamplesActivity.this, android.R.layout.simple_list_item_1, values);
					listview.setAdapter(adapter1);
				}
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String itemValue = (String) listview.getItemAtPosition(position);
				displayFileContent(itemValue);
			}
		});

	}

	public void assetsTovalues(String sw) {
		try {
			txtFilesAL.clear();
			AssetManager am = getAssets();
			String[] names = am.list("Samples");
			for (String file : names) {
				if (file.endsWith(".txt") && file.startsWith(sw)) {
					txtFilesAL.add(file);
				}
			}
			values = txtFilesAL.toArray(new String[txtFilesAL.size()]);
		} catch (Exception ex) {
		}

	}

	private void displayFileContent(String name) {
		try {
			InputStream is = getAssets().open("Samples/"+name);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String current_line, file_content;
			StringBuilder buff = new StringBuilder();
			while ((current_line = br.readLine()) != null) {
				buff.append(current_line + "\n");
			}
			file_content = buff.toString();
			buff.setLength(0);
			Intent returnIntent = new Intent();
			returnIntent.putExtra("result", file_content);
			setResult(RESULT_OK, returnIntent);
			finish();
		} catch (Exception ex) {
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.back, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_back) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
