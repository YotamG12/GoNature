package gui;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.ReportData;
import entities.Request;
import entities.User;


/**
 * Controller class for the use report view. It displays data on the application's use, 
 * such as periods of full capacity. This class manages navigation and session management 
 * for the user viewing the report.
 */
public class ViewUseReportController implements Initializable {
	
	@FXML
	private Button btnHome;
	@FXML
	private Button btnExit;
    @FXML
    private TableView<ReportData> tblNotFullCapacity;
    @FXML
    private TableColumn<String, String> clmDate;

    @FXML
    private TableColumn<String, String> clmHour;
    
    @FXML
    private BarChart<?, ?> BarChart;
    
    public static User user;
    /**
     * Navigates back to the user's respective management screen, either Park Manager or Department Manager, 
     * based on their permissions.
     *
     * @param event The event triggered by pressing the Home button.
     * @throws IOException If the FXML file cannot be loaded.
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
     * Handles the application logout procedure and closes the application window.
     *
     * @param event The event triggered by pressing the Exit button.
     * @throws IOException If an I/O error occurs.
     */
 @FXML
	void Exit (ActionEvent event) throws IOException {
    	logoutFunc(event);
		ClientUI.chat.closeConnection();
    	ClientUI.chat.quit();
	}
 /**
  * Performs logout for the current user session and navigates back to the welcome page.
  * This method sends a logout request to the server for the current user.
  *
  * @param event The event triggered by user action that requires logout.
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
     * Initializes the controller class. Specifically, it sets up the table to display the use report data.
     * 
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setBarChart();
		
	}
	/**
	 * Starts the view use report stage, setting up the necessary scene and styles.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If any error occurs during the application startup.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/viewUseReport.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/viewUseReport.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	} 
	/**
	 * Sets up a bar chart based on the data obtained from ClientUI.
	 * The bar chart represents the count of reports for each hour.
	 */
	void setBarChart() {
	    ArrayList<ReportData> reportList = new ArrayList<>();
	    for (String data : ClientUI.chat.useReportData) {
	        String[] dataArray = data.split(",");
	        reportList.add(new ReportData(dataArray[0], dataArray[1]));
	    }

	    TreeMap<String, Integer> hourCounts = new TreeMap<>();
	    for (ReportData data : reportList) {
	        String hour = data.getHour();
	        hourCounts.put(hour, hourCounts.getOrDefault(hour, 0) + 1);
	    }
	    System.out.println(hourCounts.toString());
	    XYChart.Series seriesUse= new XYChart.Series();
	    for (Map.Entry<String, Integer> entry : hourCounts.entrySet()) {
	        seriesUse.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
	    }
	    
	    seriesUse.setName("Amount days the park wasn't in full capacity");
	    BarChart.getData().addAll(seriesUse);
	}
}