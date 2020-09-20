package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PostRequestHandler
 */																																																				
public class SearchDate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	PostRequestHandler object = new PostRequestHandler();
	File file = new File("D:\\\\data.txt");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchDate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		// Here we initialize the PrintWriter object
		PrintWriter out = response.getWriter();
		// Here we print HTML tags
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Search</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Error</h1>");
		out.println("<p>This servlet does not handle HTTP Get requests!</p>");
		out.println("<div style='text-align:center'><a href='index.html'>Main page</a></div>");
		out.println("</body>");
		out.println("</html>");

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * Here we use the ServletRequest.getParameter(String name) method to read
		 *
		 * the values of the parameters in the HTML form
		 */
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
		int count = 0;
		String dateSearch = request.getParameter("dateSearch");
		
		while(fi.available() > 0) {
			try {
				Message data = (Message) oi.readObject();
					if(data.getDate().substring(0, 10).contains(dateSearch)) {
						out.println("Date: " + data.getDate() + "<br />");
						out.println("Name: " + data.getName() + "<br />");
						out.println("Message: " + data.getMessage() + "<br />");
						if(data.getSports() != null) {
							out.println("Favorite sports: ");
							for(int j=0; j < data.getSports().length; j++) {
								out.println(data.getSports()[j] + " ");
							}
						}
						if(data.getViews() != null) {
							out.println("<br />");
							out.println("Favorite views: ");
							for(int y=0; y < data.getViews().length; y++) {
								out.println(data.getViews()[y] + " ");
							}
						}
						out.println("<hr>");
						count++;
					}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		if(count == 0) {
			out.println("<h1>No message on " + dateSearch + "</h1>");
		}
		out.println("<div style='text-align:center'><a href='index.html'>Main page</a></div>");
		out.println("</body>");
		out.println("</html>");
	}
}