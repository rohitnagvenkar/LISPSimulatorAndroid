package com.rncorp.lispcompiler.core;

import java.util.ArrayList;

public class ReadCode {

	String code, output;
	ArrayList<String> codeDataAL = new ArrayList<String>();
	ArrayList<String> NcodeDataAL = new ArrayList<String>();
	ProcessCode pc = new ProcessCode();
	IOFileHandling iofh = new IOFileHandling();

	public ReadCode(String code) {
		this.code = code;
	}

	public String read() {
		char ch;
		int pos, siz, count = 0, loop_pos = 0, i = 0;
		String token, temp;
		StringBuilder buff = new StringBuilder();
		boolean isQuote = false, isLoop = false, isDefun = false;
		buff.setLength(0);
		for (i = 0; i < code.length(); i++) {
			ch = code.charAt(i);
			isLoop = false;
			switch (ch) {
			case '(':
				if (buff.length() != 0) {
					token = buff.toString();
					codeDataAL.add(token);
					buff.setLength(0);
				}
				if (i > 0 && code.charAt(i - 1) == '\'') {
					codeDataAL.remove(codeDataAL.size() - 1);
					isQuote = true;
				}
				codeDataAL.add("(");
				break;
			case ')':
				count = 0;
				if (buff.length() != 0) {
					token = buff.toString();
					codeDataAL.add(token);
					buff.setLength(0);
				}
				pos = codeDataAL.lastIndexOf("(");
				siz = codeDataAL.size();
				for (int j = pos + 1; j < siz; j++) {
					NcodeDataAL.add(codeDataAL.get(j));
					count += 1;
				}
				if(NcodeDataAL.contains("defun"))
				{
					isDefun = false;
				}
				if (isDefun == true) {
					codeDataAL.add(pos, exprTostring(NcodeDataAL));
				} else if (isQuote == true) {
					codeDataAL.add(pos, exprTostring(NcodeDataAL));
					isQuote = false;
				} else {
					temp = pc.processExpression(NcodeDataAL);
					if (temp.equals("#LOOP")) {
						i = loop_pos;
						buff.setLength(0);
						count = 0;
						codeDataAL.clear();
						isLoop = true;
					} else if (temp.equals("#STOP")) {
						String strTempp = iofh.getFileContent("LOOPING", "/LISP_APP/TEMP/");
						if(strTempp.equals(""))
						{
							pos=0;
							codeDataAL.clear();
							isLoop=true;
							codeDataAL.add("nil");
						}else{
							codeDataAL.add(pos, strTempp);
						}
					} else {
						codeDataAL.add(pos, temp);
					}
				}
				if (isLoop == false) {
					for (int k = 0; k <= count; k++) {
						codeDataAL.remove(pos + 1);
					}
				}
				output = codeDataAL.toString();
				NcodeDataAL.clear();
				break;
			default:
				if (!(Character.isWhitespace(ch))) {
					buff.append(ch);
				} else {
					token = buff.toString();
					if (!(token.isEmpty())) {
						codeDataAL.add(token);
						if (token.equals("loop")) {
							loop_pos = i - 6;
						}
						if (codeDataAL.contains("defun")) {
							isDefun = true;
						} else {
							isDefun = false;
						}
					}
					buff.setLength(0);
				}
				break;
			}
		}
		return output;
	}

	public String exprTostring(ArrayList<String> exprAL) {
		String exprStr = "(" + exprAL.get(0);
		for (int i = 1; i < exprAL.size(); i++) {
			exprStr = exprStr + " " + exprAL.get(i);
		}
		exprStr = exprStr + ")";
		return exprStr;
	}

}
