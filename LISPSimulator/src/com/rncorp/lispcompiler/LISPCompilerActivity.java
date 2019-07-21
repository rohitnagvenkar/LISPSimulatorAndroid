package com.rncorp.lispcompiler;

import com.rncorp.lispcompiler.extras.CodeNumbering;
import com.rncorp.lispcompiler.extras.ColorParenthesis;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LISPCompilerActivity extends Activity {

	EditText eDisplay;
	TextView tNumber;
	boolean lineNumbering = true;
	String textSizeStr = "14";
	SharedPreferences spSettings;
	int maxLineCount = 15;
	ColorParenthesis cp;
	CodeNumbering cn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lispcompiler);
		eDisplay = (EditText) findViewById(R.id.editText_code);
		tNumber = (TextView) findViewById(R.id.textView_numbering);
		PreferenceManager.setDefaultValues(LISPCompilerActivity.this, R.xml.settings, true);

		eDisplay.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable e) {
				cp = new ColorParenthesis();
				cn = new CodeNumbering();
				if (eDisplay.getLineCount() >= maxLineCount) {
					String text = eDisplay.getText().toString();
					String newText = text.substring(0, text.lastIndexOf("\n"));
					eDisplay.setText("");
					eDisplay.append(newText);
				} else {
					if (lineNumbering == true) {
						tNumber.setText("1");
						cn.codeNumbering(eDisplay, tNumber);
					}
				}
				cp.colorParentheis(eDisplay, LISPCompilerActivity.this);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
		SharedPreferences spPause = getSharedPreferences("SharedFile", 0);
		SharedPreferences.Editor spePause = spPause.edit();
		spePause.putString("LastActivity", eDisplay.getText().toString());
		spePause.commit();
	}

	@Override
	public void onStart() {
		super.onStart();

		spSettings = PreferenceManager.getDefaultSharedPreferences(LISPCompilerActivity.this);
		lineNumbering = spSettings.getBoolean("lineNumberingSetting", true);
		textSizeStr = spSettings.getString("fontSizeSetting", "14");
		
		float size = Float.parseFloat(textSizeStr);
		eDisplay.setTextSize(size);
		tNumber.setTextSize(size);

		if (lineNumbering == false) {
			tNumber.setVisibility(View.INVISIBLE);
		} else {
			tNumber.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lispcompiler, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent settingsIntent = new Intent(LISPCompilerActivity.this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		case R.id.action_samples:
			Intent samplesIntent = new Intent(LISPCompilerActivity.this, SamplesActivity.class);
			startActivityForResult(samplesIntent, 1);
			return true;
		case R.id.action_documentation:
			Intent docsIntent = new Intent(LISPCompilerActivity.this, DocumentationActivity.class);
			LISPCompilerActivity.this.startActivity(docsIntent);
			return true;
		case R.id.action_lActivity:
			SharedPreferences spLast = getSharedPreferences("SharedFile", 0);
			String psLA = spLast.getString("LastActivity", null);
			if (psLA != null) {
				eDisplay.setText(psLA);
			}
			return true;
		case R.id.action_oFile:
			Intent openFileIntent = new Intent(LISPCompilerActivity.this, OFileListActivity.class);
			startActivityForResult(openFileIntent, 1);
			return true;
		case R.id.action_sFile:
			String code_save;
			code_save = eDisplay.getText().toString();
			Intent saveFileIntent = new Intent(LISPCompilerActivity.this, SFileActivity.class);
			saveFileIntent.putExtra("Code_save", code_save);
			LISPCompilerActivity.this.startActivity(saveFileIntent);
			return true;
		case R.id.action_clear:
			eDisplay.setText("");
			return true;
		case R.id.action_execute:
			String code_execute;
			code_execute = eDisplay.getText().toString();
			Intent consoleIntent = new Intent(LISPCompilerActivity.this, ConsoleActivity.class);
			consoleIntent.putExtra("Code_execute", code_execute);
			LISPCompilerActivity.this.startActivity(consoleIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				tNumber.setText("1");
				eDisplay.setText(data.getStringExtra("result"));
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}
}
