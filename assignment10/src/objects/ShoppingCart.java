package objects;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServlet;

public class ShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, String> map;
	private String customer;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ShoppingCart(String customer) {
		if (customer == null)
			this.customer = "Anonymous";
		else
			this.customer = customer;
		map = Collections.synchronizedMap(new HashMap());
	}

	public String getCustomer() {
		return this.customer;
	}

	public void put(String key, String value) {
		if (key == null || value == null)
			return;
		String currentValue;
		if ((currentValue = map.get(key)) != null) {
			Integer currentAmount = Integer.parseInt(currentValue) + Integer.parseInt(value);
			map.put(key, currentAmount + "");
		} else {
			map.put(key, value);
		}
	}

	public void remove(String key) {
		if (key == null)
			return;
		if (map.containsKey(key)) {
			map.remove(key);
		}
	}

	public void removeAll() {
		if (map != null)
			map.clear();
	}

	public Map<String, String> getMap() {
		return map;
	}

	public String getValues() {
		StringBuffer content = new StringBuffer("<tr><th>Product</th><th>Amount</th></tr>");
// Here we explicitly synchronise since we use an iterator
// This makes the the map thread safe so that only one thread a time
// can access the map
		synchronized (map) {
// Iterator i=keys.iterator();
			Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
				content.append(
						"<tr><form method='post' action='handle_shopping_cart'><td><input type='text' name='product' value='"
								+ me.getKey() + "' readonly></td><td><input type='text' name='amount' value='"
								+ me.getValue()
								+ "'  readonly></td><td><input type='submit' name='submit' value='Delete'></form></td></tr>");
			}
		} // end of synchronised
		return content.toString();
	}

	public String checkOut() {
		StringBuffer content = new StringBuffer();
		content.append("<style>" + "body {background-color: #f4f7c5; text-align: center;}" + "h1 {color: #ea907a}"
				+ ".order_history {bottom: 80%} </style>");
		content.append(
				"<form class='login-form' action='checkout' method='post' enctype='multipart/form-data'><label for='fullname'>Full name:</label>"
						+ "<input type='text' id='fullname' name='fullname'></br>"
						+ "<label for='address'>Address:</label><input type='text' id='address' name='address'></br>");
		content.append("Product: </br>");
		synchronized (map) {
			// Iterator i=keys.iterator();
			Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
				content.append(me.getValue() + ": " + me.getKey() + "</br>");
			}
		}
		content.append("<input class='login-button' type='submit' value='Buy'></form>");
		return content.toString();
	}

	public int getSize() {
		return map.size();
	}

	public double getTotalPrice() {
		double totalPrice = 0;
		synchronized (map) {
			// Iterator i=keys.iterator();
			Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
				if (me.getKey().toString().equals("TV")) {
					totalPrice += 100.50 * Integer.parseInt(me.getValue());
				} else if (me.getKey().toString().equals("Digibox")) {
					totalPrice += 80.15 * Integer.parseInt(me.getValue());
				} else if (me.getKey().toString().equals("Freezer")) {
					totalPrice += 170.80 * Integer.parseInt(me.getValue());
				} else if (me.getKey().toString().equals("Digicamera")) {
					totalPrice += 300.75 * Integer.parseInt(me.getValue());
				} else if (me.getKey().toString().equals("PlayStation")) {
					totalPrice += 160.15 * Integer.parseInt(me.getValue());
				} else {
					totalPrice += 50.30 * Integer.parseInt(me.getValue());
				}
			}
		}
		totalPrice = (double) Math.round(totalPrice * 100) / 100;
		return totalPrice;
	}

	public String getAllProduct() {
		StringBuffer content = new StringBuffer();
		synchronized (map) {
			// Iterator i=keys.iterator();
			Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
				content.append(me.getValue() + ":" + me.getKey() + "&");
			}
		}
		return content.toString();
	}

	@Override
	public String toString() {
		return getClass().getName() + "[" + map + "]";
	}
}