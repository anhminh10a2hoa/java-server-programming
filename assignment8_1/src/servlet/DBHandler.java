package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;
import helper.Utility;

public class DBHandler {

	private String dbName;
	private String dbUsername;
	private String dbPassword;
	private Connection dbConnection;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData resultSetMeatData;

	private String dbServerURL;
	private String dbURL;
	private String selectQuery;
	private String dbDriver;
	private String serverTimeZone = "?serverTimezone=";
	private String[] acceptedImageSuffixes;
	private ResourceBundle resourceBundle;

	private static String resourceDir, tempImageDir, tempImageDirAbsolute;
	private File tempImageDirAbsoluteFileObj;
	private String key;

	public DBHandler(String dbName, String dbUsername, String dbPassword) throws Exception {

		this.dbName = dbName;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		try {

			resourceBundle = ResourceBundle.getBundle("servlet.settings", new Locale(""));

			dbServerURL = resourceBundle.getString("db_server_url").trim();

			serverTimeZone = serverTimeZone + resourceBundle.getString("server_time_zone").trim();

			dbDriver = resourceBundle.getString("db_driver").trim();
			selectQuery = resourceBundle.getString("select_query") + " ";

			// Here we specify the database full path as well as the time zone in which the
			// query is executed.
			dbURL = dbServerURL + "/" + dbName + serverTimeZone;
			
			acceptedImageSuffixes=resourceBundle.getString("accepted_image_suffixes").trim().split(" ");
	        key=resourceBundle.getString("encryption_key");

		} catch (Exception e) {
			System.out.println(e);
			throw new Exception();
		}

	}

	private String openConnection() {

		// For mySQL database the above code would look LIKE this:
		try {

			Class.forName(dbDriver);

			// Here we CREATE connection to the database
			dbConnection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
		} catch (ClassNotFoundException e) {
			return e.getLocalizedMessage();
		} catch (SQLException e) {
			return e.getLocalizedMessage();
		}

		return "";

	}

	private String closeConnection() {

		try {
			// Here we close all open streams
			if (statement != null)
				statement.close();
			if (dbConnection != null)
				dbConnection.close();
		} catch (SQLException sqlexc) {
			return "Exception occurred while closing database connection!";
		}

		return "";

	}


	public String getImageName(String dbTableName, String imageName) {

		String query = "SELECT NAME FROM " + dbTableName + " WHERE lower(NAME) LIKE '" + imageName.toLowerCase() + "'";
		// String query = "SELECT NAME FROM " + dbTableName + " WHERE lower(NAME) LIKE
		// ?";

		StringBuilder queryResult = new StringBuilder();

		// queryResult.append(openConnection());

		// Here we CREATE the statement object for executing SQL commands.
		try {

			/*
			 * PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
			 *
			 * preparedStatement.setString(1, imageName.trim());
			 *
			 *
			 * //Here we execute the SQL query and save the results to a ResultSet object
			 * resultSet = preparedStatement.executeQuery(query);
			 */

			Statement statement = dbConnection.createStatement();
			resultSet = statement.executeQuery(query);

			/*
			 * while(resultSet.next()) { queryResult.append(resultSet.getString(1)); }
			 */

			// Here we return either true or false depending on whether the result SET is
			// empty or not
			return resultSet.next() + "";

			// return queryResult.toString();

		} catch (SQLException e) {
			queryResult.append(e.getMessage());
		} finally {
			// queryResult.append(closeConnection());
		}

		return queryResult.toString();
	}

