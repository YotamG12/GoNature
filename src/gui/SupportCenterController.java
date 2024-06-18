package gui;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GNClient.ClientUI;
import common.RequestType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Request;
import entities.User;
/**
 * The SupportCenterController class handles the interaction between the user and the support center
 * interface within the application. It allows users to submit their IDs for registration as group guides,
 * facilitating the connection to support services.
 */
public class SupportCenterController {

    @FXML
    private Button btnSubmit;
    
    @FXML
    private Button btnExit;

    @FXML
    private Button btnLogout;


    @FXML
    private Label lblTitle;
    
    


    @FXML
    private TextField txtFID;

    
    
    public static User user;
    
    private String id;
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
     * Calculates the total and average number of cancellations per day based on data
     * retrieved from the server.
     * 
     * @param hashMap A mapping of dates to the number of cancellations.
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
     * Logs out the user and navigates back to the welcome page.
     * 
     * @param event The action event that triggered the logout.
     */
    @FXML
    void Logout(ActionEvent event) {
    	logoutFunc(event);
    }
    
    /**
     * Submits the user's ID for registration as a group guide. This method collects the ID input from
     * the text field, constructs a request, and sends it to the server to register the user as a group guide.
     * It displays an alert based on the success or failure of the registration process.
     *
     * @param event The action event triggered by clicking the submit button.
     */
    @FXML
    void Submit(ActionEvent event) {
    	id=txtFID.getText();
    	Request request= new Request();
    	request.setType(RequestType.REGISTER_GROUP_GUIDE);
   		request.setRequest(id);
   		byte[] arr;
   		try {
   			arr = request.getBytes();
   			ClientUI.chat.handleMessageFromClientUI(arr);
   			if(ClientUI.chat.RegisterGroupGuide.equals("no")) {
   				Alert alert = new Alert(AlertType.ERROR);
		        // Set the title of the Alert
		        alert.setTitle("Error");
		        // Set the header text of the Alert
		        alert.setHeaderText(null);
		        // Set the content text of the Alert
		        alert.setContentText("The register not succeed ");

		        // Display the Alert
		        alert.showAndWait();
   			}
   			else {
   				Alert alert = new Alert(AlertType.CONFIRMATION);
		        // Set the title of the Alert
		        alert.setTitle("Succeeded");
		        // Set the header text of the Alert
		        alert.setHeaderText(null);
		        // Set the content text of the Alert
		        alert.setContentText("The register  succeed! ");

		        // Display the Alert
		        alert.showAndWait();
   				
   			}
    	} 
    	catch (IOException e) {
   			e.printStackTrace();
   		}
   	}
    /**
     * Initializes and displays the support center GUI.
     * 
     * This method loads the SupportCenter FXML file and sets it on the primary stage.
     * It prepares and shows the support center screen where users can enter their ID for group guide registration.
     * 
     * @param primaryStage The primary stage for this application.
     * @throws Exception If any error occurs during loading the FXML file.
     */
    public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/SupportCenter.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/SupportCenter.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}	

} 
    



