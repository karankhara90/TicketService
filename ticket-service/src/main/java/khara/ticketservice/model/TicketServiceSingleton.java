package khara.ticketservice.model;

import java.util.HashMap;
import java.util.Map;

/*
 * This singleton class initializes the Data Store object 
 * which represents the data model for the Ticket Service.It is also
 * used to access the data object
 */
public class TicketServiceSingleton {
	private static TicketServiceSingleton ticketServiceRepository;		// private and static member variable 

	private DataStore dataStore;

	public DataStore getInMemoryData() {
		return dataStore;
	}

	private TicketServiceSingleton() {   // private constructor
		dataStore = new DataStore();
		Map<Integer, SeatLevelData> seatingInfoMap = dataStore
				.getSeatingInfoMap();
		if (dataStore.getSeatingInfoMap() == null) {
			seatingInfoMap = new HashMap<Integer, SeatLevelData>();
			seatingInfoMap.put(new Integer(1), storeSeatLevelDetail("Orchestra", 100.00, 25, 50));
			seatingInfoMap.put(new Integer(2), storeSeatLevelDetail("Main     ", 75.00, 20, 100));
			seatingInfoMap.put(new Integer(3), storeSeatLevelDetail("Balcony 1", 50.00, 15, 100));
			seatingInfoMap.put(new Integer(4), storeSeatLevelDetail("Balcony 2", 40.00, 15, 100));
			dataStore.setSeatingInfoMap(seatingInfoMap);
		}
	}

	private SeatLevelData storeSeatLevelDetail(String seatName, Double seatPrice, int rowCount, int seatsInRow) {
		SeatLevelData seatLevelInfo = new SeatLevelData();
		seatLevelInfo.setName(seatName);
		seatLevelInfo.setPrice(seatPrice);
		seatLevelInfo.setRowCount(rowCount);
		seatLevelInfo.setSeatsPerRowCount(seatsInRow);
		seatLevelInfo.settotalSeatsInCurrentLevel(seatsInRow * rowCount);
		seatLevelInfo.setSeatingArray(getSeatArray(rowCount, seatsInRow));
		return seatLevelInfo;
	}
	
	private String[][] getSeatArray(int rowCount, int seatPerRow) {
		String[][] seatArray = new String[rowCount][seatPerRow];
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < seatPerRow; column++) {
				seatArray[row][column] = "A";
			}
		}
		return seatArray;

	}

	//  make the global access method synchronized, so that only one thread can execute this method at a time
	public static synchronized TicketServiceSingleton getInstance() { 		// lazy instantiation of singelton pattern   
		if (ticketServiceRepository == null) {
			ticketServiceRepository = new TicketServiceSingleton();
		}
		return ticketServiceRepository;
	}
}
