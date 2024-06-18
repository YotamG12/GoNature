package entities;

import java.io.Serializable;
/**
 * Represents a reservation within the park management system.
 * This class stores information about a reservation, including details
 * about the user, visit date and time, and reservation type.
 */
public class Reservation implements Serializable{
	private Integer ReservationID;
	private String userID;
	private String parkName;
	private String numOfVisitors;
	private String date;
	private String time;
	private String phoneNum;
	private String email;
	private boolean prepayment;
	private String type; //"Single"/ "Group"/ "Single_occ"/"Group_occ"
	public  float entryTicket = 100;
	/**
     * Constructs a Reservation instance with specified details.
     *
     * @param reservationID Unique identifier for the reservation.
     * @param userID The ID of the user making the reservation.
     * @param parkName The name of the park for the reservation.
     * @param numOfVisitors Number of visitors included in the reservation.
     * @param date The date of the reservation.
     * @param time The time of the reservation.
     * @param phoneNum Contact phone number for the reservation.
     * @param email Contact email for the reservation.
     * @param prepayment Indicates if the reservation has been prepaid.
     * @param type The type of reservation.
     */
	public Reservation(int reservationID, String userID, String parkName, String numOfVisitors, String date,
			String time, String phoneNum, String email, boolean prepayment, String type) {

		ReservationID = reservationID;
		this.userID = userID;
		this.parkName = parkName;
		this.numOfVisitors = numOfVisitors;
		this.date = date;
		this.time = time;
		this.phoneNum = phoneNum;
		this.email = email;
		this.prepayment=prepayment;
		this.type=type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getReservationID() {
		return ReservationID;
	}
	public void setReservationID(Integer reservationID) {
		ReservationID = reservationID;
	}
	public boolean isPrepayment() {
		return prepayment;
	}
	public void setPrepayment(boolean prepayment) {
		this.prepayment = prepayment;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public String getNumOfVisitors() {
		return numOfVisitors;
	}
	public void setNumOfVisitors(String numOfVisitors) {
		this.numOfVisitors = numOfVisitors;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return ""+parkName.toLowerCase()+","+ReservationID+","+userID+","+numOfVisitors+","+date+","+time+","+phoneNum+","+email+","+prepayment+","+type;
	}
	  /**
     * Calculates the price for a single preorder reservation.
     * Applies a discount to the base ticket price.
     *
     * @return The calculated price as a String.
     */
	public  String calculatePriceSinglePreorder() {
		Double price = (Float.parseFloat(numOfVisitors) * entryTicket) * 0.85;
		return price.toString();	
	}
	 /**
     * Calculates the price for a single reservation without preorder.
     *
     * @return The full price without any discount as a String.
     */
	public  String calculatePriceSingle() {
		Float price = Float.parseFloat(numOfVisitors) * entryTicket;
		return price.toString();	
	}
	 /**
     * Calculates the price for a group preorder reservation.
     * Applies a discount to the base ticket price and excludes the group guide from the total.
     *
     * @return The calculated price as a String.
     */
	public  String calculatePriceGroupPreorder() {
		Double price = ((Float.parseFloat(numOfVisitors) - 1) * entryTicket) * 0.75; // -1 because the group guide not pay
		return price.toString();	
	}
	 /**
     * Applies an additional discount to a given price.
     *
     * @param price The original price before applying the discount.
     * @return The price after applying the additional discount as a String.
     */
	public  String calculateaAnotherDiscount(String price) {
		Double finalPrice = Float.parseFloat(price) * 0.88;
		return finalPrice.toString();
	}
	 /**
     * Calculates the price for a group reservation without preorder.
     * Applies a discount to the base ticket price.
     *
     * @return The calculated price as a String.
     */
	public  String calculatePriceGroup() {
		Double price = ((Float.parseFloat(numOfVisitors)) * entryTicket) * 0.9; 
		return price.toString();	
	}
	
}
