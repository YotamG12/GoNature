package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import GNClient.ClientUI;
import common.RequestType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Parameters;
import entities.Request;
import entities.Reservation;
import entities.User;
/**
 * Controls the Reservation Management GUI for managing reservations. 
 * It provides functionality to approve or cancel reservations, navigate back, and exit the application.
 */
public class ReservationManagmentController implements Initializable{

    @FXML
    private Button btnApprove;

    @FXML
    private Button btnCancelOrder;
    
    @FXML
    private Button btnBack;

    @FXML
    private Button btnExit;

    @FXML
    private TableColumn<String,Integer> clmReservationID;

    @FXML
    private TableColumn<String, String> clmUserID;

    @FXML
    private TableColumn<String, String> clmdate;

    @FXML
    private TableColumn<String, String> clmemail;

    
    @FXML
    private TableColumn<String, String> clmnumOfVisitors;

    @FXML
    private TableColumn<String, String> clmparkName;

    @FXML
    private TableColumn<String, String> clmphoneNum;

    @FXML
    private TableColumn<String, String> clmtime;

    @FXML
    private Label lblNote;

    @FXML
    private TableView<Reservation> tblReservationManagment;
    
    private static Reservation reservation=new Reservation(-1,"","","","","","","", false,"");
    private Integer ReservationID;
   
    
    public static User user;
    /**
     * Handles the logout functionality, terminating the user session and closing the application.
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
     * Navigates back to the previous screen based on the user's permission level.
     * @param event The action event that triggers this method.
     * @throws IOException If an error occurs during navigation.
     */
    @FXML
    void Back(ActionEvent event) throws IOException {
    	if(user.getPermission().equals("Group")) {
    		((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
    		Stage primaryStage = new Stage();
    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/GroupGuideGUI.fxml"));
		
		
    		Scene scene = new Scene(root);			
    		//scene.getStylesheets().add(getClass().getResource("/fxml/GroupGuideGUI.css").toExternalForm());
    		primaryStage.setTitle("");
    		primaryStage.initStyle(StageStyle.UNDECORATED);
    		primaryStage.setScene(scene);
    		primaryStage.setResizable(false);
			
    		primaryStage.show();
		}
    	else {
    		((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
    		Stage primaryStage = new Stage();
    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/SingleTravelerGUI.fxml"));
		
		
    		Scene scene = new Scene(root);			
    		//scene.getStylesheets().add(getClass().getResource("/fxml/SingleTravelerGUI.css").toExternalForm());
    		primaryStage.setTitle("");
    		primaryStage.initStyle(StageStyle.UNDECORATED);
    		primaryStage.setScene(scene);
    		primaryStage.setResizable(false);
			
    		primaryStage.show();
    	}
	
	
    }
    /**
     * Approves a selected reservation.
     * @param event The action event that triggers this method.
     */
    @FXML
    void ApproveReservation(ActionEvent event) {
    	Reservation row=tblReservationManagment.getSelectionModel().getSelectedItem();
    	System.out.println(row.toString());
    	ReservationID=row.getReservationID();
    	for(Reservation res :ClientUI.chat.reservation ) {
    		if(res.getReservationID()==ReservationID) {
    			reservation=res;
    		}
    	}
    	Request request= new Request();
    	request.setType(RequestType.APPROVE_RESERVATION);
    	
    	request.setRequest(reservation);
		System.out.println(request.getRequest().toString());
	    byte[] arr;
	    try {
	    	arr = request.getBytes();
	        System.out.println("Serialized bytes: " + arr.length);
	        ClientUI.chat.handleMessageFromClientUI(arr);
	            
	    } catch (IOException e) {
	         e.printStackTrace();
	    }
    	
    
	    tblReservationManagment.getItems().remove(row);
    }
    	
    /**
     * Cancels a selected reservation.
     * @param event The action event that triggers this method.
     */
    @FXML
    void CancelOrder(ActionEvent event) {
    	Reservation row=tblReservationManagment.getSelectionModel().getSelectedItem();
    	System.out.println(row.toString());
    	ReservationID=row.getReservationID();
    	for(Reservation res :ClientUI.chat.reservation ) {
    		if(res.getReservationID()==ReservationID) {
    			reservation=res;
    		}
    	}
    	Request request= new Request();
    	request.setType(RequestType.CANCEL_RESERVATIONS);
    	
    	request.setRequest(reservation);
		System.out.println(request.getRequest().toString());
	    byte[] arr;
	    try {
	    	arr = request.getBytes();
	        System.out.println("Serialized bytes: " + arr.length);
	        ClientUI.chat.handleMessageFromClientUI(arr);
	            
	    } catch (IOException e) {
	         e.printStackTrace();
	    }
    	
    
	    tblReservationManagment.getItems().remove(row);
    }

    /**
     * Initializes the controller class. This method is called after the FXML file has been loaded.
     * It sets up the table view with reservation data.
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<Reservation> obsReportData = FXCollections.observableArrayList(ClientUI.chat.reservation);
        clmReservationID.setCellValueFactory(new PropertyValueFactory<>("ReservationID"));
        clmUserID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        clmdate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        clmnumOfVisitors.setCellValueFactory(new PropertyValueFactory<>("numOfVisitors"));
        clmparkName.setCellValueFactory(new PropertyValueFactory<>("parkName"));
        clmphoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        clmtime.setCellValueFactory(new PropertyValueFactory<>("time"));

        tblReservationManagment.setItems(obsReportData);
       
		
	}
	   /**
     * Starts the stage for the Reservation Management GUI.
     * @param primaryStage The primary stage for this application.
     * @throws Exception If an error occurs during loading the FXML file.
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ReservationManagment.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/ReservationManagment.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
    

}

