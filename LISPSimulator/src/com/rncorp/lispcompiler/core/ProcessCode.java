package com.rncorp.lispcompiler.core;

import java.util.ArrayList;


public class ProcessCode {

	int k = 0;
	int keyword_id, keyword_min_arguments, keyword_max_arguments, numbersAL_count;
	double keyword_start_value;
	ArrayList<String> argumentsAL = new ArrayList<String>();
	ArrayList<String> local_variableAL = new ArrayList<String>();
	ArrayList<String> local_variable_valueAL = new ArrayList<String>();
	ImportantMethods im = new ImportantMethods();
	Primitives primitives = new Primitives();
	boolean expr_contains_variable = false;

	public String processExpression(ArrayList<String> expressionAL) {
		if (!expressionAL.isEmpty()) {
			String firstAtom = expressionAL.get(0);
			return processToken(firstAtom, expressionAL);
		} else {
			return "nil";
		}

	}

	public String processToken(String token, ArrayList<String> new_expressionAL) {
		boolean isKW = false,isUKW = false;
		if ((isKW = im.isKeyWord(token)) || (isUKW = im.isUserDefinedFunction(token))) {
			String data[] = im.getKeywordData().split(",");
			keyword_id = Integer.parseInt(data[0]);
			keyword_start_value = Double.parseDouble(data[1]);
			keyword_min_arguments = Integer.parseInt(data[2]);
			keyword_max_arguments = Integer.parseInt(data[3]);

			ArrayList<String> argumentsAL = im.getArgumentsAL(new_expressionAL);
			int argsSize = argumentsAL.size();
			if (argsSize >= keyword_min_arguments && argsSize <= keyword_max_arguments) {
				if(isKW)
				{
					return primitives.processKeyword(keyword_id, argumentsAL, keyword_start_value);
				}else if(isUKW){
					return primitives.processKeyword(keyword_id, new_expressionAL, keyword_start_value);
				}
			} else {
				im.errorHandling(token + " with odd number of args.");
				return "nil";
			}
		}
		im.errorHandling("Illegal argument in functor position: " + token + " in " + im.expressiontoStringspace(new_expressionAL));
		return "nil";
	}
}
