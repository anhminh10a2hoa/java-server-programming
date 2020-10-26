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
import java.util.Map;
//import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;



//We could configure the Servlet inside the code
/*@WebServlet(name = "FileUploadServlet", urlPatterns = { "/file_upload_servlet" }, initParams = {
        @WebInitParam(name = "upload_path", value = "uplod/public/files/") })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10 MB
        maxFileSize = 1024 * 1024 * 50, // 50 MB
        maxRequestSize = 1024 * 1024 * 100) // 100 MB
*/

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy_HHmmss");
	// Here we define the destination directory for saving uploaded files.
	// private final String UPLOAD_DIR = "upload/files/".replace('/',
	// File.separatorChar);
	String uploadFilePath;
    String relativePath;
    
    String dataFileName;
    String dataPath;
    String filePath;
    String separator;
    File filePathDir;
//    public static Vector<Message> messages = new Vector<Message>();

	public void init() {
		// Here we get the absolute path of the destination directory
		// uploadFilePath = this.getServletContext().getRealPath(UPLOAD_DIR) +
		// File.separator;

		relativePath = getServletContext().getInitParameter("upload_path");
        
        uploadFilePath = this.getServletContext().getRealPath(relativePath)
                + File.separator;
        /*
         * uploadFilePath =
         * this.getServletContext().getRealPath(getServletConfig().getInitParameter(
         * "upload_path")) + File.separator;
         */
        // Here we create the destination directory under the project main directory if
        // it does not exists
        File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
        
        dataFileName = getServletContext().getInitParameter("data_file");
        dataPath = getServletContext().getInitParameter("data_path");
        filePath = this.getServletContext().getRealPath(dataPath) + File.separator;
        filePathDir = new File(filePath);
        if (!filePathDir.exists()) {
            filePathDir.mkdir();
        }
        filePath += dataFileName;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FileInputStream fi = new FileInputStream(filePath);
		ObjectInputStream oi = new ObjectInputStream(fi);
		resp.setContentType("text/html");
		// Here we initialize the PrintWriter object
		PrintWriter out = resp.getWriter();
		String nameSearch = req.getParameter("nameSearch");
		int count = 0;

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Welcome</title>");
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
				+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
				+ "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>");
		out.println("</head>");
		out.println("<body class=\"text-center\">"); 
		out.println("<h1>Forum</h1>");
		out.println("<div class=\"container\"><div class=\"row\"><div class=\"col-12 col-md-12\"><ul class=\"list-group\">");
		Map<String, String[]> paramMap = req.getParameterMap();
		if (paramMap != null) {
			while (fi.available() > 0) {
				try {
					Message data = (Message) oi.readObject();
					if (data.getName().contains(nameSearch)) {
						out.println("<li class=\"list-group-item\">");
						out.println("<h3>" + data.getName() + "</h3>");
						out.println("<h4>" + data.getComment() + "</h4>");
						out.println("<img src='" + relativePath + File.separator + data.getImage() + "' width='200' height='200'>");
						out.println("</li>");
						count++;
					}

				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		out.println("</ul></div></div></div>");
		if(count == 0) {
			out.println("<h1> No data on " + nameSearch + "</h1>");
		}
		out.println("<div style='text-align:center'><a href='index.html'>Main page</a></div>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fileName = null;
		File fileObj = null;
		String userName = request.getParameter("username");
		String comment = request.getParameter("comment");
		File file = new File(filePath);
		boolean exists = file.exists();
		FileOutputStream f = new FileOutputStream(file, true);
		ObjectOutputStream o = exists ? new ObjectOutputStream(f) {
			protected void writeStreamHeader() throws IOException {
				reset();
			}
		} : new ObjectOutputStream(f);
		for (Part part : request.getParts()) {
			fileName = getFileName(part);
			if (!fileName.equals("")) {
				fileObj = new File(fileName);
				fileName = fileObj.getName();
				fileName = userName + "_" + dateFormat.format(new Date()) + "_" + fileName;
				fileObj = new File(uploadFilePath + fileName);
				
				part.write(fileObj.getAbsolutePath());
//				messages.addElement());
			}
		}
		// Here we get all the parts from request and write it to the file on server
		o.writeObject(new Message(userName, comment, fileName));
		o.close();
		f.close();
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Welcome</title>");
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
				+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
				+ "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>");
		out.println("</head>");
		out.println("<body class=\"text-center\">"); 
		out.println("<h1>Forum</h1>");
		out.println("<div class=\"container\"><div class=\"row\"><div class=\"col-12 col-md-12\"><ul class=\"list-group\">");
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap != null) {
			FileInputStream fi = new FileInputStream(filePath);
			ObjectInputStream oi = new ObjectInputStream(fi);
			while (fi.available() > 0) {
				try {
					Message data = (Message) oi.readObject();
//					for(int i=0; i< data.size(); i++) {
						out.println("<li class=\"list-group-item\">");
						out.println("<h3>" + data.getName() + "</h3>");
						out.println("<h4>" + data.getComment() + "</h4>");
						out.println("<img src='" + relativePath + File.separator + data.getImage() + "' width='200' height='200'>");
						out.println("</li>");
//					}

				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		out.println("</ul></div></div></div>");
		out.println("<div style='text-align:center'><a href='index.html'>Main page</a></div>");
		out.println("</body>");
		out.println("</html>");
		/*
		 * request.setAttribute("message", feedback.toString() );
		 * getServletContext().getRequestDispatcher("/response.jsp").forward(request,
		 * response);
		 */
	}

	// This method extracts the name of the uploaded file from the header part
	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		if (contentDisp != null) {
			/*
			 * In the following line we split a piece of text like the following: form-data;
			 * name="fileName"; filename="C:\Users\Public\Pictures\Sample Pictures\img.jpg"
			 */
			String[] tokens = contentDisp.split(";");
			for (String token : tokens) {
				if (token.trim().startsWith("filename")) {
					return new File(token.split("=")[1].replace('\\', '/')).getName().replace("\"", "");
				}
			}
		}
		return "";
	}
}