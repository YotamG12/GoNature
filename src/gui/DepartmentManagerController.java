package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import GNClient.ClientUI;
import common.RequestType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;

import entities.Parameters;
import entities.ParkDetails;
import entities.Report;
import entities.ReportData;
import entities.Request;
import entities.User;
/**
 * The {@code DepartmentManagerController} class controls the GUI for the Department Manager's interface.
 * It facilitates the interaction with various functionalities such as viewing and approving parameter updates,
 * creating and viewing reports, logging out, and managing park details.
 */
public class DepartmentManagerController implements Initializable {

	@FXML
	private DatePicker DateTo;

	@FXML
	private DatePicker DateFrom;

	@FXML
	private TableColumn<String, String> ckmParkName;

	@FXML
	private TableColumn<String, String> clmParameter;

	@FXML
	private TableColumn<String, String> clmValue;

	@FXML
	private TableColumn<String, String> clmStatus;

	@FXML
	private TableView<Parameters> tbiParametersToApprove;

	@FXML
	private TableColumn<ParkDetails, String> clmCapacity;

	@FXML
	private TableColumn<ParkDetails, String> clmDurationTime;

	@FXML
	private TableColumn<ParkDetails, String> clmGap;

	@FXML
	private TableColumn<ParkDetails, String> clmParkName;

	@FXML
	private TableColumn<ParkDetails, String> clmParkManager;

	@FXML
	private TableView<ParkDetails> tblParkDetails;

	@FXML
	private Button btnAUP;

	@FXML
	private Button btnCreateReports;

	@FXML
	private Button btnCreateandView;

	@FXML
	private Button btnExit;

	@FXML
	private Button btnLogout;

	@FXML
	private Button btnView;

	@FXML
	private Button btnViewReports;

	@FXML
	private Button btnApprove;

	@FXML
	private Button btnDecline;

	@FXML
	private ComboBox<String> comboChooseReport;

	@FXML
	private ComboBox<String> comboChoosePark;

	@FXML
	private Label lblTitle;

