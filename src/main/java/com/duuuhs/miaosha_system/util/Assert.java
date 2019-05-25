package com.duuuhs.miaosha_system.util;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assert {

	// isNull
	public static boolean isNull(Object o){
		return o==null;
	}
	
	public static boolean isNull(String str){
		return (str==null || str.isEmpty());
	}
	
	
	public static boolean isNull(Collection<?> c){
		return c==null || c.isEmpty();
	}

	public static boolean isNull(Map<?, ?> m){
		return m==null || m.isEmpty();
	}
	
	// notNull
	public static boolean notNull(Object obj){
		return obj!=null;
	}
	
	public static boolean notNull(String str){
		return !isNull(str);
	}

	
	public static boolean notNull(Collection<?> c){
		return !isNull(c);
	}

	public static boolean notNull(Map<?, ?> m){
		return !isNull(m);
	}
	
	// isNotNull
	//-----------------------------------------------------------------------
	public static boolean isNotNull(Object obj){
		return obj!=null;
	}
	
	public static boolean isNotNull(String str){
		return !isNull(str);
	}

	
	public static boolean isNotNull(Collection<?> c){
		return !isNull(c);
	}

	public static boolean isNotNull(Map<?, ?> m){
		return !isNull(m);
	}
	
	// isEmpty
	//-----------------------------------------------------------------------
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static boolean isEmpty(Collection<?> c) {
		return c == null || c.isEmpty();
	}

	public static boolean isEmpty(Map<?, ?> m) {
		return m == null || m.isEmpty();
	}
	
	public static boolean isEmpty(Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(long[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}

	
	public static boolean isEmpty(int[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(short[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(char[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(byte[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}

	
	public static boolean isEmpty(double[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}

	
	public static boolean isEmpty(float[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}

	
	public static boolean isEmpty(boolean[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}
	// notEmpty
	//-----------------------------------------------------------------------
	public static boolean notEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean notEmpty(Collection<?> c) {
		return !isEmpty(c);
	}

	public static boolean notEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static boolean notEmpty(Object[] array) {
		return !isEmpty(array);
	}
	
	// isNotEmpty
	//-----------------------------------------------------------------------
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isNotEmpty(Collection<?> c) {
		return !isEmpty(c);
	}

	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static boolean isNotEmpty(Object[] array) {
		return !isEmpty(array);
	}
	
	
	// Date
	//-----------------------------------------------------------------------
	public static boolean isDate(String str){
		return str!=null && str.matches("^\\d{4}-\\d{2}-\\d{2}$");
	}

	public static boolean isTime(String str){
		return str!=null && str.matches("^\\d{2}:\\d{2}:\\d{2}$");
	}

	public static boolean isDateTime(String str){
		return str!=null && str.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");
	}
	
	
	public static boolean isMobile(String mobile){
		//return (mobile!=null && mobile.matches("(^13\\d{9}$)|(^15[0-35-9]\\d{8}$)|(^1[6789]\\d{9}$)"));
		return (mobile!=null && mobile.matches("^1\\d{10}$"));
	}
	
	public static boolean isEmail(String email){
		return email.matches("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
	}	
	
	// eq
	//------------------------------------------------------------------
	public static <T> boolean eq(T o1,T o2){
		if(o1!=null){
			return o1.equals(o2);
		}
		return o2==null;
	}
	
	// neq
	//------------------------------------------------------------------
	public static <T> boolean neq(T o1,T o2){
		return !eq( o1 ,o2);
	}
	

	// gt nulls is litter 空值最小
	//-----------------------------------------------------------------------
	public static <T extends Number> boolean gt(T n1,T n2){
		if(n1==null){
			return false;
		}
		if(n2==null){
			return true;
		}
		return n1.doubleValue() > n2.doubleValue();
	}
	
	public static boolean gt(String s1,String s2){
		if(s1==null){
			return false;
		}
		if(s2==null){
			return true;
		}
		return s1.compareTo(s2) > 0;
	}
	
	public static boolean gt(Character c1,Character c2){
		if(c1==null){
			return false;
		}
		if(c2==null){
			return true;
		}
		return c1.charValue() > c2.charValue();
	}

	public static boolean gt(Comparable c1, Comparable c2) {
		if(c1==null){
			return false;
		}
		if(c2==null){
			return true;
		}
		return c1.compareTo(c2) > 0;
	}
	
	//gt > 0 
	//-----------------------------------------------------------------------
	public static <T extends Number> boolean gt(T n1){
		if(n1==null){
			return false;
		}
		return n1.doubleValue() > 0;
	}
	
	
	// gte nulls is litter 空值最小
	//-----------------------------------------------------------------------
	public static <T extends Number> boolean gte(T n1,T n2){
		if(n1==null){
			return n2==null;
		}
		if(n2==null){
			return true;
		}
		return n1.doubleValue() >= n2.doubleValue();
	}
	
	public static boolean gte(String s1,String s2){
		if(s1==null){
			return s2==null;
		}
		if(s2==null){
			return true;
		}
		return s1.compareTo(s2) >= 0;
	}
	
	public static boolean gte(Character c1,Character c2){
		if(c1==null){
			return c2==null;
		}
		if(c2==null){
			return true;
		}
		return c1.charValue() >= c2.charValue();
	}

	public static boolean gte(Comparable c1, Comparable c2) {
		if(c1==null){
			return c2==null;
		}
		if(c2==null){
			return true;
		}
		return c1.compareTo(c2) >= 0;
	}
	
	public static <T extends Number> boolean gte(T n1){
		if(n1==null){
			return false;
		}
		return n1.doubleValue() >= 0;
	}
	
	
	// lt nulls is litter 空值最小
	//-----------------------------------------------------------------------
	public static <T extends Number> boolean lt(T n1,T n2){
		if(n1==null){
			return true;
		}
		if(n2==null){
			return false;
		}
		return n1.doubleValue() < n2.doubleValue();
	}
	
	public static boolean lt(String s1,String s2){
		if(s1==null){
			return true;
		}
		if(s2==null){
			return false;
		}
		return s1.compareTo(s2) < 0;
	}
	
	public static boolean lt(Character c1,Character c2){
		if(c1==null){
			return true;
		}
		if(c2==null){
			return false;
		}
		return c1.charValue() < c2.charValue();
	}

	public static boolean lt(Comparable c1, Comparable c2) {
		if(c1==null){
			return true;
		}
		if(c2==null){
			return false;
		}
		return c1.compareTo(c2) < 0;
	}
	
	//lt < 0 
	//-----------------------------------------------------------------------
	public static  <T extends Number> boolean lt(T n1){
		if(n1==null){
			return false;
		}
		return n1.doubleValue() < 0;
	}
	
	// lte nulls is litter 空值最小
	//-----------------------------------------------------------------------
	public static <T extends Number> boolean lte(T n1,T n2){
		if(n1==null){
			return n2==null;
		}
		if(n2==null){
			return false;
		}
		return n1.doubleValue() <= n2.doubleValue();
	}
	
	public static boolean lte(String s1,String s2){
		if(s1==null){
			return s2==null;
		}
		if(s2==null){
			return false;
		}
		return s1.compareTo(s2) <= 0;
	}
	
	public static boolean lte(Character c1,Character c2){
		if(c1==null){
			return c2==null;
		}
		if(c2==null){
			return false;
		}
		return c1.charValue() <= c2.charValue();
	}

	public static boolean lte(Comparable c1, Comparable c2) {
		if(c1==null){
			return c2==null;
		}
		if(c2==null){
			return false;
		}
		return c1.compareTo(c2) <= 0;
	}
	
	public static  <T extends Number> boolean lte(T n1){
		if(n1==null){
			return false;
		}
		return n1.doubleValue() <= 0;
	}
	
	//-----------------------------------------------------------------------
	public static boolean isTrue(Boolean bool) {
		if (bool == null) {
			return false;
		}
		return bool.booleanValue();
	}

	public static boolean isNotTrue(Boolean bool) {
		return !isTrue(bool);
	}

	public static boolean isFalse(Boolean bool) {
		if (bool == null) {
			return false;
		}
		return bool.booleanValue();
	}

	public static boolean isNotFalse(Boolean bool) {
		return !isFalse(bool);
	}
	
	//-----------------------------------------------------------------------

	/**
	 * <p>Checks if the String contains only unicode digits.
	 * A decimal point is not a unicode digit and returns false.</p>
	 *
	 * <p><code>null</code> will return <code>false</code>.
	 * An empty String ("") will return <code>true</code>.</p>
	 *
	 * <pre>
	 * StringUtils.isNumeric(null)   = false
	 * StringUtils.isNumeric("")     = true
	 * StringUtils.isNumeric("  ")   = false
	 * StringUtils.isNumeric("123")  = true
	 * StringUtils.isNumeric("12 3") = false
	 * StringUtils.isNumeric("ab2c") = false
	 * StringUtils.isNumeric("12-3") = false
	 * StringUtils.isNumeric("12.3") = false
	 * </pre>
	 *
	 * @param str  the String to check, may be null
	 * @return <code>true</code> if only contains digits, and is non-null
	 */
	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}


	// Character Tests
	//-----------------------------------------------------------------------
	/**
	 * <p>Checks if the String contains only unicode letters.</p>
	 *
	 * <p><code>null</code> will return <code>false</code>.
	 * An empty String ("") will return <code>true</code>.</p>
	 *
	 * <pre>
	 * StringUtils.isAlpha(null)   = false
	 * StringUtils.isAlpha("")     = true
	 * StringUtils.isAlpha("  ")   = false
	 * StringUtils.isAlpha("abc")  = true
	 * StringUtils.isAlpha("ab2c") = false
	 * StringUtils.isAlpha("ab-c") = false
	 * </pre>
	 *
	 * @param str  the String to check, may be null
	 * @return <code>true</code> if only contains letters, and is non-null
	 */
	public static boolean isAlpha(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetter(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	//-----------------------------------------------------------------------
	/**
	 * <p>Checks whether the <code>String</code> contains only
	 * digit characters.</p>
	 *
	 * <p><code>Null</code> and empty String will return
	 * <code>false</code>.</p>
	 *
	 * @param str  the <code>String</code> to check
	 * @return <code>true</code> if str contains only unicode numeric
	 */
	public static boolean isDigits(String str) {
		if (isNull(str)) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>Checks whether the String a valid Java number.</p>
	 *
	 * <p>Valid numbers include hexadecimal marked with the <code>0x</code>
	 * qualifier, scientific notation and numbers marked with a type
	 * qualifier (e.g. 123L).</p>
	 *
	 * <p><code>Null</code> and empty String will return
	 * <code>false</code>.</p>
	 *
	 * @param str  the <code>String</code> to check
	 * @return <code>true</code> if the string is a correctly formatted number
	 */
	public static boolean isNumber(String str) {
		if (isNull(str)) {
			return false;
		}
		char[] chars = str.toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		int start = (chars[0] == '-') ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && chars[start + 1] == 'x') {
				int i = start + 2;
				if (i == sz) {
					return false; // str == "0x"
				}
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9')
							&& (chars[i] < 'a' || chars[i] > 'f')
							&& (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
		// for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent   
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp) {
					// two E's
					return false;
				}
				if (!foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// no type qualifier, OK
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				// can't have an E at the last byte
				return false;
			}
			if (!allowSigns
					&& (chars[i] == 'd'
						|| chars[i] == 'D'
							|| chars[i] == 'f'
								|| chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l'
				|| chars[i] == 'L') {
				// not allowing L with an exponent
				return foundDigit && !hasExp;
			}
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
		return !allowSigns && foundDigit;
	}

	/*
	 * 判断是否是手机号码
	 */
	 public static boolean isMOBILE(String mobile){
		 Pattern mobile_pattern = Pattern.compile("1\\d{10}");
		 Matcher matcher = mobile_pattern.matcher(mobile);
		 return matcher.matches();
	 }

	public static boolean inArray(byte value, byte ... array){
		for(int i=0;i<array.length;i++){
			if(value == array[i]){
				return true;
			}
		}
		return false;
	}
	
	public static boolean inArray(short value, short ... array){
		for(int i=0;i<array.length;i++){
			if(value == array[i]){
				return true;
			}
		}
		return false;
	}
	
	public static boolean inArray(char value, char ... array){
		for(int i=0;i<array.length;i++){
			if(value == array[i]){
				return true;
			}
		}
		return false;
	}
	
	public static boolean inArray(int value, int ... array){
		for(int i=0;i<array.length;i++){
			if(value == array[i]){
				return true;
			}
		}
		return false;
	}
	
	public static boolean inArray(long value, long ... array){
		for(int i=0;i<array.length;i++){
			if(value == array[i]){
				return true;
			}
		}
		return false;
	}
	
	public static boolean inArray(float value, float ... array){
		for(int i=0;i<array.length;i++){
			if(value == array[i]){
				return true;
			}
		}
		return false;
	}
	
	public static boolean inArray(double value, double ... array){
		for(int i=0;i<array.length;i++){
			if(value == array[i]){
				return true;
			}
		}
		return false;
	}

	
	public static <T> boolean inArray(T value, T ... array){
		if(value == null){
			for(T e:array){
				if(e == null){
					return true;
				}
			}
		}else{
			for(T e:array){
				if(value.equals(e)){
					return true;
				}
			}
		}
		return false;
	}
	
}
