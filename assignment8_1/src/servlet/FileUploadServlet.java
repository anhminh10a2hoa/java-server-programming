package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.Vector;

import javax.servlet.ServletException;
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
	private DBHandler dbHandler;
    String dataFileName;
    String dataPath;
    String filePath;
    String separator;
    String uploadTempImagePath;
    String uploadTempImageDir;
    String relativePath;
    String uploadFilePath;
    File filePathDir;
    
//    public static Vector<Message> messages = new Vector<Message>();

	public void init() {
		// Here we get the absolute path of the destination directory
		// uploadFilePath = this.getServletContext().getRealPath(UPLOAD_DIR) +
		// File.separator;

		uploadTempImagePath = getServletContext().getInitParameter("temp_image_dir");
		relativePath = getServletContext().getInitParameter("resource_dir");
        
        uploadTempImageDir = this.getServletContext().getRealPath(uploadTempImagePath)
                + File.separator;
        
        
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
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		String nameSearch = req.getParameter("nameSearch");
		out.println(dbHandler.searchData("data2", uploadTempImageDir, uploadTempImagePath, nameSearch));
//		out.println(dbHandler.searchName("data1", relativePath,nameSearch));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		// Connect to database
		try {
            dbHandler = new DBHandler("e1800956_Data","e1800956","xnmzsakPTzEr");
	    } catch (Exception e) {
	        out.println(e.getLocalizedMessage());
	    }
		
		String fileName = null;
		File fileObj = null;
		String userName = request.getParameter("username");
		String comment = request.getParameter("comment");
		String tableRowData="";
		
		for (Part part : request.getParts()) {
			fileName = getFileName(part);
			if (!fileName.equals("")) {
				fileObj = new File(fileName);
				fileName = fileObj.getName();
				fileName = userName + "_" + dateFormat.format(new Date()) + "_" + fileName;
				fileObj = new File(uploadFilePath + fileName);
				part.write(fileObj.getAbsolutePath());
			}
		}
		tableRowData = userName + ";" + comment + ";" + fileName;
		out.println(dbHandler.PostAData("data2", tableRowData, uploadFilePath));
		out.println(dbHandler.getAllData("data2", uploadTempImageDir, uploadTempImagePath));
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