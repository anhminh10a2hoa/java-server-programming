package utility;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Helper {

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