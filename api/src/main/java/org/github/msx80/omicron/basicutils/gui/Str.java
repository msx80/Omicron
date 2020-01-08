package org.github.msx80.omicron.basicutils.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Str {
	/**
	 * Metodo di utilita' per verificare se una stringa e' vuota
	 * 
	 * @param s
	 *            la stringa da testare
	 * @return true se e' diversa da null e diversa da "", false altrimenti
	 */
	public static boolean isEmpty(String s) {
		return (s == null) || (s.equals(""));
	}
	
	/**
	 * Effettua l'escape di tutti i caratteri di una stringa in formato %HH dove HH e' il codice
	 * esadecimale del carattere
	 * 
	 * @param in
	 *            la stringa di cui effettuare l'escape
	 * @return la stringa con l'escape
	 */
	public static String escapeAll(String in) {
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < in.length(); i++) {
			res.append('%');
			res.append(Integer.toString((int) in.charAt(i), 16));
		}
		return res.toString();
	}
	
	/**
	 * Effettua una codifica di una stringa utile per essere usata nel Javascript. La stringa di
	 * ritorno e' un'espressione Javascript che costruisce la stringa
	 * 
	 * @param in
	 * @return
	 */
	public static String superEscape(String in) {
		StringBuffer res = new StringBuffer("String.fromCharCode(");
		for (int i = 0; i < in.length(); i++) {
			
			res.append((int) in.charAt(i));
			if (i < in.length() - 1) {
				res.append(',');
			}
			
		}
		res.append(")");
		return res.toString();
	}
	
	/**
	 * Unisce una lista in un'unica stringa usando il dato separatore
	 * 
	 * @param l
	 * @param separatore
	 * @return
	 */
	public static String join(List<?> l, String separatore) {
		String res = "";
		for (int i = 0; i < l.size(); i++) {
			if (i > 0)
				res += separatore;
			res += l.get(i);
		}
		return res;
	}
	
	public static String join(String[] l, String separatore) {
		String res = "";
		for (int i = 0; i < l.length; i++) {
			if (i > 0)
				res += separatore;
			res += l[i];
		}
		return res;
	}
	
	public static String filtraSoloAlfanumerico(String in) {
		String res = "";
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			if ((c >= '0') && (c <= '9')) {
				res += c;
			} else if ((c >= 'a') && (c <= 'z')) {
				res += c;
			} else if ((c >= 'A') && (c <= 'Z')) {
				res += c;
			}
		}
		return res;
	}
	
	public static boolean isNumerico(String numStr) {
		if (!isEmpty(numStr)) {
			boolean isNumerico = true;
			try {
				Double.parseDouble(numStr);
			} catch (NumberFormatException e) {
				isNumerico = false;
			}
			return isNumerico;
		} else {
			return false;
		}
	}
	
	public static String dopad(Object valo, int finalSize, boolean right, char padchar) {
		String val = (valo == null) ? "" : valo.toString();
		
		if (finalSize <= val.length()) {
			return val.substring(0, finalSize);
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < (finalSize - val.length()); i++) {
			sb.append(padchar);
		}
		if (right)
			return val + sb.toString();
		else
			return sb.toString() + val;
	}
	
	public static String rpad(Object val, int finalSize, char padchar) {
		return dopad(val, finalSize, true, padchar);
	}
	
	public static String rpad(Object val, int finalSize) {
		return dopad(val, finalSize, true, ' ');
	}
	
	public static String lpad(Object val, int finalSize, char padchar) {
		return dopad(val, finalSize, false, padchar);
	}
	
	public static String lpad(Object val, int finalSize) {
		return dopad(val, finalSize, false, ' ');
	}
	
	public static String nvl(Object s, String alt) {
		if ((s == null) || ("".equals(s.toString())))
			return alt;
		else
			return s.toString();
	}
	
	public static String ltrim(String s, char c) {
		if (isEmpty(s)) {
			return s;
		}
		while ((s.length() > 0) && (s.charAt(0) == c)) {
			s = s.substring(1);
		}
		return s;
	}
	
	public static String rtrim(String s, char c) {
		if (isEmpty(s)) {
			return s;
		}
		int i = s.length() - 1;
		while ((s.length() > 0) && (s.charAt(i) == c)) {
			s = s.substring(0, i);
			i--;
		}
		
		return s;
		
	}
	
	public static boolean dataPosterioreOggi(String strData) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date data = sdf.parse(strData);
		Date oggi = new Date(System.currentTimeMillis());
		return data.after(oggi);
	}
	
	public static boolean dataCompare(String strDataDa, String strDataA) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dataDa = sdf.parse(strDataDa);
		Date dataA = sdf.parse(strDataA);
		return dataDa.after(dataA);
	}
	
	public static boolean cmp(String s1, String s2) {
		if (isEmpty(s1)) {
			return isEmpty(s2);
		} else {
			return s1.equals(s2);
		}
	}
	
	/**
	 * Ritorna la parte di una stringa compresa tra la prima occorrenza di <code>token1</code> se
	 * <code>first1</code> true, altrimenti l'ultima occorrenza di <code>token1</code>, e la prima
	 * occorrenza di <code>token2</code> se <code>first2</code> e' true, altrimenti l'ultima
	 * occorrenza di <code>token2</code>, occorrenze escluse. Se almeno un'occorrenza non viene
	 * trovata allora viene restituita la stringa vuota.
	 * 
	 * @param in,
	 *            stringa da cui fare l'estrazione
	 * @param token1,
	 *            prima stringa da escludere
	 * @param token2,
	 *            seconda stringa da escludere
	 * @param first1,
	 *            indica se considerare la prima o l'ultima occorrenza di token1
	 * @param first2,
	 *            indica se considerare la prima o l'ultima occorrenza di token1
	 * @return
	 */
	public static String getStringBetween(String in, String token1, String token2, boolean first1, boolean first2) {
		
		if (Str.isEmpty(in) || Str.isEmpty(token1) || Str.isEmpty(token2)) {
			return "";
		}
		
		int index1 = (first1 ? in.indexOf(token1) : in.lastIndexOf(token1));
		int index2 = (first2 ? in.indexOf(token2) : in.lastIndexOf(token2));
		
		if (index1 == -1 || index2 == -1) {
			return "";
		} else {
			index1 += token1.length();
		}
		return in.substring(index1, index2);
	}
	
	public static String getSubstring(String field, int begin, int end) {
		int length = field.length();
		if (length < begin) {
			return "";
		} else {
			if (length >= end) {
				return field.substring(begin, end);
			} else {
				return field.substring(begin, length);
			}
		}
	}

	public static List<String> wrapStringWidthNewline(String text, int width, int indent)
	{
		List<String> strings = new ArrayList<String>();
		 String[] tok = text.split("\n");
		 for (String s : tok) {
			strings.addAll(wrapString(s, width, indent));
		}
		 
		 return strings;
	}
	
	/**
	 * Splits a string into lines of a max width.
	 * @param txt 
	 * @param width the maximum width of each line
	 * @param indent optional indent for lines after the first
	 * @return
	 */
	public static List<String> wrapString(String txt, int width, int indent)
	{
		List<String> r = new ArrayList<String>();
		if(txt.length()<width)
		{
			r.add(txt);
			return r;
		}
		String first = txt.substring(0, width);
		r.add(first);
		String remainder = txt.substring(width);
		addFixedLengthTokens(r, remainder, width-indent, lpad("", indent, ' '));
		return r;
	}

	private static void addFixedLengthTokens(List<String> r, String txt, int width, String prefix) {
		while(txt.length()>width)
		{
			String first = txt.substring(0, width);
			r.add(prefix+first);
			txt = txt.substring(width);
		}
		if(txt.length()>0) r.add(prefix+txt);
		
	}
	public static void main(String[] args)
	{
		String s = "Pippo2";
		System.out.println(s.matches("[A-Za-z][A-Za-z0-9_][A-Za-z0-9_]+"));
		s = "Pippo_2";
		System.out.println(s.matches("[A-Za-z][A-Za-z0-9_][A-Za-z0-9_]+"));
		s = "2Pippo";
		System.out.println(s.matches("[A-Za-z][A-Za-z0-9_][A-Za-z0-9_]+"));
		s = "Pi_ppo";
		System.out.println(s.matches("[A-Za-z][A-Za-z0-9_][A-Za-z0-9_]+"));
		s = "_Pippo";
		System.out.println(s.matches("[A-Za-z][A-Za-z0-9_][A-Za-z0-9_]+"));
		s = "Pip*";
		System.out.println(s.matches("[A-Za-z][A-Za-z0-9_][A-Za-z0-9_]+"));
		s = "Pi";
		System.out.println(s.matches("[A-Za-z][A-Za-z0-9_][A-Za-z0-9_]+"));
		
		
		System.out.println(ellipsize("ciao", 10));
		System.out.println(ellipsize("ciao come va?", 10));
		System.out.println(ellipsize("ciao a tutti", 10));
		System.out.println(ellipsize("pippopluto", 10));
		System.out.println(ellipsize("pippoplut", 10));
		System.out.println(ellipsize("pippoplutop", 10));
	}
	
	 public static List<String> splitBySize(String text, int size)
	 {
		 if(text.isEmpty()) return Arrays.asList("");
		 List<String> strings = new ArrayList<String>();
		 int index = 0;
		 while (index < text.length()) {
		     strings.add(text.substring(index, Math.min(index + size,text.length())));
		     index += size;
		 }
		 return strings;
	 } 
	 public static List<String> splitBySizeAndNewline(String text, int size)
	 {
		 List<String> paragraphs = new ArrayList<String>();
		 StringBuilder paragraph = new StringBuilder();
		 for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			paragraph.append(c);
			if(c == '\n')
			{
				paragraphs.add(paragraph.toString());
				paragraph = new StringBuilder();
			}
		}
		paragraphs.add(paragraph.toString());
		
		List<String> lines = new ArrayList<String>();
		for (String p : paragraphs) {
			lines.addAll(splitBySize(p, size));
		}
		 
		 return lines;
	 }

	 

	 public static String ellipsize(String s, int maxSize)
	 {
		 if(maxSize<=0) throw new RuntimeException("MaxSize"); 
		 if(s.length()<=maxSize) return s;
		 if(s.length()<=4) return s.substring(0, maxSize);
		 if(maxSize<3) return s.substring(0, maxSize);
		 return s.substring(0, maxSize-3)+"...";
	 }

	 public static String toJavaLiteral(String s)
	 {
		 s = s.replace("\"", "\\\"").replace("\n", "\\\n");
		 return '"'+s+'"';
	 }
	 
}
