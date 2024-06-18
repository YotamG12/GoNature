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
 * Controller class for the Worker Login Page.
 * This class handles the authentication process for workers attempting to log in to the system.
 */
public class WorkerLoginController {
	@FXML
	private Button btnExit;
    @FXML
    private Button btnLogin;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblRC;

    @FXML
    private Label lblUserName;
    
    @FXML
    private Label lblInvalidPW;
    
    @FXML
    private Label lblIDnr;
    
    @FXML
    private Label lblconnected;

    @FXML
    private PasswordField txtFPassword;

    @FXML
    private TextField txtFUserName;
    /**
     * Exits the application.
     * This method is triggered when the Exit button is pressed.
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
     * Navigates back to the Welcome Page.
     * This method is triggered when the Back button is pressed.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during loading the FXML.
     */
    @FXML
    void Back(ActionEvent event) throws IOException {
    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomePage.fxml"));
		
		
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/fxml/WelcomePage.css").toExternalForm());
		primaryStage.setTitle("Reservation Confirmation");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
			
		primaryStage.show();
	
	
    }
    /**
     * Attempts to log in a worker.
     * This method validates the user's credentials against the system's records. If successful, 
     * it redirects the user to their respective dashboard based on their role. If the login attempt fails,
     * appropriate error messages are displayed.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If an error occurs during the process.
     */
    @FXML
    void Login(ActionEvent event) throws IOException {
		String id=txtFUserName.getText();
		String password=txtFPassword.getText();
		User user = new User(id,password);

		//send to server:
		Request request= new Request();
		request.setType(RequestType.WORKER_LOGIN);
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
		System.out.println(ClientUI.chat.user.toString());
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
		case "Park Manager":
			ParkManagerController.user=user;
			((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
			Stage primaryStage1 = new Stage();
			Parent root1 = FXMLLoader.load(getClass().getResource("/fxml/ParkManager.fxml"));

			Scene scene1 = new Scene(root1);			
			//scene1.getStylesheets().add(getClass().getResource("/fxml/ParkManager.css").toExternalForm());
			primaryStage1.setTitle("");
			primaryStage1.initStyle(StageStyle.UNDECORATED);
			primaryStage1.setScene(scene1);
			primaryStage1.setResizable(false);
			primaryStage1.show();
			break;
		case "Park Worker" :
			WorkerPageController.user=user;
			((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
			Stage primaryStage2 = new Stage();
			Parent root2 = FXMLLoader.load(getClass().getResource("/fxml/WorkerPage.fxml"));
			Scene scene2 = new Scene(root2);			
			//scene2.getStylesheets().add(getClass().getResource("/fxml/WorkerPage.css").toExternalForm());
			primaryStage2.setTitle("");
			primaryStage2.initStyle(StageStyle.UNDECORATED);
			primaryStage2.setScene(scene2);	
			primaryStage2.setResizable(false);
			primaryStage2.show();
			break;
		
		case "Department Manager" :
			DepartmentManagerController.user=user;
			((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
			Stage primaryStage3 = new Stage();
			Parent root3 = FXMLLoader.load(getClass().getResource("/fxml/DepartmentManager.fxml"));
			Scene scene3 = new Scene(root3);			
			//scene3.getStylesheets().add(getClass().getResource("/fxml/DepartmentManager.css").toExternalForm());
			primaryStage3.initStyle(StageStyle.UNDECORATED);
			primaryStage3.setTitle("");
			primaryStage3.setScene(scene3);
			primaryStage3.setResizable(false);
			primaryStage3.show();
			break;
		case  "Support Center":
			SupportCenterController.user=user;
			((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
			Stage primaryStage4 = new Stage();
			Parent root4 = FXMLLoader.load(getClass().getResource("/fxml/SupportCenter.fxml"));
			Scene scene4 = new Scene(root4);			
			//scene4.getStylesheets().add(getClass().getResource("/fxml/SupportCenter.css").toExternalForm());
			primaryStage4.setTitle("");
			primaryStage4.initStyle(StageStyle.UNDECORATED);
			primaryStage4.setScene(scene4);
			primaryStage4.setResizable(false);
			primaryStage4.show();
			break;

		}
    }
    /**
     * Initializes the login view for workers.
     * This method is the entry point for the Worker Login GUI, setting up necessary UI components.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML.
     */

    public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/WorkerLogin.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/WorkerLogin.css").toExternalForm());
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setTitle("");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	} 

}
