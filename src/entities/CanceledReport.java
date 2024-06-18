package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
/**
* Represents a report detailing canceled visits and unresolved orders for a specific park.
* This class encapsulates data that helps in analyzing reports related to cancellations.
*/
public class CanceledReport implements Serializable {
	private String ParkName;
	private HashMap<String,Integer> AmountOfUnresolvedOrders;
	private HashMap<String,Integer> AmountOfCanceledVisitInDate;
	public String getParkName() {
		return ParkName;
	}
	public void setParkName(String parkName) {
		ParkName = parkName;
	}
	 /**
     * A map holding the count of unresolved orders by their unique identifiers.
     */
	public HashMap<String,Integer> getAmountOfUnresolvedOrders() {
		return AmountOfUnresolvedOrders;
	}
	public void setAmountOfUnresolvedOrders(HashMap<String,Integer> amountOfUnresolvedOrders) {
		AmountOfUnresolvedOrders = amountOfUnresolvedOrders;
	}
	public HashMap<String, Integer> getAmountOfCanceledVisitInDate() {
		return AmountOfCanceledVisitInDate;
	}
	public void setAmountOfCanceledVisitInDate(HashMap<String, Integer> amountOfCanceledVisitInDate) {
		AmountOfCanceledVisitInDate = amountOfCanceledVisitInDate;
	}
	/**
     * Constructs a new CanceledReport with specified details.
     * 
     * @param parkName The name of the park.
     * @param amountOfUnresolvedOrders A map of unresolved orders with their counts.
     * @param amountOfCanceledVisitInDate A map of canceled visit dates with their counts.
     */
	public CanceledReport(String parkName, HashMap<String,Integer> amountOfUnresolvedOrders,
			HashMap<String, Integer> amountOfCanceledVisitInDate) {
		ParkName = parkName;
		AmountOfUnresolvedOrders = amountOfUnresolvedOrders;
		AmountOfCanceledVisitInDate = amountOfCanceledVisitInDate;
	}
    /**
     * Returns a string representation of the CanceledReport instance.
     * Includes the park name, unresolved orders, and canceled visits.
     * 
     * @return A string representation of the CanceledReport instance.
     */
	@Override
	public String toString() {
		return "CanceledReport [ParkName=" + ParkName + ", AmountOfUnresolvedOrders=" + AmountOfUnresolvedOrders
				+ ", AmountOfCanceledVisitInDate=" + AmountOfCanceledVisitInDate + "]";
	}
	
}
