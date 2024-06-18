package gui;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import GNClient.ChatClient;
import GNClient.ClientUI;
import common.RequestType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Request;
import entities.Reservation;
import entities.SetTime;
import entities.User;
/**
 * Controller for the Worker Page in the application. 
 * It provides functionalities for workers to manage park entrances and exits, 
 * process payments, and handle occasional reservations.
 */
public class WorkerPageController implements Initializable{

    @FXML
    private Button btnEntance;

    @FXML
    private Button btnExitPark;
    
    @FXML
    private Button btnPayment;

    @FXML
    private Button btnSubmit;
    
    @FXML
    private Button btnLogout;
    
    @FXML
    private Button btnExit;
    
    @FXML
    private Button btnOR;

    
    @FXML
    private Label lblReservationID;
    
    @FXML
    private Label lblPay;
    
    @FXML
    private Label lblGoodbye;

    
    @FXML
    private TextField txtFReservationID;

    @FXML
    private Text txtParkName;
    
    @FXML
    private SplitPane splitPane1;
    
    public static User user;
    
    public static Reservation reservation;
    
    private SetTime setTime;
    /**
     * Logs out the current user and closes the application.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void Logout(ActionEvent event) {
    	logoutFunc(event);
    }
    /**
     * Exits the application.
     * Closes the connection to the server and quits the application.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during the process.
     */
    @FXML
	void Exit (ActionEvent event) throws IOException {
    	logoutFunc(event);
		ClientUI.chat.closeConnection();
    	ClientUI.chat.quit();
	}
    /**
     * Performs the logout operation.
     * This method sends a logout request to the server and navigates to the Welcome Page.
     *
     * @param event The action event triggering this method.
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
     * Handles the process for setting entrance times for visitors.
     * This method prepares the UI for inputting a reservation ID to set the entrance time.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void SetEntrance(ActionEvent event) {
    	lblGoodbye.setVisible(false);
    	lblPay.setVisible(false);
    	txtFReservationID.setVisible(true);
    	txtFReservationID.setText("");
    	//btnPayment.setVisible(true);
    	ClientUI.chat.EntranceTime=null;
    	ClientUI.chat.LeavingTime=false;
    	btnSubmit.setVisible(true);
    	setTime= new SetTime(null,"","Entrance Time", user.getParkName());
    	
    	
    	
    }
    /**
     * Handles the process for setting exit times for visitors.
     * Prepares the UI for inputting a reservation ID to set the exit time.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void setExitPark(ActionEvent event) {
    	lblGoodbye.setVisible(false);
    	lblPay.setVisible(false);
    	txtFReservationID.setVisible(true);
    	txtFReservationID.setText("");
    	btnPayment.setVisible(false);
    	ClientUI.chat.LeavingTime=false;
    	ClientUI.chat.EntranceTime=null;
    	btnSubmit.setVisible(true);
    	setTime= new SetTime(null,"","Leaving Time",user.getParkName());
    }
    /**
     * Moves the user to the payment processing section.
     * This method navigates the user to the Final Bill page to process payments.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during loading the FXML.
     */
    @FXML
    void MoveToPayment(ActionEvent event) throws IOException {
    	
    	Request request= new Request();
		request.setType(RequestType.PAYMENT);
		request.setRequest(ClientUI.chat.EntranceTime);
	        byte[] arr;
	        try {
	            arr = request.getBytes();
	            System.out.println("Serialized bytes: " + arr.length);
	            ClientUI.chat.handleMessageFromClientUI(arr);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        FinalBillController.reservation=reservation;
	        FinalBillController.totalAmount=ClientUI.chat.totalAmount;
	        FinalBillController.user=user;
	       ((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
	    	Stage primaryStage = new Stage();
	    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/FinalBill.fxml"));
	    	FinalBillController finalbillcontroller = new FinalBillController();		
	    		
	    	Scene scene = new Scene(root);			
	    	//scene.getStylesheets().add(getClass().getResource("/fxml/FinalBill.css").toExternalForm());
	    	primaryStage.setTitle("");
	    	primaryStage.initStyle(StageStyle.UNDECORATED);
	    	primaryStage.setResizable(false);
	    	primaryStage.setScene(scene);
	    	primaryStage.show();
	        
	
    	
    }
    /**
     * Submits the reservation ID for entrance or exit time setting.
     * Validates the reservation ID and, based on the action (entrance or exit), sets the time.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void Submit(ActionEvent event) {
    	String fixedHour;
    	setTime.setReservationID((Integer.parseInt(txtFReservationID.getText())));
    	fixedHour=setHourFormatted(LocalTime.now());
    	//send to server to check reservation ID if exist 
    	
    	Request request= new Request();
    	request.setType(RequestType.CHECK_RESERVATION_ID);
    	request.setRequest(setTime);
    	 byte[] arr;
	        try {
	            arr = request.getBytes();
	            System.out.println("Serialized bytes: " + arr.length);
	            ClientUI.chat.handleMessageFromClientUI(arr);
	            if(ClientUI.chat.checkReservationID.equals("yes")) {
	            	if(setTime.getType().equals("Entrance Time")) {
		        		setTime.setTime(fixedHour);
		        		request.setType(RequestType.SET_ENTRANCE_TIME);
		        	}
		        	else {
		        		setTime.setTime(fixedHour);
		        		request.setType(RequestType.SET_LEAVING_TIME);
		        	}
		    		request.setRequest(setTime);
		    	        try {
		    	            arr = request.getBytes();
		    	            System.out.println("Serialized bytes: " + arr.length);
		    	            ClientUI.chat.handleMessageFromClientUI(arr);
		    	            if(ClientUI.chat.EntranceTime!=null) {
		    	            	if(ClientUI.chat.EntranceTime.isPrepayment()==false) {
		    	            		btnPayment.setVisible(true);
		    	            		lblGoodbye.setVisible(false);
		    	            		reservation=ClientUI.chat.EntranceTime;
		    	            		ClientUI.chat.LeavingTime=false;
		    	            	}
		    	            	if(ClientUI.chat.EntranceTime.isPrepayment()==true) {
		    	            	lblPay.setVisible(true);
		    	            	lblGoodbye.setVisible(false);
		    	            	btnPayment.setVisible(false);
		    	            	reservation=ClientUI.chat.EntranceTime;
		    	            	}
		    	            }
		    	            if(ClientUI.chat.LeavingTime==true) {
		    	            	System.out.println("i63");
		    	            	ClientUI.chat.EntranceTime=null;
		    	            	lblGoodbye.setVisible(true);
		    	            	btnPayment.setVisible(false);
		    	            	
		    	            }
		    	            
		    	            
		    	        } catch (IOException e) {
		    	            e.printStackTrace();
		    	        }
	            }
	            else {
	            	Alert alert = new Alert(AlertType.ERROR);
			        // Set the title of the Alert
			        alert.setTitle("Error");
			        // Set the header text of the Alert
			        alert.setHeaderText(null);
			        // Set the content text of the Alert
			        alert.setContentText("Reservation ID isn't exist");

			        // Display the Alert
			        alert.showAndWait();
			        
	            }
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	
    	
	       
    }
    /**
     * Handles occasional reservation creation.
     * Navigates the user to the Occasional Reservation page.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during loading the FXML.
     */
    @FXML
    void occasionalreservation(ActionEvent event) throws IOException {
    	lblPay.setVisible(false);
    	lblGoodbye.setVisible(false);
    	OccasionalReservationController.user=user;
    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/OccasionalReservation.fxml"));
		OccasionalReservationController occasionalReservationcontroller = new OccasionalReservationController();		
	
		Scene scene = new Scene(root);			
		//scene.getStylesheets().add(getClass().getResource("/fxml/FOccasionalReservation.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
    	
    
    }
    /**
     * Formats a LocalTime object to a string with the format "HH:mm".
     *
     * @param time The LocalTime object to format.
     * @return A string representation of the time.
     */
    private String setHourFormatted(LocalTime time) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    	return time.format(formatter);
    }
    /**
     * Initializes the controller class.
     * Automatically called after the FXML file has been loaded. Sets the initial state of the UI.
     *
     * @param arg0 The location used to resolve relative paths for the root object, or null if not known.
     * @param arg1 The resources used to localize the root object, or null if the root object was not localized.
     */
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtParkName.setText(user.getParkName());
		txtParkName.setVisible(true);

		
	}
	/**
     * Starts the worker page view.
     * This method sets up and displays the scene for the worker page.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML.
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/WorkerPage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/WorkerPage.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
