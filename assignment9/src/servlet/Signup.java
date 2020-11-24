package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import db.dbHandler;

public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy_HHmmss");
	// Here we define the destination directory for saving uploaded files.
	// private final String UPLOAD_DIR = "upload/files/".replace('/',
	// File.separatorChar);
	private dbHandler dbHandler;
	String dataFileName;
	String dataPath;
	String filePath;
	String separator;
	String uploadTempImagePath;
	String uploadTempImageDir;
	String relativePath;
	String uploadFilePath;
	File filePathDir;

//    public static Vector<Message> messages = new Vector<Message>();

	public void init() {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			resp.sendRedirect("shop_servlet");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		// Connect to database
		try {
			dbHandler = new dbHandler("e1800956_Dataa", "e1800956", "xnmzsakPTzEr");
		} catch (Exception e) {
			out.println(e.getLocalizedMessage());
		}
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirm_password = request.getParameter("cfpassword");
		System.out.println(username + password + confirm_password);
		if (password.equals(confirm_password)) {
			String data = "";
			data = username + ";" + password;
			out.println(dbHandler.SignUp("user", data));
		} else {
			out.println("Password and confirm password does not match");
		}
	}
}