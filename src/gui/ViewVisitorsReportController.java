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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Report;
import entities.Request;
import entities.User;
/**
 * Controls the visitors report view, displaying data in a pie chart format.
 * Handles navigation and session termination functionalities for users.
 */
public class ViewVisitorsReportController implements Initializable {
	@FXML
	private Button btnHome;
	
	@FXML
	private Button btnExit;
    @FXML
    private PieChart PChart;
    public static User user;
    public static Report report;
    /**
     * Handles navigation back to the home screen of the current user's role, 
     * either Park Manager or Department Manager.
     *
     * @param event The action event that triggers this method.
     * @throws IOException If an error occurs during the screen transition.
     */
    @FXML
    void Home(ActionEvent event) throws IOException {
    	if(user.getPermission().equals("Park Manager")) {
    		((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
    		Stage primaryStage = new Stage();
    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ParkManager.fxml"));

    		Scene scene = new Scene(root);			
    		//scene1.getStylesheets().add(getClass().getResource("/fxml/ParkManager.css").toExternalForm());
    		primaryStage.setTitle("");
    		primaryStage.initStyle(StageStyle.UNDECORATED);
    		primaryStage.setScene(scene);
    		primaryStage.setResizable(false);
    		primaryStage.show();
    	}
    	else {
    		((Node) event.getSource()).getScene().getWindow().hide();; //hiding primary window
    		Stage primaryStage = new Stage();
    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/DepartmentManager.fxml"));

    		Scene scene = new Scene(root);			
    		//scene1.getStylesheets().add(getClass().getResource("/fxml/DepartmentManager.css").toExternalForm());
    		primaryStage.setTitle("");
    		primaryStage.initStyle(StageStyle.UNDECORATED);
    		primaryStage.setScene(scene);
    		primaryStage.setResizable(false);
    		primaryStage.show();
    	}
 }
    /**
     * Closes the application window and ends the session by logging out the user.
     *
     * @param event The action event that triggers this method.
     * @throws IOException If an error occurs in closing the connection or the application.
     */
 @FXML
	void Exit (ActionEvent event) throws IOException {
    	logoutFunc(event);
		ClientUI.chat.closeConnection();
    	ClientUI.chat.quit();
	}
 /**
  * Logs out the current user and returns to the welcome page.
  * Sends a logout request to the server for session termination.
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
     * Initializes the controller class. Sets up the pie chart with the visitors report data.
     * 
     * @param url The location used to resolve relative paths for the root object, or null if unknown.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setPieChart();
		
	}
	/**
     * Configures and populates the pie chart with visitors report data.
     * Binds the chart data names to include the value for better clarity.
     */
	void setPieChart() {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Single",ClientUI.chat.visitorsReportData.get(0)),
				new PieChart.Data("Group",ClientUI.chat.visitorsReportData.get(1)));
		
		PChart.setData(pieChartData);
		PChart.setLabelsVisible(true); // Ensure labels are visible
	    PChart.getData().forEach(data -> 
	        data.nameProperty().bind(
	            javafx.beans.binding.Bindings.concat(
	                data.getName(), " ", data.pieValueProperty().intValue())));
	}
	 /**
     * Starts the stage for the visitors report GUI.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception If any error occurs during application startup.
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/viewVisitorsReport.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/viewVisitorsReport.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	} 

}