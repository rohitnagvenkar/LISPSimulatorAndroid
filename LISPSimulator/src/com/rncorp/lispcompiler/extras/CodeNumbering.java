package com.rncorp.lispcompiler.extras;

import android.widget.EditText;
import android.widget.TextView;

public class CodeNumbering {

	int previous_number = 1, new_number = 1;

	// int scrollValueY = 0;
	public void codeNumbering(EditText edittext_display,
			TextView textview_number) {
		previous_number = textview_number.getLineCount();
		new_number = edittext_display.getLineCount();
		// scrollValueY = edittext_display.getScrollY();
		if (previous_number < new_number) {
			while (previous_number != new_number) {
				++previous_number;
				textview_number.append("\n" + String.valueOf(previous_number));
				// textview_number.setScrollY(scrollValueY);
			}
		} else if (previous_number > new_number) {
			while (previous_number != new_number) {
				String str = textview_number.getText().toString();
				str = str.replace("\n" + String.valueOf(previous_number), "");
				textview_number.setText(str);
				// textview_number.setScrollY(scrollValueY);
				--previous_number;
			}
		}
	}

}
