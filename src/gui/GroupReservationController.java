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
 * Controls the group reservation GUI, facilitating the creation of new group reservations,
 * entry into waiting lists, and viewing of available slots. It validates user inputs such as
 * email, phone number, and number of visitors.
 */
public class GroupReservationController implements Initializable, Serializable {

    @FXML
    private Button btnSubmit;
    
    @FXML
    private Button btnSAS;
    
    @FXML
    private Button btnWL;
    
    @FXML
    private Button btnBack;

    @FXML
    private Button btnExit;

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
    public static User user;
    /**
     * Exits the application by closing the connection and quitting the client UI.
     *
     * @param event ActionEvent that triggers this method.
     * @throws IOException If an error occurs during the process.
     */
    @FXML
	void Exit (ActionEvent event) throws IOException {
    	logoutFunc(event);
		ClientUI.chat.closeConnection();
    	ClientUI.chat.quit();
	}

    /**
     * Navigates back to the Group Guide main GUI.
     *
     * @param event ActionEvent that triggers this method.
     * @throws IOException If an error occurs during navigation.
     */
    @FXML
    void Back(ActionEvent event) throws IOException {
    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/GroupGuideGUI.fxml"));
		
		
		Scene scene = new Scene(root);			
		//scene.getStylesheets().add(getClass().getResource("/fxml/GroupGuideGUI.css").toExternalForm());
		primaryStage.setTitle("Reservation Confirmation");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
			
		primaryStage.show();
	
	
    }

    
    /**
     * Initialises the primary stage for the group reservation GUI.
     * 
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML file.
     */

    public void start(Stage primaryStage) throws Exception {
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/GroupReservation.fxml"));

		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/fxml/GroupReservation.css").toExternalForm());
		primaryStage.setTitle("GoNature");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

	}
    
    /**
     * Creates a new group reservation with the provided user input and sends it to the server for processing.
     *
     * @param event ActionEvent that triggers this method.
     * @throws IOException If an error occurs during loading the FXML file or communicating with the server.
     */
    @FXML
    void CreateNewGroupeRservation(ActionEvent event) throws IOException {
    	String userID= txtFUserID.getText();
    	String parkName= choiseBoxParkName.getValue();
    	String numOfVisitors= txtFNumOfVisitors.getText();
    	String date= datePicker.getValue().toString();
    	String time= choiseBoxTime.getValue();
    	String phoneNum=txtFPhoneNum.getText();
    	String email= txtFEmail.getText();
    	

    	if(isValidEmail(email)&& isFutureDate(datePicker.getValue(),time) && 
    			checkPhoneNum(phoneNum)&& checknumOfVisitors(numOfVisitors)&&isEmptyParkName(parkName)	) {
    		
    		newReservation= new Reservation(-1,userID,parkName,numOfVisitors,date,time,phoneNum,email,false,"Group");
    		Request request= new Request();
    		request.setType(RequestType.NEW_GROUP_RESERVATION);
    		request.setRequest(newReservation);
    	        byte[] arr;
    	        try {
    	            arr = request.getBytes();
    	            System.out.println("Serialized bytes: " + arr.length);
    	            ClientUI.chat.handleMessageFromClientUI(arr);
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
    	        		ReservationConfimationController.user=user;
    	        	    ((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
    	        		Stage primaryStage = new Stage();
    	        		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ReservationConfirmation.fxml"));
    	        		ReservationConfimationController reservationconfimationcontroller = new ReservationConfimationController();		
    	        		
    	        		Scene scene = new Scene(root);			
    	        		//scene.getStylesheets().add(getClass().getResource("/fxml/ReservationConfirmation.css").toExternalForm());
    	        		primaryStage.setTitle("Reservation Confirmation");
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
     * Adds the current reservation to the waiting list via a request to the server.
     *
     * @param event ActionEvent that triggers this method.
     * @throws IOException If an error occurs during loading the FXML file or communicating with the server.
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
	        		WaitingListConfimationController.reservation=newReservation;
	        		WaitingListConfimationController.user=user;
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
     * Retrieves and displays available slots for reservations by communicating with the server.
     *
     * @param event ActionEvent that triggers this method.
     * @throws IOException If an error occurs during loading the FXML file or communicating with the server.
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
	
    	AvailableSlotsController.newReservation=newReservation;
    	AvailableSlotsController.user=user;
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
     * Validates the provided email address format.
     *
     * @param email The email address to validate.
     * @return true if the email address format is valid; false otherwise.
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
     * Checks if the selected reservation date and time are in the future.
     * 
     * @param date The reservation date.
     * @param time The reservation time.
     * @return true if the date and time are in the future; false otherwise.
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
      * Validates the phone number format to match a specific pattern (10 digits).
      * 
      * @param phoneNum The phone number to validate.
      * @return true if the phone number is valid; false otherwise.
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
     * Validates the number of visitors to ensure it does not exceed a predefined limit (15).
     * 
     * @param numOfVisitors The number of visitors to validate.
     * @return true if the number of visitors is within the allowed range; false otherwise.
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
    /**
     * Performs logout operation and navigates back to the welcome screen.
     * 
     * @param event The action event triggered by logout actions.
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
    
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		choiseBoxParkName.getItems().addAll("DisneyLand", "Universal", "Afek");
		choiseBoxTime.getItems().addAll("10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00");
		txtFUserID.setText(user.getUserName());
		txtFUserID.setDisable(true);
	}
}
