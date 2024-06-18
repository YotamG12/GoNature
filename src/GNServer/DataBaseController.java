package GNServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import GNServer.ServerGUIController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import entities.CanceledReport;
import entities.Parameters;
import entities.ParkDetails;
import entities.Report;

import entities.Reservation;
import entities.SetTime;
import entities.User;
import entities.Visit;

/**
 * The class manages all incoming and outgoing information from the database,
 * including executing SQL queries while maintaining the integrity of the
 * constructed database.
 *
 * @author Shoval Ben-Shushan
 * @author Gad Azriel
 * @author Adar Budomski
 * @author Yotam Gilad
 * @author Tomer Ben-Lulu
 * @version March 2024
 */

public class DataBaseController {

	public static Connection con;
	// Auxiliary variables to track the serial number of an order for each park
	public static Integer resevationIDDisneyLand;
	public static Integer resevationIDUniversal;
	public static Integer resevationIDAfek;

	// Auxiliary variables to track the queue number of waiting list for each park
	public static Integer queueIDDisneyLand;
	public static Integer queueIDUniversal;
	public static Integer queueIDAfek;

	/**
	 * Establishes a connection to the database.
	 *
	 * @param empty.
	 * @return void. But success or failure messages for each operation are printed
	 *         to the console.
	 */
	public DataBaseController() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			String link = "jdbc:mysql://" + ServerGUIController.hostName + "/" + ServerGUIController.sechma
					+ "?serverTimezone=IST";
			con = DriverManager.getConnection(link, ServerGUIController.userName, ServerGUIController.password);
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Constructs a DataBaseController to manage database connections and initial
	 * data loading. It initializes the connection, fetches starting data for parks
	 * (Disneyland, Universal, Afek), and cleans outdated records. Errors during any
	 * step are logged to the console.
	 * 
	 * @param host     - Database server host.
	 * @param sechma   - Database schema name.
	 * @param userName - Database access username.
	 * @param password - Database access password.
	 */
	public DataBaseController(String host, String sechma, String userName, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			// con =
			// DriverManager.getConnection("jdbc:mysql://localhost/gonature?serverTimezone=IST",
			// "root", "Aa123456");
			String link = "jdbc:mysql://" + host + "/" + sechma + "?serverTimezone=IST";
			con = DriverManager.getConnection(link, userName, password);
			System.out.println("SQL connection succeed");
			JOptionPane.showMessageDialog(null, "SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		Statement stmt1, stmt2, stmt3;
		String query1 = "SELECT LastIDOrder FROM parks_details WHERE ParkName = 'disneyland'";
		String query2 = "SELECT LastIDOrder FROM parks_details WHERE ParkName = 'universal'";
		String query3 = "SELECT LastIDOrder FROM parks_details WHERE ParkName = 'afek'";
		try {
			stmt1 = con.createStatement();
			PreparedStatement ps1 = con.prepareStatement(query1);
			ResultSet rs1 = ps1.executeQuery(query1);
			stmt2 = con.createStatement();
			PreparedStatement ps2 = con.prepareStatement(query2);
			ResultSet rs2 = ps2.executeQuery(query2);
			stmt3 = con.createStatement();
			PreparedStatement ps3 = con.prepareStatement(query3);
			ResultSet rs3 = ps3.executeQuery(query3);
			rs1.next();
			resevationIDDisneyLand = rs1.getInt(1);
			rs2.next();
			resevationIDUniversal = rs2.getInt(1);
			rs3.next();
			resevationIDAfek = rs3.getInt(1);
			System.out.println("The last ID reservation for DisneyLand: " + resevationIDDisneyLand);
			System.out.println("The last ID reservation for Afek " + resevationIDAfek);
			System.out.println("The last ID reservation for Universal " + resevationIDUniversal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Statement stmt4, stmt5, stmt6;
		String query4 = "SELECT QueueID FROM parks_details WHERE ParkName = 'disneyland'";
		String query5 = "SELECT QueueID FROM parks_details WHERE ParkName = 'universal'";
		String query6 = "SELECT QueueID FROM parks_details WHERE ParkName = 'afek'";
		try {
			stmt4 = con.createStatement();
			PreparedStatement ps4 = con.prepareStatement(query4);
			ResultSet rs4 = ps4.executeQuery(query4);
			stmt5 = con.createStatement();
			PreparedStatement ps5 = con.prepareStatement(query5);
			ResultSet rs5 = ps5.executeQuery(query5);
			stmt6 = con.createStatement();
			PreparedStatement ps6 = con.prepareStatement(query6);
			ResultSet rs6 = ps6.executeQuery(query6);
			rs4.next();
			queueIDDisneyLand = rs4.getInt(1);
			rs5.next();
			queueIDUniversal = rs5.getInt(1);
			rs6.next();
			queueIDAfek = rs6.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// A function that deletes outdated records from the waiting list.
		DeletionOfOutdatedRecordsFromWaitingList("disneyland");
		DeletionOfOutdatedRecordsFromWaitingList("universal");
		DeletionOfOutdatedRecordsFromWaitingList("afek");
	}

	/**
	 * Inserts a new reservation into the database based on the provided Reservation
	 * object and updates the reservation ID..
	 * 
	 * @param order - The reservation details to insert into the database.
	 */
	public void InsertNewReservation(Reservation order) {
		Statement stmt;
		Integer reservationID = 0;
		try {
			if ((order.getParkName().toLowerCase()).equals("disneyland")) {
				reservationID = resevationIDDisneyLand;
				reservationID = reservationID + 1;
				resevationIDDisneyLand = reservationID;
			}
			if ((order.getParkName().toLowerCase()).equals("universal")) {
				reservationID = resevationIDUniversal;
				reservationID = reservationID + 1;
				resevationIDUniversal = reservationID;
			}
			if ((order.getParkName().toLowerCase()).equals("afek")) {
				reservationID = resevationIDAfek;
				reservationID = reservationID + 1;
				resevationIDAfek = reservationID;
			}
			System.out.println("Reservation ID: " + reservationID);
			order.setReservationID(reservationID);
			System.out.println(reservationID.toString());
			stmt = con.createStatement();
			System.out.println((order.getParkName().toLowerCase()));
			String query = "INSERT INTO " + (order.getParkName().toLowerCase())
					+ " (ReservationID, UserID, NumOfVisitors, VisitDay, VisitTime, PhoneNum, Email, ReservationStatus, VisitorsType, Prepayment, EntryTime, ExitTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, reservationID.toString());
			ps.setString(2, order.getUserID());
			ps.setString(3, order.getNumOfVisitors());
			ps.setString(4, order.getDate());
			ps.setString(5, order.getTime());
			ps.setString(6, order.getPhoneNum());
			ps.setString(7, order.getEmail());
			ps.setString(8, "Approve");
			ps.setString(9, order.getType());
			ps.setString(11, "00:00");
			ps.setString(12, "00:00");
			if (order.isPrepayment())
				ps.setString(10, "true");
			else
				ps.setString(10, "false");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String query = "UPDATE parks_details SET LastIDOrder = '" + reservationID + "' WHERE ParkName = '"
				+ (order.getParkName().toLowerCase()) + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Checks if a reservation ID exists within the specified park's database.
	 * 
	 * @param order - The Reservation object containing the ID and park name to
	 *              check.
	 * @return "yes" if the reservation ID exists, "no" otherwise.
	 */
	public String checkReservationID(Reservation order) {
		String result;
		System.out.println("12345");
		String query = "SELECT ReservationID FROM " + (order.getParkName().toLowerCase());
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("fffff");
			while (rs.next()) {
				String id = rs.getString("ReservationID");
				if (id.equals(order.getReservationID().toString())) {
					System.out.println("yes");
					result = "yes";
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = "no";
		return result;
	}

	/**
	 * Checks if a user exists in the database.
	 * 
	 * @param userName - The username(String) to check.
	 * @return true if the user exists, false otherwise.
	 */
	public boolean checkIfUserExsit(String userName) {
		String result;
		String query = "SELECT UserName FROM users";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				String id = rs.getString("UserName");
				if (id.equals(userName)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Inserts a new single visitor's details into the database.
	 * 
	 * @param order The Reservation object containing the visitor's details.
	 */
	public void InsertNewSingleVisitor(Reservation order) {
		if (!checkIfUserExsit(order.getUserID())) {
			Statement stmt;
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(
						"INSERT INTO users (UserID, UserType, UserPassword, PhoneNum, Email, UserName, ConnectionStatus, Potential) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				ps.setString(1, order.getUserID());
				ps.setString(2, order.getType());
				ps.setString(3, ""); // Single visitor is not need password
				ps.setString(4, order.getPhoneNum());
				ps.setString(5, order.getEmail());
				ps.setString(6, order.getUserID());
				ps.setString(7, "Disconnected");
				ps.setString(8, "");
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks for availability of slots for a given reservation.
	 * 
	 * @param order The Reservation object containing the reservation details.
	 * @return true if there are enough slots available, false otherwise.
	 */
	public boolean CheckFreeSlots(Reservation order) {
		String maxOfVisitors = "", timeOfStay = "", difference = "";
		boolean result;
		Integer amoutOfVisitors = 0, checkSolot = 0;
		String query = "SELECT MaxOfVisitors, TimeOfStay, Difference FROM parks_details WHERE ParkName = '"
				+ (order.getParkName().toLowerCase()) + "'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				maxOfVisitors = rs.getString("MaxOfVisitors");
				timeOfStay = rs.getString("TimeOfStay");
				difference = rs.getString("Difference");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(maxOfVisitors + " " + timeOfStay + " " + difference);
		String[] timeStay = timeOfStay.split(":");
		String[] timeParts = order.getTime().split(":");
		Integer hours = Integer.parseInt(timeParts[0]);
		hours = hours + Integer.parseInt(timeStay[0]);
		if (hours > 21)
			hours = 21;
		String until = hours.toString() + ":00";
		hours = Integer.parseInt(timeParts[0]);
		hours = hours - Integer.parseInt(timeStay[0]) + 1; // We put +1 because we need only 3(The time of stay in the
															// park,3 is example) hours later
		if (hours < 10)
			hours = 10;
		String from = hours.toString() + ":00";
		String query2 = "SELECT NumOFVisitors FROM " + (order.getParkName().toLowerCase()) + " WHERE VisitDay = '"
				+ order.getDate() + "' AND VisitTime BETWEEN '" + from + "' AND '" + until
				+ "' AND (ReservationStatus = 'Approve' OR ReservationStatus = 'Payment')";

		try {
			stmt = con.createStatement();
			stmt = con.prepareStatement(query2);
			ResultSet rs = stmt.executeQuery(query2);
			while (rs.next()) {
				String s = rs.getString("NumOFVisitors");
				amoutOfVisitors = amoutOfVisitors + Integer.parseInt(s);
			}
			String[] amountDifference = difference.split(":");
			checkSolot = Integer.parseInt(maxOfVisitors) - Integer.parseInt(amountDifference[0]) - amoutOfVisitors
					- Integer.parseInt(order.getNumOfVisitors());
			System.out.println(checkSolot);
			if (checkSolot >= 0) {
				result = true;
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		result = false;
		return result;
	}

	/**
	 * Retrieves available visiting hours for a given day based on the reservation
	 * details.
	 * 
	 * @param order - The Reservation object with the specified date.
	 * @return An ArrayList of available hours as strings.
	 */
	public ArrayList<String> CheckFreeSlotsByDate(Reservation order) {
		ArrayList<String> hours = new ArrayList<>();
		Integer checkHour = 10; // 10 = the time that the park open
		while (checkHour < 21) {
			String sendHour = checkHour.toString() + ":00";
			order.setTime(sendHour);
			if (CheckFreeSlots(order))
				hours.add(sendHour);
			checkHour++;
		}
		return hours;
	}

	/**
	 * Adds a reservation to the waiting list for a park.
	 * 
	 * @param order - The Reservation object to add to the waiting list.
	 */
	public void EnterToWaitingList(Reservation order) {
		Statement stmt;
		Integer queueID = 0;
		String tableName = "waiting_list_" + (order.getParkName().toLowerCase());
		try {
			if ((order.getParkName().toLowerCase()).equals("disneyland"))
				queueID = ++queueIDDisneyLand;
			if ((order.getParkName().toLowerCase()).equals("universal"))
				queueID = ++queueIDUniversal;
			if ((order.getParkName().toLowerCase()).equals("afek"))
				queueID = ++queueIDAfek;
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement("INSERT INTO " + tableName
					+ " (QueueID, UserID, NumOFVisitors, VisitDay, VisitTime, PhoneNum, Email, VisitorsType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, queueID.toString());
			ps.setString(2, order.getUserID());
			ps.setString(3, order.getNumOfVisitors());
			ps.setString(4, order.getDate());
			ps.setString(5, order.getTime());
			ps.setString(6, order.getPhoneNum());
			ps.setString(7, order.getEmail());
			ps.setString(8, order.getType());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String query = "UPDATE parks_details SET QueueID = '" + queueID + "' WHERE ParkName = '"
				+ (order.getParkName().toLowerCase()) + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Changes the status of a reservation to "Payment".
	 * 
	 * @param order The Reservation object whose status will be changed.
	 */
	public void ChangeStatustoPayment(Reservation order) {
		Statement stmt;
		String query = "UPDATE " + (order.getParkName().toLowerCase())
				+ " SET ReservationStatus = 'Payment' WHERE ReservationID = '" + order.getReservationID().toString()
				+ "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Statement stmt1;
		String query1 = "UPDATE " + (order.getParkName().toLowerCase())
				+ " SET Prepayment = 'true' WHERE ReservationID = '" + order.getReservationID().toString() + "'";
		try {
			stmt1 = con.createStatement();
			PreparedStatement ps1 = con.prepareStatement(query1);
			ps1.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Attempts to log in a traveler with the provided user details.
	 * 
	 * @param user - The User object containing login credentials.
	 * @return The updated User object after attempting login.
	 */
	public User TravellerLogin(User user) {
		Statement stmt;
		String password = "", connectionStatus = "", userType = "";
		String query = "SELECT UserPassword,ConnectionStatus,UserType FROM users WHERE UserID = '" + user.getUserName()
				+ "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				password = rs.getString("UserPassword");
				connectionStatus = rs.getString("ConnectionStatus");
				userType = rs.getString("UserType");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userType.equals("")) { // if the userType is empty so the user does not exist in the users table.
			user.setPermission("ID not registered");
			return user;
		}
		if (!password.equals(user.getPassword())) {
			user.setPermission("Password invalid");
			return user;
		}
		if (connectionStatus.equals("Connected")) {
			user.setPermission("Connected");
			return user;
		}
		String query1 = "UPDATE users SET ConnectionStatus = 'Connected' WHERE UserID = '" + user.getUserName() + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query1);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		user.setPermission(userType);
		return user;
	}

	/**
	 * Attempts to log in a worker with the provided user details.
	 * 
	 * @param user - The User object containing login credentials.
	 * @return The updated User object after attempting login.
	 */
	public User WorkerLogin(User user) {
		Statement stmt;
		String userName = "", password = "", connectionStatus = "", workerRole = "", parkName = "";
		String query2 = "SELECT Park, UserPassword,ConnectionStatus,WorkerRole FROM workers WHERE WorkerID = '"
				+ user.getUserName() + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query2);
			ResultSet rs = ps.executeQuery(query2);
			while (rs.next()) {
				parkName = rs.getString("Park");
				password = rs.getString("UserPassword");
				connectionStatus = rs.getString("ConnectionStatus");
				workerRole = rs.getString("WorkerRole");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (workerRole.equals("")) { // if the workerRole is empty so the worker does not exist in the workers table.
			user.setPermission("ID not registered");
			user.setParkName(parkName);
			return user;
		}
		if (!password.equals(user.getPassword())) {
			user.setPermission("Password invalid");
			user.setParkName(parkName);
			return user;
		}
		if (connectionStatus.equals("Connected")) {
			user.setPermission("Connected");
			user.setParkName(parkName);
			return user;
		}
		String query1 = "UPDATE workers SET ConnectionStatus = 'Connected' WHERE WorkerID = '" + user.getUserName()
				+ "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query1);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		user.setPermission(workerRole);
		user.setParkName(parkName);
		return user;
	}

	/**
	 * Logs out a traveler, updating their connection status in the database.
	 * 
	 * @param user - The User object to log out.
	 */
	public void TravellerLogout(User user) {
		Statement stmt;
		String query = "UPDATE users SET ConnectionStatus = 'Disconnected' WHERE UserName = '" + user.getUserName()
				+ "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Logs out a worker, updating their connection status in the database.
	 * 
	 * @param user - The User object to log out.
	 */
	public void WorkerLogout(User user) {
		Statement stmt;
		String query = "UPDATE workers SET ConnectionStatus = 'Disconnected' WHERE WorkerID = '" + user.getUserName()
				+ "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves a list of park names managed by a specific department manager. This
	 * method queries the 'parks_details' table to find parks where the
	 * 'ParkDepartmentManager' column matches the provided user's username. It
	 * compiles a list of these park names and returns them.
	 * 
	 * @param user - The user object whose username is used to identify the parks
	 *             they manage as a department manager.
	 * @return An ArrayList of strings containing the names of the parks managed by
	 *         the specified user.
	 */
	public ArrayList<String> DMparks(User user) {
		ArrayList<String> parks = new ArrayList<String>();
		Statement stmt;
		String query = "SELECT ParkName FROM parks_details WHERE ParkDepartmentManager = '" + user.getUserName() + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				parks.add(rs.getString("ParkName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parks;
	}

	/**
	 * Generates a total visitor number report for a specific park and time period.
	 * This method calculates the total number of single and group visitors to a
	 * park within a specified date range, based on the 'report' parameter details.
	 * It then inserts these totals into the 'visitors_report' table in the
	 * database.
	 * 
	 * @param report - The report (object) details including park name, start date,
	 *               and end date for the visitor count.
	 * @return A string "yes" indicating the operation completed successfully.
	 *         Errors during execution are logged via e.printStackTrace().
	 */
	public String TotalVisitorsNumReport(Report report) {
		Integer visitorsGroup = 0, visitorsSingle = 0;
		Statement stmt;
		String query1 = "SELECT SUM(NumOFVisitors) FROM " + (report.getParkName().toLowerCase())
				+ " WHERE VisitDay BETWEEN '" + report.getDateFrom() + "' AND '" + report.getDateTo()
				+ "' AND VisitorsType = 'Single'";
		System.out.println(query1);
		System.out.println(query1.equals(
				"SELECT SUM(NumOFVisitors) FROM universal WHERE VisitDay BETWEEN '2024-02-01' AND '2024-02-29' AND VisitorsType = 'Single'"));
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query1);
			ResultSet rs = ps.executeQuery(query1);
			while (rs.next()) {
				visitorsSingle = rs.getInt("SUM(NumOFVisitors)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String query2 = "SELECT SUM(NumOFVisitors) FROM " + (report.getParkName().toLowerCase())
				+ " WHERE VisitDay BETWEEN '" + report.getDateFrom() + "' AND '" + report.getDateTo()
				+ "' AND VisitorsType = 'Group'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query2);
			ResultSet rs = ps.executeQuery(query2);
			while (rs.next()) {
				visitorsGroup = rs.getInt("SUM(NumOFVisitors)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String query3 = "INSERT INTO visitors_report (ParkName, DateFrom, DateTo, NumOfSingleVisitors, NumOfGroupVisitors) VALUES (?, ?, ?, ?, ?)";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query3);
			ps.setString(1, (report.getParkName().toLowerCase()));
			ps.setString(2, report.getDateFrom());
			ps.setString(3, report.getDateTo());
			ps.setString(4, visitorsSingle.toString());
			ps.setString(5, visitorsGroup.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "yes";
	}

	/**
	 * Retrieves the total number of single and group visitors from a previously
	 * generated report. This method selects the number of single and group visitors
	 * from the 'visitors_report' table for a specific park and date range, as
	 * specified in the 'report' parameter. The numbers are added to an ArrayList,
	 * with the first element representing single visitors and the second element
	 * representing group visitors.
	 * 
	 * @param report - The report (object) details, including the park name, and the
	 *               start and end dates of the report period.
	 * @return An ArrayList containing two integers: the number of single visitors
	 *         and the number of group visitors, respectively.
	 */
	public ArrayList<Integer> GetTotalVisitorsNumReport(Report report) {
		ArrayList<Integer> NumOfVisitors = new ArrayList<Integer>();
		Statement stmt;
		String query = "SELECT NumOfSingleVisitors,NumOfGroupVisitors FROM visitors_report WHERE DateFrom = '"
				+ report.getDateFrom() + "' AND DateTo = '" + report.getDateTo() + "' AND ParkName =  '"
				+ (report.getParkName().toLowerCase()) + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				NumOfVisitors.add(rs.getInt("NumOfSingleVisitors"));
				NumOfVisitors.add(rs.getInt("NumOfGroupVisitors"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NumOfVisitors;
	}

	/**
	 * Creates a usage report for a specific park and time period. This method
	 * inserts a new entry into the 'use_report' table with the park name, start
	 * date, and end date specified in the 'report' parameter.
	 * 
	 * @param report - The details of the report (object), including the park name
	 *               and the date range for the report.
	 * @return A string "yes" indicating that the operation was completed
	 *         successfully. SQL exceptions are logged.
	 */
	public String CreateUseReport(Report report) {
		Statement stmt;
		String query = "INSERT INTO use_report (ParkName, DateFrom, DateTo) VALUES (?, ?, ?)";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, (report.getParkName().toLowerCase()));
			ps.setString(2, report.getDateFrom());
			ps.setString(3, report.getDateTo());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "yes";
	}

	/**
	 * Generates a list of dates and times when the park was not at full capacity
	 * within a given date range. This method iterates over each day in the
	 * specified date range, checking for free slots (times when the park was not at
	 * full capacity) on each day.
	 * 
	 * @param report - The report (object) containing the park name and the date
	 *               range (from and to) for which to generate the usage data.
	 * @return An ArrayList of strings, each entry corresponding to a date and time
	 *         when the park was not fully booked.
	 */
	public ArrayList<String> DisplayUseReport(Report report) {
		ArrayList<String> NotFullyCapacityDates = new ArrayList<String>();
		Reservation order = new Reservation(-1, "", report.getParkName().toLowerCase(), "1", report.getDateFrom(), "",
				"", "", false, "");
		ArrayList<String> freeSlotsDay = new ArrayList<String>();
		while (!(order.getDate().equals(report.getDateTo()))) {
			freeSlotsDay = CheckFreeSlotsByDateForReport(order);
			int i = 0;
			while (freeSlotsDay.size() > i) {
				NotFullyCapacityDates.add(order.getDate() + "," + freeSlotsDay.get(i));
				i++;
			}
			String[] dateParts = order.getDate().split("-");
			Integer day = Integer.parseInt(dateParts[2]);
			day = day + 1;
			String newDay = day.toString();
			if (day < 10)
				newDay = "0" + day.toString();
			newDay = dateParts[0] + "-" + dateParts[1] + "-" + newDay;
			order.setDate(newDay);
		}
		// for the last day in the month
		freeSlotsDay = CheckFreeSlotsByDateForReport(order);
		int i = 0;
		while (freeSlotsDay.size() > i) {
			NotFullyCapacityDates.add(order.getDate() + "," + freeSlotsDay.get(i));
			i++;
		}
		return NotFullyCapacityDates;
	}

	/**
	 * Checks if a use report already exists for a specified date range and park.
	 * 
	 * @param report The Report object specifying the park name, start date, and end
	 *               date for checking.
	 * @return true if the report already exists, false otherwise.
	 */
	public boolean IsUseReportExist(Report report) {
		Statement stmt;
		String query = "SELECT * FROM use_report WHERE DateFrom = '" + report.getDateFrom() + "' AND DateTo = '"
				+ report.getDateTo() + "' AND ParkName =  '" + (report.getParkName().toLowerCase()) + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks if there are free slots available at a specific hour for a given
	 * reservation, considering the park's maximum visitors and stay duration.
	 * 
	 * @param order The Reservation object containing details such as park name,
	 *              visit date, and visit time.
	 * @return true if there are free slots available at the specified hour, false
	 *         otherwise.
	 */
	public boolean CheckFreeSlotsByHourForReport(Reservation order) {
		String maxOfVisitors = null, timeOfStay = null;
		boolean result;
		Integer amoutOfVisitors = 0, checkSolot = 0;
		String query = "SELECT MaxOfVisitors, TimeOfStay FROM parks_details WHERE ParkName = '"
				+ (order.getParkName().toLowerCase()) + "'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			// ps.setString(1, "'"+order.getParkName()+"'");
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				maxOfVisitors = rs.getString("MaxOfVisitors");
				timeOfStay = rs.getString("TimeOfStay");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] timeStay = timeOfStay.split(":"); // The amount of time traveler can stay in the park
		String[] timeParts = order.getTime().split(":");
		Integer hours = Integer.parseInt(timeParts[0]);
		hours = hours - Integer.parseInt(timeStay[0])+1;
		if (hours < 10)
			hours = 10;
		String from = hours.toString() + ":00";
		String query2 = "SELECT NumOFVisitors FROM " + (order.getParkName().toLowerCase()) + " WHERE VisitDay = '"
				+ order.getDate() + "' AND VisitTime BETWEEN '" + from + "' AND '" + order.getTime() + "' AND ReservationStatus = 'Inactive'";
		try {
			stmt = con.createStatement();
			stmt = con.prepareStatement(query2);
			ResultSet rs = stmt.executeQuery(query2);
			while (rs.next()) {
				String s = rs.getString("NumOFVisitors");
				amoutOfVisitors = amoutOfVisitors + Integer.parseInt(s);
			}
			checkSolot = Integer.parseInt(maxOfVisitors) - amoutOfVisitors;
			if (checkSolot > 0) {
				result = true;
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		result = false;
		return result;
	}

	/**
	 * Checks for available visiting hours for a given day tailored for reporting
	 * purposes, considering the park's capacity and stay duration.
	 * 
	 * @param order The Reservation object with the specified date and park name for
	 *              the report.
	 * @return An ArrayList of strings representing available hours for visitation.
	 */
	public ArrayList<String> CheckFreeSlotsByDateForReport(Reservation order) {
		ArrayList<String> hours = new ArrayList<>();
		Integer checkHour = 10; // 10 = the time that the park open
		String timeOfStay = "";
		String query = "SELECT TimeOfStay FROM parks_details WHERE ParkName = '"
				+ (order.getParkName().toLowerCase()) + "'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				timeOfStay = rs.getString("TimeOfStay");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] timeStay = timeOfStay.split(":"); // The amount of time traveler can stay in the park
		Integer hoursOfTimeOfStay = Integer.parseInt(timeStay[0]);
		while (checkHour < 21) {
			String sendHour = checkHour.toString() + ":00";
			order.setTime(sendHour);
			if (CheckFreeSlotsByHourForReport(order)) {
				hours.add(sendHour);
				checkHour++;
			} else
				checkHour = checkHour + hoursOfTimeOfStay;

		}
		return hours;
	}

	/**
	 * Processes entrance to the park, updating the reservation status to "Active"
	 * and setting the entry time.
	 * 
	 * @param entranceTime A SetTime object containing the reservation ID, park
	 *                     name, and entry time.
	 * @return A Reservation object updated with entry time and status.
	 */
	public Reservation EntranceToThePark(SetTime entranceTime) {
		Statement stmt;
		String userID = "", numOfVisitors = "", date = "", time = "", phoneNum = "", email = "", type = "";
		boolean prepayment = false;
		String query = "UPDATE " + (entranceTime.getParkName().toLowerCase()) + "  SET EntryTime = '"
				+ entranceTime.getTime() + "',ReservationStatus = 'Active' WHERE ReservationID = '"
				+ entranceTime.getReservationID().toString() + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String query2 = "SELECT * FROM " + (entranceTime.getParkName().toLowerCase()) + " WHERE ReservationID = '"
				+ entranceTime.getReservationID().toString() + "'";
		try {
			stmt = con.createStatement();
			stmt = con.prepareStatement(query2);
			ResultSet rs = stmt.executeQuery(query2);
			while (rs.next()) {
				userID = rs.getString("UserID");
				numOfVisitors = rs.getString("NumOFVisitors");
				date = rs.getString("VisitDay");
				time = rs.getString("VisitTime");
				phoneNum = rs.getString("PhoneNum");
				email = rs.getString("Email");
				type = rs.getString("VisitorsType");
				prepayment = rs.getBoolean("Prepayment");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		Reservation order = new Reservation(entranceTime.getReservationID(), userID, entranceTime.getParkName(),
				numOfVisitors, date, time, phoneNum, email, prepayment, type);
		return order;
	}

	/**
	 * Marks a reservation as "Inactive" upon exiting the park and records the exit
	 * time.
	 * 
	 * @param exitTime A SetTime object containing the reservation ID, park name,
	 *                 and exit time.
	 * @return true if the operation was successful.
	 */
	public boolean ExitFromThePark(SetTime exitTime) {
		Statement stmt;
		String query = "UPDATE " + (exitTime.getParkName().toLowerCase()) + "  SET ExitTime = '" + exitTime.getTime()
				+ "',ReservationStatus = 'Inactive' WHERE ReservationID = '" + exitTime.getReservationID().toString()
				+ "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Checks if the reservation ID provided exists in the park's database when
	 * attempting to enter the park.
	 * 
	 * @param entranceTime A SetTime object containing the reservation ID and park
	 *                     name.
	 * @return "yes" if the reservation ID exists in the database, "no" otherwise.
	 */
	public String checkReservationIDWhenEntranceToThePark(SetTime entranceTime) {
		String result;
		String query = "SELECT ReservationID FROM " + (entranceTime.getParkName().toLowerCase());
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				String id = rs.getString("ReservationID");
				if (id.equals(entranceTime.getReservationID().toString())) {
					result = "yes";
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = "no";
		return result;
	}

	/**
	 * Generates a report of visits for a specific park within a given date range
	 * where the reservation status was 'Inactive'. This method queries the database
	 * for visits based on the 'report' parameters, selecting the visit time, entry
	 * time, exit time, and type of visitors for each inactive reservation within
	 * the specified date range. It calculates the duration of each visit and stores
	 * the information in a list of {@link Visit} objects.
	 * 
	 * @param report The {@link Report} object containing details such as the park
	 *               name and the date range for the report.
	 * @return An ArrayList of {@link Visit} objects, each representing a specific
	 *         visit that occurred under the conditions specified.
	 */
	public ArrayList<Visit> VisitReport(Report report) {
		Statement stmt;
		ArrayList<Visit> visitReport = new ArrayList<Visit>();
		String visitTime = "", entryTime = "", exitTime = "", visitorsType = "";
		Float duration;
		String query = "SELECT VisitTime,EntryTime,ExitTime,VisitorsType FROM " + (report.getParkName().toLowerCase())
				+ " WHERE ReservationStatus = 'Inactive' AND VisitDay BETWEEN '" + report.getDateFrom() + "' AND '"
				+ report.getDateTo() + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				visitTime = rs.getString("VisitTime");
				entryTime = rs.getString("EntryTime");
				exitTime = rs.getString("ExitTime");
				visitorsType = rs.getString("VisitorsType");
				duration = CalcDuration(entryTime, exitTime);
				Visit visit = new Visit(visitTime, visitorsType, duration);
				visitReport.add(visit);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return visitReport;

	}

	/**
	 * Calculates the duration between two times in hours.
	 * 
	 * @param entryTime The entry time in the format "HH:MM".
	 * @param exitTime  The exit time in the format "HH:MM".
	 * @return The duration between entry and exit times in hours as a Float.
	 */
	public Float CalcDuration(String entryTime, String exitTime) {
		String[] startParts = entryTime.split(":");
		String[] endParts = exitTime.split(":");
		int startHours = Integer.parseInt(startParts[0]);
		int startMinutes = Integer.parseInt(startParts[1]);
		int endHours = Integer.parseInt(endParts[0]);
		int endMinutes = Integer.parseInt(endParts[1]);
		int startTotalMinutes = startHours * 60 + startMinutes;
		int endTotalMinutes = endHours * 60 + endMinutes;
		int durationMinutes = endTotalMinutes - startTotalMinutes;
		float durationHours = durationMinutes / 60.0f;
		return durationHours;
	}

	/**
	 * Registers a user as a group guide based on their ID, if they have the
	 * potential and are not already a group guide. This method checks if the user
	 * with the specified ID has marked themselves as 'wanna be' a group guide and
	 * is not already registered as a 'Group' type user. If the conditions are met,
	 * it updates the user's type to 'Group' in the database. If the user does not
	 * exist, is not marked as 'wanna be', or is already a group guide, the method
	 * returns "no".
	 * 
	 * @param id The user ID to check and potentially update.
	 * @return A string "yes" if the user was successfully registered as a group
	 *         guide, "no" otherwise.
	 */
	public String RegisterGroupGuide(String id) {
		Statement stmt;
		String userName = "", userType = "";
		String query = "SELECT UserName,UserType FROM users WHERE UserID = '" + id + "' AND Potential = 'wanna be'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				userName = rs.getString("UserName");
				userType = rs.getString("UserType");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userName.equals(""))
			return "no";
		if (userType.equals("Group"))
			return "no";
		String query1 = "UPDATE users SET UserType = 'Group' WHERE UserID = '" + id + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query1);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "yes";
	}

	/**
	 * Generates a report detailing the number of unresolved and canceled
	 * reservations for a park over a given period. This function counts unresolved
	 * (either approved or awaiting payment) and canceled reservations per day
	 * within the specified range. It stores these counts in two HashMaps keyed by
	 * date, then compiles these into a CanceledReport object that summarizes the
	 * data.
	 * 
	 * @param report The criteria for the report, including park name and date
	 *               range.
	 * @return A CanceledReport object with daily counts of unresolved and canceled
	 *         reservations.
	 */
	public CanceledReport CreateCanceledReport(Report report) {
		HashMap<String, Integer> AmountOfUnresolvedOrders = new HashMap<>();
		HashMap<String, Integer> AmountOfCanceledVisitInDate = new HashMap<>();
		Statement stmt;
		String date = report.getDateFrom();
		String lastDate = report.getDateTo();
		LocalDate localDateForLastDate = LocalDate.parse(lastDate);
		LocalDate nextDayLast = localDateForLastDate.plusDays(1);
		lastDate = nextDayLast.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		while (!(lastDate.equals(date))) {
			String query = "SELECT COUNT(NumOFVisitors) FROM " + (report.getParkName().toLowerCase())
					+ " WHERE VisitDay = '" + date
					+ "'  AND (ReservationStatus = 'Approve' OR ReservationStatus = 'Payment')";
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery(query);
				while (rs.next()) {
					AmountOfUnresolvedOrders.put(date, rs.getInt("COUNT(NumOFVisitors)"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String query2 = "SELECT COUNT(NumOFVisitors) FROM " + (report.getParkName().toLowerCase())
					+ " WHERE VisitDay = '" + date + "'  AND ReservationStatus = 'Canceled'";
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query2);
				ResultSet rs = ps.executeQuery(query2);
				while (rs.next()) {
					AmountOfCanceledVisitInDate.put(date, rs.getInt("COUNT(NumOFVisitors)"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			LocalDate localDate = LocalDate.parse(date);
			LocalDate nextDay = localDate.plusDays(1);
			String newDate = nextDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			date = newDate;
		}
		CanceledReport canceledReport = new CanceledReport(report.getParkName(), AmountOfUnresolvedOrders,
				AmountOfCanceledVisitInDate);
		return canceledReport;
	}

	/**
	 * Creates a consolidated report of canceled visits and unresolved orders for
	 * all parks. This method takes a report object, splits the park names, and
	 * generates individual canceled reports for each park. It then aggregates the
	 * data for all parks into a single report, summing up the amounts of canceled
	 * visits and unresolved orders across all parks.
	 *
	 * @param report The initial report object containing the park names and the
	 *               relevant dates. The park names should be in a comma-separated
	 *               string format within the report object.
	 * @return A {@code CanceledReport} object that contains the total amounts of
	 *         unresolved orders and canceled visits across all parks specified in
	 *         the input report. The 'parkName' of the returned report will be set
	 *         to "all" to indicate that it represents aggregated data for all
	 *         parks.
	 */
	public CanceledReport CreateCanceledReportForAllParks(Report report) {
		HashMap<String, Integer> AmountOfUnresolvedOrders;
		HashMap<String, Integer> AmountOfCanceledVisitInDate;
		HashMap<String, Integer> AmountOfUnresolvedOrdersOtherPark;
		HashMap<String, Integer> AmountOfCanceledVisitInDateOtherPark;
		String[] parks = report.getParkName().split(",");
		int i = 0;
		CanceledReport[] canceledArray = new CanceledReport[parks.length];
		for (i = 0; i < parks.length; i++) {
			report.setParkName(parks[i]);
			canceledArray[i] = CreateCanceledReport(report);

		}
		AmountOfCanceledVisitInDate = canceledArray[0].getAmountOfCanceledVisitInDate();
		AmountOfUnresolvedOrders = canceledArray[0].getAmountOfUnresolvedOrders();
		AmountOfUnresolvedOrdersOtherPark = canceledArray[1].getAmountOfUnresolvedOrders();
		AmountOfCanceledVisitInDateOtherPark = canceledArray[1].getAmountOfCanceledVisitInDate();
		AmountOfUnresolvedOrdersOtherPark.forEach((date, amount) -> {
			AmountOfUnresolvedOrders.merge(date, amount, Integer::sum);
		});
		AmountOfCanceledVisitInDateOtherPark.forEach((date, amount) -> {
			AmountOfCanceledVisitInDate.merge(date, amount, Integer::sum);
		});
		for (i = 2; i < parks.length; i++) {
			AmountOfUnresolvedOrdersOtherPark = canceledArray[i].getAmountOfUnresolvedOrders();
			AmountOfCanceledVisitInDateOtherPark = canceledArray[i].getAmountOfCanceledVisitInDate();
			AmountOfUnresolvedOrdersOtherPark.forEach((date, amount) -> {
				AmountOfUnresolvedOrders.merge(date, amount, Integer::sum);
			});
			AmountOfCanceledVisitInDateOtherPark.forEach((date, amount) -> {
				AmountOfCanceledVisitInDate.merge(date, amount, Integer::sum);
			});
		}
		CanceledReport canceledReport = new CanceledReport("all", AmountOfUnresolvedOrders,
				AmountOfCanceledVisitInDate);
		return canceledReport;
	}

	/**
	 * Requests an update to park parameters, inserting or updating a request as
	 * needed. If no existing request matches the given parameters, a new one is
	 * added. Otherwise, the existing request is updated. Both operations set the
	 * request status to 'Waiting'. Consistency in park name casing is maintained.
	 * 
	 * @param parameters Details of the parameter update request.
	 * @return true, indicating the request submission was successful.
	 */
	public boolean RequestUpdateParameters(Parameters parameters) {
		Statement stmt;
		String DMId = GetDMOfPark(parameters.getParkName().toLowerCase());
		if (!CheckIfParametersExist(parameters)) {
			String query = "INSERT INTO parameters (ParkName, parametersType, NewValue, parametersStatus, ParkDepartmentManager) VALUES (?, ?, ?, ?, ?)";
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, (parameters.getParkName().toLowerCase()));
				ps.setString(2, parameters.getType());
				ps.setString(3, parameters.getNewValue());
				ps.setString(4, "Waiting");
				ps.setString(5, DMId);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String query = "UPDATE parameters SET NewValue = '" + parameters.getNewValue()
					+ "' , parametersStatus = 'Waiting' WHERE ParkName = '" + (parameters.getParkName().toLowerCase())
					+ "' AND parametersType = '" + parameters.getType() + "'";
			// UPDATE parameters
			// SET NewValue = '03:00', parametersStatus = 'Waiting'
			// WHERE ParkName = 'afek' AND parametersType = 'Duration Time';

			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * Determines if an update request for a specific parameter in a park already
	 * exists. Searches the database for a matching parameter type within the
	 * specified park.
	 * 
	 * @param parameters The park and parameter type to check for existing requests.
	 * @return true if an existing request is found, false otherwise.
	 */
	public boolean CheckIfParametersExist(Parameters parameters) {
		Statement stmt;
		String parametersType = "";
		String query = "SELECT parametersType FROM parameters WHERE ParkName = '"
				+ parameters.getParkName().toLowerCase() + "'";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				parametersType = rs.getString("parametersType");
				if (parametersType.equals(parameters.getType()))
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Fetches the Department Manager's ID for a given park. Performs a database
	 * search using the park name to find the associated Department Manager's ID.
	 * Returns the ID if found, or an empty string if not found or an error occurs.
	 * 
	 * @param parkName The park's name.
	 * @return Department Manager's ID or empty string.
	 */
	public String GetDMOfPark(String parkName) {
		String id = "";
		String query = "SELECT ParkDepartmentManager FROM parks_details WHERE ParkName = '" + parkName + "'";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				id = rs.getString("ParkDepartmentManager");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * Fetches pending parameter update requests for a specific Department Manager.
	 * Looks up requests with 'Waiting' status assigned to the given Department
	 * Manager's ID, compiling them into a list.
	 * 
	 * @param id Department Manager's ID.
	 * @return List of pending Parameters.
	 */
	public ArrayList<Parameters> DisplayParametersForDM(String id) {
		ArrayList<Parameters> dMParameters = new ArrayList<Parameters>();
		String type = "", status = "", newValue = "", parkName = "";
		String query = "SELECT * FROM parameters WHERE ParkDepartmentManager = '" + id
				+ "' AND parametersStatus = 'Waiting'";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				parkName = rs.getString("ParkName");
				type = rs.getString("parametersType");
				newValue = rs.getString("NewValue");
				status = rs.getString("parametersStatus");
				Parameters parameters = new Parameters(type, status, newValue, parkName);
				dMParameters.add(parameters);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dMParameters;
	}

	/**
	 * Updates the status of parameter requests and applies changes if approved. For
	 * a given parameter request, this function sets its status to either approved
	 * or declined. If approved, it also updates the park's details with the new
	 * parameter value.
	 * 
	 * @param parameters Details of the parameter update, including status and new
	 *                   value.
	 * @return Always returns true, indicating the operation was attempted.
	 */
	public boolean AprrovedDeclinedParameters(Parameters parameters) {
		Statement stmt;
		String command = "";
		// DELETE FROM parameters WHERE ParkName = 'afek' AND parametersType = 'Duration
		// Time';
		String query = "UPDATE parameters SET parametersStatus = '" + parameters.getStatus() + "' WHERE ParkName = '"
				+ (parameters.getParkName().toLowerCase()) + "' AND parametersType = '" + parameters.getType() + "'";
		// String query = "DELETE FROM parameters WHERE ParkName = '" +
		// (parameters.getParkName().toLowerCase()) + "' AND parametersType = '" +
		// parameters.getType() + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (parameters.getStatus().equals("Approve")) {
			switch (parameters.getType()) {
			case "Capacity":
				command = "MaxOfVisitors";
				break;
			case "Gap":
				command = "Difference";
				break;
			case "Duration Time":
				command = "TimeOfStay";
				break;
			}
			String query1 = "UPDATE parks_details SET " + command + "= '" + parameters.getNewValue()
					+ "' WHERE ParkName = '" + (parameters.getParkName().toLowerCase()) + "'";
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query1);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * Verifies the existence of a user by their ID. Searches the database for a
	 * user with the given ID. Returns true if the user is found, otherwise false.
	 * 
	 * @param id User's ID to check.
	 * @return true if user exists, false if not.
	 */
	public boolean CheckGroupGuide(String id) {
		Statement stmt;
		String userName = "";
		String query = "SELECT UserName FROM users WHERE UserID = '" + id + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				userName = rs.getString("UserName");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userName.equals(""))
			return false;
		return true;
	}

	/**
	 * Retrieves all reservations for a specified park. This method fetches
	 * reservation details from the database for the given park name. It constructs
	 * a list of Reservation objects from the database records, including
	 * reservation ID, user ID, number of visitors, visit day, visit time, phone
	 * number, email, reservation status, visitors type, and prepayment status.
	 * 
	 * @param parkName The name of the park for which to fetch reservations.
	 * @return A list of Reservation objects containing details of each reservation.
	 */
	public ArrayList<Reservation> fetchOrders(String parkName) {
		ArrayList<Reservation> orders = new ArrayList<>();
		Integer reservationID = -1;
		String userID = "", numOFVisitors = "", visitDay = "", visitTime = "", phoneNum = "", email = "",
				reservationStatus = "", visitorsType = "";
		boolean prepayment = false;
		Statement stmt;
		String query = "SELECT * FROM " + parkName.toLowerCase() + "";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				// ReservationID, UserID, NumOFVisitors, VisitDay, VisitTime, PhoneNum, Email,
				// ReservationStatus, VisitorsType, Prepayment, EntryTime, ExitTime
				reservationID = rs.getInt("ReservationID");
				userID = rs.getString("UserID");
				numOFVisitors = rs.getString("NumOFVisitors");
				visitDay = rs.getString("visitDay");
				visitTime = rs.getString("VisitTime");
				phoneNum = rs.getString("PhoneNum");
				email = rs.getString("Email");
				reservationStatus = rs.getString("ReservationStatus");
				visitorsType = rs.getString("VisitorsType");
				prepayment = rs.getBoolean("Prepayment");
				// entryTime = rs.getString("EntryTime");
				// exitTime = rs.getString("ExitTime");
				// Create Order object and add it to the list
				orders.add(new Reservation(reservationID, userID, parkName, numOFVisitors, visitDay, visitTime,
						phoneNum, email, prepayment, visitorsType));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * Cancels a specific reservation. Updates the reservation status to 'Canceled'
	 * in the database for the given reservation by setting the 'ReservationStatus'
	 * field of the specified park's table.
	 * 
	 * @param order The reservation to cancel, identified by its reservation ID and
	 *              park name.
	 */
	public void CancelReservation(Reservation order) {
		Statement stmt;
		String query = "UPDATE " + order.getParkName().toLowerCase()
				+ " SET ReservationStatus = 'Canceled' WHERE ReservationID = '" + order.getReservationID().toString()
				+ "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes a specific entry from a waiting list table in the database. This
	 * method constructs the table name is dynamically based on the provided park
	 * name and deletes the entry that matches the specified queue ID, visit day and
	 * visit time.
	 * 
	 * @param parkName The name of the park, used to determine the specific waiting
	 *                 list table.
	 * @param date     The date of the visit in the format 'YYYY-MM-DD'.
	 * @param hour     The hour of the visit in 24-hour format 'HH:MM'.
	 * @param queueID  The unique identifier of the queue entry to be deleted.
	 */
	public void DeleteFromWaitingList(String parkName, String date, String hour, Integer queueID) {
		Statement stmt;
		String tableName = "waiting_list_" + parkName.toLowerCase();
		String query = "DELETE FROM " + tableName + " WHERE QueueID = '" + queueID.toString() + "' AND VisitDay = '"
				+ date + "' AND VisitTime = '" + hour + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Moves a reservation from the waiting list to regular reservations if a slot
	 * becomes available. This method checks the waiting list for the specified park
	 * and date/time to find the next eligible reservation. If there's an available
	 * slot (checked via {@code CheckFreeSlots}), it deletes the reservation from
	 * the waiting list and adds it as a regular reservation. It then notifies the
	 * user about the update to their reservation status.
	 * 
	 * @param order The base reservation details used to identify the waiting list
	 *              entry.
	 */
	public void SwitchingFromWaitingListToRegularReservation(Reservation order) {
		Statement stmt;
		Integer queueID = -1;
		String userID = "", visitDay = "", numOFVisitors = "", visitTime = "", phoneNum = "", email = "",
				visitorsType = "";
		Reservation newOrder = new Reservation(-2, "", "", "", "", "", "", "", false, "");

		String query = "SELECT * FROM waiting_list_" + order.getParkName().toLowerCase() + " WHERE VisitDay = '"
				+ order.getDate() + "' AND VisitTime = '" + order.getTime() + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				queueID = rs.getInt("QueueID");
				userID = rs.getString("UserID");
				numOFVisitors = rs.getString("NumOFVisitors");
				visitDay = rs.getString("visitDay");
				visitTime = rs.getString("VisitTime");
				phoneNum = rs.getString("PhoneNum");
				email = rs.getString("Email");
				visitorsType = rs.getString("VisitorsType");
				newOrder = new Reservation(queueID, userID, order.getParkName().toLowerCase(), numOFVisitors, visitDay,
						visitTime, phoneNum, email, false, visitorsType);
				if (CheckFreeSlots(newOrder))
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!(newOrder.getReservationID() == -2)) {
			DeleteFromWaitingList(newOrder.getParkName(), newOrder.getDate(), newOrder.getTime(),
					newOrder.getReservationID());
			InsertNewReservation(newOrder);
			String message = "Dear traveler (" + newOrder.getUserID()
					+ "),\na spot in the park has become available for you.\nYour reservation on the waiting list has been approved.\nIf you cannot attend, please log into the system to cancel your booking.\nWe look forward to seeing you, Go Nature system.\nPhone num: "
					+ newOrder.getPhoneNum() + " Email: " + newOrder.getEmail();
			JOptionPane.showMessageDialog(null, message);
		}
	}

	/**
	 * Retrieves all reservations for a specific ID from multiple theme parks. This
	 * method aggregates reservations from all the existing parks in the system.
	 * based on the provided unique identifier (id = username). It's designed to
	 * compile a comprehensive list of all reservations associated with the given ID
	 * across these parks.
	 * 
	 * @param id The unique identifier for which reservations are to be retrieved.
	 * @return An ArrayList of {@link Reservation} objects containing all
	 *         reservations found for the specified ID across the mentioned parks.
	 *         If no reservations are found, returns an empty list.
	 */
	public ArrayList<Reservation> GetAllReservationsFromAllParksById(String id) {
		ArrayList<Reservation> universalOrders = GetAllReservationsById(id, "universal");
		ArrayList<Reservation> afekOrders = GetAllReservationsById(id, "afek");
		ArrayList<Reservation> disneylandOrders = GetAllReservationsById(id, "disneyland");
		ArrayList<Reservation> allOrders = new ArrayList<>();
		allOrders.addAll(universalOrders);
		allOrders.addAll(afekOrders);
		allOrders.addAll(disneylandOrders);
		return allOrders;
	}

	/**
	 * Fetches all approved or prepaid reservations for a user in a specified park.
	 * Looks up reservations by user ID and park name, returning those that have
	 * been approved or have received payment. Each reservation's details, like
	 * visitor count and visit times, are included.
	 * 
	 * @param id       The user's ID.
	 * @param parkName The park's name.
	 * @return A list of the user's reservations in that park.
	 */
	public ArrayList<Reservation> GetAllReservationsById(String id, String parkName) {
		Statement stmt;
		ArrayList<Reservation> orders = new ArrayList<>();
		Integer reservationID = -1;
		String userID = "", numOFVisitors = "", visitDay = "", visitTime = "", phoneNum = "", email = "",
				visitorsType = "";
		boolean prepayment = false;
		String query = "SELECT * FROM " + parkName + " WHERE UserID = '" + id
				+ "' AND (ReservationStatus = 'Approve' OR ReservationStatus = 'Payment')";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				reservationID = rs.getInt("ReservationID");
				userID = rs.getString("UserID");
				numOFVisitors = rs.getString("NumOFVisitors");
				visitDay = rs.getString("visitDay");
				visitTime = rs.getString("VisitTime");
				phoneNum = rs.getString("PhoneNum");
				email = rs.getString("Email");
				// reservationStatus = rs.getString("ReservationStatus");
				visitorsType = rs.getString("VisitorsType");
				prepayment = rs.getBoolean("Prepayment");
				orders.add(new Reservation(reservationID, userID, parkName, numOFVisitors, visitDay, visitTime,
						phoneNum, email, prepayment, visitorsType));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * Removes outdated reservations from the waiting list of a specified park. This
	 * method deletes records from the park's waiting list that are dated before
	 * today, effectively cleaning up past reservation attempts that were never
	 * fulfilled. It iterates through the waiting list, identifying and removing
	 * such outdated entries.
	 * 
	 * @param parkName - The name of the park whose waiting list is to be cleaned.
	 */
	public void DeletionOfOutdatedRecordsFromWaitingList(String parkName) {
		LocalDate today = LocalDate.now();
		String todaySTR = today.toString();
		String tableName = "waiting_list_" + parkName.toLowerCase();
		Statement stmt;
		Integer queueID = -1;
		String visitDay = "", visitTime = "";
		String query = "SELECT * FROM " + tableName + " WHERE VisitDay < '" + todaySTR + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				queueID = rs.getInt("QueueID");
				visitDay = rs.getString("visitDay");
				visitTime = rs.getString("VisitTime");
				DeleteFromWaitingList(parkName, visitDay, visitTime, queueID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets details for parks managed by a given Department Manager ID. Queries the
	 * database for parks managed by the specified ID, including park name, max
	 * visitors, stay duration, and manager details. It then converts manager IDs to
	 * names by looking up each manager in the workers table.
	 * 
	 * @param id Department Manager's ID.
	 * @return List of parks' details managed by the Department Manager.
	 */
	public ArrayList<ParkDetails> GetAllParkDetailsByDMId(String id) {
		Statement stmt;
		ArrayList<ParkDetails> parkDetails = new ArrayList<>();
		String parkManagerName = "", parkName = "", timeOfStay = "", difference = "", maxOfVisitors = "",
				firstName = "", lastName = "";
		String query = "SELECT * FROM parks_details WHERE ParkDepartmentManager = '" + id + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				parkName = rs.getString("ParkName");
				maxOfVisitors = rs.getString("MaxOfVisitors");
				timeOfStay = rs.getString("TimeOfStay");
				parkManagerName = rs.getString("ParkManager");
				difference = rs.getString("Difference");
				parkDetails.add(new ParkDetails(parkManagerName, parkName, timeOfStay, difference, maxOfVisitors));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int i = 0;
		while (i < parkDetails.size()) {
			String query1 = "SELECT FirstName,LastName FROM workers WHERE WorkerID = '"
					+ parkDetails.get(i).getParkManagerName() + "'";
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query1);
				ResultSet rs = ps.executeQuery(query1);
				while (rs.next()) {
					firstName = rs.getString("FirstName");
					lastName = rs.getString("LastName");
					String name = firstName + " " + lastName;
					parkDetails.get(i).setParkManagerName(name);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}
		return parkDetails;
	}

	public ParkDetails GetAllParkDetailsById(String id) {
		Statement stmt;
		ParkDetails parkDetails = new ParkDetails("", "", "", "", "");
		String parkManagerName = "", parkName = "", timeOfStay = "", difference = "", maxOfVisitors = "",
				firstName = "", lastName = "";
		String query = "SELECT * FROM parks_details WHERE ParkManager = '" + id + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				parkName = rs.getString("ParkName");
				maxOfVisitors = rs.getString("MaxOfVisitors");
				timeOfStay = rs.getString("TimeOfStay");
				parkManagerName = rs.getString("ParkManager");
				difference = rs.getString("Difference");
				parkDetails = new ParkDetails(parkManagerName, parkName, timeOfStay, difference, maxOfVisitors);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String query1 = "SELECT FirstName,LastName FROM workers WHERE WorkerID = '" + parkDetails.getParkManagerName()
				+ "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query1);
			ResultSet rs = ps.executeQuery(query1);
			while (rs.next()) {
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				String name = firstName + " " + lastName;
				parkDetails.setParkManagerName(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parkDetails;
	}

	/**
	 * Retrieves park details for a given park manager's ID. Queries the park's
	 * details managed by the specified manager, including name, visitor limits, and
	 * stay duration. Additionally, converts the manager's ID to their full name for
	 * easier identification.
	 * 
	 * @param id ID of the park manager.
	 * @return Park details including manager's name and park's operational
	 *         parameters.
	 */
	public static void ImportWorkersData() {
		Statement stmt, stmt1;
		String workerID = "", firstName = "", lastName = "", email = "", workerRole = "", park = "", userPassword = "";
		String externalTable = "";
		String query2 = "SELECT * FROM import_data";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query2);
			ResultSet rs = ps.executeQuery(query2);
			while (rs.next()) {
				externalTable = rs.getString("ExternalTable");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(externalTable);
		if (externalTable.equals("no")) {
			String query3 = "UPDATE import_data SET ExternalTable = 'yes'";
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query3);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String query = "SELECT * FROM external_table";
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery(query);
				while (rs.next()) {
					workerID = rs.getString("WorkerID");
					firstName = rs.getString("FirstName");
					lastName = rs.getString("LastName");
					email = rs.getString("Email");
					workerRole = rs.getString("WorkerRole");
					park = rs.getString("Park");
					userPassword = rs.getString("UserPassword");
					String query1 = "INSERT INTO workers (WorkerID, FirstName, LastName, Email, WorkerRole, Park,ConnectionStatus, UserPassword) VALUES (?, ?, ?, ?, ?, ?, ? ,?)";
					try {
						stmt1 = con.createStatement();
						PreparedStatement ps1 = con.prepareStatement(query1);
						ps1.setString(1, workerID);
						ps1.setString(2, firstName);
						ps1.setString(3, lastName);
						ps1.setString(4, email);
						ps1.setString(5, workerRole);
						ps1.setString(6, park);
						ps1.setString(7, "Disconnected");
						ps1.setString(8, userPassword);
						ps1.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String message = "Dear user,\nyou have just performed the insertion of an external table into your database.\nThank you,\nGo Nature system.";
			JOptionPane.showMessageDialog(null, message);
		} else {
			String message = "Dear user,\nthe insertion of an external table has ALREADY BEEN PERFORMED.\nTherefore, it is not possible to perform it again.\nThank you,\nGo Nature system.";
			JOptionPane.showMessageDialog(null, message);
		}

	}

	/**
	 * Imports user data from an external table to the main users table if it hasn't
	 * been done already. First, checks whether the import process has occurred. If
	 * not, it proceeds to import user data, updating their status to indicate
	 * completion. Users are informed through a message about the import status -
	 * whether it was successful or had already been completed previously.
	 */
	public static void ImportUsersData() {
		Statement stmt, stmt1;
		String userID = "", userType = "", userPassword = "", phoneNum = "", email = "";
		String externalUser = "";
		String query2 = "SELECT * FROM import_data";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query2);
			ResultSet rs = ps.executeQuery(query2);
			while (rs.next()) {
				externalUser = rs.getString("ExternalUser");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(externalUser);
		if (externalUser.equals("no")) {
			String query3 = "UPDATE import_data SET ExternalUser = 'yes'";
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query3);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String query = "SELECT * FROM external_user";
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery(query);
				while (rs.next()) {
					userID = rs.getString("UserID");
					userType = rs.getString("UserType");
					userPassword = rs.getString("UserPassword");
					phoneNum = rs.getString("PhoneNum");
					email = rs.getString("Email");
					// UserID, UserType, UserPassword, PhoneNum, Email, UserName, ConnectionStatus,
					// Potential
					String query1 = "INSERT INTO users (UserID, UserType, UserPassword, PhoneNum, Email, UserName,ConnectionStatus,Potential) VALUES (?, ?, ?, ?, ?, ?, ?, ? )";
					try {
						stmt1 = con.createStatement();
						PreparedStatement ps1 = con.prepareStatement(query1);
						ps1.setString(1, userID);
						ps1.setString(2, "Single");
						ps1.setString(3, userPassword);
						ps1.setString(4, phoneNum);
						ps1.setString(5, email);
						ps1.setString(6, userID);
						ps1.setString(7, "Disconnected");
						ps1.setString(8, userType);
						ps1.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String message = "Dear user,\nyou have just performed the insertion of an external table into your database.\nThank you,\nGo Nature system.";
			JOptionPane.showMessageDialog(null, message);
		} else {
			String message = "Dear user,\nthe insertion of an external table has ALREADY BEEN PERFORMED.\nTherefore, it is not possible to perform it again.\nThank you,\nGo Nature system.";
			JOptionPane.showMessageDialog(null, message);
		}
	}

	/**
	 * Marks a reservation as paid. Verifies if a specific reservation hasn't been
	 * paid yet. If that's the case, updates its status to indicate the payment has
	 * been made.
	 * 
	 * @param order Details of the reservation to update.
	 */
	public void ChangeStatusToPayment(Reservation order) {
		Statement stmt;
		boolean payment = false;
		String query = "SELECT Prepayment FROM " + order.getParkName().toLowerCase() + " WHERE ReservationID = '"
				+ order.getReservationID().toString() + "'";
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				payment = rs.getBoolean("Prepayment");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!payment) {
			payment = true;
			String query1 = "UPDATE " + order.getParkName().toLowerCase() + " SET Prepayment = '" + payment
					+ "' WHERE ReservationID = '" + order.getReservationID().toString() + "'";
			try {
				stmt = con.createStatement();
				PreparedStatement ps = con.prepareStatement(query1);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
