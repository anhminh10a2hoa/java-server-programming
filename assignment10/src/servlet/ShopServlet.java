package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utility.Helper;

public class ShopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String productHtmlList;
	private String amountHtmlList;

	public ShopServlet() {
		super();
	}

	public void init() {

		productHtmlList = Helper.getProductHtmlList();
		amountHtmlList = Helper.getAmounttHtmlList();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		// Here we get access to the current session
		HttpSession session = request.getSession(false);
		Object customer = null;
		if (session == null) {
			response.sendRedirect("login.html");
		} else {
			customer = session.getAttribute("username");
			out.println(
					"<!DOCTYPE html><html><head><title>Online Shop</title><style>body {background-color: #f4f7c5; text-align: center;}"
							+ "h1 {color: #ea907a}" + "p {color: #d45079}"
							+ "</style></head><body><h1>Shopping list</h1>");
			out.println("<p>Customer username: " + customer + "</p>");
			out.println("<p>" + (request.getParameter("info") == null ? "" : request.getParameter("info")) + "</p>");
			out.println("<form method='post' action='handle_shopping_cart'>" + "<table>");
			out.println(
					"<tr><th>Product:</th><td>" + productHtmlList + "</td><th>Amount:</th>" + "<td>" + amountHtmlList
							+ "</td>" + "<td> <input type='submit' name='submit' value='Add To Cart'></td></tr>");
			out.println("</table>");
			out.println("<hr/>");
			out.println(
					"<p style='text-align:center'><input style='border: none; background: none; display: inline; color: blue; text-decoration: underline;' type='submit' name='submit' value='Empty Cart'></p>");
			out.println("</form>");
			out.println("<div style='text-align:center'><a href='logout'>Logout</a>" + " "
					+ "<a href='http://localhost:8080/assignment10/order_history'>Order history</a>" + "</div>");
			out.println("</body></html>");
			out.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}