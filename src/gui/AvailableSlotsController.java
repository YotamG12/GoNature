package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import GNClient.ClientUI;
import common.RequestType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Request;
import entities.Reservation;
import entities.User;

/**
 * The  AvailableSlotsController class controls the GUI for displaying
 * available slots for reservations. It enables users to select a new entrance
 * time for their reservation from the available slots provided by the system.
 */
public class AvailableSlotsController implements Initializable {
	public static Reservation newReservation;
	private List<String> EmptyList = new ArrayList<>();

	@FXML
	private ChoiceBox<String> CboxAvailableTime;

	@FXML
	private Button btnSubmit;

	@FXML
	private Button btnExit;

	@FXML
	private Label lblError;

	@FXML
	private Label lblText;

	@FXML
	private Label lblTitle;

	public static User user = new User("", "");

	@FXML
	void Exit(ActionEvent event) throws IOException {
		logoutFunc(event);
		ClientUI.chat.closeConnection();
		ClientUI.chat.quit();
	}

	/**
	 * Updates the entrance time for the reservation based on the user's selection
	 * and communicates with the server to confirm the updated reservation.
	 *
	 * @param event The action event that triggered this method.
	 * @throws IOException if an error occurs during the FXML file loading.
	 */
	@FXML
	void UpdateReservationEntranceTime(ActionEvent event) throws IOException {
		/**
		 * Initializes the controller class. This method is automatically called after
		 * the FXML file has been loaded. It populates the choice box with available
		 * slots or displays an error if no slots are available.
		 *
		 * @param arg0 The location used to resolve relative paths for the root object,
		 *             or null if the location is not known.
		 * @param arg1 The resources used to localize the root object, or null if the
		 *             root object was not localized.
		 */
		newReservation.setTime(CboxAvailableTime.getValue());

		if (newReservation.getType().equals("Single")) {
			Request request = new Request();
			request.setType(RequestType.NEW_SINGLE_RESERVATION);
			request.setRequest(newReservation);
			byte[] arr;
			try {
				arr = request.getBytes();
				System.out.println("Serialized bytes: " + arr.length);
				ClientUI.chat.handleMessageFromClientUI(arr);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			Request request = new Request();
			request.setType(RequestType.NEW_SINGLE_RESERVATION);
			request.setRequest(newReservation);
			byte[] arr;
			try {
				arr = request.getBytes();
				System.out.println("Serialized bytes: " + arr.length);
				ClientUI.chat.handleMessageFromClientUI(arr);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		if (ClientUI.chat.confirmationResevation.equals("no")) {
			// Exit Button

		} else {
			System.out.println("got here");
			newReservation.setReservationID(Integer.parseInt(ClientUI.chat.confirmationResevation));
			ReservationConfimationController.newReservation = newReservation;

			((Node) event.getSource()).getScene().getWindow().hide();
			; // hiding primary window
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/ReservationConfirmation.fxml"));
			ReservationConfimationController reservationconfimationcontroller = new ReservationConfimationController();

			Scene scene = new Scene(root);
			// scene.getStylesheets().add(getClass().getResource("/fxml/ReservationConfirmation.css").toExternalForm());
			primaryStage.setTitle("");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		}

	}
	 /**
     * Logs out the current user and navigates back to the welcome page.
     *
     * @param event The action event that triggered this method.
     */
	void logoutFunc(ActionEvent event) {
		Request request = new Request();
		if (!user.getPermission().equals("")) {
			if (user.getPermission().equals("Single") || user.getPermission().equals("Group")) {
				request.setType(RequestType.TRAVELER_LOGOUT);

			} else {
				request.setType(RequestType.WORKER_LOGOUT);
			}

			request.setRequest(user);
			System.out.println(request.getRequest().toString());
			byte[] arr;
			try {
				arr = request.getBytes();
				System.out.println("Serialized bytes: " + arr.length);
				ClientUI.chat.handleMessageFromClientUI(arr);
				if (ClientUI.chat.logout == true) {// Navigating back to the welcome page upon successful logout
					((Node) event.getSource()).getScene().getWindow().hide();
					; // hiding primary window
					Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomePage.fxml"));
					Stage primaryStage = new Stage();
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("/fxml/WelcomePage.css").toExternalForm());
					primaryStage.setTitle("Welcome Page");
					primaryStage.initStyle(StageStyle.UNDECORATED);
					primaryStage.setScene(scene);
					primaryStage.setResizable(false);
					primaryStage.show();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	 /**
     * Navigates the user to the Home page based on their permissions.
     *
     * @param event The action event that triggered this method.
     * @throws IOException if an error occurs during navigation.
     */
	@FXML
	void Home(ActionEvent event) throws IOException {
		if (user.getPermission().equals("Group Guide")) {
			((Node) event.getSource()).getScene().getWindow().hide();
			; // hiding primary window
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/GroupGuideGUI.fxml"));

			Scene scene = new Scene(root);
			// scene1.getStylesheets().add(getClass().getResource("/fxml/GroupGuideGUI.css").toExternalForm());
			primaryStage.setTitle("");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} else {
			((Node) event.getSource()).getScene().getWindow().hide();
			; // hiding primary window
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomePage.fxml"));

			Scene scene = new Scene(root);
			// scene1.getStylesheets().add(getClass().getResource("/fxml/WelcomePage.css").toExternalForm());
			primaryStage.setTitle("");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		}

	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (ClientUI.chat.AvailableSlots.equals(EmptyList)) {
			lblError.setVisible(true);
			btnExit.setVisible(true);
		} else {
			CboxAvailableTime.getItems().addAll(ClientUI.chat.AvailableSlots);
		}

	}
	public void start(Stage primaryStage) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/AvailableSlots.fxml"));
		AvailableSlotsController availableSlotsController = new AvailableSlotsController();		
	
		Scene scene = new Scene(root);			
		//scene.getStylesheets().add(getClass().getResource("/fxml/AvailableSlots.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);

		
		primaryStage.show();
	}

}
