package khara.ticketservice.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * class to hold seats
 */
public class SeatHoldData {
	private int SeatHoldId;
	private double totalSeatPrice;
	private String customerEmail;
	private int requestedSeatsCount;
	private int nonHeldSeatCount;	
	private Map<Integer, List<String>> seatHeldMap = new HashMap<Integer, List<String>>();
	
	public int getSeatHoldId() {
		return SeatHoldId;
	}

	public void setSeatHoldId(int seatHoldId) {
		SeatHoldId = seatHoldId;
	}
	public int getNonHeldSeatCount() {
		return nonHeldSeatCount;
	}

	public void setNonHeldSeatCount(int nonHeldSeatCount) {
		this.nonHeldSeatCount = nonHeldSeatCount;
	}

	public double getTotalPrice() {
		return totalSeatPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalSeatPrice = totalPrice;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public int getRequestSeatCount() {
		return requestedSeatsCount;
	}

	public void setRequestSeatCount(int requestSeatCount) {
		this.requestedSeatsCount = requestSeatCount;
	}

	public Map<Integer, List<String>> getSeatHeldMap() {
		return seatHeldMap;
	}

	public void setSeatHeldMap(Map<Integer, List<String>> seatHeldMap) {
		this.seatHeldMap = seatHeldMap;
	}

}
