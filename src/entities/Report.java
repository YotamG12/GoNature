package entities;

import java.io.Serializable;
/**
 * Represents a general report within a park management system. 
 * This class encapsulates data for a specific report, including its type, 
 * the date range it covers, and the park it is associated with.
 */
public class Report implements Serializable {
	private String dateFrom;
	private String dateTo;
	private String parkName;
	private String type;//"Visitors"/"Use"/...
	/**
     * Constructs a new Report with specified details.
     * 
     * @param dateFrom The start date of the report period.
     * @param dateTo The end date of the report period.
     * @param type The type of report.
     * @param parkName The name of the park the report is about.
     */
	public Report(String dateFrom, String dateTo, String type, String parkName) {
		
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.type = type;
		this.parkName=parkName;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
    * Provides a string representation of this report, including its date range,
    * type, and associated park name.
    * 
    * @return A string representation of the report.
    */
	@Override
	public String toString() {
		return dateFrom + ","+ dateTo+"," + type+","+parkName;
	}
	

}
