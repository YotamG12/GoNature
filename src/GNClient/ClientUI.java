package GNClient;



import gui.ServerIpController;
import javafx.application.Application;

import javafx.stage.Stage;

public class ClientUI extends Application {
	public static ChatClient chat; // only one instance
	public static String host;
	public static int port;

	public static void main(String args[]) throws Exception {

		launch(args);
	} // end main

	@Override
	public void start(Stage primaryStage) throws Exception {
		//chat = new ChatClient(host, port, this);
		ServerIpController aFrame = new ServerIpController();
		

		aFrame.start(primaryStage);

	}

}
