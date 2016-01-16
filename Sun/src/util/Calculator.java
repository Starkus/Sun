package util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import client.Client;
import exception.NotFoundException;
import exception.SyntaxException;

public class Calculator {
	
	static Hashtable<String, Double> variables;
	
	public static double parse(String text, Hashtable<String, Double> vars) throws SyntaxException, ArithmeticException{
		variables = vars;
		variables.put("PI", Math.PI);
		
		Client.current.console = "";

		text = text.replaceAll("[\n ]", "");
		text = replaceVariables(text);	// Replace variables
		
		double res = 0;
		res = processLine(text);
		
		return res;
	}
	
	static double processLine(String line) throws SyntaxException, ArithmeticException{
		
		try {
			
			// try parenthesis
			try {
				return solveParenthesis(line);
			} 
			catch (NotFoundException e) {} 
			catch (ArithmeticException | SyntaxException e) {
				throw e;
			}

			line = replaceVariables(line);	// Replace variables
			line = line.replaceAll("[()]", "");
			
			// try add
			int i = line.indexOf('+');
			if (i > 0) {
				String[] ssum = {line.substring(0, i), line.substring(i+1)};
				return processLine(ssum[0]) + processLine(ssum[1]);
			}
			
			// try substract
			i = line.indexOf('-', 1);
			if (i > 0) {
				if (Character.isDigit(line.charAt(i-1))) {  // Check if minus isn't just for negativity
					String[] sres = {line.substring(0, i), line.substring(i+1)};
					return processLine(sres[0]) - processLine(sres[1]);
				}
			}
			
			// try mult
			i = line.indexOf('*');
			if (i > 0) {
				String[] smul = {line.substring(0, i), line.substring(i+1)};
				return processLine(smul[0]) * processLine(smul[1]);
			}
			
			// try div
			i = line.indexOf('/');
			if (i > 0) {
				String[] sdiv = {line.substring(0, i), line.substring(i+1)};
				double[] parsed = {processLine(sdiv[0]), processLine(sdiv[1])};
				if (parsed[1] == 0)
					throw new ArithmeticException("Can't divide by zero");
				return parsed[0] / parsed[1];
			}
			
			// try exp
			i = line.indexOf('^');
			if (i > 0) {
				String[] sdiv = {line.substring(0, i), line.substring(i+1)};
				return Math.pow(processLine(sdiv[0]), processLine(sdiv[1]));
			}
			
			// try rest
			i = line.indexOf('%');
			if (i > 0) {
				String[] sdiv = {line.substring(0, i), line.substring(i+1)};
				return processLine(sdiv[0]) % processLine(sdiv[1]);
			}
			
			return Double.parseDouble(line);
		
		} catch (Exception e) {
			Client.current.console += "\20\150\0\0EXCEPTION parsing " + line + '\n';
			throw e;
		}
	}
	
	
	private static String replaceVariables(String line) {

		Enumeration<String> vars = variables.keys();

		while (vars.hasMoreElements()) {
			String var = vars.nextElement();
			
			Double val = variables.get(var);
			if (line.contains(var)) {
				line = '\2' + line + '\3';
				String[] sp = line.split(var);
				
				String temp = "";
				for (int i = 0; i < sp.length; i++) {
					String s = sp[i];
					if (!temp.isEmpty()) {
						if (sp[i-1].isEmpty())
							temp += '*';
						else {
							char beforeVar = sp[i-1].charAt(sp[i-1].length()-1);
							if (Character.isDigit(beforeVar) || beforeVar == ')')
								temp += '*';
						}
						
						
						temp += "(" + val.doubleValue() + ")";
						
						
						if (!sp[i].isEmpty()) {
							char afterVar = sp[i].charAt(0);
							if (Character.isDigit(afterVar) || afterVar == '(')
								temp += '*';
						}
					}
					temp += s;
				}
				line = temp.substring(1, temp.length()-1);
			}
		}
		
		return line;
	}
	
