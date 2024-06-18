package GNServer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import entities.ConnectedClient;
import GNServer.DataBaseController;
import server.EchoServer;
import server.ServerUI;



import java.util.List;

/**
 * The ServerGUIController class controls the GUI and handles events for the server application.
 */
public class ServerGUIController {

	String temp = "";

	@FXML
	private Button btnDone = null;
	@FXML
	private Label lbllist;

	@FXML
	private TextField portxt;
	ObservableList<String> list;

	@FXML
	private Button btnServer;

	@FXML
	private Button btnImportData;

	@FXML
	private Button btnImportUserData1;

	@FXML
	private Button btnExit;

	@FXML
	private TableColumn<ConnectedClient, String> clmCs;

	@FXML
	private TableColumn<ConnectedClient, String> clmHN;

	@FXML
	private TableColumn<ConnectedClient, String> clmIP;

	@FXML
	private TableView<ConnectedClient> tblClients;

	@FXML
	private TextField txtHostName;

	@FXML
	private TextField txtPassword;

	@FXML
	private TextField txtUserName;

	@FXML
	private TextField txtSechmaName;

	@FXML
	private Label labserver;

	public static String userName, password, hostName, sechma;

	/**
	 * Updates and displays the status of connected clients.
	 * Checks each client's connection status, marking any inactive clients as 'disconnected'. Refreshes the table to show the latest status.
	 * @param event Triggered by clicking the server status button.
	 */
	@FXML
	void handleServerButtonClick(ActionEvent event) {

		// going over all list, if client is not alive change status to disconnected
		for (ConnectedClient connectedClient : EchoServer.clientConnections) {
			if (!connectedClient.getClient().isAlive())
				connectedClient.setStatus("disconnected");
		}
		tblClients.getItems().clear();
		setTableOrder(EchoServer.clientConnections);

	}

	/**
	 * Fills the client table with data.
	 * 
	 * Converts a list of client connections to an observable list to be displayed in the GUI table, showing each client's host, IP, and status.
	 * 
	 * @param Connectionstatus List of clients to display.
	 */
	private void setTableOrder(List<ConnectedClient> Connectionstatus) {
		ObservableList<ConnectedClient> data = FXCollections.observableArrayList(Connectionstatus);
		clmHN.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("Host"));
		clmIP.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("IP"));
		clmCs.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("status"));
		tblClients.setItems(data);
	}

	/**
	 * Retrieves the port number entered in the text field.
	 * 
	 * @return The port number as a string.
	 */
	private String getport() {
		return portxt.getText();
	}

	/**
	 * Starts the server with the provided configuration.
	 *
	 * Retrieves server settings and initiates the server startup process.
	 * Displays an error message if any field is left empty.
	 *
	 * @param event The action event triggering this method.
	 * @throws Exception If an error occurs during server startup.
	 */
	public void Done(ActionEvent event) throws Exception {
		String p;

		p = getport();
		userName = txtUserName.getText();
		password = txtPassword.getText();
		hostName = txtHostName.getText();
		sechma = txtSechmaName.getText();
		if (p.trim().isEmpty() || password.equals("") || hostName.equals("") || userName.equals("")
				|| sechma.equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			// Set the title of the Alert
			alert.setTitle("Error");
			// Set the header text of the Alert
			alert.setHeaderText("An error occurred");
			// Set the content text of the Alert
			alert.setContentText("\t\t\tYou must fill in all the fields.\n\t\t\tThank you,\n\t\t\tGo Nature System");

			// Display the Alert
			alert.showAndWait();

		} else {
			/*
			 * ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary
			 * window Stage primaryStage = new Stage(); FXMLLoader loader = new
			 * FXMLLoader();
			 */
			ServerUI.runServer(p);
		}
	}

	/**
	 * Handles the action event triggered when the "Import Data" button is clicked.
	 * Initiates the process of importing workers' data.
	 *
	 * @param event The action event that triggered this method.
	 */
	@FXML
	void importData(ActionEvent event) {
		DataBaseController.ImportWorkersData();
	}

	/**
	 * Handles the action event triggered when the "Import User Data" button is clicked.
	 * Initiates the process of importing users' data.
	 *
	 * @param event The action event that triggered this method.
	 */
	@FXML
	void importUserData(ActionEvent event) {
		DataBaseController.ImportUsersData();
	}

	/**
	 * Handles the action event triggered when the "Exit" button is clicked.
	 * Exits the GoNature Server application.
	 *
	 * @param event The action event that triggered this method.
	 */
	@FXML
	void exit(ActionEvent event) {
		System.out.println("Exit GoNature Server app.");
		System.exit(0);
	}

	/**
	 * Starts the server user interface by loading the ServerGUI.fxml file.
	 *
	 * @param primaryStage The primary stage of the JavaFX application.
	 * @throws Exception If an error occurs during the loading of the FXML file.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/GNServer/ServerGUI.fxml")); // TODO package

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/GNServer/ServerGUI.css").toExternalForm());// TODO package
		primaryStage.setTitle("ServerUI");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}