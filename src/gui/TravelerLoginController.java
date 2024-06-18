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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Request;
import entities.User;
/**
 * Controls the traveler login GUI. This class handles user input for login credentials,
 * communicates with the server for authentication, and navigates the user based on the login response.
 */
public class TravelerLoginController {

	 	@FXML
	    private Button btnLogin;
	 	
	 	@FXML
	    private Button btnBack;

	    @FXML
	    private Button btnExit;

	    @FXML
	    private Label lblID;

	    @FXML
	    private Label lblIDnr;

	    @FXML
	    private Label lblInvalidPW;

	    @FXML
	    private Label lblPassword;

	    @FXML
	    private Label lblRC;

	    @FXML
	    private Label lblconnected;

	    @FXML
	    private TextField txtFID;

	    @FXML
	    private PasswordField txtFPassword;
	    
	    /**
	     * Navigates back to the welcome page.
	     * 
	     * @param event The action event that triggered the navigation.
	     * @throws IOException If an I/O error occurs.
	     */
	    
	    @FXML
	    void Back(ActionEvent event) throws IOException {
	    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomePage.fxml"));
			
			
			Scene scene = new Scene(root);			
			//scene.getStylesheets().add(getClass().getResource("/fxml/WelcomePage.css").toExternalForm());
			primaryStage.setTitle("");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
				
			primaryStage.show();
		
		
	    }
	    /**
	     * Processes the login request with the entered ID and password. Communicates with the server to
	     * authenticate the user and navigates based on the login response (invalid credentials, single traveler, group guide).
	     * 
	     * @param event The action event that triggered the login.
	     * @throws IOException If an I/O error occurs.
	     */
    @FXML
    void Login(ActionEvent event) throws IOException {
    	String id=txtFID.getText();
		String password=txtFPassword.getText();
		User user = new User(id,password);

		//send to server:
		Request request= new Request();
		request.setType(RequestType.TRAVELER_LOGIN);
		request.setRequest(user);
		System.out.println(request.getRequest().toString());
		byte[] arr;
		try {
			arr = request.getBytes();
			System.out.println("Serialized bytes: " + arr.length);
			ClientUI.chat.handleMessageFromClientUI(arr);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		user=ClientUI.chat.user;
		switch (ClientUI.chat.user.getPermission()) {

		case "Password invalid":
			lblInvalidPW.setVisible(true);
			break;
		case "ID not registered":
			lblIDnr.setVisible(true);
			break;
		case "Connected":
			lblconnected.setVisible(true);
			break;	
		case "Single":
			SingleTravelerGUIController.user=user;
			((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
			Stage primaryStage1 = new Stage();
			Parent root1 = FXMLLoader.load(getClass().getResource("/fxml/SingleTravelerGUI.fxml"));

			Scene scene1 = new Scene(root1);			
			//scene1.getStylesheets().add(getClass().getResource("/fxml/SingleTravelerGUI.css").toExternalForm());
			primaryStage1.setTitle("");
			primaryStage1.initStyle(StageStyle.UNDECORATED);
			primaryStage1.setScene(scene1);
			primaryStage1.show();
			break;
		case "Group" :
			GroupGuideGUIController.user=user;
			((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
			Stage primaryStage2 = new Stage();
			Parent root2 = FXMLLoader.load(getClass().getResource("/fxml/GroupGuideGUI.fxml"));
			Scene scene2 = new Scene(root2);			
			//scene2.getStylesheets().add(getClass().getResource("/fxml/GroupGuideGUI.css").toExternalForm());
			primaryStage2.setTitle("");
			primaryStage2.initStyle(StageStyle.UNDECORATED);
			primaryStage2.setScene(scene2);		
			primaryStage2.show();
			break;
		
		
		}
    }
    /**
     * Closes the connection to the server and quits the application.
     * 
     * @param event The action event that triggered the exit.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
	void Exit (ActionEvent event) throws IOException {
    	
		ClientUI.chat.closeConnection();
    	ClientUI.chat.quit();
	}
    /**
     * Initializes and displays the traveler login GUI.
     * 
     * @param primaryStage The primary stage for this application.
     * @throws Exception If any error occurs during loading the FXML file.
     */
    public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TravelerLogin.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/TravelerLogin.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
   

}