	private static double solveParenthesis(String line) throws NotFoundException, ArithmeticException, SyntaxException {
		
		if (line.contains("(")) {
		
			int open = -1;
			int close = -1;
			int pcount = 0;
			
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				if (c == '(') {
					if (open == -1)
						open = i;
					pcount++;
				}
				else if (c == ')') {
					pcount--;
					if (pcount == 0 && open != -1) {
						close = i;
						break;
					}
				}
			}
			
			if (open != -1) {
				
				String beforePar = line.substring(0, open);
				String par = line.substring(open+1, close);
				String afterPar = line.substring(close+1, line.length());
				
				// Math functions with multiple args
				String[] args = par.split(",");
				
				// Three args
				if (args.length > 3)
					throw new SyntaxException("Too many arguments");
				
				// Two args
				if (args.length > 2)
					throw new SyntaxException("Too many arguments");
				
				if (beforePar.endsWith("pow")) {
					String funcResult = toString(Math.pow(processLine(args[0]), processLine(args[1])));
					String beforeFunc = beforePar.substring(0, beforePar.length()-3);
					return processLine(beforeFunc + funcResult + afterPar);
				}
				
				if (beforePar.endsWith("assign")) {
					variables.put(args[0], processLine(args[1]));
					String beforeFunc = beforePar.substring(0, beforePar.length()-6);
					return processLine(beforeFunc + " " + afterPar);
				}
	
				
				// Math functions with single arg
				if (args.length > 1)
					throw new SyntaxException("Too many arguments");
				
				double parResult = processLine(par);
				

				if (line.split("[a-z]").length > 1) {  //  If there are letters, check for functions
				
					if (beforePar.endsWith("sqrt")) {
						if (parResult < 0) throw new ArithmeticException("Negative value under square root");
						
						String funcResult = toString(Math.sqrt(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-4);
						return processLine(beforeFunc + funcResult + afterPar);
					}
					if (beforePar.endsWith("cbrt")) {
						String funcResult = toString(Math.cbrt(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-4);
						return processLine(beforeFunc + funcResult + afterPar);
					}
		
					if (beforePar.endsWith("sin")) {
						String funcResult = toString(Math.sin(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-3);
						return processLine(beforeFunc + funcResult + afterPar);
					}
					if (beforePar.endsWith("cos")) {
						String funcResult = toString(Math.cos(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-3);
						return processLine(beforeFunc + funcResult + afterPar);
					}
					if (beforePar.endsWith("tan")) {
						String funcResult = toString(Math.tan(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-3);
						return processLine(beforeFunc + funcResult + afterPar);
					}
		
					if (beforePar.endsWith("radians")) {
						String funcResult = toString(Math.toRadians(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-7);
						return processLine(beforeFunc + funcResult + afterPar);
					}
					if (beforePar.endsWith("degrees")) {
						String funcResult = toString(Math.toDegrees(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-7);
						return processLine(beforeFunc + funcResult + afterPar);
					}
					
					if (beforePar.endsWith("int")) {
						String funcResult = toString((int) parResult);
						String beforeFunc = beforePar.substring(0, beforePar.length()-3);
						return processLine(beforeFunc + funcResult + afterPar);
					}
		
					if (beforePar.endsWith("floor")) {
						String funcResult = toString(Math.floor(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-5);
						return processLine(beforeFunc + funcResult + afterPar);
					}
					if (beforePar.endsWith("ceil")) {
						String funcResult = toString(Math.ceil(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-4);
						return processLine(beforeFunc + funcResult + afterPar);
					}
					if (beforePar.endsWith("round")) {
						String funcResult = toString(Math.round(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-5);
						return processLine(beforeFunc + funcResult + afterPar);
					}
					
					if (beforePar.endsWith("abs")) {
						String funcResult = toString(Math.abs(parResult));
						String beforeFunc = beforePar.substring(0, beforePar.length()-3);
						return processLine(beforeFunc + funcResult + afterPar);
					}
					
					if (beforePar.endsWith("rand")) {
						Random random = new Random((long) (Double.hashCode(parResult*parResult)));
						String funcResult = toString(random.nextDouble());
						String beforeFunc = beforePar.substring(0, beforePar.length()-4);
						return processLine(beforeFunc + funcResult + afterPar);
					}
				}
				
				// Else just solve the parenthesis
				String parR = toString(parResult);
				if (beforePar.endsWith("-") && parR.startsWith("-"))
					return processLine(beforePar.substring(0, beforePar.length()-1) + "+" +
							parR.substring(1) + afterPar);
				return processLine(
						beforePar + parR + afterPar);
			}
		}
		throw new NotFoundException("No parenthesis found");
	}
	
	
	private static String toString(double d) {
		DecimalFormat df = new DecimalFormat("#");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		dfs.setGroupingSeparator(' ');
		df.setDecimalFormatSymbols(dfs);
		df.setMaximumFractionDigits(8);
		return df.format(d);
	}
}
