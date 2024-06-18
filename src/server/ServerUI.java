package server;

import javafx.application.Application;
import javafx.stage.Stage;
import GNServer.SendNotification;
import GNServer.ServerGUIController;
import server.EchoServer;

/**
 * The {@code ServerUI} class is responsible for launching the server-side user interface.
 * It extends the {@link javafx.application.Application} class and provides the entry point
 * for the JavaFX application.
 */
public class ServerUI extends Application {
	  /** Default port for the server to listen on. */
	final public static int DEFAULT_PORT = 5555 ;


	/**
     * The main method of the {@code ServerUI} class.
     * It launches the JavaFX application.
     *
     * @param args Command-line arguments.
     * @throws Exception If an error occurs during execution.
     */
	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  } // end main
	
	/**
     * Starts the server-side user interface.
     * This method is called by the JavaFX runtime when the application is launched.
     *
     * @param primaryStage The primary stage for the application.
     * @throws Exception If an error occurs during execution.
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub				  		
		ServerGUIController aFrame = new ServerGUIController(); 
		 
		aFrame.start(primaryStage);
	}
	
	 /**
     * Runs the server with the specified port.
     *
     * @param p The port on which the server will listen for connections.
     */
	public static void runServer(String p)
	{
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(p); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);
		SendNotification notification = new SendNotification();
		notification.start();
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		} 
	}
	

}
