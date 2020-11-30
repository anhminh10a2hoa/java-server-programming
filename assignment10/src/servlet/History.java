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

public class History extends HttpServlet {
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
			String username = "";
			shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
			username = session.getAttribute("username").toString();
			try {
				dbHandler = new dbHandler();
			} catch (Exception e) {
				out.println(e.getLocalizedMessage());
			}
			out.println(dbHandler.History("order", username.toString()));
			session.removeAttribute("objects.ShoppingCart");
			session.setAttribute("objects.ShoppingCart", new objects.ShoppingCart(username));
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
