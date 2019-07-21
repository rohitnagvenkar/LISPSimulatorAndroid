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

public class DocumentationActivity extends Activity {
	Button buttonShow;
	EditText textviewDocm;
	ListView listview;
	String[] values;
	ArrayList<String> txtFilesAL = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_documentation);
		listview = (ListView) findViewById(R.id.listView_documentation);
		buttonShow = (Button) findViewById(R.id.button_documentation);
		textviewDocm = (EditText) findViewById(R.id.editText_documentation);
		searchassetsTovalues("");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(DocumentationActivity.this, android.R.layout.simple_list_item_1, values);
		listview.setAdapter(adapter);
		
		buttonShow.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.button_documentation)
				{
					searchassetsTovalues(textviewDocm.getText().toString());
					ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(DocumentationActivity.this, android.R.layout.simple_list_item_1, values);
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
	
	public void searchassetsTovalues(String value) {
		try {
			txtFilesAL.clear();
			AssetManager am = getAssets();
			String[] names = am.list("Documentation");
			for (String file : names) {
				if (file.endsWith(".txt") && file.startsWith(value)) {
					txtFilesAL.add(file);
				}
			}
			values = txtFilesAL.toArray(new String[txtFilesAL.size()]);
		} catch (Exception ex) {
		}
	}

	private void displayFileContent(String name) {
		try {
			InputStream is = getAssets().open("Documentation/"+name);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String current_line, file_content;
			StringBuilder buff = new StringBuilder();
			while ((current_line = br.readLine()) != null) {
				buff.append(current_line + "\n");
			}
			file_content = buff.toString();
			buff.setLength(0);
			Intent displayDocmIntent = new Intent(DocumentationActivity.this, DisplayDocumentationActivity.class);
			displayDocmIntent.putExtra("data_docm", file_content);
			DocumentationActivity.this.startActivity(displayDocmIntent);
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
		if(item.getItemId() == R.id.action_back)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
