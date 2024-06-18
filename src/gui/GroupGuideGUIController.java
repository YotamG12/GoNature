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

public class GroupGuideGUIController {

    @FXML
    private Button btnCreateRes;
    
    @FXML
    private Button btnLogout;
    
    @FXML
    private Button btnResManag;
    
    public static User user;
    /**
     * Exits the application by closing the connection and quitting the client UI.
     *
     * @param event The action event that triggers this method.
     * @throws IOException If an error occurs during the process.
     */
    @FXML
	void Exit (ActionEvent event) throws IOException {
    	logoutFunc(event);
		ClientUI.chat.closeConnection();
    	ClientUI.chat.quit();
	}
    /**
     * Navigates to the interface for creating a new group reservation.
     *
     * @param event The action event that triggers this method.
     * @throws IOException If an error occurs during the navigation.
     */
    @FXML
    void CreateRes(ActionEvent event) throws IOException {
    	GroupReservationController.user=user;
		((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/GroupReservation.fxml"));

		Scene scene = new Scene(root);			
		//scene1.getStylesheets().add(getClass().getResource("/fxml/GroupReservation.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
    }

    /**
     * Moves to the Reservation Management interface for managing existing reservations.
     *
     * @param event The action event that triggers this method.
     * @throws IOException If an error occurs during the navigation.
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
     * Logs out the current user and returns to the welcome page.
     *
     * @param event The action event that triggers this method.
     */
    
    @FXML
    void Logout(ActionEvent event) {
    	logoutFunc(event);
    }
    /**
     * Handles the logout functionality by sending a logout request to the server
     * and navigating back to the welcome page upon successful logout.
     *
     * @param event The action event that triggers this method.
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
     * Initializes and displays the Group Guide GUI.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception If an error occurs during the initialization.
     */
    public void start(Stage primaryStage) throws Exception {
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/GroupGuideGUI.fxml"));
    	Scene scene = new Scene(root);
    	scene.getStylesheets().add(getClass().getResource("/fxml/GroupGuideGUI.css").toExternalForm());
    	primaryStage.setTitle("");
    	primaryStage.initStyle(StageStyle.UNDECORATED);
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
    }

}