package db;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import helper.Utility;

public class dbHandler {

	private String dbName;
	private String dbUsername;
	private String dbPassword;
	private Connection dbConnection;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData resultSetMeatData;
	public String nameSession;

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

	public dbHandler(String dbName, String dbUsername, String dbPassword) throws Exception {

		this.dbName = dbName;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		try {

			resourceBundle = ResourceBundle.getBundle("db.settings", new Locale(""));

			dbServerURL = resourceBundle.getString("db_server_url").trim();

			serverTimeZone = serverTimeZone + resourceBundle.getString("server_time_zone").trim();

			dbDriver = resourceBundle.getString("db_driver").trim();
			selectQuery = resourceBundle.getString("select_query") + " ";

			// Here we specify the database full path as well as the time zone in which the
			// query is executed.
			dbURL = dbServerURL + "/" + dbName + serverTimeZone;

			acceptedImageSuffixes = resourceBundle.getString("accepted_image_suffixes").trim().split(" ");
			key = resourceBundle.getString("encryption_key");

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

	public String Login(String dbTableName, String data) {

		String query = selectQuery + dbTableName;
		String[] columnData = data.split(";");
		query = "SELECT * FROM " + dbTableName + " WHERE USERNAME='" + columnData[0] + "' AND PASSWORD=MD5('"
				+ columnData[1] + "')";
		System.out.println(query);

		StringBuilder queryResult = new StringBuilder();
		queryResult.append(openConnection());

		try {
			statement = dbConnection.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				queryResult.append("<style>" + "body {background-color: #f4f7c5; text-align: center;}"
						+ "h1 {color: #ea907a}" + ".order_history {bottom: 80%} </style>");
				queryResult.append("<h1>Hi " + columnData[0] + "!, do you want shopping? </h1></br>");
				queryResult.append("<a href='http://localhost:8080/assignment9/index.html'>Shopping?</a>");
				queryResult.append("</div>");
			} else {
				queryResult.append("<style>" + "body {background-color: #f4f7c5; text-align: center;}"
						+ "h1 {color: #ea907a}" + ".order_history {bottom: 80%} </style>");
				queryResult.append(
						"<h1>Wrong password or username.<a href='http://localhost:8080/assignment9/login.html'>Try again!</a></h1>");
			}
		} catch (SQLException e) {
			queryResult.append(e.getMessage());
		} finally {
			queryResult.append(closeConnection());
		}
		return queryResult.toString();
	}

	public String SignUp(String dbTableName, String data) {
		if (data == null || data.isEmpty())
			return Utility.getInvalidDataErrorMessage();

		StringBuilder queryResult = new StringBuilder();
		String query = selectQuery + dbTableName;

		String[] columnData = data.split(";");
		// Insert command
		String insertCommand;

		String checkCommand;

		// check user does exist or not
		checkCommand = "SELECT * FROM " + dbTableName + " WHERE USERNAME='" + columnData[0] + "'";
		System.out.println(checkCommand);
		queryResult.append(openConnection());
		try {
			statement = dbConnection.createStatement();
			resultSet = statement.executeQuery(checkCommand);

			if (resultSet.next()) {
				queryResult.append(
						"<p>Username is already taken.<a href='http://localhost:8080/assignment9/signup.html'>Try again!</a></p>");
			} else {
				insertCommand = "INSERT INTO " + dbTableName + " VALUES('" + columnData[0] + "',MD5('" + columnData[1]
						+ "'))";
				System.out.println(insertCommand);
				try {
					statement = dbConnection.createStatement();
					statement.executeUpdate(insertCommand);
					queryResult.append("<style>" + "body {background-color: #f4f7c5; text-align: center;}"
							+ "h1 {color: #ea907a}</style>");
					queryResult.append("<h1>Sign up successfully!</h1></br>");
					queryResult.append("<a href='http://localhost:8080/assignment9/login.html'>Log in</a>");
					queryResult.append("</div>");

				} catch (SQLException e) {
					queryResult.append("<p>" + e.getMessage() + "</p>");
				} finally {
					queryResult.append(closeConnection());
				}
			}
		} catch (SQLException e) {
			queryResult.append(e.getMessage());
		} finally {
			queryResult.append(closeConnection());
		}
		return queryResult.toString();
	}

	public String Checkout(String dbTableName, String data) {
		if (data == null || data.isEmpty())
			return Utility.getInvalidDataErrorMessage();

		StringBuilder queryResult = new StringBuilder();

		String[] columnData = data.split(";");
		// Insert command
		String insertCommand;
		queryResult.append(openConnection());
		insertCommand = "INSERT INTO `" + dbTableName
				+ "`(`USERNAME`, `FULLNAME`, `ADDRESS`, `PRODUCTS`, `PRICE`) VALUES('" + columnData[0] + "', '"
				+ columnData[1] + "', '" + columnData[2] + "', '" + columnData[3] + "', " + columnData[4] + ")";
		System.out.println(insertCommand);
		try {
			statement = dbConnection.createStatement();
			statement.executeUpdate(insertCommand);
			queryResult.append("<style>" + "body {background-color: #f4f7c5; text-align: center;}"
					+ "table {text-align: center; width:100%} " + "table, th, td {border: 1px solid black;} "
					+ "h1 {color: #ea907a}" + ".order_history {bottom: 80%} </style>");
			queryResult.append("<h1>Order successfully!</h1></br>");
			queryResult.append("<div class='order_history'>");
			queryResult.append("<a href='http://localhost:8080/assignment9/order_history'>Order history</a>");
			queryResult.append(" ");
			queryResult.append("<a href='http://localhost:8080/assignment9/logout'>Log out</a>");
			queryResult.append("</div>");

		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}
		return queryResult.toString();
	}

	public String History(String dbTableName, String username) {
		String query = selectQuery + dbTableName;

		// Here we define SQL query to decrypt the aes_decrypted column and then convert
		// it from HEX to char variable
		query = "SELECT * FROM `" + dbTableName + "` WHERE USERNAME='" + username + "'";
		StringBuilder queryResult = new StringBuilder();
		queryResult.append("<style>" + "body {background-color: #ffd5cd;}" + "div {text-align: center;} "
				+ "table {text-align: center; width:100%} " + "table, th, td {border: 1px solid black;} </style>");
		queryResult.append("<div>");
		queryResult.append("<h2>History order of username: " + username + "</h2>");
		queryResult.append("<TABLE style='border:1px solid black;'><tr>");

		queryResult.append(openConnection());

		// Here we CREATE the statement object for executing SQL commands.
		try {

			statement = dbConnection.createStatement();

			// Here we execute the SQL query and save the results to a ResultSet
			// object
			resultSet = statement.executeQuery(query);

			// Here we get the metadata of the query results
			resultSetMeatData = resultSet.getMetaData();
			// Here we calculate the number of columns
			int columns = resultSetMeatData.getColumnCount();
			// Here we print column names in TABLE header cells
			// Pay attention that the column index starts with 1
			for (int i = 1; i <= columns; i++) {
				queryResult.append("<th> " + resultSetMeatData.getColumnName(i) + "</th>");
			}
			queryResult.append("</tr>");

			while (resultSet.next()) {
				queryResult.append("<tr>");
				// Here we print the value of each column
				for (int i = 1; i <= columns; i++) {
					if (resultSet.getObject(i) != null)
						queryResult.append("<td>" + resultSet.getObject(i).toString() + "</td>");
					else
						queryResult.append("<td>---</td>");
				}
				/*
				 * out.println("<td>" + resultSet.getString(1)+"</td>"); out.println("<td>" +
				 * resultSet.getInt(2)+"</td>"); out.println("<td>" +
				 * resultSet.getInt(3)+"</td>");
				 */
				queryResult.append("</tr>");
			}
			queryResult.append("</TABLE></div>");
			queryResult.append("<div class='order_history'>");
			queryResult.append("<a href='http://localhost:8080/assignment9/index.html'>Continue shoppingy</a>");
			queryResult.append(" ");
			queryResult.append("<a href='http://localhost:8080/assignment9/logout'>Log out</a>");
			queryResult.append("</div>");

		} catch (SQLException e) {
			queryResult.append(e.getMessage());
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
