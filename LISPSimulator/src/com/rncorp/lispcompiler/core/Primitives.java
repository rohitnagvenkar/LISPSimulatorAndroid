package com.rncorp.lispcompiler.core;

import java.util.ArrayList;

public class Primitives {

	final int UDEFUN = 0, PLUS = 1, MINUS = 2, MULTIPLY = 3, DIVIDE = 4, LT = 5, GT = 6, LE = 7, GE = 8, IF = 9, EQ = 10, SETQ = 11, WHEN = 12, UNLESS = 13,
			DEFUN = 14, PRINT = 15, FIRST = 16, REST = 17, RETURN = 18, LOOP = 19;

	boolean set_compute_bool = true, set_divide_bool = true;
	IOFileHandling iofh = new IOFileHandling();
	ImportantMethods im = new ImportantMethods();

	public String processKeyword(int idNumber, ArrayList<String> argumentsAL, double start_value) {
		switch (idNumber) {
		case PLUS:
			return numCompute('+', argumentsAL, start_value);
		case MINUS:
			return numCompute('-', argumentsAL, start_value);
		case MULTIPLY:
			return numCompute('*', argumentsAL, start_value);
		case DIVIDE:
			return numCompute('/', argumentsAL, start_value);
		case EQ:
			return numCompare('=', argumentsAL);
		case LT:
			return numCompare('<', argumentsAL);
		case GT:
			return numCompare('>', argumentsAL);
		case LE:
			return numCompare('L', argumentsAL);
		case GE:
			return numCompare('G', argumentsAL);
		case IF:
			return compute_IF_Statement(argumentsAL);
		case WHEN:
			return when_compute(argumentsAL);
		case UNLESS:
			return unless_compute(argumentsAL);
		case DEFUN:
			return compute_defun(argumentsAL);
		case UDEFUN:
			return compute_user_defun(argumentsAL);
		case PRINT:
			// done
			return print_argument(argumentsAL);
		case SETQ:
			// done
			return createVariable(argumentsAL);
		case FIRST:
			// done
			return findFirstRest(argumentsAL, 'f');
		case REST:
			// done
			return findFirstRest(argumentsAL, 'r');
		case RETURN:
			// done
			return "#RET";
		case LOOP:
			// done
			return compute_loop(argumentsAL);
		default:
			return "";
		}

	}

	private String compute_loop(ArrayList<String> argumentsAL) {
		if (argumentsAL.get(0).equals("nil")) {
			iofh.appendFileContent("LOOPING", "/LISP_APP/TEMP/", argumentsAL.get(1));
			return "#LOOP";
		} else if (argumentsAL.get(0).equals("#RET")) {
			return "#STOP";
		} else {
			return "nil";
		}
	}

	private String findFirstRest(ArrayList<String> argumentsAL, char c) {
		boolean isList = false, isVariable = false;
		String varValue = "";
		ArrayList<String> listArgsAL = new ArrayList<String>();
		if (im.isList(argumentsAL.get(0))) {
			// just list
			listArgsAL.clear();
			isList = true;
			listArgsAL = im.listToargs(argumentsAL.get(0));
		} else {

			if (im.isVariableInitialized(argumentsAL.get(0))) {
				// variable
				varValue = im.getVariableValue(argumentsAL.get(0));
				isVariable = true;
				if (im.isList(varValue)) {
					// variable and list
					listArgsAL.clear();
					isList = true;
					listArgsAL = im.listToargs(varValue);
				} else {
					isList = false;
				}

			}
		}
		switch (c) {
		case 'f':
			if (isList == true) {
				return listArgsAL.get(0);
			} else if (isVariable == true) {
				return varValue;
			} else {
				return argumentsAL.get(0);
			}
		case 'r':
			if (listArgsAL.size() == 1 || listArgsAL.isEmpty()) {
				return "nil";
			} else if (!listArgsAL.isEmpty() && listArgsAL.size() > 1) {
				String restStr = "(" + listArgsAL.get(1);
				for (int i = 2; i < listArgsAL.size(); i++) {
					restStr = restStr + " " + listArgsAL.get(i);
				}
				restStr = restStr + ")";
				return restStr;
			}
		}
		return "nil";
	}

	private String createVariable(ArrayList<String> argumentsAL) {
		String vName = argumentsAL.get(0);
		String vValue = argumentsAL.get(1);
		
		if(vValue.charAt(0) == '\'')
		{
			vValue = vValue.substring(1,vValue.length());
		}else{
			if(im.isWord(vValue)){
				if(im.isVariable(vValue)){
					vValue  = im.getVariableValue(vValue);
				}
			}else{
				
			}
		}
		
		if (im.isVariableInitialized(vName)) {
			// replace variable
			im.replaceVariable(vName, vValue);
		} else {
			// add variable
			im.addVariable(vName, vValue);
		}
		return vValue;
	}

	private String print_argument(ArrayList<String> argumentsAL) {
		String str[] = argumentsAL.get(0).split("'");
		if (str.length > 1) {
			// String
			return str[1];
		} else {
			// variable
			if(im.isVariableInitialized(str[0]))
			{
				return im.getVariableValue(str[0]);
			}else{
				return str[0];
			}
			
		}

	}

