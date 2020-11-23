 package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

public void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {

HttpSession session = request.getSession();
// Here we force the user to logout by invalidating the HttpSession
	session.invalidate();
	response.sendRedirect("/assignment6/index.html");
}

public void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {

doGet(request, response);

}

}