package gui;
import java.io.IOException;
import javafx.application.Application;
import GNClient.ChatClient;
import GNClient.ClientUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import gui.SingleReservationController;
/**
 * Controls the GUI for setting the server IP and port number for the client to connect.
 * Allows the user to enter the IP address and port number of the server they wish to connect to.
 * Upon submitting the IP and port, the application attempts to establish a connection with the server.
 */
public class ServerIpController extends Application {
	public static String IP;
	public static int Port;

    @FXML
    private Button btnIP;

    @FXML
    private Label lblServerip;

    @FXML
    private TextField txtIP;
    
    @FXML
    private TextField txtPortNumber;
    /**
     * Exits the application, closing any open connections.
     * @param event The event that triggered this action.
     * @throws IOException if an I/O error occurs.
     */
    @FXML
	void Exit (ActionEvent event) throws IOException {
		System.exit(0);
	}
    /**
     * Retrieves the IP address and port number entered by the user, sets the client configuration, and attempts to connect to the server.
     * If successful, transitions to the WelcomePage GUI.
     * @param event The event that triggered this action.
     * @throws Exception if an error occurs during connection or scene transition.
     */
    public void getIP(ActionEvent event) throws Exception{
    	
    	try
	    {
    		IP=txtIP.getText();
    		Port=Integer.parseInt(txtPortNumber.getText());
    		
    		System.out.println(Port);
    		System.out.println(IP);
    		ClientUI.host=IP;
        	System.out.println(ClientUI.host);
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	    	ClientUI.host = "localhost";
	    }
    	
    	ClientUI.chat=new ChatClient(ClientUI.host,Port,new ClientUI());
    	((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomePage.fxml"));
				
	
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/fxml/WelcomePage.css").toExternalForm());
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);

		primaryStage.setResizable(false);
		primaryStage.show();
		
    	
    	
    	
    }
    /**
     * Initializes and shows the primary stage for setting the server IP and port.
     * @param primaryStage The primary stage for this application.
     * @throws IOException if an I/O error occurs.
     */
	public void start(Stage primaryStage) throws IOException {
		// TODO Auto-generated method stub
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ServerIP.fxml"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/serverIP.css").toExternalForm());
		
		primaryStage.setTitle("Connect to server");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	/**
     * The main method that launches the application.
     * @param args Command line arguments.
     * @throws Exception if an error occurs during application launch.
     */
	public static void main( String args[] ) throws Exception{ 
		
		
	    launch(args); 
} // end main


}


