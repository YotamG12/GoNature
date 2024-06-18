package gui;

import java.io.IOException;

import GNClient.ClientUI;
import common.RequestType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Request;
import entities.User;
/**
 * Controls the GUI for single travelers. This class provides functionality for logging out,
 * exiting the application, and moving to the reservation management page.
 */
public class SingleTravelerGUIController {
	
    @FXML
    private Button btnLogout;
    
    @FXML
    private Button btnResManag;
    
    @FXML
    private Button btnExit;
    
    public static User user;
    /**
     * Handles the exit action. This method closes the connection to the server and quits the application.
     * 
     * @param event The action event that triggered the exit.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
	void Exit (ActionEvent event) throws IOException {
    	logoutFunc(event);
		ClientUI.chat.closeConnection();
    	ClientUI.chat.quit();
	}
    /**
     * Manages the logout functionality. If the user's session is active, it sends a logout request to the server.
     * 
     * @param event The action event that triggered the logout.
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
     * Navigates the user to the reservation management page.
     * Before moving, it fetches all reservations associated with the user from the server.
     * 
     * @param event The action event that triggered this navigation.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    void MoveToResManag(ActionEvent event) throws IOException {
    	
    	ReservationManagmentController.user=user;
    	
    	Request request= new Request();
    	request.setType(RequestType.GET_ALL_RESERVATIONS);
    	
    	request.setRequest(user.getUserName());
		System.out.println(request.getRequest().toString());
	    byte[] arr;
	    try {
	    	arr = request.getBytes();
	        System.out.println("Serialized bytes: " + arr.length);
	        ClientUI.chat.handleMessageFromClientUI(arr);
	            
	    } catch (IOException e) {
	         e.printStackTrace();
	    }
    	
    
	   
    
		((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ReservationManagment.fxml"));

		Scene scene = new Scene(root);			
		//scene1.getStylesheets().add(getClass().getResource("/fxml/ReservationManagment.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
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
     * Initializes and displays the single traveler GUI.
     * 
     * @param primaryStage The primary stage for this application.
     * @throws Exception If any error occurs during loading the FXML file.
     */
    public void start(Stage primaryStage) throws Exception {
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/SingleTravelerGUI.fxml"));
    	Scene scene = new Scene(root);
    	scene.getStylesheets().add(getClass().getResource("/fxml/SingleTravelerGUI.css").toExternalForm());
    	primaryStage.setTitle("");
    	primaryStage.initStyle(StageStyle.UNDECORATED);
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
    }
}