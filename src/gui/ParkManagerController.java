
package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entities.Parameters;
import entities.ParkDetails;
import entities.Report;
import entities.Request;
import entities.User;
/**
 * The ParkManagerController class is responsible for handling all park manager-related actions
 * within the GUI. This includes generating reports, updating park parameters, and logging out.
 * It provides the functionality to create visitors and usage reports for a specific date range,
 * update park capacity, gap between visits, and the duration of a visit. It also handles user logout
 * and exit actions.
 */
public class ParkManagerController implements Initializable {

	@FXML
	private DatePicker DateFrom;

	@FXML
	private DatePicker DateTo;

	@FXML
	private Button btnCreate;

	@FXML
	private Button btnUP;

	@FXML
	private Button btnUseReport;

	@FXML
	private Button btnVisitorsReport;

	@FXML
	private Button btnLogout;

	@FXML
	private Button btnSubmirPar;

	@FXML
	private ComboBox<String> comboxPar;

	@FXML
	private Label lblTitle;

	@FXML
	private TextField txtValue;

	@FXML
	private Text txtFail;
	@FXML
	private Button btnExit;
	@FXML
	private TableView<ParkDetails> tblParkDetails;

	@FXML
	private TableColumn<ParkDetails, String> clmCapacity;

	@FXML
	private TableColumn<ParkDetails, String> clmDurationTime;

	@FXML
	private TableColumn<ParkDetails, String> clmGap;

	@FXML
	private TableColumn<ParkDetails, String> clmParkName;
	
	@FXML
    private Label lblParkDetails;
	
    @FXML
    private Text txtReoport;

	public static User user;
	private Report report;

