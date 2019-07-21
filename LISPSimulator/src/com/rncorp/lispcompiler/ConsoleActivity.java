package com.rncorp.lispcompiler;

import com.rncorp.lispcompiler.core.IOFileHandling;
import com.rncorp.lispcompiler.core.ReadCode;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class ConsoleActivity extends Activity {

	TextView tConsole;
	TextView terror;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_console);
		tConsole = (TextView) findViewById(R.id.textview_console);
		terror = (TextView) findViewById(R.id.textview_error);
		tConsole.setMovementMethod(new ScrollingMovementMethod());
		
		String code = getIntent().getExtras().getString("Code_execute");
		ReadCode rc = new ReadCode(code);
		IOFileHandling iofh = new IOFileHandling();
		iofh.saveFileContent("LOOPING", "/LISP_APP/TEMP/", "");
		iofh.saveFileContent("ERRORS", "/LISP_APP/ERROR/", "");
		String output = rc.read();
		String errors = iofh.getFileContent("ERRORS", "/LISP_APP/ERROR/");
		if(errors.equals(""))
		{
			tConsole.setText(output);
		}else{
			//tConsole.setText(output+"         "+errors);
			terror.setText(errors);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lispcompiler, menu);
		return true;
	}
}
