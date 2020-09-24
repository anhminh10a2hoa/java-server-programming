package servlet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class Assignment3c extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String error = "";
    private int count = 0;
    File file = null;
    private void displayError(HttpServletRequest request, HttpServletResponse response, String error) {
        response.setContentType("text/html");
        try {
            // Here we initialise the PrintWriter object
            PrintWriter out = response.getWriter();
            // Here we print HTML tags
            out.println("<html>");
            out.println("<head>");
            out.println("<title>View Resource Servlet Error Message</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<center>");
            out.println("<h1>" + error + "</h1>");
            out.println("<p><b>Error:</b> " + error);
            out.println("<p><a href='/assignment3/assignment3c'>Back</a>");
            out.println("</center>");
            out.println("</body>");
            out.println("</html>");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // String separator = System.getProperty("file.separator");
        // String privateFilePath =
        // "/WEB-INF/private_files/".replace(File.separatorChar, '/');
        // String absolutePath = getServletContext().getRealPath(separator) +
        // separator;
        // In the following we initialize necessary objects
        String error=null;
        URL url = null;
        URLConnection urlConnection = null;
        PrintWriter printWriter = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        
        printWriter = response.getWriter();
        try {
            // Here we access the web resource within the web application
            // as a URL object
            // url = getServletContext().getResource(filePath);
            
            String siteName = request.getParameter("site_name");
            String fileName = request.getParameter("file_name");
            file = new File(getServletContext().getRealPath(this.getInitParameter("file_path")+'/'+fileName));
            if(siteName==null || siteName.isEmpty() || fileName==null || fileName.isEmpty()) {
              error="Site name or file name has not been specified!";
              displayError(request, response, error);
              return;
                
            }
            
            url = new URL(siteName);
        
            
            
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-16");
            urlConnection = url.openConnection();
            
            // Here we establish connection with URL representing web.xml
            urlConnection.connect();
            
            
            // The following would be useful to read data in binary format
            /*
             * BufferedInputStream inputStream = new
             * BufferedInputStream(urlConnection.getInputStream()); int readByte;
             * while((readByte=inputStream.read())!=-1) printWriter.write(readByte);
             */
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String urlContent = "";
            // save file
            writer = new BufferedWriter(new FileWriter(file));
            while ((line = reader.readLine()) != null) {
                urlContent += line;
            }
            writer.append(urlContent);
            printWriter.print("<p>Success</p>");
            printWriter.println("<p style='text-align:center;'><a href='/assignment3/assignment3c'>Back</a></p>");
        } catch (Exception e) {
            error = "Something wrong with: " + url.toString() + " " + e;
            displayError(request, response, error);
        } finally {
            // Here we close the input/output streams
            if (printWriter != null)
                printWriter.close();
            if (reader != null) {
                reader.close();
            }
        }
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Here we redirect the request to the index page
        //response.sendRedirect("index.html");
    	PrintWriter out = response.getWriter();
    	response.setContentType("text/html");
    	out.print("<html><head>");
    	out.print("<title>File Uploading Form</title>");
    	out.print("</head>");
    	out.print("<body style=\"background-color: #fffff2;\">");
    	out.print("<div align=\"center\">");
    	out.print("<form action=\"assignment3c\" method=\"post\">");
    	out.print("<table border=\"0\">");
	    	out.print("<tr>");
	    		out.print("<th valign=\"top\">Resource name:</th>");
	    		out.print("<td><input type=\"text\" name=\"site_name\" value=\"http://www.\"></td>");
	    	out.print("</tr>");
	    	out.print("<tr>");
	    		out.print("<th valign=\"top\">File name:</th>");
	    		out.print("<td><input type=\"text\" name=\"file_name\"></td>");
	    	out.print("</tr>");
    	out.print("<tr><td></td>");   
    	out.print("<td>");  
    	out.print("<input type=\"submit\" value=\"Download\">");
    	out.print("</td>");  
    	out.print("<tr>"); 
    	out.print("</form>");
    	out.print("<a href='/assignment3/index.html'>Home</a></p>");
    	out.print("</div>");
    	out.print("</body></html>");
    }
}