	private Parameters parameters = new Parameters("", "", "", "");
	/**
     * Creates a report based on the selected date range and report type. This report can be either a visitors report
     * or a usage report. The method checks if the selected dates constitute a full month and sends a request to the server
     * to generate the report.
     *
     * @param event ActionEvent that triggers report creation.
     */
	@FXML
	void CreateReport(ActionEvent event) {
		// just create with no viewing them
		if (checkFullMonth(DateFrom.getValue(), DateTo.getValue())) {
			report.setDateFrom(DateFrom.getValue().toString());
			report.setDateTo(DateTo.getValue().toString());

			Request request = new Request();
			if (report.getType().equals("Visitors")) {
				request.setType(RequestType.TOTAL_VISITORS_NUM_REPORT);

			} else {
				request.setType(RequestType.USE_REPORT);
			}
			request.setRequest(report);
			System.out.println(request.getRequest().toString());
			byte[] arr;
			try {
				arr = request.getBytes();
				System.out.println("Serialized bytes: " + arr.length);
				ClientUI.chat.handleMessageFromClientUI(arr);
				if (ClientUI.chat.createReportStatus.equals("yes")) {
					
					txtFail.setVisible(false);
					btnCreate.setDisable(true);
					Request request1 = new Request();
					if (report.getType().equals("Visitors")) {

						request1.setType(RequestType.VIEW_TOTAL_VISITORS_NUM_REPORT);

					} else {
						request1.setType(RequestType.VIEW_USE_REPORT);
					}
					request1.setRequest(report);
					System.out.println(request1.getRequest().toString());
					byte[] arr1;
					try {
						arr1 = request1.getBytes();
						System.out.println("Serialized bytes: " + arr1.length);
						ClientUI.chat.handleMessageFromClientUI(arr1);
						if (report.getType().equals("Visitors")) {
							if (ClientUI.chat.visitorsReportData.isEmpty()) {
								Alert alert = new Alert(AlertType.ERROR);
								// Set the title of the Alert
								alert.setTitle("Error");
								// Set the header text of the Alert
								alert.setHeaderText(null);
								// Set the content text of the Alert
								alert.setContentText("There is no available report to show");

								// Display the Alert
								alert.showAndWait();
							} else {

								ViewVisitorsReportController.user = user;
								((Node) event.getSource()).getScene().getWindow().hide();
								; // hiding primary window
								Stage primaryStage = new Stage();
								Parent root = FXMLLoader.load(getClass().getResource("/fxml/ViewVisitorsReport.fxml"));

								Scene scene = new Scene(root);
								// scene1.getStylesheets().add(getClass().getResource("/fxml/ViewVisitorsReport.css").toExternalForm());
								primaryStage.setTitle("");
								primaryStage.initStyle(StageStyle.UNDECORATED);
								primaryStage.setScene(scene);
								primaryStage.setResizable(false);
								primaryStage.show();
							}

						} else {
							if (ClientUI.chat.useReportData.isEmpty()) {
								Alert alert = new Alert(AlertType.ERROR);
								// Set the title of the Alert
								alert.setTitle("Error");
								// Set the header text of the Alert
								alert.setHeaderText(null);
								// Set the content text of the Alert
								alert.setContentText("There is no available report to show");

								// Display the Alert
								alert.showAndWait();
							} else {
								ViewUseReportController.user = user;
								((Node) event.getSource()).getScene().getWindow().hide();
								; // hiding primary window
								Stage primaryStage = new Stage();
								Parent root = FXMLLoader.load(getClass().getResource("/fxml/ViewUseReport.fxml"));

								Scene scene = new Scene(root);
								// scene1.getStylesheets().add(getClass().getResource("/fxml/ViewUseReport.css").toExternalForm());
								primaryStage.setTitle("");
								primaryStage.initStyle(StageStyle.UNDECORATED);
								primaryStage.setScene(scene);
								primaryStage.setResizable(false);
								primaryStage.show();
							}

						}

					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					txtFail.setVisible(true);
					
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			// Set the title of the Alert
			alert.setTitle("Error");
			// Set the header text of the Alert
			alert.setHeaderText(null);
			// Set the content text of the Alert
			alert.setContentText("You must choose a full month correctly");

			// Display the Alert
			alert.showAndWait();
		}

	}
	 /**
     * Checks if the given date range constitutes a full month.
     *
     * @param date1 The start date of the range.
     * @param date2 The end date of the range.
     * @return true if the date range is a full month; false otherwise.
     */
	boolean checkFullMonth(LocalDate date1, LocalDate date2) {
		LocalDate currentDate = LocalDate.now();
		if (date1.getDayOfMonth() == 1) {
			if (date2.getDayOfMonth() == date2.lengthOfMonth()) {
				if ((date1.getMonth() == date2.getMonth()) && (date1.getYear() == date2.getYear())) {
					if (date1.isBefore(currentDate) && date2.isBefore(currentDate)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	  /**
     * Moves to the Update Parameters view, allowing the park manager to update park settings such as capacity,
     * visitation gap, and visit duration.
     *
     * @param event ActionEvent that triggers the move to Update Parameters view.
     */
	@FXML
	void MoveToUpdateParameters(ActionEvent event) {
		DateFrom.setVisible(false);
		DateTo.setVisible(false);
		btnCreate.setVisible(false);
		txtReoport.setVisible(false);
		txtFail.setVisible(false);
		tblParkDetails.setVisible(false);

		comboxPar.setVisible(true);
		txtValue.setVisible(true);
		btnSubmirPar.setVisible(true);

	}
	/**
     * Prepares the GUI for creating a Use Report, making necessary UI elements visible and setting up the report object.
     *
     * @param event ActionEvent that triggers the preparation for Use Report.
     */
	@FXML
	void MoveToUseReport(ActionEvent event) {
		comboxPar.setVisible(false);
		txtValue.setVisible(false);
		btnSubmirPar.setVisible(false);
		DateFrom.setVisible(true);
		DateTo.setVisible(true);
		btnCreate.setVisible(true);
		btnCreate.setDisable(false);
		txtReoport.setVisible(true);
		txtReoport.setText("Use Report");

		txtFail.setVisible(false);
		tblParkDetails.setVisible(false);
		report = new Report("", "", "Use", user.getParkName());
	}
	/**
     * Prepares the GUI for creating a Visitors Report, making necessary UI elements visible and setting up the report object.
     *
     * @param event ActionEvent that triggers the preparation for Visitors Report.
     */
	@FXML
	void MoveToVisitorsReport(ActionEvent event) {
		comboxPar.setVisible(false);
		txtValue.setVisible(false);
		btnSubmirPar.setVisible(false);
		DateFrom.setVisible(true);
		DateTo.setVisible(true);
		btnCreate.setVisible(true);
		btnCreate.setDisable(false);
		txtReoport.setVisible(true);
		txtReoport.setText("Visitors Report");

		txtFail.setVisible(false);
		tblParkDetails.setVisible(false);
		report = new Report("", "", "Visitors", user.getParkName());
	}
	/**
     * Logs the user out of the application and closes the application.
     *
     * @param event The action event that triggers this method.
     * @throws IOException If an I/O error occurs.
     */
	@FXML
	void Exit(ActionEvent event) throws IOException {
		logoutFunc(event);
		ClientUI.chat.closeConnection();
		ClientUI.chat.quit();
	}
	/**
     * Handles the logout action, terminating the session for the current user and navigating back to the login screen.
     *
     * @param event ActionEvent that triggers logout.
     */
	@FXML
	void Logout(ActionEvent event) {
		logoutFunc(event);
	}
	/**
    * Handles user logout, terminating the current session and navigating back to the login screen.
    *
    * @param event The event triggered by clicking the logout button.
    */
	void logoutFunc(ActionEvent event) {
		Request request = new Request();
		if (!user.getPermission().equals("")) {
			if (user.getPermission().equals("Single") || user.getPermission().equals("Group")) {
				request.setType(RequestType.TRAVELER_LOGOUT);

			} else {
				request.setType(RequestType.WORKER_LOGOUT);
			}

			request.setRequest(user);
			System.out.println(request.getRequest().toString());
			byte[] arr;
			try {
				arr = request.getBytes();
				System.out.println("Serialized bytes: " + arr.length);
				ClientUI.chat.handleMessageFromClientUI(arr);
				if (ClientUI.chat.logout == true) {
					((Node) event.getSource()).getScene().getWindow().hide();
					; // hiding primary window
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
     * Submits the new parameters for the park to the server for approval. Validates the input before sending the request.
     *
     * @param event ActionEvent that triggers parameter submission.
     */

	@FXML
	void SumitParameters(ActionEvent event) {
		parameters.setType(comboxPar.getValue().toString());
		parameters.setParkName(user.getParkName());
		parameters.setNewValue(txtValue.getText());
		boolean flag = false;
		if (parameters.getType().equals("Capacity") && checkCapacity(parameters.getNewValue())) {
			flag = true;
		} else if (parameters.getType().equals("Gap") && checkGap(parameters.getNewValue())) {
			flag = true;
		} else if (parameters.getType().equals("Duration Time") && checkDurationTime(parameters.getNewValue())) {
			flag = true;
		}
		if (flag) {
			Request request = new Request();
			request.setType(RequestType.UPDATE_PARAMETERS);
			request.setRequest(parameters);
			System.out.println(request.getRequest().toString());
			byte[] arr;
			try {
				arr = request.getBytes();
				System.out.println("Serialized bytes: " + arr.length);
				ClientUI.chat.handleMessageFromClientUI(arr);

			} catch (IOException e) {
				e.printStackTrace();
			}
			if (ClientUI.chat.SendToUpdate == true) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				// Set the title of the Alert
				alert.setTitle("Send To Update");
				// Set the header text of the Alert
				alert.setHeaderText(null);
				// Set the content text of the Alert
				alert.setContentText("Your Request To Update were Send");

				// Display the Alert
				alert.showAndWait();
			}
		}

	}
	/**
     * Displays park details in a table view, fetching the information from the server based on the current user's park.
     *
     * @param event ActionEvent that triggers the display of park details.
     */
	@FXML
	void ParkDetails(ActionEvent event) {
		tblParkDetails.setVisible(true);
		DateFrom.setVisible(false);
		DateTo.setVisible(false);
		btnCreate.setVisible(false);
		btnCreate.setDisable(false);
		comboxPar.setVisible(false);
		txtValue.setVisible(false);
		btnSubmirPar.setVisible(false);
		txtFail.setVisible(false);
		txtReoport.setVisible(false);
		Request request = new Request();

		request.setRequest(user.getUserName());
		request.setType(RequestType.PARK_DETAILS);
		System.out.println(request.getRequest().toString());
		byte[] arr;
		try {
			arr = request.getBytes();
			System.out.println("Serialized bytes: " + arr.length);
			ClientUI.chat.handleMessageFromClientUI(arr);
			tblParkDetails.setVisible(true);
			ArrayList<ParkDetails> parkDetails=new ArrayList<>();
			parkDetails.add(ClientUI.chat.parkDetails);
			ObservableList<ParkDetails> obsReportData = FXCollections.observableArrayList(parkDetails);

			clmParkName.setCellValueFactory(new PropertyValueFactory<>("parkName"));
			clmCapacity.setCellValueFactory(new PropertyValueFactory<>("maxOfVisitors"));
			clmGap.setCellValueFactory(new PropertyValueFactory<>("difference"));
			clmDurationTime.setCellValueFactory(new PropertyValueFactory<>("timeOfStay"));
			

			tblParkDetails.setItems(obsReportData);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 /**
     * Validates the entered capacity to ensure it's numerical.
     *
     * @param Capacity The entered capacity.
     * @return true if valid, false otherwise.
     */
	private boolean checkCapacity(String Capacity) {
		if (Capacity.matches("[0-9]+"))
			return true;
		Alert alert = new Alert(AlertType.ERROR);
		// Set the title of the Alert
		alert.setTitle("Error");
		// Set the header text of the Alert
		alert.setHeaderText(null);
		// Set the content text of the Alert
		alert.setContentText("Capacity must contains only numbers");

		// Display the Alert
		alert.showAndWait();
		return false;

	}
	 /**
     * Validates the entered gap between visits to ensure it's numerical.
     *
     * @param Gap The gap value to validate.
     * @return true if the gap is a valid number, false otherwise.
     */
	private boolean checkGap(String Gap) {
		if (Gap.matches("[0-9]+"))
			return true;
		Alert alert = new Alert(AlertType.ERROR);
		// Set the title of the Alert
		alert.setTitle("Error");
		// Set the header text of the Alert
		alert.setHeaderText(null);
		// Set the content text of the Alert
		alert.setContentText("Gap must contains only numbers");

		// Display the Alert
		alert.showAndWait();
		return false;

	}
	 /**
     * Validates the duration time format to ensure it matches the HH:MM format and is logical.
     *
     * @param DurationTime The duration time to validate.
     * @return true if the duration time is in the correct format and logical, false otherwise.
     */
	private boolean checkDurationTime(String DurationTime) {
		String timePattern = "^(0?[0-9]|1[0]):00$";
		Pattern pattern = Pattern.compile(timePattern);
		Matcher matcher = pattern.matcher(DurationTime);
		if (matcher.matches())
			return true;
		Alert alert = new Alert(AlertType.ERROR);
		// Set the title of the Alert
		alert.setTitle("Error");
		// Set the header text of the Alert
		alert.setHeaderText(null);
		// Set the content text of the Alert
		alert.setContentText("DurationTime must be in HH:MM format");

		// Display the Alert
		alert.showAndWait();
		return false;

	}
	  /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		comboxPar.getItems().addAll("Capacity", "Gap", "Duration Time");
	}
	/**
     * Starts the stage for the Park Manager GUI.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML file.
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ParkManager.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/ParkManager.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
