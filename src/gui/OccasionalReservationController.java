package gui;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Request;
import entities.Reservation;
import entities.SetTime;
import entities.User;
/**
 * Controller for managing occasional reservation creation in the application. This class 
 * facilitates the interaction with the GUI for creating occasional reservations by 
 * validating user input and submitting reservation requests to the server.
 */
public class OccasionalReservationController implements Initializable , Serializable {
	
	
		@FXML
		private Button btnCheckGroupGuide;
	
	    @FXML
	    private Button btnSubmit;

	    @FXML
	    private ComboBox <String> choiseboxType;

	    @FXML
		private Button btnExit;
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
	    public static SetTime settime;
	    /**
	     * Starts the stage for occasional reservation creation GUI.
	     * 
	     * @param primaryStage The primary stage for this application.
	     * @throws Exception if an error occurs during loading the FXML file.
	     */
	    public void start(Stage primaryStage) throws Exception {
	    	
	    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/OccasionalReservation.fxml"));

			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("/fxml/OccasionalReservation.css").toExternalForm());
			primaryStage.setTitle("GoNature");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			
	    	//choiseBoxParkName.getItems().addAll("DisneyLand","Universal","Afek");
			primaryStage.show();

		}
	    /**
	     * Handles the exit action to logout the user, close the connection, and quit the application.
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
	     * Navigates back to the worker page GUI.
	     * 
	     * @param event The action event that triggers this method.
	     * @throws IOException If an error occurs during navigation.
	     */
	    @FXML
	    void Back(ActionEvent event) throws IOException {
	    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/WorkerPage.fxml"));
				
			
			Scene scene = new Scene(root);			
			//scene.getStylesheets().add(getClass().getResource("/fxml/WorkerPage.css").toExternalForm());
			primaryStage.setTitle("");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
				
			primaryStage.show();
		
		
	    }
	    
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
	     * Formats the current hour into a string with the format HH:00.
	     * 
	     * @param time The LocalTime object to format.
	     * @return A string representing the formatted hour.
	     */
	    private String setHourFormatted(LocalTime time) {
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:00");

	    	return time.format(formatter);
	    }
	    
	    /**
	     * Submits a new occasional reservation request based on the user input.
	     * 
	     * @param event The action event that triggers this method.
	     * @throws IOException If an error occurs during processing the request.
	     */
	    @FXML
	    void CreateNewOccasionalReservation(ActionEvent event) throws IOException {
	    	
	    	String userID= txtFUserID.getText();
	    	
	    	String numOfVisitors= txtFNumOfVisitors.getText();
	    	String date= LocalDate.now().toString();
	    	String time= setHourFormatted(LocalTime.now());
	    	String phoneNum=txtFPhoneNum.getText();
	    	String email= txtFEmail.getText();
	    	String type=choiseboxType.getValue();
	    	
	    	if(isValidEmail(email) && 
	    			checkPhoneNum(phoneNum)&& checknumOfVisitors(numOfVisitors)	) {
	    		newReservation= new Reservation(-1,userID,user.getParkName(),numOfVisitors,date,time,phoneNum,email,false,type);
	    		Request request= new Request();
	    		request.setType(RequestType.NEW_OCCASIONAL_RESERVATION);
	    		request.setRequest(newReservation);
	    	        byte[] arr;
	    	        try {
	    	            arr = request.getBytes();
	    	            System.out.println("Serialized bytes: " + arr.length);
	    	            ClientUI.chat.handleMessageFromClientUI(arr);
	    	            if(ClientUI.chat.OccasionalReservasion.equals("no")){
	    	            	Alert alert = new Alert(AlertType.ERROR);
	    			        // Set the title of the Alert
	    			        alert.setTitle("Error");
	    			        // Set the header text of the Alert
	    			        alert.setHeaderText(null);
	    			        // Set the content text of the Alert
	    			        alert.setContentText("The park is in full capacity ");

	    			        // Display the Alert
	    			        alert.showAndWait();
	    		    	}
	    		    	else {
	    		    		
	    		    		newReservation.setReservationID(Integer.parseInt(ClientUI.chat.OccasionalReservasion));
	    		    		settime= new SetTime(newReservation.getReservationID(),setHourFormattedNow(LocalTime.now()),"Entrance Time",newReservation.getParkName() );
	    		    		Request request2= new Request();
	    		    		request2.setType(RequestType.SET_ENTRANCE_TIME);
	    		    		request2.setRequest(settime);
	    		    	        byte[] arr2;
	    		    	        try {
	    		    	            arr2 = request2.getBytes();
	    		    	            
	    		    	            ClientUI.chat.handleMessageFromClientUI(arr2);
	    		    	        } catch (IOException e) {
	    		    	            e.printStackTrace();
	    		    	        }
	    		    		Request request1= new Request();
	    		    		request1.setType(RequestType.OCCASIONAL_PAYMENT);
	    		    		request1.setRequest(newReservation);
	    		    	        byte[] arr1;
	    		    	        try {
	    		    	            arr1 = request1.getBytes();
	    		    	            
	    		    	            ClientUI.chat.handleMessageFromClientUI(arr1);
	    		    	        } catch (IOException e) {
	    		    	            e.printStackTrace();
	    		    	        }
	    		    	        
	    		    	        
	    		    	        if(newReservation.getType().equals("Single")) {
	    		    	        	newReservation.setType("Single_occ"); 
	    		    	        }else {
	    		    	        	newReservation.setType("Group_occ"); 
	    		    	        }
	    		    	        
	    		    	    FinalBillController.reservation=newReservation;  
	    		    	    FinalBillController.totalAmount=ClientUI.chat.totalAmount;
	    		    	    FinalBillController.user=user;
	    		    	    ((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
	    		    		Stage primaryStage = new Stage();
	    		    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/FinalBill.fxml"));
	    		    		FinalBillController finalbillcontroller = new FinalBillController();		
	    		    		
	    		    		Scene scene = new Scene(root);			
	    		    		//scene.getStylesheets().add(getClass().getResource("/fxml/FinalBill.css").toExternalForm());
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
	     * Validates the provided email address against a regex pattern.
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
	    
	    
	        
	    
	    
	    
	    
	    /**
	     * Validates the provided phone number for correct format and length.
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
	     * Validates the number of visitors does not exceed a predefined maximum.
	     * 
	     * @param numOfVisitors The number of visitors to validate.
	     * @return true if the number of visitors is within the allowed limit, false otherwise.
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
	     * Checks if the group guide is registered in the system before allowing the reservation submission.
	     * 
	     * @param event The action event that triggers this method.
	     */
	    @FXML
	    void CheckGroupGuide(ActionEvent event) {
	    	Request request= new Request();
    		request.setType(RequestType.CHECK_GROUP_GUIDE);
    		request.setRequest(txtFUserID.getText());
    	    byte[] arr1;
    	    try {
    	        arr1 = request.getBytes();
    	            
    	        ClientUI.chat.handleMessageFromClientUI(arr1);
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	    if(!ClientUI.chat.checkGroupGuide) {
    	    	btnSubmit.setDisable(false);
    	    	Alert alert = new Alert(AlertType.ERROR);
    	        // Set the title of the Alert
    	        alert.setTitle("Error");
    	        // Set the header text of the Alert
    	        alert.setHeaderText(null);
    	        // Set the content text of the Alert
    	        alert.setContentText("Group Guide is not registered in the system");

    	        // Display the Alert
    	        alert.showAndWait();
    	    }
    	    if(ClientUI.chat.checkGroupGuide==true) {
    	    	btnSubmit.setDisable(false);
            	btnCheckGroupGuide.setVisible(false);
            	txtFNumOfVisitors.setVisible(true);
            	txtFPhoneNum.setVisible(true);
            	txtFEmail.setVisible(true);
            	btnSubmit.setVisible(true);
            }
    	        

	    }
	    /**
	     * Formats the current hour into a string with the format HH:MM.
	     * 
	     * @param time The LocalTime object to format.
	     * @return A string representing the formatted hour.
	     */
	    private String setHourFormattedNow(LocalTime time) {
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

	    	return time.format(formatter);
	    }
	    /**
	     * Initializes the controller class. This method is automatically called
	     * after the FXML file has been loaded. Initializes choice boxes and sets default values.
	     */
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			
			choiseboxType.getItems().addAll("Single","Group");
			choiseboxType.valueProperty().addListener((observable, oldValue, newValue) -> {
	            // Check if the selected text is the specific text you are looking for
	            if ("Group".equals(newValue)) {
	            	btnCheckGroupGuide.setVisible(true); // Make the button visible
	            } else {
	            	btnCheckGroupGuide.setVisible(false); // Otherwise, keep the button hidden
	            }
	            
	            if ("Single".equals(newValue)) {
	            	
	            	
	            	txtFNumOfVisitors.setVisible(true);
	            	txtFPhoneNum.setVisible(true);
	            	txtFEmail.setVisible(true);
	            	btnSubmit.setVisible(true);
	            	btnSubmit.setDisable(false);
	            }
	            
	        });
	    }
		
}
