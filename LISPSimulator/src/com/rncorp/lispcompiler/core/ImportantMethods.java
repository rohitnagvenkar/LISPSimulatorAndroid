package com.rncorp.lispcompiler.core;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Locale;
import android.content.Context;

public class ImportantMethods {

	ArrayList<String> keyWordsAL = new ArrayList<String>();
	ArrayList<String> UDkeyWordsAL = new ArrayList<String>();
	ArrayList<String> variablesAL = new ArrayList<String>();
	Context context;
	String keyword_id, keyword_start_value, keyword_min_arguments, keyword_max_arguments;
	IOFileHandling iofh = new IOFileHandling();

	public ImportantMethods() {
		keywordInitialize();
	}

	public void keywordInitialize() {
		// (id, name, similar_names, min_arguments,
		// result_start_value,max_arguments)0,1,2,3,4,5
		keyWordsAL.clear();
		keyWordsAL.add("0," + "UDEFUN," + "," + "2," + "0.0," + "2");

		keyWordsAL.add("1," + "PLUS," + "+_plus," + "2," + "0.0," + "99");
		keyWordsAL.add("2," + "MINUS," + "-_minus," + "2," + "0.0," + "99");
		keyWordsAL.add("3," + "MULTIPLY," + "*_multiply," + "2," + "1.0," + "99");
		keyWordsAL.add("4," + "DIVIDE," + "/_divide," + "2," + "1.0," + "2");

		keyWordsAL.add("5," + "LT," + "<_lt," + "2," + "0.0," + "2");
		keyWordsAL.add("6," + "GT," + ">_gt," + "2," + "0.0," + "2");
		keyWordsAL.add("7," + "LE," + "<=_le," + "2," + "0.0," + "2");
		keyWordsAL.add("8," + "GE," + ">=_ge," + "2," + "0.0," + "2");

		keyWordsAL.add("9," + "IF," + "if," + "3," + "0.0," + "3");
		keyWordsAL.add("10," + "EQ," + "=_eq," + "2," + "0.0," + "2");
		keyWordsAL.add("11," + "SETQ," + "setq," + "2," + "0.0," + "2");
		keyWordsAL.add("12," + "WHEN," + "when," + "2," + "0.0," + "2");
		keyWordsAL.add("13," + "UNLESS," + "unless," + "2," + "0.0," + "2");
		keyWordsAL.add("14," + "DEFUN," + "defun," + "2," + "0.0," + "5");
		keyWordsAL.add("15," + "PRINT," + "print," + "1," + "0.0," + "1");
		keyWordsAL.add("16," + "FIRST," + "first_car," + "1," + "0.0," + "1");
		keyWordsAL.add("17," + "REST," + "rest_cdr," + "1," + "0.0," + "1");
		keyWordsAL.add("18," + "RETURN," + "return," + "0," + "0.0," + "0");
		keyWordsAL.add("19," + "LOOP," + "loop," + "2," + "0.0," + "3");
		
		
		keyWordsAL.add("20," + "NIL," + "nil," + "2," + "0.0," + "3");
	}

	public boolean isString(String token) {
		if (token.charAt(0) == '\'') {
			return true;
		}
		return false;
	}

	public boolean isWord(String token) {
		int c;
		for (int i = 0; i < token.length(); i++) {
			c = token.toLowerCase(Locale.ENGLISH).charAt(i);
			if (c >= 97 && c <= 122) {
				
			}else{
				return false;
			}
		}
		return true;
	}

	public boolean isKeyWord(String token) {
		String word = token.toLowerCase(Locale.US);
		for (int i = 0; i < keyWordsAL.size(); i++) {
			String data[] = keyWordsAL.get(i).split(",");
			String sub_data[] = data[2].split("_");
			for (int j = 0; j < sub_data.length; j++) {
				if (sub_data[j].toLowerCase(Locale.US).equals(word)) {
					keyword_id = data[0];
					keyword_min_arguments = data[3];
					keyword_start_value = data[4];
					keyword_max_arguments = data[5];
					return true;
				}
			}
		}
		return false;
	}

