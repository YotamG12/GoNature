package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import GNClient.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * Controller for the Welcome Page of the application. 
 * This class handles the navigation from the welcome page to other pages based on the user's choice, 
 * including traveler login, worker login, and creating a reservation.
 */
public class WelcomePageController implements Initializable {
	@FXML
	private Button btnExit;
    @FXML
    private Button btnCO;

    @FXML
    private Button btnTravelerLogin;

    @FXML
    private Button btnWorkerLogin;

    @FXML
    private Label lblIDnr;

    @FXML
    private Label lblInvalidPW;

    @FXML
    private Label lblconnected;
    /**
     * Handles the action to exit the application. 
     * This method closes the connection to the server and quits the application.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during the process.
     */
    @FXML
	void Exit (ActionEvent event) throws IOException {
		ClientUI.chat.closeConnection();
    	ClientUI.chat.quit();
	}
    /**
     * Navigates to the page for creating a reservation. 
     * This method hides the current window and opens the reservation page.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during loading the FXML.
     */
    @FXML
    void CreateReservation(ActionEvent event) throws IOException {
    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/SingleReservation.fxml"));

		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/fxml/SingleReservation.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
    }
    /**
     * Navigates to the traveler login page. 
     * This method hides the current window and opens the traveler login page.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during loading the FXML.
     */
    @FXML
    void TravelerLogin(ActionEvent event) throws IOException {
    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TravelerLogin.fxml"));

		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/fxml/TravelerLogin.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
    }
    /**
     * Navigates to the worker login page. 
     * This method hides the current window and opens the worker login page.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during loading the FXML.
     */
    @FXML
    void WorkerLogin(ActionEvent event) throws IOException  {
    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/WorkerLogin.fxml"));

		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/fxml/WorkerLogin.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
    }

    /*public void start(Stage primaryStage) throws Exception {
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomePage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/WelcomePage.css").toExternalForm());
		primaryStage.setTitle("Welcome Page");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
   }*/
    /**
     * Initializes the controller class. 
     * This method is automatically called after the FXML file has been loaded. 
     * It can be used to initialize the state of the controller.
     *
     * @param arg0 The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param arg1 The resources used to localize the root object, or null if the root object was not localized.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
	    

		
	    
	    
