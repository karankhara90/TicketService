package khara.ticketservice.utils;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import khara.ticketservice.model.SeatHoldData;
import khara.ticketservice.model.TicketServiceAccessData;
import khara.ticketservice.service.TicketService;
import khara.ticketservice.service.TicketServiceImplement;

public class HeldSeatExpiration extends TimerTask {

	private int seatHoldId;
	private String customerEmail;
	private Timer timer;
	private TicketServiceAccessData ticketServiceDAO;

	public HeldSeatExpiration(int seatHoldId, String customerEmail,
			Timer timer,TicketServiceAccessData ticketServiceDAO) {
		this.seatHoldId = seatHoldId;
		this.customerEmail = customerEmail;
		this.timer = timer;
		this.ticketServiceDAO=ticketServiceDAO;
	}

	@Override
	public void run() {
		
		Map<String, Map<Integer, SeatHoldData>> userHoldMap = ticketServiceDAO.getUserHoldMap();
				
		if (userHoldMap != null && !userHoldMap.isEmpty()) {
			Map<Integer, SeatHoldData> seatHoldMap = userHoldMap.get(customerEmail);
			if (seatHoldMap != null && !seatHoldMap.isEmpty()) {
				ticketServiceDAO.clearSeatHoldInfo(seatHoldMap.get(new Integer(
						seatHoldId)));
				ticketServiceDAO.getUserHoldMap().get(customerEmail)
						.remove(seatHoldId);
				System.out.println("\nSorry.Your seat hold Id " + seatHoldId
						+ " has expired. Try again");
			}

		}
		timer.cancel();
	}

}