	public boolean isNumeric(String token) {
		try {
			int c;
			for (int i = 0; i < token.length(); i++) {
				c = token.charAt(i);
				if (!((c >= 48 && c <= 57) || c == 46)) {
					return false;
				}
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean isBoolean(String token) {
		if (token.equals("T") || token.equals("F")) {
			return true;
		}
		return false;
	}

	public boolean isVariable(String token) {
		if (isKeyWord(token) == false && isNumeric(token) == false && isBoolean(token) == false) {
			return true;
		}
		return false;
	}

	public String getKeywordData() {
		String data;
		data = keyword_id + "," + keyword_start_value + "," + keyword_min_arguments + "," + keyword_max_arguments;
		return data;
	}

	public boolean isUserDefinedFunction(String token) {
		UDkeyWordsAL.clear();
		try {
			String word = token.toLowerCase(Locale.US);
			String content = iofh.getFileContent("UDEFUN", "/LISP_APP/DEFUN/");
			String currentLine = "";
			BufferedReader br = new BufferedReader(new StringReader(content));
			while ((currentLine = br.readLine()) != null) {
				UDkeyWordsAL.add(currentLine);
			}
			for (int i = 0; i < UDkeyWordsAL.size(); i++) {
				String data[] = UDkeyWordsAL.get(i).split(",");
				String sub_data[] = data[2].split("_");
				for (int j = 0; j < sub_data.length; j++) {
					if (sub_data[j].toLowerCase(Locale.US).equals(word)) {
						keyword_id = data[0];
						keyword_min_arguments = data[3];
						keyword_start_value = data[4];
						keyword_max_arguments = data[5];
						return true;
					}
				}
			}
		} catch (Exception ex) {
		}
		return false;
	}

	public String argsTostring(ArrayList<String> argumentsAL) {
		String str = "(" + argumentsAL.get(0);
		for (int i = 1; i < argumentsAL.size(); i++) {
			str = str + "," + argumentsAL.get(i);
		}
		str = str + ")";
		return str;
	}

	public String expressiontoString(ArrayList<String> new_expressionAL) {
		String output = "(" + new_expressionAL.get(0);
		for (int i = 1; i < new_expressionAL.size(); i++) {
			output = output + "," + new_expressionAL.get(i);
		}
		output = output + ")";
		return output;
	}

	public String expressiontoStringspace(ArrayList<String> new_expressionAL) {
		String output = "(" + new_expressionAL.get(0);
		for (int i = 1; i < new_expressionAL.size(); i++) {
			output = output + " " + new_expressionAL.get(i);
		}
		output = output + ")";
		return output;
	}

	public void errorHandling(String error) {
		String errorF = iofh.getFileContent("ERRORS", "/LISP_APP/ERROR/");

		String newErrorF;
		if (errorF.equals("")) {
			newErrorF = "Error: " + error;
		} else {
			newErrorF = errorF + error;
		}
		iofh.saveFileContent("ERRORS", "/LISP_APP/ERROR/", newErrorF);
	}

	public ArrayList<String> getArgumentsAL(ArrayList<String> exprAL) {
		ArrayList<String> argsAL = new ArrayList<String>();
		for (int i = 1; i < exprAL.size(); i++) {
			argsAL.add(exprAL.get(i));
		}
		return argsAL;
	}

	public String getVariableValue(String variableName) {
		initializeVariable();
		for (int i = 0; i < variablesAL.size(); i++) {
			String str[] = variablesAL.get(i).split("=");
			if (str[0].equals(variableName)) {
				return str[1];
			}
		}

		errorHandling("The variable " + variableName + " is unbound.");
		return "nil";
	}

	public void initializeVariable() {
		try {
			String currentLine;
			variablesAL.clear();
			String variableFileContent = iofh.getFileContent("VARIABLES", "/LISP_APP/VARIABLES/");
			BufferedReader br = new BufferedReader(new StringReader(variableFileContent));
			while ((currentLine = br.readLine()) != null) {
				variablesAL.add(currentLine);
			}
		} catch (Exception ex) {
		}
	}

	public boolean isVariableInitialized(String variableName) {
		initializeVariable();
		for (int i = 0; i < variablesAL.size(); i++) {
			String str[] = variablesAL.get(i).split("=");
			if (str[0].equals(variableName)) {
				return true;
			}
		}
		return false;
	}

	public int getIdVariableAL(String variableName) {
		initializeVariable();
		// use only when variable is present
		for (int i = 0; i < variablesAL.size(); i++) {
			String str[] = variablesAL.get(i).split("=");
			if (str[0].equals(variableName)) {
				return i;
			}
		}
		return 0;
	}

	public boolean isVariableInitialized(String variableName, String variableValue) {
		initializeVariable();
		for (int i = 0; i < variablesAL.size(); i++) {
			String str[] = variablesAL.get(i).split("=");
			if (str[0].equals(variableName) && str[1].equals(variableValue)) {
				return true;
			}
		}
		return false;
	}

	public void replaceVariable(String variableName, String variableValue) {
		initializeVariable();
		
		
		String variableData = variableName + "=" + variableValue;
		int id = getIdVariableAL(variableName);
		variablesAL.set(id, variableData);
		String variableFile = "";
		variableFile = variablesAL.get(0);
		for (int i = 1; i < variablesAL.size(); i++) {
			variableFile = variableFile + "\n" + variablesAL.get(i);
		}
		iofh.saveFileContent("VARIABLES", "/LISP_APP/VARIABLES/", variableFile);
	}

	public void addVariable(String variableName, String variableValue) {
		initializeVariable();
		
		String variableData = variableName + "=" + variableValue;
		String variableFile = "";
		if (variablesAL.isEmpty()) {
			variableFile = variableData;
		} else {
			variableFile = variablesAL.get(0);
			for (int i = 1; i < variablesAL.size(); i++) {
				variableFile = variableFile + "\n" + variablesAL.get(i);
			}
			variableFile = variableFile + "\n" + variableData;
		}
		iofh.saveFileContent("VARIABLES", "/LISP_APP/VARIABLES/", variableFile);
	}

	public boolean isList(String expr) {
		String str = expr;
		if (expr.length() > 1) {
			if (str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')') {
				return true;
			}
		}
		return false;
	}

	public ArrayList<String> listToargs(String expr) {
		String str = expr;
		ArrayList<String> listargsAL = new ArrayList<String>();
		char ch;
		String token;
		StringBuilder buff = new StringBuilder();
		for (int i = 1; i < str.length(); i++) {
			ch = str.charAt(i);
			switch (ch) {
			case '(':
				if (buff.length() != 0) {
					token = buff.toString();
					listargsAL.add(token);
					buff.setLength(0);
				}
				break;
			case ')':
				if (buff.length() != 0) {
					token = buff.toString();
					listargsAL.add(token);
					buff.setLength(0);
				}
				break;
			default:
				if (!(Character.isWhitespace(ch))) {
					buff.append(ch);
				} else {
					token = buff.toString();
					if (!(token.length() == 0)) {
						listargsAL.add(token);
					}
					buff.setLength(0);
				}
				break;
			}
		}
		return listargsAL;
	}

	public ArrayList<String> getArgumentsfromString(String str) {
		String args[] = str.split(" ");
		ArrayList<String> argsAL = new ArrayList<String>();
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].replace("(", "");
			args[i] = args[i].replace(")", "");
			argsAL.add(args[i]);
		}
		return argsAL;
	}

}
