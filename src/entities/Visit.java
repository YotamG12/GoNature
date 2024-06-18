package entities;

import java.io.Serializable;
/**
 * Represents a visit with entrance time, reservation type, and duration time.
 */
public class Visit implements Serializable {
	
	private String entrancetime;
	private String reservationType;
	private Float durationTime;
	  /**
     * Constructs a Visit object with specified parameters.
     *
     * @param entrancetime    The entrance time of the visit.
     * @param reservationType The type of reservation (e.g., online, phone).
     * @param durationTime    The duration of the visit in hours.
     */
	public Visit(String entrancetime, String reservationType, Float durationTime) {
		this.entrancetime = entrancetime;
		this.reservationType = reservationType;
		this.durationTime = durationTime;
	}

	public String getEntrancetime() {
		return entrancetime;
	}

	public void setEntrancetime(String entrancetime) {
		this.entrancetime = entrancetime;
	}

	public String getReservationType() {
		return reservationType;
	}

	public void setReservationType(String reservationType) {
		this.reservationType = reservationType;
	}

	public Float getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(Float durationTime) {
		this.durationTime = durationTime;
	}

	@Override
	public String toString() {
		return "" +  entrancetime + "," + reservationType + "," + durationTime;
	}
	
}
