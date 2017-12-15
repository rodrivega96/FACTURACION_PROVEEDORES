package com.vates.facpro.persistence.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseoService {

	static final DateFormat FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	static final DateFormat DATE_TIME = new SimpleDateFormat(
			"dd-MM-yyyy HH:mm:ss");

	public static Integer parseInteger(String input) {
		Integer value = null;
		try {
			value = Integer.valueOf(input);
		} catch (Exception e) {
			// nothing
		}
		return value;
	}
	
	public static Long parseLong(String input) {
		Long value = null;
		try {
			value = Long.valueOf(input);
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
	
	public static Double roundTwoDecimal(Double total) {
		return Math.round((total != null ? total : 0.0d) * 100.0) / 100.0;
	}

	public static String alwaysTwoDecimal(Double total) {
		return String.format("%.2f", roundTwoDecimal(total != null ? total
				: 0.0d));
	}
	
	public static String prepareString(String input) {
		StringBuffer result = new StringBuffer();
		result.append("%");
		result.append(input == null ? "" : input);
		result.append("%");
		return result.toString();
	}

}
