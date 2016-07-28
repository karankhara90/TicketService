package khara.ticketservice.model;

import java.util.HashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import khara.ticketservice.service.TicketServiceOutput;


/*
 * This class has data  access related getters and setters
 */
public class TicketServiceAccessData {
	private TicketServiceSingleton ticketRepository;

	// constructor parameter takes Singleton class TicketServiceSingleton's only object
	public TicketServiceAccessData(TicketServiceSingleton ticketRepository) {   // constructor
		this.ticketRepository = ticketRepository;
	}

	// getUserHoldMap()
	public Map<String, Map<Integer, SeatHoldData>> getUserHoldMap() {
		return ticketRepository.getInMemoryData().getUserHoldMap();
	}

	// getSeatingInfoMap()
	public Map<Integer, SeatLevelData> getSeatingInfoMap() {
		return ticketRepository.getInMemoryData().getSeatingInfoMap();
	}
	
	// getPreviousSeatHoldId()
	public int getPreviousSeatHoldId() {
		return ticketRepository.getInMemoryData().getPreviousSeatHoldId();
	}

	// setPreviousSeatHoldId
	public void setPreviousSeatHoldId(int seatHoldId) {
		ticketRepository.getInMemoryData().setPreviousSeatHoldId(seatHoldId);
	}

	// getReservationCode()
	public int getReservationCode() {
		return ticketRepository.getInMemoryData().getPreviousReservationCode();
	}
	
	// setReservationCode
	public void setReservationCode(int previousReservationCode) {
		ticketRepository.getInMemoryData().setPreviousReservationCode(previousReservationCode);
	}

	// removeUserFromUserHoldMap
	public void removeUserFromUserHoldMap(String customerEmail) {
		ticketRepository.getInMemoryData().getUserHoldMap().remove(customerEmail);
	}

	// removeSeatHoldFromSeatHoldMap
	public void removeSeatHoldFromSeatHoldMap(String customerEmail,int SeatHoldId) {
		ticketRepository.getInMemoryData().getUserHoldMap().get(customerEmail).remove(SeatHoldId);
	}

	// saveOrUpdateSeatingInfoMap
	public void updateSeatingInfoMap(Integer level,SeatLevelData seatLevelInfo) {
		ticketRepository.getInMemoryData().getSeatingInfoMap().put(level, seatLevelInfo);
	}

	// clearSeatHeld()
	public void clearSeatHeld() {
		String loggedCustomerEmail = TicketServiceOutput.getLoggedInEmail();
		// Get seatHoldMap details of the user;
		Map<Integer, SeatHoldData> seatHoldMap = getUserHoldMap().get(loggedCustomerEmail);
		if (seatHoldMap != null && !seatHoldMap.isEmpty()) {
			Iterator<Integer> iterator = seatHoldMap.keySet().iterator();
			while (iterator.hasNext()) {
				Integer seatHoldId = iterator.next();
				if (seatHoldId != null) {
					clearSeatHoldInfo(seatHoldMap.get(seatHoldId));
					iterator.remove();
				}
			}
		}
		removeUserFromUserHoldMap(loggedCustomerEmail);

	}
	public void clearSeatHoldInfo(SeatHoldData seatHold) {
		if (seatHold != null) {
			Map<Integer, List<String>> seatHeldMap = seatHold.getSeatHeldMap();

			if (seatHeldMap != null && !seatHeldMap.isEmpty()) {
				for (Integer level : seatHeldMap.keySet()) {
					List<String> seatsHeldInLevel = seatHeldMap.get(level);
					SeatLevelData seatLevelInfo = ticketRepository
							.getInMemoryData().getSeatingInfoMap().get(level);
					String[][] seatingArray = seatLevelInfo.getSeatingArray();
					for (String rowDetails : seatsHeldInLevel) {
						String[] rowDetail = rowDetails.split(":");
						int row = Integer.parseInt(rowDetail[0]);
						int column = Integer.parseInt(rowDetail[1]);
						if (seatingArray[row][column] == "h") {
							seatingArray[row][column] = "A";
						}
					}
					int heldSeats = seatLevelInfo.getHeldSeats();
					seatLevelInfo.setHeldSeats(heldSeats
							- seatsHeldInLevel.size());
					updateSeatingInfoMap(level, seatLevelInfo);
				}
			}
		}
	}
 
	// saveOrUpdateUserHoldMap
	public void updateUserHoldMap(Integer seatHoldId, SeatHoldData seatHold,String customerEmail) {
		Map<Integer, SeatHoldData> seatHoldMap = ticketRepository.getInMemoryData().getUserHoldMap().get(customerEmail);
		if (seatHoldMap == null) {
			seatHoldMap = new HashMap<Integer, SeatHoldData>();
			seatHoldMap.put(seatHoldId, seatHold);
			ticketRepository.getInMemoryData().getUserHoldMap().put(customerEmail, seatHoldMap);
		} else {
			ticketRepository.getInMemoryData().getUserHoldMap().get(customerEmail).put(seatHoldId, seatHold);
		}

	}

	public void updateReservationMap(Integer reservationCode,
			Integer seatHoldId) {
		ticketRepository.getInMemoryData().getReservationMap()
				.put(reservationCode, seatHoldId);
	}

}
