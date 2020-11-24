package logic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utility.Helper;
public class HandleShoppingCart extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void init() {
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.html");
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Here we get access to the current session
        HttpSession session = request.getSession();
        
        String action = request.getParameter("submit");
        if (action == null)
            response.sendRedirect("index.html");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Online Shop</title><style>body {background-color: #f4f7c5; text-align: center;}"
        		+ "h1 {color: #ea907a}"
        		+ "p {color: #d45079}"
        		+ "</style></head><body><h1>Shopping list</h1>");
// Here we create a local variable which holds the shoppinCart
        objects.ShoppingCart shoppingCart = null;
// Here we get access to the shopping cart
        if (session != null)
            shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
        if (action.equalsIgnoreCase("Delete")) {
            String key = request.getParameter("product");
            shoppingCart.remove(key);
        }
        if (action.equalsIgnoreCase("Add To Cart")) {
            shoppingCart.put(request.getParameter("product"), request.getParameter("amount"));
        }
        if (action.equalsIgnoreCase("Empty Cart")) {
            if (session != null) {
            	String username = "";
            	shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
            	session.removeAttribute("objects.ShoppingCart");
    			session.setAttribute("objects.ShoppingCart", new objects.ShoppingCart(username));
            }
            	
            response.sendRedirect("handle_shopping_cart");
            return;
        }
        
        out.println("<table>");
        if (shoppingCart.getSize() < 1) {
            out.println("<p style='color:red;'>Shopping Cart is empty!</p>");
        } else {
        	out.println("<b>Customer: </b>" + session.getAttribute("username") + "</br>");
            out.println("<p>Shopping Cart:</p>");
            out.println("<b>Total price: </b>" + shoppingCart.getTotalPrice() + " euro");
            // Here we get access to the shopping cart
            out.println(shoppingCart.getValues());
        }
        out.println("</table>");
        out.println("<hr />");
        out.println("<p style='text-align: center;'><a href='index.html'>Continue Shopping</a></p>");
        out.println("<p style='text-align: center;'><a href='/assignment9/checkout'>Check out</a></p>");
        out.println("</body></html>");
        out.close();
    }
}