	public static User user;
	public static Report report;
	public static Parameters parameteras = new Parameters("", "", "", "");
	/**
     * Exits the application.
     *
     * @param event ActionEvent that triggers the method.
     * @throws IOException If an error occurs during the process.
     */
	@FXML
	void Exit(ActionEvent event) throws IOException {
		logoutFunc(event);
		ClientUI.chat.closeConnection();
		ClientUI.chat.quit();
	}
	/**
     * Displays the interface for updating application parameters.
     *
     * @param event ActionEvent that triggers the method.
     */
	@FXML
	void AppParamUpdate(ActionEvent event) {
		comboChooseReport.getItems().clear();
		comboChoosePark.getItems().clear();

		tblParkDetails.setVisible(false);
		comboChooseReport.setVisible(false);
		DateTo.setVisible(false);
		DateFrom.setVisible(false);
		comboChoosePark.setVisible(false);
		comboChooseReport.setVisible(false);
		btnView.setVisible(false);
		btnCreateandView.setVisible(false);

		tbiParametersToApprove.setVisible(true);
		btnApprove.setVisible(true);
		btnDecline.setVisible(true);
		Request request = new Request();
		request.setType(RequestType.APPROVED_PARAMETERS);

		request.setRequest(user.getUserName());
		System.out.println(request.getRequest().toString());
		byte[] arr;
		try {
			arr = request.getBytes();
			System.out.println("Serialized bytes: " + arr.length);
			ClientUI.chat.handleMessageFromClientUI(arr);

			tbiParametersToApprove.setVisible(true);
			ObservableList<Parameters> obsReportData = FXCollections
					.observableArrayList(ClientUI.chat.parametersToApprove);

			ckmParkName.setCellValueFactory(new PropertyValueFactory<>("ParkName"));
			clmParameter.setCellValueFactory(new PropertyValueFactory<>("Type"));
			clmValue.setCellValueFactory(new PropertyValueFactory<>("NewValue"));
			clmStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

			tbiParametersToApprove.setItems(obsReportData);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	 /**
     * Prepares the interface for creating reports.
     *
     * @param event ActionEvent that triggers the method.
     * @throws IOException If an error occurs during the process.
     */
	@FXML
	void CreateReports(ActionEvent event) throws IOException {
		tblParkDetails.setVisible(false);
		comboChooseReport.getItems().clear();
		comboChoosePark.getItems().clear();
		tbiParametersToApprove.setVisible(false);
		btnApprove.setVisible(false);
		btnDecline.setVisible(false);
		comboChooseReport.getItems().addAll("Visits Report", "Cancel Report");
		// send to server request to get the relevant parks for department manager

		Request request = new Request();
		request.setType(RequestType.RELEVANT_PARKS);

		request.setRequest(user);
		System.out.println(request.getRequest().toString());
		byte[] arr;
		try {
			arr = request.getBytes();
			System.out.println("Serialized bytes: " + arr.length);
			ClientUI.chat.handleMessageFromClientUI(arr);

		} catch (IOException e) {
			e.printStackTrace();
		}

		comboChoosePark.getItems().addAll(ClientUI.chat.relevantParks);
		DateTo.setVisible(true);
		DateFrom.setVisible(true);
		comboChoosePark.setVisible(true);
		comboChooseReport.setVisible(true);
		btnView.setVisible(false);
		btnCreateandView.setVisible(true);
	}
	 /**
     * Creates and views the selected report.
     *
     * @param event ActionEvent that triggers the method.
     * @throws IOException If an error occurs during the process.
     */
	@FXML
	void CreateandView(ActionEvent event) throws IOException {
		report = new Report(DateFrom.getValue().toString(), DateTo.getValue().toString(), "",
				comboChoosePark.getValue());
		if (comboChooseReport.getValue().equals("Visits Report"))
			report.setType("Visits Report");
		else
			report.setType("Cancel Report");
		Request request = new Request();
		if (report.getType().equals("Visits Report")) {
			request.setType(RequestType.VISITS_REPORT);

		} else if (comboChoosePark.getValue().equals("All")) {
			request.setType(RequestType.CANCELATION_REPORT_ALL);
			String str = "";
			for (int i = 0; i < ClientUI.chat.relevantParks.size(); i++) {
				str += ClientUI.chat.relevantParks.get(i) + ",";
			}
			str = str.substring(0, str.length() - 1);
			report.setParkName(str);
		} else {
			request.setType(RequestType.CANCELATION_REPORT);
		}
		request.setRequest(report);
		System.out.println(request.getRequest().toString());
		byte[] arr;
		try {
			arr = request.getBytes();
			System.out.println("Serialized bytes: " + arr.length);
			ClientUI.chat.handleMessageFromClientUI(arr);
			if (report.getType().equals("Visits Report")) {
				// move to view visitors report
				ViewVisitsRepoetController.user = user;
				((Node) event.getSource()).getScene().getWindow().hide();
				; // hiding primary window
				Stage primaryStage = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/fxml/ViewVisitsRepoet.fxml"));

				Scene scene = new Scene(root);
				// scene1.getStylesheets().add(getClass().getResource("/fxml/ViewVisitsReport.css").toExternalForm());
				primaryStage.setTitle("");
				primaryStage.initStyle(StageStyle.UNDECORATED);
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.show();
			} else {
				// TODO set the user in the next controller
				ViewCancelationReportController.user = user;
				((Node) event.getSource()).getScene().getWindow().hide();
				; // hiding primary window
				Stage primaryStage = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/fxml/viewCancelationReport.fxml"));

				Scene scene = new Scene(root);
				// scene1.getStylesheets().add(getClass().getResource("/fxml/viewCancelationReport.css").toExternalForm());
				primaryStage.setTitle("");
				primaryStage.initStyle(StageStyle.UNDECORATED);
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.show();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 /**
     * Views the chosen report.
     *
     * @param event ActionEvent that triggers the method.
     */
	@FXML
	void ViewChosenReport(ActionEvent event) {
		// send to server and move to the view report page
		if (checkFullMonth(DateFrom.getValue(), DateTo.getValue())) {
			report = new Report(DateFrom.getValue().toString(), DateTo.getValue().toString(), "",
					comboChoosePark.getValue());
			if (comboChooseReport.getValue().equals("Visitors Report"))
				report.setType("Visitors");
			else
				report.setType("Use");
			Request request = new Request();
			if (report.getType().equals("Visitors")) {
				request.setType(RequestType.VIEW_TOTAL_VISITORS_NUM_REPORT);

			} else {
				request.setType(RequestType.VIEW_USE_REPORT);
			}
			request.setRequest(report);
			System.out.println(request.getRequest().toString());
			byte[] arr;
			try {
				arr = request.getBytes();
				System.out.println("Serialized bytes: " + arr.length);
				ClientUI.chat.handleMessageFromClientUI(arr);
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
						// move to view visitors report
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
     * Displays the interface for viewing reports.
     *
     * @param event ActionEvent that triggers the method.
     */
	@FXML
	void ViewReports(ActionEvent event) {

		tblParkDetails.setVisible(false);
		comboChooseReport.getItems().clear();
		comboChoosePark.getItems().clear();
		tbiParametersToApprove.setVisible(false);
		btnApprove.setVisible(false);
		btnDecline.setVisible(false);
		btnCreateandView.setVisible(false);
		comboChooseReport.getItems().addAll("Visitors Report", "Use Report");
		// send to server request to get the relevant parks for department manager

		Request request = new Request();
		request.setType(RequestType.RELEVANT_PARKS);

		request.setRequest(user);
		System.out.println(request.getRequest().toString());
		byte[] arr;
		try {
			arr = request.getBytes();
			System.out.println("Serialized bytes: " + arr.length);
			ClientUI.chat.handleMessageFromClientUI(arr);

		} catch (IOException e) {
			e.printStackTrace();
		}

		comboChoosePark.getItems().addAll(ClientUI.chat.relevantParks);
		comboChoosePark.setVisible(true);
		DateTo.setVisible(true);
		DateFrom.setVisible(true);
		comboChooseReport.setVisible(true);
		btnView.setVisible(true);

	}
	 /**
     * Validates if the selected date range covers a full month.
     *
     * @param date1 Start date.
     * @param date2 End date.
     * @return true if the date range covers a full month, false otherwise.
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
     * Approves the selected parameters update.
     *
     * @param event ActionEvent that triggers the method.
     */
	@FXML
	void DeclineParameters(ActionEvent event) {
		Parameters row = tbiParametersToApprove.getSelectionModel().getSelectedItem();
		System.out.println(row.toString());
		parameteras.setParkName(row.getParkName());
		parameteras.setNewValue(row.getNewValue());
		parameteras.setStatus("Decline");
		parameteras.setType(row.getType());
		Request request = new Request();
		request.setType(RequestType.APPROVED_DECLINED);

		request.setRequest(parameteras);
		System.out.println(request.getRequest().toString());
		byte[] arr;
		try {
			arr = request.getBytes();
			System.out.println("Serialized bytes: " + arr.length);
			ClientUI.chat.handleMessageFromClientUI(arr);

		} catch (IOException e) {
			e.printStackTrace();
		}

		tbiParametersToApprove.getItems().remove(row);

	}
	/**
     * Approves the selected parameters update.
     *
     * @param event ActionEvent that triggers the method.
     */
	@FXML
	void ApproveParameters(ActionEvent event) {
		Parameters row = tbiParametersToApprove.getSelectionModel().getSelectedItem();
		System.out.println(row.toString());
		parameteras.setParkName(row.getParkName());
		parameteras.setNewValue(row.getNewValue());
		parameteras.setStatus("Approve");
		parameteras.setType(row.getType());
		Request request = new Request();
		request.setType(RequestType.APPROVED_DECLINED);

		request.setRequest(parameteras);
		System.out.println(request.getRequest().toString());
		byte[] arr;
		try {
			arr = request.getBytes();
			System.out.println("Serialized bytes: " + arr.length);
			ClientUI.chat.handleMessageFromClientUI(arr);

		} catch (IOException e) {
			e.printStackTrace();
		}

		tbiParametersToApprove.getItems().remove(row);
	}
	/**
     * Logs out the user and returns to the welcome page.
     *
     * @param event ActionEvent that triggers the method.
     */
	@FXML
	void Logout(ActionEvent event) {
		logoutFunc(event);
	}
	/**
     * Displays the park details interface.
     *
     * @param event ActionEvent that triggers the method.
     */
	@FXML
	void ParkDetails(ActionEvent event) {
		tblParkDetails.setVisible(false);
		btnApprove.setVisible(false);
		btnCreateandView.setVisible(false);
		btnDecline.setVisible(false);
		btnView.setVisible(false);
		DateTo.setVisible(false);
		DateFrom.setVisible(false);
		comboChoosePark.setVisible(false);
		comboChooseReport.setVisible(false);
		
		Request request = new Request();

		request.setRequest(user.getUserName());
		request.setType(RequestType.PARK_DETAILS_DM);
		System.out.println(request.getRequest().toString());
		byte[] arr;
		try {
			arr = request.getBytes();
			System.out.println("Serialized bytes: " + arr.length);
			ClientUI.chat.handleMessageFromClientUI(arr);
			tblParkDetails.setVisible(true);
			ObservableList<ParkDetails> obsReportData = FXCollections.observableArrayList(ClientUI.chat.parkDetailsDm);

			clmParkName.setCellValueFactory(new PropertyValueFactory<>("parkName"));
			clmCapacity.setCellValueFactory(new PropertyValueFactory<>("maxOfVisitors"));
			clmGap.setCellValueFactory(new PropertyValueFactory<>("difference"));
			clmDurationTime.setCellValueFactory(new PropertyValueFactory<>("timeOfStay"));
			clmParkManager.setCellValueFactory(new PropertyValueFactory<>("ParkManagerName"));

			tblParkDetails.setItems(obsReportData);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 /**
     * Handles the logout functionality and navigates to the welcome page.
     *
     * @param event ActionEvent that triggers the method.
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
	}  /**
     * Initializes the DepartmentManagerController class. This method is called after the FXML file has been loaded.
    *
    * @param location The location used to resolve relative paths for the root object.
    * @param resources The resources used to localize the root object.
    */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tblParkDetails.setVisible(false);
		comboChooseReport.valueProperty().addListener((observable, oldValue, newValue) -> {

			if ("Cancel Report".equals(newValue))
				comboChoosePark.getItems().add("All");

		});

	}
	/**
     * Starts the Department Manager's GUI.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML file.
     */

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/DepartmentManager.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/fxml/DepartmentManagar.css").toExternalForm());
		primaryStage.setTitle("");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
