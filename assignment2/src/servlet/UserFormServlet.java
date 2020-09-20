package servlet;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserFormServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    // Here we define a directory for user information.
    private Hashtable<String, String> userDirectory;
    public void init() {
        // Here we initialise the user information directory.
        userDirectory = new Hashtable<String, String>();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        // Here we read the value of the submit button
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String action = request.getParameter("submit");
        
        boolean usernameCheck = checkParameter(username);
        boolean passwordCheck = checkParameter(password);
        /*
         * boolean emailCheck = (email != null && !email.trim().isEmpty() && email
         * .indexOf("@") != -1);
         */
        // Here we set the MIME type of the response, "text/html"
        response.setContentType("text/html");
        // Here we use a PrintWriter to send text data
        // to the client who has requested the servlet
        PrintWriter out = response.getWriter();
        
        // Here we start assembling the HTML content
        out.println("<!Doctype html><html><head>");
        out.println("<title>Login</title>");
        out.println("<link rel='stylesheet' type='text/css' href='styles/styles.css'>");
        out.println("</head><body>");
        out.println("<div class='user_form' style='text-align:center'>");
        out.println("<h2>Login</h2>");
        // Here we set the value for method to post, so that
        // the servlet service method calls doPost in the
        // response to this form submit
        out.println("<form method='GET' + action='/assignment1/login'>");
        out.println("<table border style='margin-left: auto; margin-right: auto'><tr><th>");
        out.println("User name: </th><td ");
        if (action != null && !usernameCheck)
            out.println(" style='background-color:red;'");
        out.println("><input type='text' name='username' value='" + (username == null ? "" : username)
                + "' size='20'></td></tr>");
        out.println("<tr><th>Password: </th><td ");
        if (action != null && !passwordCheck)
            out.println(" style='background-color:red;'");
        out.println("><input type='password' name='password' value='" + (password == null ? "" : password)
                + "' size='20'></td></tr>");
        out.println("</textarea></td></tr>");
        out.println("<tr><th></th><td><input type='submit' name='submit' value='Login'></td></tr>");
        out.println("</table></form>");
        out.println(!usernameCheck ? "" : "<p style='color:red;'>Please fill the password</p>");
        out.println(!passwordCheck ? "" : "<p style='color:red;'>Please fill the username</p>");
        out.println("</div>");
        out.println("</body></html>");
        if (usernameCheck && passwordCheck) {
        	doPost(request, response);
        }
    }
    private boolean checkParameter(String parameter) {
        if (parameter != null && !parameter.trim().isEmpty() && !parameter.equals("null"))
            return true;
        return false;
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
    	response.sendRedirect("/assignment1/forum?username=" + request.getParameter("username") );
    }
}