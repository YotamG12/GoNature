package gui;


import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import GNClient.ClientUI;
import common.RequestType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import entities.Request;
import entities.Reservation;
import entities.User;
/**
 * The {@code ReservationConfimationController} class controls the reservation confirmation
 * GUI. It allows users to view the details of their reservation, including the option
 * to make a prepayment for group reservations.
 */
/**
 * Controls the reservation confirmation screen, displaying reservation details and allowing for prepayment
 * for group reservations. It provides functionalities to go back to the home screen, exit the application,
 * and initiate prepayment for eligible reservations.
 */

public class ReservationConfimationController implements Initializable , Serializable {
	
	    @FXML
	    private Label lblCON;
	    
	    @FXML
	    private Label lblPrepayment;
	    
	    @FXML
	    private Button btnPrePayment;
	    
	    @FXML
	    private Button btnHome;
	    
	    @FXML
	    private Button btnExit;

	    @FXML
	    private Label lblDate;

	    @FXML
	    private Label lblET;

	    @FXML
	    private Label lblEmail;

	    @FXML
	    private Label lblID;

	    @FXML
	    private Label lblNOV;

	    @FXML
	    private Label lblPN;

	    @FXML
	    private Label lblRC;

	    @FXML
	    private Label lblRID;

	    @FXML
	    private Label lblTN;

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
	    
	    public static Reservation newReservation;
	    public static User user=new User("","");
	    /**
	     * Navigates the user back to the home screen based on their permission level.
	     *
	     * @param event The event triggered by clicking the home button.
	     * @throws IOException If an error occurs during the navigation process.
	     */
	    @FXML
	    void Home(ActionEvent event) throws IOException {
	    	if(user.getPermission().equals("Group")) {
	    		((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
	    		Stage primaryStage = new Stage();
	    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/GroupGuideGUI.fxml"));

	    		Scene scene = new Scene(root);			
	    		//scene1.getStylesheets().add(getClass().getResource("/fxml/GroupGuideGUI.css").toExternalForm());
	    		primaryStage.setTitle("");
	    		primaryStage.initStyle(StageStyle.UNDECORATED);
	    		primaryStage.setScene(scene);
	    		primaryStage.setResizable(false);
	    		primaryStage.show();
	    	}
	    	else {
	    		((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
	    		Stage primaryStage = new Stage();
	    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomePage.fxml"));

	    		Scene scene = new Scene(root);			
	    		scene.getStylesheets().add(getClass().getResource("/fxml/WelcomePage.css").toExternalForm());
	    		primaryStage.setTitle("");
	    		primaryStage.initStyle(StageStyle.UNDECORATED);
	    		primaryStage.setScene(scene);
	    		primaryStage.setResizable(false);
	    		primaryStage.show();
	    	}
	    }
	    /**
	     * Logs out the user, closes the connection, and exits the application.
	     *
	     * @param event The event triggered by clicking the exit button.
	     * @throws IOException If an error occurs during the logout process.
	     */
	    @FXML
		void Exit (ActionEvent event) throws IOException {
	    	logoutFunc(event);
			ClientUI.chat.closeConnection();
	    	ClientUI.chat.quit();
		}

	    /**
	     * Handles user logout, terminating the current session and navigating back to the login screen.
	     *
	     * @param event The event triggered by clicking the logout button.
	     */
	    void logoutFunc(ActionEvent event) {
	    	Request request= new Request();
	    	if(!user.getPermission().equals("")) {
	    		if(user.getPermission().equals("Single")||user.getPermission().equals("Group") ) {
	    			request.setType(RequestType.TRAVELER_LOGOUT);
	    		
	    		}
	    		else {
	    			request.setType(RequestType.WORKER_LOGOUT);
	    		}
	    	
	    		request.setRequest(user);
	    		System.out.println(request.getRequest().toString());
	    		byte[] arr;
	    		try {
	    			arr = request.getBytes();
	    			System.out.println("Serialized bytes: " + arr.length);
	    			ClientUI.chat.handleMessageFromClientUI(arr);
	    			if(ClientUI.chat.logout==true) {
	    				((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
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
	     * Fills the text fields on the confirmation page with details from the reservation object.
	     *
	     * @param reservation The reservation whose details are to be displayed.
	     */
	    
	    public void SetTextConfirmationPage(Reservation reservation) {
	    	System.out.println(reservation);
	    	txtRID3.setText(reservation.getReservationID().toString());
	    	txtID.setText(reservation.getUserID());
	    	txtPN.setText(reservation.getParkName());
	    	txtNOV.setText(reservation.getNumOfVisitors());
	    	txtDate.setText(reservation.getDate());
	    	txtTime.setText(reservation.getTime());
	    	txtTN.setText(reservation.getPhoneNum());
	    	txtEmail.setText(reservation.getEmail());
	    	//newReservation=reservation;
	    	
	    	
	    }
		   /**
     * Makes the prepayment option visible on the confirmation page.
     */
	    
	    public void setPrePaymentVisible() {
	    	lblPrepayment.setVisible(true);
	    	btnPrePayment.setVisible(true);
	    }
	    /**
	     * Handles the action of clicking the prepayment button, initiating the prepayment process for the reservation.
	     *
	     * @param event The event triggered by clicking the prepayment button.
	     * @throws IOException If an error occurs during the navigation process.
	     */
	    @FXML
	    void CreatePrepayment(ActionEvent event) throws IOException{
	    	//on clicking at prepyment button
	    	newReservation.setPrepayment(true);
	    	Request request= new Request();
    		request.setType(RequestType.PREPAYMENT);
    		request.setRequest(newReservation);
    	        byte[] arr;
    	        try {
    	            arr = request.getBytes();
    	            System.out.println("Serialized bytes: " + arr.length);
    	            ClientUI.chat.handleMessageFromClientUI(arr);
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    	

	    	
	    	//set total amount in final bill page
	    	FinalBillController.totalAmount=ClientUI.chat.totalAmount;
	    	FinalBillController.reservation=newReservation;
	    	FinalBillController.user=user;
	    	
	    	//moving to Bill page
	    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/FinalBill.fxml"));
			FinalBillController reservationconfimationcontroller = new FinalBillController();		
		
			Scene scene = new Scene(root);			
			//scene.getStylesheets().add(getClass().getResource("/fxml/FinalBill.css").toExternalForm());
			primaryStage.setTitle("");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
	    	
	    }
	    /**
	     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
	     * It fills in the reservation details on the confirmation page and displays the prepayment option for group reservations.
	     */
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			
			SetTextConfirmationPage(newReservation);
			if(newReservation.getType().equals("Group")) {
				setPrePaymentVisible();
			}
		}
		 /**
	     * Initializes and starts the stage for the reservation confirmation screen.
	     *
	     * @param primaryStage The primary stage for this application.
	     * @throws Exception If an error occurs during loading the FXML file.
	     */
		public void start(Stage primaryStage) throws Exception {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/ReservationConfirmation.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/fxml/ReservationConfirmation.css").toExternalForm());
			primaryStage.setTitle("");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		}
	    
	    
	    
}
	    	
	   






