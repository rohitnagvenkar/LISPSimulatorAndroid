package com.rncorp.lispcompiler.extras;

import java.util.ArrayList;

import com.rncorp.lispcompiler.LISPCompilerActivity;
import com.rncorp.lispcompiler.core.ImportantMethods;

import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

public class ColorParenthesis {

	ArrayList<Integer> parenthesis_count_AL = new ArrayList<Integer>();
	char ch;
	ImportantMethods im = new ImportantMethods();

	public void colorParentheis(EditText edittext_display, LISPCompilerActivity lispCompilerActivity) {
		String code = edittext_display.getText().toString();
		Spannable spannable = edittext_display.getText();
		StringBuilder buff = new StringBuilder();
		String token;
		parenthesis_count_AL.clear();
		for (int i = 0; i < code.length(); i++) {
			ch = code.charAt(i);
			switch (ch) {
			case '(':
				parenthesis_count_AL.add(i);
				spannable.setSpan(new ForegroundColorSpan(Color.RED), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				buff.setLength(0);
				break;
			case ')':
				if (parenthesis_count_AL.size() > 0) {
					int pq = parenthesis_count_AL.get(parenthesis_count_AL.size() - 1);
					spannable.setSpan(new ForegroundColorSpan(Color.GREEN), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					spannable.setSpan(new ForegroundColorSpan(Color.GREEN), pq, pq + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					parenthesis_count_AL.remove(parenthesis_count_AL.size() - 1);
				} else {
					spannable.setSpan(new ForegroundColorSpan(Color.RED), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				buff.setLength(0);
				break;
			default:
				if (!(Character.isWhitespace(ch))) {
					buff.append(ch);
				} else {
					token = buff.toString();
					if (!(token.isEmpty())) {
						if (im.isKeyWord(token) || im.isUserDefinedFunction(token)) {
							spannable.setSpan(new ForegroundColorSpan(Color.MAGENTA), i - token.length(), i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}else{
							spannable.setSpan(new ForegroundColorSpan(Color.BLACK), i - token.length(), i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
					}
					buff.setLength(0);
				}
				break;
			}
		}
	}
}
