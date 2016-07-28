package khara.ticketservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;

import khara.ticketservice.service.TicketService;
import khara.ticketservice.utils.HeldSeatExpiration;
import khara.ticketservice.utils.TicketServiceConstants;
import khara.ticketservice.IO.MyReader;
//import khara.ticketservice.exception.TicketServiceException;
import khara.ticketservice.model.SeatHoldData;
import khara.ticketservice.model.SeatLevelData;
import khara.ticketservice.model.TicketServiceAccessData;

/*
 *  This class is implementation class of TicketService interface
 */

public class TicketServiceImplement implements TicketService {

	private TicketServiceAccessData ticketServiceAccessData;

	public TicketServiceImplement(TicketServiceAccessData ticketServiceAccessData) {		// constructor 
		this.ticketServiceAccessData = ticketServiceAccessData;
	}
	
	/*******************************************
	*  find number of available seats
	*******************************************/	
	//@Override 
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		int seatsAvailable = 0;
		if (venueLevel.isPresent()) {
			Map<Integer, SeatLevelData> seatingInfoMap = ticketServiceAccessData.getSeatingInfoMap();
			if (seatingInfoMap != null && !seatingInfoMap.isEmpty()) {
				SeatLevelData seatLevelInfo = seatingInfoMap.get(venueLevel.get());
				if (seatLevelInfo != null) {
					seatsAvailable = (seatLevelInfo.getTotalSeatsInCurrentLevel() - seatLevelInfo.getHeldSeats());
				}
			}
		} else {
			seatsAvailable = countTotalSeatsInAllLevels();
		}
		return seatsAvailable;
	} // end of numSeatsAvailable method
	private int countTotalSeatsInAllLevels() {
		int totalAvailableSeats = 0;
		Map<Integer, SeatLevelData> seatingInfoMap = ticketServiceAccessData.getSeatingInfoMap();
		if (seatingInfoMap != null && !seatingInfoMap.isEmpty()) {
			for (Integer integerKey : seatingInfoMap.keySet()) {
				SeatLevelData seatLevelInfo = seatingInfoMap.get(integerKey);
				if (seatLevelInfo != null) {
					totalAvailableSeats = totalAvailableSeats + 
									(seatLevelInfo.getTotalSeatsInCurrentLevel() - seatLevelInfo.getHeldSeats());
				}
			}
		}
		return totalAvailableSeats;
	}
	
	/*******************************************
	*  find and hold seats
	*******************************************/
	public SeatHoldData findAndHoldSeats(int numSeats, Optional<Integer> minLevel,Optional<Integer> maxLevel, String customerEmail){
		SeatHoldData seatHold = new SeatHoldData();
		Map<Integer, List<String>> seatHeldMap = new HashMap<Integer, List<String>>();
		int minimumLevel = minLevel.get().intValue();
		int maximumLevel = maxLevel.get().intValue();
		double totalPrice = 0;

		seatHold.setCustomerEmail(customerEmail);
		seatHold.setRequestSeatCount(numSeats);
		for (int i = minimumLevel; i <= maximumLevel; i++) {			
			SeatLevelData seatLevelInfo = getLevelSeatInfo(Optional.of(i));
			if (seatLevelInfo != null) {
				int heldSeats = seatLevelInfo.getHeldSeats();  				
				int availableSeats = seatLevelInfo.getTotalSeatsInCurrentLevel()- heldSeats; // Deduct seats already held 

				/*
				 * Check if requested seats are available in requested level
				 * if available, then proceed
				 * else, warn customer of limited seats availability
				 */
				if (availableSeats != 0) {
					if (availableSeats >= numSeats) {
						heldSeats = heldSeats + numSeats;
						seatLevelInfo.setHeldSeats(heldSeats);
						seatHeldMap.put(new Integer(i),heldSeatsInArray(numSeats, seatLevelInfo));
						ticketServiceAccessData.updateSeatingInfoMap(new Integer(i), seatLevelInfo);
						totalPrice = totalPrice+ (seatLevelInfo.getPrice() * numSeats);
						numSeats=0;
						break;
					} else {
						System.out.println("======================================================================");
						System.out.println("\nNOTE: You requested for "+numSeats +" seats. But "
								+ "only "+availableSeats+" seats are available in level #"+ minimumLevel);						
						System.out.println("   Type 1: if you want to continue");
						System.out.println("   Type 2: If you want to reset level");
						System.out.println("======================================================================");
						System.out.print("Enter type: ");
						MyReader reader = new MyReader();
						int type = reader.readInt();
						
						if(type == 1){
							heldSeats = heldSeats + availableSeats;
							seatLevelInfo.setHeldSeats(heldSeats);
							seatHeldMap.put(new Integer(i),heldSeatsInArray(availableSeats,seatLevelInfo));
							ticketServiceAccessData.updateSeatingInfoMap(new Integer(i), seatLevelInfo);
							numSeats = numSeats - availableSeats;
							totalPrice = totalPrice + (seatLevelInfo.getPrice() * availableSeats);
						}
						else{							
							return null;
						}
					
					}
				}
			}
		}

		int previousSeatHoldId = ticketServiceAccessData.getPreviousSeatHoldId() + 1;
		ticketServiceAccessData.setPreviousSeatHoldId(previousSeatHoldId);
		seatHold.setSeatHeldMap(seatHeldMap);
		seatHold.setTotalPrice(totalPrice);
		seatHold.setSeatHoldId(previousSeatHoldId);

		// Store seats Held by various customers into userSeatHoldMap map
		Map<Integer, SeatHoldData> userSeatHoldMap = ticketServiceAccessData.getUserHoldMap().get(customerEmail);
		if (userSeatHoldMap == null) {
			userSeatHoldMap = new HashMap<Integer, SeatHoldData>();
		}
		ticketServiceAccessData.updateUserHoldMap(new Integer(previousSeatHoldId), seatHold, customerEmail);
		if(numSeats>0){
			seatHold.setNonHeldSeatCount(numSeats);			
		}
		return seatHold;

	} // end of findAndHoldSeats method 
	
	private List<String> heldSeatsInArray(int seats, SeatLevelData seatLevelInfo) {
		int rowCount = seatLevelInfo.getRowCount();
		int columnCount = seatLevelInfo.getSeatsPerRowCount();
		String[][] seatArray = seatLevelInfo.getSeatingArray();
		List<String> seatNumberList = new ArrayList<String>();

		for (int row = 0; row < rowCount; row++) {
			if (seats > 0) {
				for (int column = 0; column < columnCount; column++) {
					if ("A".equalsIgnoreCase(seatArray[row][column])) {
						// Seat found available and is marked held
						seatArray[row][column] = "h";
						seatNumberList.add(row + ":" + column);
						seats--;
						if (seats == 0) {
							break;
						}
					}
				}
			}
		}
		return seatNumberList;

	}
	private SeatLevelData getLevelSeatInfo(Optional<Integer> level) {
		return ticketServiceAccessData.getSeatingInfoMap().get(level.get());
	}

	public Map<Integer,SeatLevelData> getSeatingInfoMap(){
		return ticketServiceAccessData.getSeatingInfoMap();
	}
	
	/*******************************************
	*  reserve seats
	*******************************************/
	public String reserveSeats(int seatHoldId, String customerEmail) {		
		Map<Integer, SeatHoldData> seatHoldMap = ticketServiceAccessData.getUserHoldMap().get(customerEmail);
		if (seatHoldMap != null && !seatHoldMap.isEmpty()) {
			SeatHoldData seatHold = seatHoldMap.get(new Integer(seatHoldId));

			if (seatHold != null) {
				Map<Integer, List<String>> seatHeldMap = seatHold.getSeatHeldMap();
				if (seatHeldMap != null && !seatHeldMap.isEmpty()) {
					for (Integer level : seatHeldMap.keySet()) {
						List<String> seatsHeldInLevel = seatHeldMap.get(level);
						SeatLevelData seatLevelInfo = ticketServiceAccessData.getSeatingInfoMap().get(level);
						String[][] seatingArray = seatLevelInfo.getSeatingArray();
						for (String rowDetails : seatsHeldInLevel) {
							String[] rowDetail = rowDetails.split(":");
							int row = Integer.parseInt(rowDetail[0]);
							int column = Integer.parseInt(rowDetail[1]);
							
							/*
							 * if seat is held, then mark it reserve using "x"
							 */
							if (seatingArray[row][column] == "h") { 
								seatingArray[row][column] = "x";
							}
						}
						int totalAvailableSeats = seatLevelInfo.getTotalSeatsInCurrentLevel();
						int heldSeats = seatLevelInfo.getHeldSeats();
						seatLevelInfo.settotalSeatsInCurrentLevel(totalAvailableSeats - seatsHeldInLevel.size());
						seatLevelInfo.setHeldSeats(heldSeats- seatsHeldInLevel.size());
						ticketServiceAccessData.updateSeatingInfoMap(level,seatLevelInfo);
					}
				}
			}
			seatHoldMap.remove(new Integer(seatHoldId));
			int previousReservationCode = ticketServiceAccessData.getReservationCode();
			ticketServiceAccessData.setReservationCode(previousReservationCode + 1);
			ticketServiceAccessData.updateReservationMap(new Integer(previousReservationCode), new Integer(seatHoldId));
			ticketServiceAccessData.removeSeatHoldFromSeatHoldMap(customerEmail,seatHoldId);
			return Integer.toString(previousReservationCode);
		}
		else{						
			new Exception("Seat hold data not found for the Seat Hold Id "+seatHoldId);
			return null;
		}
		
	} // end of reserveSeats method
				
	
	public void startTimer(int seatHoldId, String customerEmail) {
		Timer timer = new Timer();
		timer.schedule(new HeldSeatExpiration(seatHoldId, customerEmail,
				timer, ticketServiceAccessData), (TicketServiceConstants.HOLD_TIME_END * 1000));
	}

	public void removeSeatHeld() {
		String loggedCustomerEmail = TicketServiceOutput.getLoggedInEmail();
		// Get seatHoldMap details of the user;
		Map<Integer, SeatHoldData> seatHoldMap = ticketServiceAccessData.getUserHoldMap().get(loggedCustomerEmail);
		if (seatHoldMap != null && !seatHoldMap.isEmpty()) {
			Iterator<Integer> iterator = seatHoldMap.keySet().iterator();
			while (iterator.hasNext()) {
				Integer seatHoldId = iterator.next();
				if (seatHoldId != null) {
					ticketServiceAccessData.clearSeatHoldInfo(seatHoldMap.get(seatHoldId));
					iterator.remove();
				}
			}
		}
		ticketServiceAccessData.removeUserFromUserHoldMap(loggedCustomerEmail);
	}

}
