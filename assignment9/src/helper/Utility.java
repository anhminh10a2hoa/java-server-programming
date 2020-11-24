package helper;

public class Utility {

	private static final String phoneValidPattern = "([+][1-9]{1,3}|[0-9]{1,3})[0-9]{5,15}";
	private static final String textValidPattern = "[a-zA-Z0-9_]{2,100}";
	private static final String numberValidpattern = "[1-9][0-9]{0,4}";
	private static final String insertDataValidPattern = "[a-zA-Z0-9;_+]{12,50}";
	private static final String inValidDataErrorMessage = "Invalid data!";
	private static final String inValidTableRowDataErrorMessage = "Invalid table row data!";

	public static String getInvalidDataErrorMessage() {
		return inValidDataErrorMessage;
	}

	public static String getInValidTableRowDataErrorMessage() {
		return inValidDataErrorMessage;
	}

	public static boolean validatePhoneNumber(String param) {
		try {
			if (param.matches(phoneValidPattern))
				return true;
		} catch (Exception ex) {
			return false;
		}

		return false;
	}

	public static boolean validateTextParameter(String param) {
		try {
			if (param.matches(textValidPattern))
				return true;
		} catch (Exception ex) {
			return false;
		}

		return false;
	}

	public static boolean validateNumberParameter(String param) {

		try {
			if (param.matches(numberValidpattern))
				return true;
		} catch (Exception e) {
			return false;
		}

		return false;

	}

	public static boolean validateInsertDateParameter(String param) {

		try {
			if (param.matches(insertDataValidPattern))
				return true;
		} catch (Exception e) {
			return false;
		}

		return false;

	}

}