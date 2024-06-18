package entities;

import java.io.Serializable;
/**
 * Represents a specific data point for reporting purposes, including both a date and an hour.
 */
public class ReportData implements Serializable {
	 private String date;
	    private String hour;

	    public ReportData(String date, String hour) {
	        this.date = date;
	        this.hour = hour;
	    }

	    public String getDate() {
	        return date;
	    }

	    public String getHour() {
	        return hour;
	    }

}
