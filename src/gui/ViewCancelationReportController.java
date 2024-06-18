package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import GNClient.ClientUI;
import common.RequestType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.CanceledReport;
import entities.Request;
import entities.User;
/**
 * Handles the visualization of cancellation reports within the GUI.
 * This class is responsible for initializing and populating both bar and line charts
 * with cancellation data retrieved from the server.
 */
public class ViewCancelationReportController implements Initializable  {
	@FXML
    private Button btnHome;
    
    @FXML
    private Button btnExit;
    @FXML
    private BarChart<String, Number> BarChart;

    @FXML
    private CategoryAxis BarChartDayX;

    @FXML
    private NumberAxis BarChartY;

    @FXML
    private LineChart<String, Number> CancelLineChart;

    @FXML
    private CategoryAxis DayX;

    @FXML
    private NumberAxis camcelOrdersY;
    
    public static CanceledReport canceledReport;
    public static User user;
    
    private Float [] CancelationPerDay= new Float[7];
	private Float [] CancelationDayCounter= new Float[7];
	private Float [] UnresolvedPerDaySum= new Float[7];
	private Float [] UnresolvedDayCounter= new Float[7];
	private Float [] CancelationAvragePerDay= new Float[7];
	   /**
     * Navigates back to the Department Manager's main GUI.
     * 
     * @param event ActionEvent that triggers this method.
     * @throws IOException If an error occurs during navigation.
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
	     * Exits the application by closing the connection and quitting the client UI.
	     * 
	     * @param event ActionEvent that triggers this method.
	     * @throws IOException If an error occurs during the process.
	     */
	 @FXML
		void Exit (ActionEvent event) throws IOException {
	    	logoutFunc(event);
			ClientUI.chat.closeConnection();
	    	ClientUI.chat.quit();
		}
	 /**
	     * Calculates the total and average number of cancellations per day based on data
	     * retrieved from the server.
	     * 
	     * @param hashMap A mapping of dates to the number of cancellations.
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
	     * Calculates the total number of unresolved cancellations per day based on data
	     * retrieved from the server.
	     * 
	     * @param hashMap A mapping of dates to the number of unresolved cancellations.
	     */
	public void CalculateCancelationPerDate(HashMap<String,Integer> hashMap) {
		LocalDate localDate;
		Set<String> keys = hashMap.keySet();
		ArrayList<String> date = new ArrayList<>(keys);
		for(int i=0;i<date.size();i++) {
			localDate=LocalDate.parse(date.get(i));
			CancelationPerDay[localDate.getDayOfWeek().getValue()-1]+=hashMap.get(date.get(i));
			CancelationDayCounter[localDate.getDayOfWeek().getValue()-1]+=1;
		}
		for(int i=0;i<CancelationPerDay.length;i++) {
			if(CancelationDayCounter[i]>0)
				
				CancelationAvragePerDay[i]=(float) (CancelationPerDay[i]/CancelationDayCounter[i]);
				System.out.println(CancelationPerDay[i]);
		}
		
		
	
	}
	   /**
     * Calculates the total number of unresolved cancellations per day based on data
     * retrieved from the server.
     * 
     * @param hashMap A mapping of dates to the number of unresolved cancellations.
     */
	public void CalculateUnresolvedPerDate(HashMap<String,Integer> hashMap) {
		LocalDate localDate;
		Set<String> keys = hashMap.keySet();
		ArrayList<String> date = new ArrayList<>(keys);
		for(int i=0;i<date.size();i++) {
			localDate=LocalDate.parse(date.get(i));
			System.out.println(localDate.getDayOfWeek());
			UnresolvedPerDaySum[localDate.getDayOfWeek().getValue()-1]+=hashMap.get(date.get(i));
			UnresolvedDayCounter[localDate.getDayOfWeek().getValue()-1]+=1;
		}
	}
	 /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It initializes and populates the charts
     * with cancellation and unresolved data.
     * 
     * @param arg0 The location used to resolve relative paths for the root object, or null if unknown.
     * @param arg1 The resources used to localize the root object, or null if unknown.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		Arrays.fill(CancelationPerDay, 0.0f);
		Arrays.fill(CancelationDayCounter, 0.0f);
		Arrays.fill(UnresolvedPerDaySum, 0.0f);
		Arrays.fill(UnresolvedDayCounter, 0.0f);
		Arrays.fill(CancelationAvragePerDay, 0.0f);
		CalculateCancelationPerDate(ClientUI.chat.canceledReport.getAmountOfCanceledVisitInDate());
		CalculateUnresolvedPerDate(ClientUI.chat.canceledReport.getAmountOfUnresolvedOrders());
		
