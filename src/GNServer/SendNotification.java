package GNServer;

import java.util.Timer;
import java.util.TimerTask;
import java.net.PasswordAuthentication;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Properties;

//import org.jibble.pircbot.*;
//import javax.mail.*;
//import javax.mail.internet.*;
import javax.swing.JOptionPane;

import com.mysql.cj.Session;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
import entities.Reservation;

import GNServer.DataBaseController;

/**
 * SendNotification class implements a timer task to send notifications to users
 * scheduled to visit the parks within the next two hours.
 */
public class SendNotification extends TimerTask {

	DataBaseController db = new DataBaseController();

	// Email parameters
	private static final String EMAIL_USERNAME = "gonaturegroup11@gmail.com";
	private static final String EMAIL_PASSWORD = "Gonature11$";
	
	/**
     * The run method is called by the timer to send notifications.
     */
	@Override
	public void run() {
		try {
			// Fetch orders from the database
			ArrayList<Reservation> ordersDisneyland = db.fetchOrders("disneyland");
			ArrayList<Reservation> ordersAfek = db.fetchOrders("afek");
			ArrayList<Reservation> ordersUniversal = db.fetchOrders("universal");
			ArrayList<Reservation> mergedOrders = new ArrayList<>();
			mergedOrders.addAll(ordersDisneyland);
			mergedOrders.addAll(ordersAfek);
			mergedOrders.addAll(ordersUniversal);
			// Iterate through orders
			for (Reservation order : mergedOrders) {
				// Check if the order is scheduled in 2 hours
				// Compare current time with scheduled time
				boolean isScheduledInTwoHours = false;
				LocalDate today = LocalDate.now();
				LocalDate date = LocalDate.parse(order.getDate());
				LocalTime now = LocalTime.now();
				String timeString = order.getTime();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				LocalTime time = LocalTime.parse(timeString, formatter);
				long diff = ChronoUnit.HOURS.between(time, now);
				if (diff <= 2 && date.equals(today)) // If the difference is less than or equal to 2 hours and positive,
														// return true
					isScheduledInTwoHours = true;

				if (isScheduledInTwoHours) {

					String message = "Greetings, traveler!\nThis is a friendly reminder that you have scheduled a visit to one of our parks (" + order.getParkName() + ") for today: (" + order.getDate() + ") in " + order.getTime() + ".\n Please take a moment to confirm or cancel your booking via our system within the next two hours.\n If we don't hear from you within this timeframe, we regret to inform you that your reservation will automatically be canceled.\n We've sent this notice to your phone number: "
							+ order.getPhoneNum() + " and to your email address: " + order.getEmail() + ".";
					JOptionPane.showMessageDialog(null, message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * Starts the timer to send notifications periodically.
     */
	public void start() {
		/// We add for send notification
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new SendNotification(), 0, 3600000); // 3600000 milliseconds = 1 hour
	}
}
