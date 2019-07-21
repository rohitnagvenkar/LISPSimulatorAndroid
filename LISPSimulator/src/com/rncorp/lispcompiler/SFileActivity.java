package com.rncorp.lispcompiler;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SFileActivity extends Activity {

	EditText eFname;
	Button bSave,bcancel;
	String android_path_name, edittext_path, code;
	byte[] byte_path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sfile);
		android_path_name = Environment.getExternalStorageDirectory().getPath().toString();
		eFname = (EditText) findViewById(R.id.editText_fname);
		bSave = (Button) findViewById(R.id.button_fname);
		bcancel = (Button) findViewById(R.id.button_cancel);
		code = getIntent().getExtras().getString("Code_save");
		bSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					edittext_path = eFname.getText().toString();
					if (!edittext_path.contains(".txt")) {
						edittext_path = edittext_path + ".txt";
					}
					
					File direct = new File(android_path_name + "/LISP_APP/");
					File file = new File(direct, edittext_path);
					if (file.exists()) {
						file.delete();
					}
					FileOutputStream fos = new FileOutputStream(file);
					byte_path = new byte[code.length()];
					byte_path = code.getBytes();
					fos.write(byte_path);
					fos.flush();
					fos.close();
					finish();
				} catch (Exception ex) {
				}
			}
		});
		bcancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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