		XYChart.Series seriesCancelation= new XYChart.Series();
		XYChart.Series seriesAvrageCancelationPerDay= new XYChart.Series();
		XYChart.Series seriesUnresolved= new XYChart.Series();
		
		seriesCancelation.getData().add(new XYChart.Data("Sunday",CancelationPerDay[6]));
		seriesCancelation.getData().add(new XYChart.Data("Monday",CancelationPerDay[0]));
		seriesCancelation.getData().add(new XYChart.Data("Tuesday",CancelationPerDay[1]));
		seriesCancelation.getData().add(new XYChart.Data("Wednesday",CancelationPerDay[2]));
		seriesCancelation.getData().add(new XYChart.Data("Thursday",CancelationPerDay[3]));
		seriesCancelation.getData().add(new XYChart.Data("Friday",CancelationPerDay[4]));
		seriesCancelation.getData().add(new XYChart.Data("Saturday",CancelationPerDay[5]));
		
		
		seriesUnresolved.getData().add(new XYChart.Data("Sunday",UnresolvedPerDaySum[6]));
		seriesUnresolved.getData().add(new XYChart.Data("Monday",UnresolvedPerDaySum[0]));
		seriesUnresolved.getData().add(new XYChart.Data("Tuesday",UnresolvedPerDaySum[1]));
		seriesUnresolved.getData().add(new XYChart.Data("Wednesday",UnresolvedPerDaySum[2]));
		seriesUnresolved.getData().add(new XYChart.Data("Thursday",UnresolvedPerDaySum[3]));
		seriesUnresolved.getData().add(new XYChart.Data("Friday",UnresolvedPerDaySum[4]));
		seriesUnresolved.getData().add(new XYChart.Data("Saturday",UnresolvedPerDaySum[5]));
		
		
		seriesAvrageCancelationPerDay.getData().add(new XYChart.Data("Sunday",CancelationAvragePerDay[6]));
		seriesAvrageCancelationPerDay.getData().add(new XYChart.Data("Monday",CancelationAvragePerDay[0]));
		seriesAvrageCancelationPerDay.getData().add(new XYChart.Data("Tuesday",CancelationAvragePerDay[1]));
		seriesAvrageCancelationPerDay.getData().add(new XYChart.Data("Wednesday",CancelationAvragePerDay[2]));
		seriesAvrageCancelationPerDay.getData().add(new XYChart.Data("Thursday",CancelationAvragePerDay[3]));
		seriesAvrageCancelationPerDay.getData().add(new XYChart.Data("Friday",CancelationAvragePerDay[4]));
		seriesAvrageCancelationPerDay.getData().add(new XYChart.Data("Saturday",CancelationAvragePerDay[5]));
		
		seriesCancelation.setName("Amount of canceled reservations per day");
		seriesUnresolved.setName("Amount of unresolved reservations per day");
		seriesAvrageCancelationPerDay.setName("Average canceled reservations per day");
		
		BarChart.getData().addAll(seriesUnresolved);
		BarChart.getData().addAll(seriesCancelation);
		CancelLineChart.getData().addAll(seriesAvrageCancelationPerDay);
	}
	  /**
     * Starts the stage for the cancellation report GUI.
     * 
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML file.
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/viewCancelationReport.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/viewCancelationReport.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	} 

}
