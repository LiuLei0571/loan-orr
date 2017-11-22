package com.load.third.jqm.utils;

import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static boolean isBlank(String str) {
		return str == null || str.length() == 0;
	}
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static String trimToEmpty(String str) {
		return str == null ? "" : str.trim();
	}

	public static String getTextValue(TextView textView) {
		return (textView != null && textView.getText() != null) ? trimToEmpty(textView
				.getText().toString()) : "";
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^(1[34578])\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static String doubleFormat(Double num) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(num);
	}

}
