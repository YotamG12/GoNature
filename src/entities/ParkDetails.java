package entities;

import java.io.Serializable;
/**
 * Represents the details of a park, including the name of the park manager, park name,
 * time of stay, difference, and maximum number of visitors allowed.
 */
public class ParkDetails implements Serializable {

	private String parkManagerName;
	private String parkName;
	private String timeOfStay;
	private String difference;
	private String maxOfVisitors;
	/**
     * Constructs a new ParkDetails instance.
     *
     * @param parkManagerName The name of the park manager.
     * @param parkName The name of the park.
     * @param timeOfStay The allowed time of stay in the park.
     * @param difference The difference parameter, context dependent.
     * @param maxOfVisitors The maximum number of visitors allowed.
     */
	public ParkDetails(String parkManagerName, String parkName, String timeOfStay, String difference,
			String maxOfVisitors) {
		this.parkManagerName = parkManagerName;
		this.parkName = parkName;
		this.timeOfStay = timeOfStay;
		this.difference = difference;
		this.maxOfVisitors = maxOfVisitors;
	}

	public String getParkManagerName() {
		return parkManagerName;
	}

	public void setParkManagerName(String parkManagerName) {
		this.parkManagerName = parkManagerName;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public String getTimeOfStay() {
		return timeOfStay;
	}

	public void setTimeOfStay(String timeOfStay) {
		this.timeOfStay = timeOfStay;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	public String getMaxOfVisitors() {
		return maxOfVisitors;
	}

	public void setMaxOfVisitors(String maxOfVisitors) {
		this.maxOfVisitors = maxOfVisitors;
	}
	 /**
     * Returns a string representation of the ParkDetails instance.
     */
	@Override
	public String toString() {
		return "ParkDetails [parkManagerName=" + parkManagerName + ", parkName=" + parkName + ", timeOfStay="
				+ timeOfStay + ", difference=" + difference + ", maxOfVisitors=" + maxOfVisitors + "]";
	}
	
}
