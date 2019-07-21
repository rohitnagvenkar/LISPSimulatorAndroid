package com.rncorp.lispcompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class OFileListActivity extends Activity {

	ListView listview;
	Button buttonShow;
	EditText textviewOpenFile;
	String file_path = Environment.getExternalStorageDirectory() + "/LISP_APP/";
	String[] values;
	ArrayList<String> fileNamesAL = new ArrayList<String>();
	File directory = new File(file_path);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_openfile);
		listview = (ListView) findViewById(R.id.listView_ofile);
		buttonShow = (Button) findViewById(R.id.button_openfile);
		textviewOpenFile = (EditText) findViewById(R.id.editText_openfile);
		assignFileNamesToValues(directory,"");
		if(!values.equals(null))
		{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(OFileListActivity.this, android.R.layout.simple_list_item_1, values);
			listview.setAdapter(adapter);
		}
		
		buttonShow.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.button_openfile)
				{
					assignFileNamesToValues(directory,textviewOpenFile.getText().toString());
					ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(OFileListActivity.this, android.R.layout.simple_list_item_1, values);
					listview.setAdapter(adapter1);
				}
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String itemValue = (String) listview.getItemAtPosition(position);
				displayFileContent(directory, itemValue);
			}
		});
	}

	private void assignFileNamesToValues(File directory,String sw) {
		String name;
		fileNamesAL.clear();
		File[] fileNames = directory.listFiles();
		for (int i = 0; i < fileNames.length; i++) {
			name = fileNames[i].getName();
			if (name.endsWith(".txt") && name.startsWith(sw)) {
				fileNamesAL.add(name);
			}
		}
		values = fileNamesAL.toArray(new String[fileNamesAL.size()]);
	}

	private void displayFileContent(File directory, String name) {
		try {
			File file = new File(directory, name);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String current_line, file_content;
			StringBuilder buff = new StringBuilder();
			while ((current_line = br.readLine()) != null) {
				buff.append(current_line + "\n");
			}
			br.close();
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
