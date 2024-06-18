package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.ReportData;
import entities.Request;
import entities.User;
import entities.Visit;
/**
 * Controller for the View Visits Report Screen. 
 * This class handles the UI logic for displaying the visits report, 
 * including the average duration time of visits grouped by single and group reservations.
 */
public class ViewVisitsRepoetController implements Initializable {
		@FXML
		private Button btnHome;
	 	@FXML
		private Button btnExit;
	 	@FXML
	    private CategoryAxis SingleChartX;

	    @FXML
	    private NumberAxis SingleChartY;

	    @FXML
	    private ScatterChart<String, Number> VisitChart;

	   

	
	public static User user;
	private int [] SingleEntranceTimeCounter= new int[11];
	private int [] GroupEntranceTimeCounter= new int[11];
	private Float [] SingleDurationTimeSum= new Float[11];
	private Float [] GroupDurationTimeSum= new Float[11];
	  /**
     * Handles the Home button click event.
     * This method hides the current window and opens the DepartmentManager screen.
     *
     * @param event The action event triggering this method.
     * @throws IOException if an error occurs during loading the FXML.
     */
	@FXML
    void Home(ActionEvent event) throws IOException {
    	
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
	/**
     * Handles the Exit button click event.
     * This method performs a logout operation and closes the application.
     *
     * @param event The action event triggering this method.
     * @throws IOException if an error occurs during the logout process.
     */
 @FXML
	void Exit (ActionEvent event) throws IOException {
    	logoutFunc(event);
		ClientUI.chat.closeConnection();
    	ClientUI.chat.quit();
	}
 /**
  * Performs the logout operation.
  * This method sends a logout request to the server and navigates to the Welcome Page.
  *
  * @param event The action event triggering this method.
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
     * Calculates the average duration time of visits based on their reservation type (Single or Group).
     * This method processes a list of Visit objects to compute the average visit duration for each hour.
     *
     * @param visit The list of Visit objects to be processed.
     */
	
	public void CalculateAvrageDurationTime(ArrayList <Visit>  visit) {
		
		    Arrays.fill(SingleEntranceTimeCounter, 0);
		    Arrays.fill(GroupEntranceTimeCounter, 0);
		    Arrays.fill(SingleDurationTimeSum, 0.0f);
		    Arrays.fill(GroupDurationTimeSum, 0.0f);

		    for (Visit v : visit) {
		        int index = Integer.parseInt(v.getEntrancetime().split(":")[0]) - 10;
		        if (index >= 0 && index < SingleEntranceTimeCounter.length) {
		            if (v.getReservationType().equals("Single")) {
		                SingleEntranceTimeCounter[index]++;
		                SingleDurationTimeSum[index] += v.getDurationTime();

		            } else {
		                GroupEntranceTimeCounter[index]++;
		                GroupDurationTimeSum[index] += v.getDurationTime();

		            }
		        }
		    }

		    for (int i = 0; i < SingleEntranceTimeCounter.length; i++) {
		        if (SingleEntranceTimeCounter[i] > 0) {
		            SingleDurationTimeSum[i] /= SingleEntranceTimeCounter[i];
		        }
		        if (GroupEntranceTimeCounter[i] > 0) {
		            GroupDurationTimeSum[i] /= GroupEntranceTimeCounter[i];
		        }
		    }
		        for (int i = 0; i < SingleEntranceTimeCounter.length; i++) {
		            if (SingleEntranceTimeCounter[i] > 0) {
		                SingleDurationTimeSum[i] /= SingleEntranceTimeCounter[i];
		            } else {
		                SingleDurationTimeSum[i] = 0.0f;  
		            }
		            if (GroupEntranceTimeCounter[i] > 0) {
		                GroupDurationTimeSum[i] /= GroupEntranceTimeCounter[i];
		            } else {
		                GroupDurationTimeSum[i] = 0.0f;  
		            }

		            
		            System.out.println("Time: " + (10 + i) + ":00");
		            System.out.println("Single Duration Sum: " + SingleDurationTimeSum[i]);
		            System.out.println("Group Duration Sum: " + GroupDurationTimeSum[i]);
		        }
		        
	}
	/**
     * Initializes the controller class.
     * This method is automatically called after the FXML file has been loaded. It sets up the chart data.
     *
     * @param arg0 The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param arg1 The resources used to localize the root object, or null if the root object was not localized.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 	
		 	CalculateAvrageDurationTime(ClientUI.chat.visit);
		    XYChart.Series<String, Number> seriesSingle = new XYChart.Series<>();
		    XYChart.Series<String, Number> seriesGroup = new XYChart.Series<>();

		    for (int i = 0; i < SingleDurationTimeSum.length; i++) {
		        String timeLabel = String.format("%02d:00", 10 + i);
		        System.out.println(timeLabel);
		        System.out.println(SingleDurationTimeSum[i]);
		        System.out.println(SingleEntranceTimeCounter[i]);
		        
		        seriesSingle.getData().add(new XYChart.Data<>(timeLabel, SingleDurationTimeSum[i]));
		        seriesGroup.getData().add(new XYChart.Data<>(timeLabel, GroupDurationTimeSum[i]));
		        
		    }
		    seriesSingle.setName("average duration time according to entrance time for single reservations");
		    seriesGroup.setName("average duration time according to entrance time for single reservations");
		    SingleChartX.setCategories(FXCollections.observableArrayList("10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00"));

		    VisitChart.getData().add(seriesSingle);
		    VisitChart.getData().add(seriesGroup);
	
	}
	  /**
     * Starts the view visits report scene.
     * This method sets up and displays the scene for the view visits report.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML.
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/viewVisitsRepoet.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/viewVisitsRepoet.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	} 
}
