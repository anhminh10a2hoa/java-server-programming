package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.dbHandler;

public class Checkout extends HttpServlet {
	private dbHandler dbHandler;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendRedirect("login.html");
		} else {
			objects.ShoppingCart shoppingCart = null;
			shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
			out.println("<h1>Check out</h1>");
			out.println(shoppingCart.checkOut());
			out.println("<b>Total price: </b>" + shoppingCart.getTotalPrice() + " euro");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendRedirect("login.html");
		} else {
			objects.ShoppingCart shoppingCart = null;
			Object username = null;
			shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
			username = session.getAttribute("username");
			String fullname = request.getParameter("fullname");
			String address = request.getParameter("address");
			System.out.println(fullname + address);
			try {
				dbHandler = new dbHandler("e1800956_Dataa", "e1800956", "xnmzsakPTzEr");
			} catch (Exception e) {
				out.println(e.getLocalizedMessage());
			}
			double totalPrice = shoppingCart.getTotalPrice();

			String data = "";
			data = username + ";" + fullname + ";" + address + ";" + shoppingCart.getAllProduct() + ";" + totalPrice;
			out.println(dbHandler.Checkout("order", data));
		}

	}
}
