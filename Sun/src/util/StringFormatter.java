package util;

public class StringFormatter {

	public static String format(String str) {
		
		String res = "\2" + str + "\3";
		
		String normalColor = "\20\200\200\200";
		
		String[] mathFuncs = "sqrt,abs,pow,sin,cos,tan,asin,acos,atan,asinh,acosh,int,gamma,radians,degrees,floor,ceil,round,rand".split(",");
		String mathFColor = "\20\200\0\200";
		
		String[] literals = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "\\.", ","};
		String litColor = "\20\0\100\100";
		
		String[] variables = {"x", "y", "z", "PI", "TIME"};
		String varColor = "\20\160\160\0";
		
		

		for (String func : mathFuncs) {
			String temp = "";
			String[] spl = res.split(func);
			if (spl.length > 1) {
				for (String s : spl) {
					if (!temp.isEmpty()) {
						temp += mathFColor + func + normalColor;
					}
					temp += s;
				}
				res = temp;
			}
		}
		for (String lit : literals) {
			String temp = "";
			String[] spl = res.split(lit);
			if (spl.length > 1) {
				for (String s : spl) {
					if (!temp.isEmpty()) {
						lit = lit.substring(lit.length()-1, lit.length());
						temp += litColor + lit + normalColor;
					}
					temp += s;
				}
				res = temp;
			}
		}
		for (String var : variables) {
			String temp = "";
			String[] spl = res.split(var);
			if (spl.length > 1) {
				for (String s : spl) {
					if (!temp.isEmpty()) {
						temp += varColor + var + normalColor;
					}
					temp += s;
				}
				res = temp;
			}
		}
		
		return res.substring(1, res.length()-1);
	}
}
