package servlet;

import java.io.Serializable;

public class Message implements Serializable {
	private String name;
	private String message;
	private String date;
	private String[] sports;
	private String[] views;
	public Message(String name, String message, String date, String[] sports, String[] views) {
		this.name = name;
		this.message = message;
		this.date = date;
		this.sports = sports;
		this.views = views;
	}
	public String toString() {
		return "Date: " + date + "<br />" + "Name: " + name + "<br />" + "Message: " + message + "<br />" + "Sports: " + sports + "<br />" + "<hr />";
	}
	public String getDate() {
		return date;
	}
	public String getName() {
		return name;
	}
	public String getMessage() {
		return message;
	}
	public String[] getSports() {
		return sports;
	}
	public String[] getViews() {
		return views;
	}
}
