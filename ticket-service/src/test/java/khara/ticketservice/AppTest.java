package khara.ticketservice;

import static org.junit.Assert.*;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

import khara.ticketservice.model.SeatHoldData;
import khara.ticketservice.model.TicketServiceAccessData;
import khara.ticketservice.model.TicketServiceSingleton;
import khara.ticketservice.service.TicketService;
import khara.ticketservice.service.TicketServiceImplement;

public class AppTest {

	TicketService ticketService;
   @Before
    public void setUp() throws Exception {
    	TicketServiceSingleton ticketServiceSingleton = TicketServiceSingleton.getInstance();
		TicketServiceAccessData ticketServiceAccessData = new TicketServiceAccessData(
				ticketServiceSingleton);
		ticketService = new TicketServiceImplement(ticketServiceAccessData);
    
	}
	
	@Test
	public void test() {
		try{
			// test cases for ticketService.numSeatsAvailable method
			assertEquals(ticketService.numSeatsAvailable(Optional.of(1)),1250 );
	        assertEquals(ticketService.numSeatsAvailable(Optional.of(2)),2000 );
	        assertEquals(ticketService.numSeatsAvailable(Optional.of(3)),1500 );
	        assertEquals(ticketService.numSeatsAvailable(Optional.of(4)),1500 );	        	      
	        
		}
        catch (Exception e) {
			e.printStackTrace();
		}
        try {
        	// test cases for ticketService.findAndHoldSeats method
	        SeatHoldData seatHoldData1=ticketService.findAndHoldSeats(1000, Optional.of(1), Optional.of(2), "k.k@gmail.com");        
	        SeatHoldData seatHoldData2=ticketService.findAndHoldSeats(1500, Optional.of(2), Optional.of(3), "aaa@gmail.com");        	
	        assertNotNull(seatHoldData1);
        	assertNotNull(seatHoldData2);        	
        	
        	// test cases for ticketService.reserveSeats method
        	assertNotNull(ticketService.reserveSeats(seatHoldData1.getSeatHoldId(), "k.k@gmail.com"));
        	assertNotNull(ticketService.reserveSeats(seatHoldData2.getSeatHoldId(), "aaa@gmail.com"));
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}

}
