package entities;

import java.io.Serializable;

/**
 * Represents the parameters for configuring various settings of a park.
 * This class is used to hold the details of configuration changes, 
 * such as capacity adjustments, gap settings, and duration time modifications.
 */
public class Parameters implements Serializable {
	private String Type;// Capacity, Gap, Duration Time
	private String Status;
	private String NewValue;
	private String ParkName;
	 /**
     * Constructs a new Parameters object with specified details.
     * 
     * @param type The type of parameter being modified.
     * @param status The current status of the modification request.
     * @param newValue The new value for the parameter.
     * @param parkName The name of the park to which the parameter modification applies.
     */
	public Parameters(String type, String status, String newValue, String parkName) {
		
		Type = type;
		Status = status;
		NewValue = newValue;
		ParkName = parkName;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getNewValue() {
		return NewValue;
	}
	public void setNewValue(String newValue) {
		NewValue = newValue;
	}
	public String getParkName() {
		return ParkName;
	}
	public void setParkName(String parkName) {
		ParkName = parkName;
	}

    /**
     * Returns a string representation of this Parameters object.
     * The string includes the type, status, new value, and park name.
     * 
     * @return A string representation of this object.
     */
	@Override
	public String toString() {
		return "Parameters [Type=" + Type + ", Status=" + Status + ", NewValue=" + NewValue + ", ParkName=" + ParkName
				+ "]";
	}
	
}