	private String compute_user_defun(ArrayList<String> argumentsAL) {
		try {
			String fn_name = argumentsAL.get(0);
			String fn_contents = iofh.getFileContent(fn_name, "/LISP_APP/DEFUN/");
			String temp[] = fn_contents.split("/");
			String fn_argStr = temp[0];
			String fn_code = "";
			if (temp.length > 1) {
				fn_code = temp[1];
			}
			fn_argStr = fn_argStr.replace("(", "");
			fn_argStr = fn_argStr.replace(")", "");
			String fn_arg[] = fn_argStr.split(" ");
			for (int i = 0; i < fn_arg.length; i++) {
				fn_code = fn_code.replaceAll(fn_arg[i], argumentsAL.get(i+1));
			}
			ReadCode rc = new ReadCode(fn_code);
			return rc.read();
		} catch (Exception ex) {
			im.errorHandling("User Defined Function has error ");
			return "nil";
		}

	}

	private String compute_defun(ArrayList<String> argumentsAL) {
		String file_data = iofh.getFileContent("UDEFUN", "/LISP_APP/DEFUN/");
		String function_name = argumentsAL.get(0);
		ArrayList<String> funargsAL = im.getArgumentsfromString(argumentsAL.get(1));
		String kwArgs_count = String.valueOf(funargsAL.size());
		String kwData = "0,UDEFUN," + function_name + "," + kwArgs_count + ",0.0," + kwArgs_count;
		if (!file_data.equals("")) {
			file_data = kwData;
		} else {
			file_data = file_data + kwData;
		}
		String code = argumentsAL.get(1);
		if (argumentsAL.size() > 2) {
			for (int k = 2; k < argumentsAL.size(); k++) {
				code = code + "/" + argumentsAL.get(k);
			}
		}
		iofh.saveFileContent("UDEFUN", "/LISP_APP/DEFUN/", file_data);
		iofh.saveFileContent(function_name, "/LISP_APP/DEFUN/", code);
		return function_name;
	}

	private String numCompute(char c, ArrayList<String> numbersAL, double start_value) {
		double num=0;
		String tempStr;
		double compute = start_value;
		
		for(int i=0;i<numbersAL.size();i++)
		{
			if(im.isWord(numbersAL.get(i)))
			{
				if(im.isVariable(numbersAL.get(i)))
				{
					tempStr = im.getVariableValue(numbersAL.get(i));
					if(im.isWord(tempStr))
					{
						im.errorHandling("Invalid parameter: "+numbersAL.get(i));
						return "nil";
					}else if(im.isNumeric(tempStr))
					{
						num = Double.parseDouble(tempStr);
					}
					else{
						im.errorHandling("Invalid parameter: "+numbersAL.get(i));
						return "nil";
					}
				}else{
					im.errorHandling("Invalid parameter: "+numbersAL.get(i));
					return "nil";
				}
			}else if(im.isNumeric(numbersAL.get(i)))
			{
				num = Double.parseDouble(numbersAL.get(i));
			}else{
				im.errorHandling("Invalid parameter: "+numbersAL.get(i));
				return "nil";
			}
			
			switch (c) {
			case '+':
				compute += num;
				break;
			case '-':
				compute -= num;
				break;
			case '*':
				compute *= num;
				break;
			case '/':
				if(set_divide_bool)
				{
					compute = num * num;
				}
				compute /= num;
				set_divide_bool = false;
				
				break;
			}
		}
		
		return String.valueOf(compute);
	}

	private String numCompare(char c, ArrayList<String> numbersAL) {
		double num1 = 0.0, num2 = 0.0;
		ArrayList<Double> tempAL = new ArrayList<Double>();
		
		for(int i=0;i<numbersAL.size();i++)
		{
			if(im.isWord(numbersAL.get(i)))
			{
				if(im.isVariable(numbersAL.get(i)))
				{
					String tempStr = im.getVariableValue(numbersAL.get(i));
					if(im.isWord(tempStr))
					{
						im.errorHandling("Invalid parameter: "+numbersAL.get(i));
						return "nil";
					}else if(im.isNumeric(tempStr))
					{
						tempAL.add(Double.parseDouble(tempStr));
					}
					else{
						im.errorHandling("Invalid parameter: "+numbersAL.get(i));
						return "nil";
					}
				}
			}else if(im.isNumeric(numbersAL.get(i)))
			{
				tempAL.add(Double.parseDouble(numbersAL.get(i)));
			}else{
				im.errorHandling("Invalid Parameter: "+numbersAL.get(i));
				return "nil";
			}
		}
		
		num1 = tempAL.get(0);
		num2 = tempAL.get(1);
		
		switch (c) {
		case '<':
			if (num1 < num2) {
				return "T";
			}
			break;
		case '>':
			if (num1 > num2) {
				return "T";
			}
			break;
		case 'L':
			if (num1 <= num2) {
				return "T";
			}
			break;
		case 'G':
			if (num1 >= num2) {
				return "T";
			}
			break;
		case '=':
			if (num1 == num2) {
				return "T";
			}
			break;
		}
		return "F";
	}

	private String compute_IF_Statement(ArrayList<String> argumentsAL) {
		try {
			if (argumentsAL.get(0).equals("T")) {
				return argumentsAL.get(1);
			} else if (argumentsAL.get(0).equals("F")) {
				return argumentsAL.get(2);
			} else {
				return im.argsTostring(argumentsAL);
			}
		} catch (Exception ex) {
			return "missing argument";
		}
	}

	private String when_compute(ArrayList<String> argumentsAL) {
		if (argumentsAL.get(0).equals("T")) {
			return argumentsAL.get(1);
		}
		return "nil";
	}

	private String unless_compute(ArrayList<String> argumentsAL) {
		if (argumentsAL.get(0).equals("T")) {
			return "nil";
		}
		return argumentsAL.get(1);
	}

}
