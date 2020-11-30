package utility;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Helper {
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy hh.mm.ss");
    // Here we define invalid cookie data pattern: "[\\s\\[\\]()=\\\",/?@:;\\\\ ]";
    private final static String invalidCookieDataPattern = "[\\s\\[\\]()=\\\",/?@:;\\\\]";
    // Here we define invalid data pattern: "(\\s)|([nN][uU][lL][lL])";
    private final static String invalidDataPattern ="(\\s)|([nN][uU][lL][lL])";
    // Here we define cookie age of 8 hours
    private static final int cookieAge = 8 * 60 * 60;
    private static final String cookieName = "user_info";
    private static final String feedback = "Empty fields!";
    private static final String noCookieFoundFeedback="No cookies found!";
    private static String foundCookiesTitle="Found Cookies Name and Value";
    private static String readCookieTitle="Reading Cookies";
    public static String stripCookieValue(String data) {
        return (data == null ? "" : data.replaceAll(invalidCookieDataPattern, ""));
    }
    public static String stripData(String data) {
        return (data == null ? "" : data.replaceAll(invalidDataPattern, ""));
    }
    public static String getCookieName() {
        return cookieName;
    }
    
    public static String getFeedback() {
        return feedback;
    }
    public static int getCookieAge() {
        return cookieAge;
    }
    public static String getNoCookieFeedback() {
        return noCookieFoundFeedback;
    }
    public static String getFoundCookiesTitle() {
        return foundCookiesTitle;
    }
    public static String getReadCookieTitle() {
         return readCookieTitle;
    }

	private static ResourceBundle resourceBundle;
	private static String[] productList, amountList;
	private static String productHtmlList = "<select name='product'>";
	private static String amountHtmlList = "<select name='amount'>";

	public static String getProductHtmlList() {

		resourceBundle = ResourceBundle.getBundle("conf.settings", new Locale(""));

		// In the following we build HTML lists for products
		productList = resourceBundle.getString("product_list").split(";");
		for (String product : productList) {
			productHtmlList += "<option value='" + product + "'>" + product;
		}
		productHtmlList += "</select>";

		return productHtmlList;
	}

	public static String getAmounttHtmlList() {

		resourceBundle = ResourceBundle.getBundle("conf.settings", new Locale(""));

		// In the following we build HTML lists for amounts
		amountList = resourceBundle.getString("amount_list").split(";");
		for (String amount : amountList) {
			amountHtmlList += "<option value='" + amount + "'>" + amount;
		}
		amountHtmlList += "</select>";

		return amountHtmlList;
	}

	public static SimpleDateFormat getDateFormat() {
		resourceBundle = ResourceBundle.getBundle("conf.settings", new Locale(""));
		String dateTimePattern = resourceBundle.getString("date_time_pattern").trim();

		return new SimpleDateFormat(dateTimePattern);
	}
}