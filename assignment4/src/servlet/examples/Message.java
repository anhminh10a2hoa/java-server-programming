package servlet.examples;

import java.io.Serializable;

public class Message implements Serializable {
	private String name;
	private String comment;
	private String imageFile;

	public Message(String name, String comment, String imageFile) {
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

	public String getImage() {
		return this.imageFile;
	}
}