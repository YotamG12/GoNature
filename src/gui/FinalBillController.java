package gui;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

import GNClient.ClientUI;
import common.RequestType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Request;
import entities.Reservation;
import entities.User;
/**
 * Controls the GUI for displaying the final bill to the user. It provides details
 * such as the reservation type, applicable discounts, and the total amount due.
 * Users can navigate back to the home screen or log out from this interface.
 */
public class FinalBillController implements Initializable, Serializable{

    @FXML
    private Button btnExit;
    
    @FXML
    private Button btnHome;

    @FXML
    private ImageView imgBackToHomePage;

    @FXML
    private Label lblDiscountDescription;

    @FXML
    private Label lblError;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private Label lblTypeDescription;

    @FXML
    private Text txtTypeDescription;

    @FXML
    private Text txtDiscountDesctription;

    @FXML
    private Text txtTotalAmount;
    public static String totalAmount;
    public static Reservation reservation;
    
    public static User user;
    /**
     * Handles the "Exit" action by logging out and closing the application.
     *
     * @param event The event that triggers this action.
     * @throws IOException If an error occurs during navigation.
     */
    @FXML
	void Exit (ActionEvent event) throws IOException {
    	logoutFunc(event);
		ClientUI.chat.closeConnection();
    	ClientUI.chat.quit();
	}
    /**
     * Logs the user out and navigates back to the welcome page.
     *
     * @param event The event that triggers this action.
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
     * Sets the final bill details for a group reservation with prepayment.
     *
     * @param totalAmount The total amount to be paid.
     */
    public void SetTextForGroupReservationPrepayment(String totalAmount) {
    	txtTypeDescription.setVisible(true);
    	txtTypeDescription.setText("Group Reservation, Prepayment");
    	txtDiscountDesctription.setVisible(true);
    	txtDiscountDesctription.setText("25% discount from original amount,"
    			+ "12% additional discount for prepayment,"
    			+ "Group Guide has free entance");
    	txtTotalAmount.setVisible(true);
    	txtTotalAmount.setText(totalAmount+"$");
    	//Exit Button
    }
    /**
     * Sets the final bill details for a single reservation that was pre-ordered.
     *
     * @param totalAmount The total amount to be paid.
     */
    
    public void setTextForSingleReservation(String totalAmount) {
    	txtTypeDescription.setVisible(true);
    	txtTypeDescription.setText("Single Reservation, Pre-Ordered");
    	txtDiscountDesctription.setVisible(true);
    	txtDiscountDesctription.setText("15% discount from original amount");
    	txtTotalAmount.setVisible(true);
    	txtTotalAmount.setText(totalAmount+"$");
    	//Exit Button
    }
    /**
     * Sets the final bill details for a single reservation made occasionally.
     *
     * @param totalAmount The total amount to be paid.
     */
    public void setTextForSingleReservationOccasional(String totalAmount) {
    	txtTypeDescription.setVisible(true);
    	txtTypeDescription.setText("Single Reservation, Occasional");
    	txtDiscountDesctription.setVisible(true);
    	txtDiscountDesctription.setText("Full price");
    	txtTotalAmount.setVisible(true);
    	txtTotalAmount.setText(totalAmount+"$");
    	//Exit Button
    }
    /**
     * Sets the final bill details for a group reservation.
     *
     * @param totalAmount The total amount to be paid.
     */
    public void SetTextForGroupReservation(String totalAmount) {
    	txtTypeDescription.setVisible(true);
    	txtTypeDescription.setText("Group Reservation, Pre-Ordered");
    	txtDiscountDesctription.setVisible(true);
    	txtDiscountDesctription.setText("25% discount from original amount,"
    			+ "Group Guide has free entance");
    	txtTotalAmount.setVisible(true);
    	txtTotalAmount.setText(totalAmount+"$");
    	//Exit Button
    }
    /**
     * Sets the final bill details for an occasional group reservation.
     *
     * @param totalAmount The total amount to be paid.
     */
    public void SetTextForGroupReservationOccasional(String totalAmount) {
    	txtTypeDescription.setVisible(true);
    	txtTypeDescription.setText("Group Reservation, Occasional");
    	txtDiscountDesctription.setVisible(true);
    	txtDiscountDesctription.setText("10% discount from original amount");
    	txtTotalAmount.setVisible(true);
    	txtTotalAmount.setText(totalAmount+"$");
    	//Exit Button
    }
    /**
     * Navigates the user back to the home screen based on their permission level.
     *
     * @param event The event that triggers this action.
     * @throws IOException If an error occurs during navigation.
     */
    @FXML
    void Home(ActionEvent event) throws IOException {
    	if(user.getPermission().equals("Group")) {
    		((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
    		Stage primaryStage = new Stage();
    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/GroupGuideGUI.fxml"));

    		Scene scene = new Scene(root);			
    		//scene1.getStylesheets().add(getClass().getResource("/fxml/GroupGuideGUI.css").toExternalForm());
    		primaryStage.setTitle("");
    		primaryStage.initStyle(StageStyle.UNDECORATED);
    		primaryStage.setScene(scene);
    		primaryStage.setResizable(false);
    		primaryStage.show();
    	}
    	else {
    		((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
    		Stage primaryStage = new Stage();
    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/WorkerPage.fxml"));

    		Scene scene = new Scene(root);			
    		//scene1.getStylesheets().add(getClass().getResource("/fxml/WorkerPage.css").toExternalForm());
    		primaryStage.setTitle("");
    		primaryStage.initStyle(StageStyle.UNDECORATED);
    		primaryStage.setScene(scene);
    		primaryStage.setResizable(false);
    		primaryStage.show();
    	}
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up the final bill based on
     * the reservation details.
     *
     * @param location The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println(reservation.toString());
		if(reservation.getType().equals("Single")) {
			setTextForSingleReservation(totalAmount);
		}
		else if(reservation.getType().equals("Group")) {
			if(reservation.isPrepayment()) {
				SetTextForGroupReservationPrepayment(totalAmount);
			}
			else {
				SetTextForGroupReservation(totalAmount);
			}
				
		}
		else if (reservation.getType().equals("Group_occ")) {
			SetTextForGroupReservationOccasional(totalAmount);
		}
		else if (reservation.getType().equals("Single_occ")) {
			setTextForSingleReservationOccasional(totalAmount);
		}
	}
	  /**
     * Starts the final bill GUI.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML file.
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/FinalBill.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/FinalBill.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}
