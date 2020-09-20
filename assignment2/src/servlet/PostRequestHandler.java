package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PostRequestHandler
 */
public class PostRequestHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PostRequestHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	File file = new File("D:\\\\data.txt");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		FileInputStream fi = new FileInputStream(file);
		ObjectInputStream oi = new ObjectInputStream(fi);

		response.setContentType("text/html");
		// Here we initialize the PrintWriter object
		PrintWriter out = response.getWriter();
		// Here we print HTML tags
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Info</title>");
		out.println("</head>");
		out.println("<body>");
		String username = request.getParameter("username");
		if(username.length() > 0) {
			out.println("<h1>Welcome  " + username + "</h1>");
		}

		while (fi.available() > 0) {
			try {
				Message data = (Message) oi.readObject();
				out.println("Date: " + data.getDate() + "<br />");
				out.println("Name: " + data.getName() + "<br />");
				out.println("Message: " + data.getMessage() + "<br />");
				if (data.getSports() != null) {
					out.println("Favorite sports: ");
					for (int j = 0; j < data.getSports().length; j++) {
						out.println(data.getSports()[j] + " ");
					}
				}
				if (data.getViews() != null) {
					out.println("<br />");
					out.println("Favorite views: ");
					for (int y = 0; y < data.getViews().length; y++) {
						out.println(data.getViews()[y] + " ");
					}
				}
				out.println("<hr>");
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		out.println("<div style='text-align:center'><a href='index.html'>Main page</a></div>");
		out.println("<div style='text-align:center'><a href='/assignment1/login'>Login</a></div>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String name = request.getParameter("name");
		String message = request.getParameter("message");
		String[] sportOptions = request.getParameterValues("sportOptions");
		String[] viewOptions = request.getParameterValues("viewOptions");
		boolean exists = file.exists();
		FileOutputStream f = new FileOutputStream(file, true);
		ObjectOutputStream o = exists ? new ObjectOutputStream(f) {
			protected void writeStreamHeader() throws IOException {
				reset();
			}
		} : new ObjectOutputStream(f);
		o.writeObject(new Message(name, message, formatter.format(date), sportOptions, viewOptions));
		o.close();
		f.close();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Welcome</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Inbox</h1>");
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap != null) {
			FileInputStream fi = new FileInputStream(file);
			ObjectInputStream oi = new ObjectInputStream(fi);
			while (fi.available() > 0) {
				try {
					Message data = (Message) oi.readObject();
					out.println("Date: " + data.getDate() + "<br />");
					out.println("Name: " + data.getName() + "<br />");
					out.println("Message: " + data.getMessage() + "<br />");
					if (data.getSports() != null) {
						out.println("Favorite sports: ");
						for (int j = 0; j < data.getSports().length; j++) {
							out.println(data.getSports()[j] + " ");
						}
					}
					if (data.getViews() != null) {
						out.println("<br />");
						out.println("Favorite views: ");
						for (int y = 0; y < data.getViews().length; y++) {
							out.println(data.getViews()[y] + " ");
						}
					}
					out.println("<hr>");

				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		out.println("<div style='text-align:center'><a href='index.html'>Main page</a></div>");
		out.println("</body>");
		out.println("</html>");
	}
}