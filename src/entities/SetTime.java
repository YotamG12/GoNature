package entities;

import java.io.Serializable;
/**
 * Represents the setting of either the entrance or leaving time for a park reservation.
 * This class holds the information to update the time associated with a reservation.
 */
public class SetTime implements Serializable{
	private Integer ReservationID;
	private String Time;
	private String type; //Entrance Time or Leaving Time
	private String parkName;
	 /**
     * Constructs a new instance of SetTime with specified details.
     *
     * @param reservationID The unique identifier for the reservation.
     * @param time The time to be set for the reservation.
     * @param type The type of time being set (e.g., "Entrance Time" or "Leaving Time").
     * @param parkName The name of the park associated with the reservation.
     */
	public SetTime(Integer reservationID, String time, String type, String parkName) {
		super();
		ReservationID = reservationID;
		Time = time;
		this.type = type;
		this.parkName=parkName;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
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
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	
}
