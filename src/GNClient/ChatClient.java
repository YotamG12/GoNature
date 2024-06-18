// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package GNClient;

import ocsf.client.*;
import GNClient.*;
import entities.CanceledReport;
import entities.Parameters;
import entities.ParkDetails;
import entities.Request;
import entities.Reservation;
import entities.User;
import entities.Visit;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ClientUI clientUI;

	// public static List<Reservation> reservation= new ArrayList<>();
	public static List<String> AvailableSlots = new ArrayList<>();
	public static List<String> relevantParks = new ArrayList<>();
	public static List<Integer> visitorsReportData = new ArrayList<>();
	public static List<String> useReportData = new ArrayList<>();
	public static boolean check;
	public static boolean awaitResponse = false;
	public static boolean logout;
	public static String confirmationResevation;
	public static String confirmationWaitingList;
	public static String totalAmount;
	public static String createReportStatus;
	public static String OccasionalReservasion;
	public static String RegisterGroupGuide;
	public static String checkReservationID;
	public static boolean checkGroupGuide;
	public static boolean LeavingTime;
	public static boolean SendToUpdate;
	public static boolean cancelreservation;
	public static boolean approvereservation;
	public static ParkDetails parkDetails;
	public static Reservation EntranceTime;
	public static ArrayList<Visit> visit;
	public static ArrayList<Parameters> parametersToApprove;
	public static ArrayList<Reservation> reservation;
	public static ArrayList<ParkDetails> parkDetailsDm;
	public static CanceledReport canceledReport;

	public static User user;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ClientUI clientUI) throws IOException {
		super(host, port); // Call the superclass constructor

		this.clientUI = clientUI;

		try {
			openConnection();
			System.out.println("connection succeded");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {

		System.out.println("--> handleMessageFromServer");
		Request message;
		try {
			message = Request.fromBytesToObject((byte[]) msg);
			System.out.println(message.toString());
			switch (message.getType()) {

			case CONFIRMATION_SINGLE_RESERVATION:
				confirmationResevation = (String) message.getRequest();
				System.out.println(confirmationResevation);
				break;
			case CONFIRMATION_GROUP_RESERVATION:
				confirmationResevation = (String) message.getRequest();
				break;
			case ENTER_WAITING_LIST:
				confirmationWaitingList = (String) message.getRequest();
				System.out.println(confirmationWaitingList);
				break;
			case FREE_SLOTS_DATA:
				AvailableSlots = (ArrayList) message.getRequest();
				break;
			case PREPAYMENT:
				totalAmount = (String) message.getRequest();
				break;
			case WORKER_LOGIN:
				user = (User) message.getRequest();
				break;
			case TRAVELER_LOGIN:
				user = (User) message.getRequest();
				break;
			case RELEVANT_PARKS:
				relevantParks = (ArrayList) message.getRequest();
				break;
			case TOTAL_VISITORS_NUM_REPORT:
				createReportStatus = (String) message.getRequest();
				break;
			case USE_REPORT:
				createReportStatus = (String) message.getRequest();
				break;
			case VIEW_TOTAL_VISITORS_NUM_REPORT:
				visitorsReportData = (ArrayList) message.getRequest();
				break;
			case VIEW_USE_REPORT:
				useReportData = (ArrayList) message.getRequest();
				break;
			case WORKER_LOGOUT:
				logout = true;
				break;
			case TRAVELER_LOGOUT:
				logout = true;
				break;
			case SET_ENTRANCE_TIME:
				EntranceTime = (Reservation) message.getRequest();
				break;
			case SET_LEAVING_TIME:
				LeavingTime = (boolean) message.getRequest();
				break;
			case NEW_OCCASIONAL_RESERVATION:
				OccasionalReservasion = (String) message.getRequest();
				break;
			case REGISTER_GROUP_GUIDE:
				RegisterGroupGuide = (String) message.getRequest();
				break;
			case VISITS_REPORT:
				visit = (ArrayList<Visit>) message.getRequest();
				break;
			case PAYMENT:
				totalAmount = (String) message.getRequest();
				break;
			case CHECK_GROUP_GUIDE:
				checkGroupGuide = (boolean) message.getRequest();
				break;
			case CHECK_RESERVATION_ID:
				checkReservationID = (String) message.getRequest();
				break;
			case CANCELATION_REPORT:
				canceledReport = (CanceledReport) message.getRequest();
				System.out.println(canceledReport);
				break;
			case CANCELATION_REPORT_ALL:
				canceledReport = (CanceledReport) message.getRequest();
				break;
			case UPDATE_PARAMETERS:
				SendToUpdate = (boolean) message.getRequest();
				break;
			case APPROVED_PARAMETERS:
				parametersToApprove = (ArrayList<Parameters>) message.getRequest();
				break;
			case OCCASIONAL_PAYMENT:
				totalAmount = (String) message.getRequest();
				break;
			case APPROVE_RESERVATION:
				approvereservation = (boolean) message.getRequest();
				break;
			case CANCEL_RESERVATIONS:
				cancelreservation = (boolean) message.getRequest();
				break;
			case GET_ALL_RESERVATIONS:
				reservation = (ArrayList<Reservation>) message.getRequest();
				break;
			case PARK_DETAILS_DM:
				parkDetailsDm = (ArrayList<ParkDetails>) message.getRequest();
				break;
			case PARK_DETAILS:
				parkDetails = (ParkDetails) message.getRequest();
				break;

			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		awaitResponse = false;

	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(Object message) {

		try {
			awaitResponse = true;
			System.out.println(message.toString());
			sendToServer(message);

			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			// clientUI.display("Could not send message to server: Terminating client."+ e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {

		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class
