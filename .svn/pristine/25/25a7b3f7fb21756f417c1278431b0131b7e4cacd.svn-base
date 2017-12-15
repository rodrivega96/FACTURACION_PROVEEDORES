package com.vates.facpro.persistence.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseoService {

	static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	static final DateFormat DATE_TIME = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static Integer parseInteger(String input) {
		Integer value = null;
		try {
			value = Integer.valueOf(input);
		} catch (Exception e) {
			// nothing
		}
		return value;
	}

	public static Date parseDate(String input) {
		Date value = null;
		try {
			value = FORMAT.parse(input);
		} catch (Exception e) {
			// nothing
		}
		return value;
	}

	public static String getFormattedDate(Date input) {
		String value = "";
		try {
			value = FORMAT.format(input);
		} catch (Exception e) {
			// nothing
		}
		return value;
	}

	public static String getFormattedTime(Date input) {
		String value = "";
		try {
			value = DATE_TIME.format(input);
		} catch (Exception e) {
			// nothing
		}
		return value;
	}

	public static Date parseDateErrorSysdate(String input) {
		Date value = null;
		try {
			value = FORMAT.parse(input);
		} catch (Exception e) {
			value = new Date();
		}
		return value;
	}

	public static Double parseDouble(String value) {
		Double result = null;
		try {
			result = Double.parseDouble(value);
		} catch (NumberFormatException e) {
		}
		return result;
	}

}
