package khara.ticketservice.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for holding details related to customer and seats
 */
public class DataStore {
	private  Map<String, Map<Integer, SeatHoldData>> userSeatHoldMap = new HashMap<String, Map<Integer, SeatHoldData>>();
	private  Map<Integer, SeatLevelData> seatingInfoMap ;
	private  Map<Integer, Integer> reserveSeatMap = new HashMap<Integer, Integer>();
	private  int previousSeatHoldId;
	private  int previousReservationCode;
	
	
	// getter setter for userSeatHoldMap
	public Map<String, Map<Integer, SeatHoldData>> getUserHoldMap() {
		return userSeatHoldMap;
	}
	public void setUserHoldMap(Map<String, Map<Integer, SeatHoldData>> userHoldMap) {
		this.userSeatHoldMap = userHoldMap;
	}
	
	// getter setter for getSeatingInfoMap
	public Map<Integer, SeatLevelData> getSeatingInfoMap() {
		return seatingInfoMap;
	}
	public void setSeatingInfoMap(Map<Integer, SeatLevelData> seatingInfoMap) {
		this.seatingInfoMap = seatingInfoMap;
	}
	
	// getter setter for reserveSeatMap
	public Map<Integer, Integer> getReservationMap() {
		return reserveSeatMap;
	}
	public void setReservationMap(Map<Integer, Integer> reservationMap) {
		this.reserveSeatMap = reservationMap;
	}
	
	// getter setter for previousSeatHoldId
	public int getPreviousSeatHoldId() {
		return previousSeatHoldId;
	}
	public void setPreviousSeatHoldId(int previousSeatHoldId) {
		this.previousSeatHoldId = previousSeatHoldId;
	}
	
	// getter setter for previousReservationCode
	public int getPreviousReservationCode() {
		return previousReservationCode;
	}
	public void setPreviousReservationCode(int previousReservationCode) {
		this.previousReservationCode = previousReservationCode;
	}
}