	public String PostAData(String dbTableName, String tableRowData, String uploadFilePath) {

		if (tableRowData == null || tableRowData.isEmpty())
			return Utility.getInvalidDataErrorMessage();
		
		StringBuilder queryResult = new StringBuilder();
		String[] columnData = tableRowData.split(";");
		String insertImageCommand = "INSERT INTO " + dbTableName + "(NAME, COMMENT, FILE_NAME, FILE) VALUES(?,?,?,?)";

		try {
			queryResult.append(openConnection());
			File imageFile = new File(uploadFilePath + columnData[2]);
			// Here we initialise the preparedStatement object
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertImageCommand);

			// Here we get a list of available images under resourceDir
			// directory. However, FROM the received list we only pick
			// files with specified suffixes.
//			 String[] imageNames= new File(resourceDir).list(new imageNameFiletr(acceptedImages));
			// Here we get the content of the resourceDir as a list of files.
			// File image =null;
			FileInputStream fileInputStream = null;
			preparedStatement.setString(1, columnData[0]);
			preparedStatement.setString(2, columnData[1]);
			preparedStatement.setString(3, columnData[2]);
			fileInputStream = new FileInputStream(imageFile.getAbsolutePath());
			// Here we set the input stream for the file as the value for
			// the second column.
			preparedStatement.setBinaryStream(4, (InputStream) fileInputStream, (int) (imageFile.length()));
			// Here we set the length of the file as the value of the third column.
//				preparedStatement.setLong(4, imageFiles[i].length());
			// Here we insert data to the TABLE and read the returned value
			int counter = preparedStatement.executeUpdate();
			// Here we close the file input stream.
			fileInputStream.close();
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} catch (IOException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");

		} finally {
			queryResult.append(closeConnection());
		}
		return queryResult.toString();

	}

	public String getAllData(String dbTableName, String uploadTempImageDir, String uploadTempImagePath) {

		StringBuilder queryResult = new StringBuilder();

		String imageQuery = "SELECT * FROM " + dbTableName;

		// Here we empty the temporary image directory
		tempImageDirAbsoluteFileObj = new File(uploadTempImageDir);
		if (tempImageDirAbsoluteFileObj.exists()) {
			tempImageDirAbsoluteFileObj.delete();

		}

		tempImageDirAbsoluteFileObj.mkdirs();

		queryResult.append("<html>");
		queryResult.append("<head>");
		queryResult.append("<title>Welcome</title>");
		queryResult.append(
				"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
						+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
						+ "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>");
		queryResult.append("</head>");
		queryResult.append("<body class=\"text-center\">");
		queryResult.append("<h1>Forum</h1>");
		queryResult.append(
				"<div class=\"container\"><div class=\"row\"><div class=\"col-12 col-md-12\"><ul class=\"list-group\">");
		// In the following we read data FROM the database.
		try {
			queryResult.append(openConnection());
			statement = dbConnection.createStatement();

			ResultSet resultSet = statement.executeQuery(imageQuery);
			File destinationFile = null;
			InputStream inputStream = null;
			FileOutputStream fileOutputStream = null;
			String name;
			
			resultSet = statement.executeQuery(imageQuery);
			// Here we get the metadata of the query results
			resultSetMeatData = resultSet.getMetaData();
			int columns = resultSetMeatData.getColumnCount();
			
			while (resultSet.next()) {
				// Here we read the value of the first column of the TABLE.
				name = resultSet.getString(3);
				System.out.println(name);
				// Here we CREATE a File object, which refers to
				// the name read FROM the first column of the TABLE
				destinationFile = new File(uploadTempImageDir + name);
				System.out.println(destinationFile);
				// Here we prepare a FileOutputStream to write to the
				// destination file.
				fileOutputStream = new FileOutputStream(destinationFile);
				// Here we initialise the inputStream by reading FROM
				// the second column of the TABLE
				inputStream = resultSet.getBinaryStream(4);
				// Here we reserve memory area to read the image
				// content.
				byte[] imageBuffer = new byte[inputStream.available()];
				// Here we read the image data FROM the database to
				// the memory area.
				inputStream.read(imageBuffer);
				// Here write the image data FROM memory to the file.
				fileOutputStream.write(imageBuffer);
				// Here we close the output and input streams.
				fileOutputStream.close();
				for (int i = 1; i <= columns / 3; i++) {
						queryResult.append("<li class=\"list-group-item\">");
						queryResult.append("<h3>" + resultSet.getString(1) + "</h3>");
						queryResult.append("<h4>" + resultSet.getString(2) + "</h4>");
						queryResult.append("<img src='" + uploadTempImagePath + File.separator + name
								+ "' width='200' height='200'>");
						queryResult.append("</li>");
				}
			}
			queryResult.append("</ul></div></div></div>");
			queryResult.append("<div style='text-align:center'><a href='index.html'>Main page</a></div>");
			queryResult.append("</body>");
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} catch (IOException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}
		return queryResult.toString();

	}
	
	public String searchData(String dbTableName, String uploadTempImageDir, String uploadTempImagePath, String nameSeach) {

		StringBuilder queryResult = new StringBuilder();

		String imageQuery = "SELECT * FROM " + dbTableName;

		// Here we empty the temporary image directory
		tempImageDirAbsoluteFileObj = new File(uploadTempImageDir);
		if (tempImageDirAbsoluteFileObj.exists()) {
			tempImageDirAbsoluteFileObj.delete();

		}

		tempImageDirAbsoluteFileObj.mkdirs();

		queryResult.append("<html>");
		queryResult.append("<head>");
		queryResult.append("<title>Welcome</title>");
		queryResult.append(
				"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
						+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
						+ "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>");
		queryResult.append("</head>");
		queryResult.append("<body class=\"text-center\">");
		queryResult.append("<h1>Forum</h1>");
		queryResult.append(
				"<div class=\"container\"><div class=\"row\"><div class=\"col-12 col-md-12\"><ul class=\"list-group\">");
		// In the following we read data FROM the database.
		try {
			queryResult.append(openConnection());
			statement = dbConnection.createStatement();

			ResultSet resultSet = statement.executeQuery(imageQuery);
			File destinationFile = null;
			InputStream inputStream = null;
			FileOutputStream fileOutputStream = null;
			String name;
			
			resultSet = statement.executeQuery(imageQuery);
			// Here we get the metadata of the query results
			resultSetMeatData = resultSet.getMetaData();
			int columns = resultSetMeatData.getColumnCount();
			
			while (resultSet.next()) {
				// Here we read the value of the first column of the TABLE.
				name = resultSet.getString(3);
				System.out.println(name);
				// Here we CREATE a File object, which refers to
				// the name read FROM the first column of the TABLE
				destinationFile = new File(uploadTempImageDir + name);
				System.out.println(destinationFile);
				// Here we prepare a FileOutputStream to write to the
				// destination file.
				fileOutputStream = new FileOutputStream(destinationFile);
				// Here we initialise the inputStream by reading FROM
				// the second column of the TABLE
				inputStream = resultSet.getBinaryStream(4);
				// Here we reserve memory area to read the image
				// content.
				byte[] imageBuffer = new byte[inputStream.available()];
				// Here we read the image data FROM the database to
				// the memory area.
				inputStream.read(imageBuffer);
				// Here write the image data FROM memory to the file.
				fileOutputStream.write(imageBuffer);
				// Here we close the output and input streams.
				fileOutputStream.close();
				for (int i = 1; i <= columns / 3; i++) {
					if(resultSet.getString(1).contains(resultSet.getString(1))) {
						queryResult.append("<li class=\"list-group-item\">");
						queryResult.append("<h3>" + resultSet.getString(1) + "</h3>");
						queryResult.append("<h4>" + resultSet.getString(2) + "</h4>");
						queryResult.append("<img src='" + uploadTempImagePath + File.separator + name
								+ "' width='200' height='200'>");
						queryResult.append("</li>");
					}
				}
			}
			queryResult.append("</ul></div></div></div>");
			queryResult.append("<div style='text-align:center'><a href='index.html'>Main page</a></div>");
			queryResult.append("</body>");
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} catch (IOException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}
		return queryResult.toString();

	}

	public String getDbServerURL() {
		return dbServerURL;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
}
