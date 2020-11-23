package servlet;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
	private String name;
	private String comment;
	private ArrayList<String> imageFile;

	public Message(String name, String comment, ArrayList<String> imageFile) {
		this.name = name;
		this.comment = comment;
		this.imageFile = imageFile;
	}

	public String getName() {
		return this.name;
	}

	public String getComment() {
		return this.comment;
	}

	public ArrayList<String> getImage() {
		return this.imageFile;
	}
}