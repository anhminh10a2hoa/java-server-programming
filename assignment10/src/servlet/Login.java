package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import db.dbHandler;
import utility.Helper;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy_HHmmss");
	// Here we define the destination directory for saving uploaded files.
	// private final String UPLOAD_DIR = "upload/files/".replace('/',
	// File.separatorChar);
	private dbHandler dbHandler;
	private String cookieValue;
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
			resp.sendRedirect("index.html");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println(username + password);
		// Connect to database
		try {
			dbHandler = new dbHandler();
		} catch (Exception e) {
			out.println(e.getLocalizedMessage());
		}

		String data = "";
		data = username + ";" + password;
		out.println(dbHandler.Login("user", data));
		if (dbHandler.Auth("user", data) == true) {
			// Get the session if exists or create a new one.
			HttpSession session = request.getSession(true);
			// Set session attributes
			session.setAttribute("username", username);
			session.setAttribute("objects.ShoppingCart", new objects.ShoppingCart(username));
			
			Cookie[] cookies = null;
	        Cookie shoppingCartCookie = null;
	        Cookie currentUsernameCookie = null;
	        String currentShoppingCartCookie = null;
	        String usernameCookie = null;
	        // Get an array of Cookies associated with this domain
	        cookies = request.getCookies();
	        boolean shoppingCartFound = false;
	        if (cookies != null) {
	            for (Cookie c : cookies) {
	                if (c.getName().equals("ShoppingCart")) {
	                	shoppingCartFound = true;
	                	currentShoppingCartCookie = c.getValue();
	                }
	                if (c.getName().equals("username")) {
	                	usernameCookie = c.getValue();
	                }
	            }
	        }
	        System.out.println(username + ":" + usernameCookie);
	        if (!shoppingCartFound || !username.equals(usernameCookie)) {
	            // Here we create cookies for first and last names.
	            // Neither the name nor the value can contain white space or any
	            //of the following characters:
	            //[ ] ( ) = , " / ? @ : ;
	        	shoppingCartCookie = new Cookie("ShoppingCart", Helper.stripCookieValue(cookieValue));
	            // Here we set the expiry date for the cookie.
	        	shoppingCartCookie.setMaxAge(Helper.getCookieAge());
	        	
	        	currentUsernameCookie = new Cookie("username", username);
	        	currentUsernameCookie.setMaxAge(Helper.getCookieAge());
	            // Add both the cookies in the response header.
	            response.addCookie(shoppingCartCookie);
	            response.addCookie(currentUsernameCookie);
	        } else {
	        	System.out.println(currentShoppingCartCookie);
	        	String[] items = currentShoppingCartCookie.split("&");
	        	
	        	for (String i : items) {
					String[] p = i.split(":");
					objects.ShoppingCart shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
					shoppingCart.put(p[1], p[0]);
				}
	        }
		}
	}
}