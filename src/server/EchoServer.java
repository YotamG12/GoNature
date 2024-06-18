// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import common.RequestType;
import GNServer.ServerGUIController;
import entities.ConnectedClient;
import entities.Parameters;
import entities.ParkDetails;
import entities.Report;
import entities.Request;
import entities.Reservation;
import entities.SetTime;
import entities.User;
import GNServer.DataBaseController;
import GNServer.SendNotification;
import ocsf.server.*;

/**
 * The EchoServer class is responsible for handling communication between the server and the clients.
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	 /** Database controller instance for interacting with the database. */
	DataBaseController db = new DataBaseController(ServerGUIController.hostName,ServerGUIController.sechma,ServerGUIController.userName,ServerGUIController.password);
	 /** ArrayList to store client connections. */
	public static ArrayList<ConnectedClient> clientConnections;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
		clientConnections = new ArrayList<>();
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 * @throws IOException
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Request message;
		Request request = new Request();
		if (msg == null) {
			try {
				client.sendToClient(null);
			} catch (Exception e) {
			}
			;
		}
		try {
			message = Request.fromBytesToObject((byte[]) msg);
			System.out.println(message.toString());
	

//			if (msg instanceof Request) {
				System.out.println(message.toString());
				System.out.println(message.getType().toString());
				switch (message.getType()) {
				case NEW_SINGLE_RESERVATION:
					try {
						System.out.println(message.getRequest().toString());
						Reservation order = (Reservation) message.getRequest();
						System.out.println(order.toString());
						System.out.println(db.CheckFreeSlots(order));
						if (db.CheckFreeSlots(order)) {
							System.out.println("There is free slots");
							db.InsertNewReservation(order);
							System.out.println("Insert New Reservation");
							db.InsertNewSingleVisitor(order);
							System.out.println("Insert New Single Visitor");
							request.setRequest(order.getReservationID().toString());
							request.setType(RequestType.CONFIRMATION_SINGLE_RESERVATION);
							byte[] arr;
							try {
								arr = request.getBytes();
								System.out.println("Serialized bytes: " + arr.length);
								client.sendToClient(arr);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							request.setRequest("no");
							request.setType(RequestType.CONFIRMATION_SINGLE_RESERVATION);
							byte[] arr;
							try {
								arr = request.getBytes();
								System.out.println("Serialized bytes: " + arr.length);
								client.sendToClient(arr);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
					}
					;
					break;
				case ENTER_WAITING_LIST:
					try {
						Reservation order = (Reservation) message.getRequest();
						db.EnterToWaitingList(order);
						if(order.getType().equals("Single"))
							db.InsertNewSingleVisitor(order); //TODO
						request.setRequest("yes");
						request.setType(RequestType.ENTER_WAITING_LIST);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					}
					;
					break;
				case FREE_SLOTS_DATA:
					try {
						Reservation order = (Reservation) message.getRequest();
						ArrayList<String> freeSlots = db.CheckFreeSlotsByDate(order);
						request.setRequest(freeSlots);
						request.setType(RequestType.FREE_SLOTS_DATA);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					}
					;
					break;
				case NEW_GROUP_RESERVATION:
					try {
						Reservation order = (Reservation) message.getRequest();
						if (db.CheckFreeSlots(order)) {
							db.InsertNewReservation(order);
							request.setRequest(order.getReservationID().toString());
							request.setType(RequestType.CONFIRMATION_GROUP_RESERVATION);
							byte[] arr;
							try {
								arr = request.getBytes();
								System.out.println("Serialized bytes: " + arr.length);
								client.sendToClient(arr);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							request.setRequest("no");
							request.setType(RequestType.CONFIRMATION_GROUP_RESERVATION);
							byte[] arr;
							try {
								arr = request.getBytes();
								System.out.println("Serialized bytes: " + arr.length);
								client.sendToClient(arr);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
					}
					;
					break;
				case PREPAYMENT:
					try {
						Reservation order = (Reservation) message.getRequest();
						String price = order.calculateaAnotherDiscount(order.calculatePriceGroupPreorder());
						request.setRequest(price);
						request.setType(RequestType.PREPAYMENT);
						db.ChangeStatustoPayment(order);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					}
					;
					break;
				case TRAVELER_LOGIN:
					try {
						User user = (User) message.getRequest();
						request.setRequest(db.TravellerLogin(user));
						request.setType(RequestType.TRAVELER_LOGIN);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case TRAVELER_LOGOUT:
					try {
						User user = (User) message.getRequest();
						db.TravellerLogout(user);
						request.setRequest(true);
						request.setType(RequestType.TRAVELER_LOGOUT);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case WORKER_LOGIN:
					try {
						User user = (User) message.getRequest();
						System.out.println(user.toString());
						request.setRequest(db.WorkerLogin(user));
						System.out.println(user.toString());
						request.setType(RequestType.WORKER_LOGIN);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case WORKER_LOGOUT:
					try {
						User user = (User) message.getRequest();
						db.WorkerLogout(user);
						request.setRequest(true);
						request.setType(RequestType.WORKER_LOGOUT);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case RELEVANT_PARKS:
					try {
						User user = (User) message.getRequest();
						request.setRequest(db.DMparks(user));
						request.setType(RequestType.RELEVANT_PARKS);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case TOTAL_VISITORS_NUM_REPORT:
					try {
						Report report = (Report) message.getRequest();
						request.setRequest(db.TotalVisitorsNumReport(report));
						request.setType(RequestType.TOTAL_VISITORS_NUM_REPORT);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case VIEW_TOTAL_VISITORS_NUM_REPORT:
					try {
						Report report = (Report) message.getRequest();
						request.setRequest(db.GetTotalVisitorsNumReport(report));
						request.setType(RequestType.VIEW_TOTAL_VISITORS_NUM_REPORT);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case USE_REPORT:
					try {
						Report report = (Report) message.getRequest();
						request.setRequest(db.CreateUseReport(report));
						request.setType(RequestType.USE_REPORT);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case VIEW_USE_REPORT:
					try {
						Report report = (Report) message.getRequest();
						if(db.IsUseReportExist(report))
							request.setRequest(db.DisplayUseReport(report));
						else {
							ArrayList<String> result = new ArrayList<>();
							//result.add();
							request.setRequest(result);
						}
						request.setType(RequestType.VIEW_USE_REPORT);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case SET_ENTRANCE_TIME:
					try { 
						SetTime setTime = (SetTime) message.getRequest();
						request.setRequest(db.EntranceToThePark(setTime));
						request.setType(RequestType.SET_ENTRANCE_TIME);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case SET_LEAVING_TIME:
					try { 
						SetTime setTime = (SetTime) message.getRequest();
						request.setRequest(db.ExitFromThePark(setTime));
						request.setType(RequestType.SET_LEAVING_TIME);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case PAYMENT:
					try { 
						Reservation order = (Reservation) message.getRequest();
						switch (order.getType()) {
						case "Single":
							request.setRequest(order.calculatePriceSinglePreorder());
							db.ChangeStatusToPayment(order);
							break;
						case "Group":
							request.setRequest(order.calculatePriceGroupPreorder());
							db.ChangeStatusToPayment(order);
							break;
						}
						request.setType(RequestType.PAYMENT);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case OCCASIONAL_PAYMENT:
					try { 
						Reservation order = (Reservation) message.getRequest();
						switch (order.getType()) {
						case "Single":
							request.setRequest(order.calculatePriceSingle());
							db.ChangeStatusToPayment(order);
							break;
						case "Group":
							request.setRequest(order.calculatePriceGroup());
							db.ChangeStatusToPayment(order);
							break;
						}
						request.setType(RequestType.OCCASIONAL_PAYMENT);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case CHECK_RESERVATION_ID:
					try { 
						SetTime setTime = (SetTime) message.getRequest();
						request.setRequest(db.checkReservationIDWhenEntranceToThePark(setTime));
						request.setType(RequestType.CHECK_RESERVATION_ID);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case REGISTER_GROUP_GUIDE:
					try { 
						String id = (String) message.getRequest();
						request.setRequest(db.RegisterGroupGuide(id));
						request.setType(RequestType.REGISTER_GROUP_GUIDE);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case VISITS_REPORT:
					try {
						Report report = (Report) message.getRequest();
						request.setRequest(db.VisitReport(report));
						request.setType(RequestType.VISITS_REPORT);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case CANCELATION_REPORT:
					try {
						Report report = (Report) message.getRequest();
						request.setRequest(db.CreateCanceledReport(report));
						request.setType(RequestType.CANCELATION_REPORT);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case CANCELATION_REPORT_ALL:
					try {
						Report report = (Report) message.getRequest();
						request.setRequest(db.CreateCanceledReportForAllParks(report));
						request.setType(RequestType.CANCELATION_REPORT_ALL);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case NEW_OCCASIONAL_RESERVATION:
					try { 
						Reservation order = (Reservation) message.getRequest();
						if(db.CheckFreeSlotsByHourForReport(order)) {
								db.InsertNewReservation(order);
								request.setRequest(order.getReservationID().toString());
						}
						else 
							request.setRequest("no");
						request.setType(RequestType.NEW_OCCASIONAL_RESERVATION);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case CHECK_GROUP_GUIDE:
					try { 
						String id = (String) message.getRequest();
						request.setRequest(db.CheckGroupGuide(id));
						request.setType(RequestType.CHECK_GROUP_GUIDE);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case UPDATE_PARAMETERS:
					try { 
						Parameters parameters = (Parameters) message.getRequest();
						request.setRequest(db.RequestUpdateParameters(parameters));
						request.setType(RequestType.UPDATE_PARAMETERS);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case APPROVED_PARAMETERS:
					try { 
						String id = (String) message.getRequest();
						request.setRequest(db.DisplayParametersForDM(id));
						request.setType(RequestType.APPROVED_PARAMETERS);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case APPROVED_DECLINED:
					try { 
						Parameters parameters = (Parameters) message.getRequest();
						request.setRequest(db.AprrovedDeclinedParameters(parameters));
						request.setType(RequestType.APPROVED_DECLINED);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case APPROVE_RESERVATION:
					try { 
						Reservation order = (Reservation) message.getRequest();
						request.setRequest(true);
						request.setType(RequestType.APPROVE_RESERVATION);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case CANCEL_RESERVATIONS:
					try { 
						Reservation order = (Reservation) message.getRequest(); //TODO - We need to check waiting list!!!!
						db.CancelReservation(order);
						request.setRequest(true);
						request.setType(RequestType.CANCEL_RESERVATIONS);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
						db.SwitchingFromWaitingListToRegularReservation(order);
					} catch (Exception e) {
					};
					break;
				case GET_ALL_RESERVATIONS:
					try { 
						String id = (String) message.getRequest();
						request.setRequest(db.GetAllReservationsFromAllParksById(id));
						request.setType(RequestType.GET_ALL_RESERVATIONS);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case PARK_DETAILS_DM:
					try { 
						String id = (String) message.getRequest(); 
						request.setRequest(db.GetAllParkDetailsByDMId(id));
						request.setType(RequestType.PARK_DETAILS_DM);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				case PARK_DETAILS:
					try { 
						String id = (String) message.getRequest(); 
						request.setRequest(db.GetAllParkDetailsById(id));
						request.setType(RequestType.PARK_DETAILS);
						byte[] arr;
						try {
							arr = request.getBytes();
							System.out.println("Serialized bytes: " + arr.length);
							client.sendToClient(arr);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
					};
					break;
				
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

//	/**
//	 * This method is responsible for the creation of the server instance (there is
//	 * no UI in this phase).
//	 *
//	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
//	 *                is entered.
//	 */
//	public static void main(String[] args) {
//		int port = 0; // Port to listen on
//
//		try {
//			port = Integer.parseInt(args[0]); // Get port from command line
//		} catch (Throwable t) {
//			port = DEFAULT_PORT; // Set port to 5555
//		}
//
//		EchoServer sv = new EchoServer(port);
//		SendNotification notification = new SendNotification();
//		notification.start();
//		try {
//			sv.listen(); // Start listening for connections
//		} catch (Exception ex) {
//			System.out.println("ERROR - Could not listen for clients!");
//		}
//	}

////////////////////////////////////////////////////////////////////
//	@Override
//	protected void clientConnected(ConnectionToClient client) {
//		String clientHost = client.getInetAddress().getHostName();
//		String clientIP = client.getInetAddress().getHostAddress();
//
//		System.out.println("Client connected: " + clientHost + " (" + clientIP + ")");
//	}
	
	@Override
	protected void clientConnected(ConnectionToClient client) {
		String clientHost = client.getInetAddress().getHostName();
		String clientIP = client.getInetAddress().getHostAddress();

		ConnectedClient connectedClient = new ConnectedClient(client, clientHost, clientIP);
		clientConnections.add(connectedClient);

		System.out.println("Client connected: " + clientHost + " (" + clientIP + ")");
	}

}
//End of EchoServer class
