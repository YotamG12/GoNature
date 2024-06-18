package gui;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GNClient.ClientUI;
import common.RequestType;
import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import entities.Request;
import entities.Reservation;
import entities.User;
import gui.WaitingListConfimationController;
import gui.ReservationConfimationController;
import javafx.scene.Node;
/**
 * The {@code SingleReservationController} class controls the single reservation
 * GUI. It handles user inputs for creating a new single reservation, checking the
 * availability of slots, and adding to the waiting list if no slots are available.
 */
public class SingleReservationController implements Initializable , Serializable{

    @FXML
    private Button btnSubmit;
    
    @FXML
    private Button btnSAS;
    
    @FXML
    private Button btnWL;

    @FXML
    private ComboBox<String> choiseBoxParkName;
    
    @FXML
    private ComboBox<String> choiseBoxTime;

    @FXML
    private DatePicker datePicker;
    
    @FXML
    private Text txtError;


    @FXML
    private TextField txtFEmail;

    @FXML
    private TextField txtFNumOfVisitors;

    @FXML
    private TextField txtFPhoneNum;

    @FXML
    private TextField txtFUserID;
    
    public static Reservation newReservation;
    public static User user=new User("","");
    /**
     * Navigates back to the previous screen based on the user's permission level.
     * @param event The action event that triggers this method.
     * @throws IOException If an error occurs during navigation.
     */
    @FXML
    void Back(ActionEvent event) throws IOException {
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
     * Handles the logout process for the current user session.
     * @param event The action event that triggered this method.
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
     * Initializes the primary stage for the single reservation GUI.
     * 
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML file.
     */
    
    public void start(Stage primaryStage) throws Exception {
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/SingleReservation.fxml"));

		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/fxml/SingleReservation.css").toExternalForm());
		primaryStage.setTitle("GoNature");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		
    	//choiseBoxParkName.getItems().addAll("DisneyLand","Universal","Afek");
		primaryStage.show();

	}
    /**
     * Creates a new single reservation based on the user inputs and communicates
     * with the server to process the reservation.
     * 
     * @param event ActionEvent that triggered this method.
     * @throws IOException if an error occurs during loading the FXML file.
     */
    @FXML
    void CreateNewSingleReservation(ActionEvent event) throws IOException {
    	String userID= txtFUserID.getText();
    	String parkName= choiseBoxParkName.getValue();
    	String numOfVisitors= txtFNumOfVisitors.getText();
    	String date= datePicker.getValue().toString();
    	String time= choiseBoxTime.getValue();
    	String phoneNum=txtFPhoneNum.getText();
    	String email= txtFEmail.getText();
    	
    	//build new reservation object with reservationID-=0
    	
    	//check validity of fields- add pop-up alerts
    	//check validity of date- must be a future date 
    	if(isValidEmail(email)&& isFutureDate(datePicker.getValue(), time) && 
    			checkPhoneNum(phoneNum)&& checknumOfVisitors(numOfVisitors)&&isValidId(userID)&&isEmptyParkName(parkName)) {
    		newReservation= new Reservation(-1,userID,parkName,numOfVisitors,date,time,phoneNum,email,false,"Single");
    		Request request= new Request();
    		request.setType(RequestType.NEW_SINGLE_RESERVATION);
    		request.setRequest(newReservation);
    		System.out.println(request.getRequest().toString());
    	        byte[] arr;
    	        try {
    	            arr = request.getBytes();
    	            System.out.println("Serialized bytes: " + arr.length);
    	            ClientUI.chat.handleMessageFromClientUI(arr);
    	            System.out.println("hello");
        	    	System.out.println("here"+ ClientUI.chat.confirmationResevation);
        	    	if(ClientUI.chat.confirmationResevation.equals("no")){
        	    		txtError.setVisible(true);
        	    		txtError.setText("There is no available slots at the choosen time. Please choose one of the to options");
        	    		btnSAS.setVisible(true);
        	    		btnWL.setVisible(true);
        	    	}
        	    	else {
        	    		newReservation.setReservationID(Integer.parseInt(ClientUI.chat.confirmationResevation));
        	    		System.out.println("reservation ID: "+newReservation.getReservationID());
        	    		//ReservationConfimationController.SetTextConfirmationPage(newReservation);
        	    		ReservationConfimationController.newReservation= newReservation;

        	    	    ((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
        	    		Stage primaryStage = new Stage();
        	    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ReservationConfirmation.fxml"));
        	    		ReservationConfimationController reservationconfimationcontroller = new ReservationConfimationController();		
        	    		
        	    		Scene scene = new Scene(root);			
        	    		//scene.getStylesheets().add(getClass().getResource("/fxml/ReservationConfirmation.css").toExternalForm());
        	    		primaryStage.setTitle("Reservation Confirmation");
        	    		primaryStage.initStyle(StageStyle.UNDECORATED);
        	    		primaryStage.setResizable(false);
        	    		primaryStage.setScene(scene);

        	    			
        	    		primaryStage.show();
        	    	}
    	            
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    	       
    	}
    	
    		
    }

    	

    	
    	
    
    /**
     * Adds the current reservation to the waiting list through communication
     * with the server.
     * 
     * @param event ActionEvent that triggered this method.
     * @throws IOException if an error occurs during loading the FXML file.
     */
    @FXML
    void WaitingListOption (ActionEvent event) throws IOException{
    	Request request= new Request();
		request.setType(RequestType.ENTER_WAITING_LIST);
		request.setRequest(newReservation);
	        byte[] arr;
	        try {
	            arr = request.getBytes();
	            System.out.println("Serialized bytes: " + arr.length);
	            ClientUI.chat.handleMessageFromClientUI(arr);
	            if(ClientUI.chat.confirmationWaitingList.equals("yes")) {
		        	System.out.println("here2");
		        	WaitingListConfimationController.reservation=newReservation;
		    	    ((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		    		Stage primaryStage = new Stage();
		    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/WaitingListConfimation.fxml"));
		    		WaitingListConfimationController waitingListConfirmationController = new WaitingListConfimationController();		
		    	
		    		Scene scene = new Scene(root);			
		    		//scene.getStylesheets().add(getClass().getResource("/fxml/ConfirmationWaitingList.css").toExternalForm());
		    		primaryStage.setTitle("");
		    		primaryStage.initStyle(StageStyle.UNDECORATED);
		    		primaryStage.setResizable(false);
		    		primaryStage.setScene(scene);

		    		
		    		primaryStage.show();
		        }
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	
	        
	    
    }
     /**
     * Displays available slots for reservations. It communicates with the server
     * to retrieve free slots data.
     * 
     * @param event ActionEvent that triggered this method.
     * @throws IOException if an error occurs during loading the FXML file.
     */
    @FXML
    void AvailableSlots (ActionEvent event) throws IOException{
    	Request request= new Request();
		request.setType(RequestType.FREE_SLOTS_DATA);
		request.setRequest(newReservation);
	        byte[] arr;
	        try {
	            arr = request.getBytes();
	            System.out.println("Serialized bytes: " + arr.length);
	            ClientUI.chat.handleMessageFromClientUI(arr);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	// condition 
	    AvailableSlotsController.newReservation=newReservation;
    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		Stage primaryStage = new Stage();
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
    
    /**
     * Validates the email address entered by the user.
     * 
     * @param email The email address to validate.
     * @return true if the email address is valid, false otherwise.
     */
    private static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        
        if(!matcher.matches()) {
        	Alert alert = new Alert(AlertType.ERROR);
	        // Set the title of the Alert
	        alert.setTitle("Error");
	        // Set the header text of the Alert
	        alert.setHeaderText(null);
	        // Set the content text of the Alert
	        alert.setContentText("invalid email");

	        // Display the Alert
	        alert.showAndWait();
	        return false;
        }
       
        return true;
    }
    
    private  boolean isEmptyParkName(String str) {
    	String ParkName=str;
        
        if(ParkName==null) {
        	Alert alert = new Alert(AlertType.ERROR);
	        // Set the title of the Alert
	        alert.setTitle("Error");
	        // Set the header text of the Alert
	        alert.setHeaderText(null);
	        // Set the content text of the Alert
	        alert.setContentText("You must choose Park");

	        // Display the Alert
	        alert.showAndWait();
	        return false;
        }
       
        return true;
        	
        }
       
       
    
    /**
     * Validates the user ID entered by the user.
     * @param id The user ID to validate.
     * @return true if the user ID is valid; false otherwise.
     */
    private static boolean isValidId(String id) {
        	
        	if(!(id.matches("[0-9]+")&& (id.length()== 9))) {
        		Alert alert = new Alert(AlertType.ERROR);
    	        // Set the title of the Alert
    	        alert.setTitle("Error");
    	        // Set the header text of the Alert
    	        alert.setHeaderText(null);
    	        // Set the content text of the Alert
    	        alert.setContentText("invalid ID");

    	        // Display the Alert
    	        alert.showAndWait();
    	        return false;
        	}
        	return true;
    }
    /**
     * Validates that the selected date is in the future.
     * 
     * @param date The date to validate.
     * @return true if the date is in the future, false otherwise.
     */
    
    private static boolean isFutureDate(LocalDate date,String time) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Compare the given date to the current date
        if(date.isBefore(currentDate)){
        	Alert alert = new Alert(AlertType.ERROR);
	        // Set the title of the Alert
	        alert.setTitle("Error");
	        // Set the header text of the Alert
	        alert.setHeaderText(null);
	        // Set the content text of the Alert
	        alert.setContentText("You Must Enter Future Date");

	        // Display the Alert
	        alert.showAndWait();
	        return false;
        }
        LocalTime currentTime=LocalTime.now();
       
        LocalTime localTime1 = LocalTime.parse(time);
        
        if(date.equals(currentDate)) {
        	if (!(localTime1.compareTo(currentTime) > 0)) {
        		Alert alert = new Alert(AlertType.ERROR);
    	        // Set the title of the Alert
    	        alert.setTitle("Error");
    	        // Set the header text of the Alert
    	        alert.setHeaderText(null);
    	        // Set the content text of the Alert
    	        alert.setContentText("You Must Enter Future Date");

    	        // Display the Alert
    	        alert.showAndWait();
        		return false;
        	}
        }
        return true;
    }
    
     /**
     * Validates the phone number entered by the user.
     * 
     * @param phoneNum The phone number to validate.
     * @return true if the phone number is valid, false otherwise.
     */
    private boolean checkPhoneNum(String phoneNum) {
    	//check phoneNum is 9 digits
    	
    	if(!(phoneNum.matches("[0-9]+")&& (phoneNum.length()== 10))) {
    		Alert alert = new Alert(AlertType.ERROR);
	        // Set the title of the Alert
	        alert.setTitle("Error");
	        // Set the header text of the Alert
	        alert.setHeaderText(null);
	        // Set the content text of the Alert
	        alert.setContentText("invalid phoneNum");

	        // Display the Alert
	        alert.showAndWait();
	        return false;
    	}
    	return true;
    }
    /**
     * Validates the numOfVisitors entered by the user.
     * 
     * @param phoneNum The phone number to validate.
     * @return true if the phone number is valid, false otherwise.
     */

    private boolean checknumOfVisitors(String numOfVisitors) {
    	if(numOfVisitors.matches("[0-9]+"))
    		if (Integer.parseInt(numOfVisitors)<=15)
    			return true;
    	Alert alert = new Alert(AlertType.ERROR);
        // Set the title of the Alert
        alert.setTitle("Error");
        // Set the header text of the Alert
        alert.setHeaderText(null);
        // Set the content text of the Alert
        alert.setContentText("Number of Visitors must be below 15");

        // Display the Alert
        alert.showAndWait();
        return false;

    }
    
 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		choiseBoxParkName.getItems().addAll("DisneyLand", "Universal", "Afek");
		choiseBoxTime.getItems().addAll("10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00");
	}
	 @FXML
	    void CloseConnections(ActionEvent event) throws Exception {
	        // Add your action here
	        ClientUI.chat.closeConnection();
	        ClientUI.chat.quit();
	    }
}
