package gui;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

import GNClient.ChatClient;
import GNClient.ClientUI;
import common.RequestType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Request;
import entities.Reservation;
import entities.User;
/**
 * Controller for handling the waiting list confirmation view. 
 * It allows users to see the confirmation details of their reservation placed in the waiting list.
 */
public class WaitingListConfimationController implements Initializable {
	@FXML
	private Button btnExit;

	@FXML
	private TextField txtDate;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtID;

	@FXML
	private TextField txtNOV;

	@FXML
	private TextField txtPN;

	@FXML
	private TextField txtRID3;

	@FXML
	private TextField txtTN;

	@FXML
	private TextField txtTime;

	@FXML
	private Pane txtWL;
	public static Reservation reservation;

	public static User user = new User("", "");

    /**
     * Handles the Exit button action. This method closes the current view and 
     * logs the user out from the system, then redirects to the welcome page.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during navigation.
     */
	@FXML
	void Exit(ActionEvent event) throws IOException {
		logoutFunc(event);
		ClientUI.chat.closeConnection();
		ClientUI.chat.quit();
	}
	 /**
     * Performs the logout operation and navigates the user to the Welcome Page.
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
				if (ClientUI.chat.logout == true) {
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
     * Fills in the reservation details into the confirmation form.
     * This method is called upon initialization to display the reservation details.
     *
     * @param reservation The reservation object containing the details to be displayed.
     */
	// open the page if return "yes"
	public void CreateConfirmationWaitingList(Reservation reservation) {
		System.out.println(reservation);
    	txtID.setText(reservation.getUserID());
    	txtPN.setText(reservation.getParkName());
    	txtNOV.setText(reservation.getNumOfVisitors());
    	txtDate.setText(reservation.getDate());
    	txtTime.setText(reservation.getTime());
    	txtTN.setText(reservation.getPhoneNum());
    	txtEmail.setText(reservation.getEmail());

	}
	 /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It populates the form with the reservation details.
     *
     * @param arg0 The location used to resolve relative paths for the root object, or null if not known.
     * @param arg1 The resources used to localize the root object, or null if the root object was not localized.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CreateConfirmationWaitingList(reservation);
	}
	 /**
     * Handles the Home button action. This method navigates the user to their respective home page
     * based on their permission level.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during navigation.
     */
	@FXML
	void Home(ActionEvent event) throws IOException {
		if (user.getPermission().equals("Group")) {
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
	/**
     * Starts the waiting list confirmation view. 
     * This method sets up and displays the scene for the waiting list confirmation.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML.
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/WaitingListConfimation.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/WaitingListConfimation.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	} 

}
