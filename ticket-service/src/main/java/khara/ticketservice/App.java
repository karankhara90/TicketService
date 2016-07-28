package khara.ticketservice;

import khara.ticketservice.model.TicketServiceAccessData;
import khara.ticketservice.model.TicketServiceSingleton;
import khara.ticketservice.service.TicketServiceOutput;
import khara.ticketservice.service.TicketService;
import khara.ticketservice.service.TicketServiceImplement;
/*
 * This is the main class of this application
 */
public class App {
	public static void main( String[] args )
    {
        try {
			// Get the only object available from TicketServiceSingleton class
			TicketServiceSingleton ticketServiceSingleton = TicketServiceSingleton.getInstance();
			
			// TicketServiceAccessData is used for data access related methods
			TicketServiceAccessData ticketServiceAccessData = new TicketServiceAccessData(ticketServiceSingleton);
			
			// TicketServiceImplement implements TicketService interface.
			TicketService ticketService = new TicketServiceImplement(ticketServiceAccessData);
			
			//TicketServiceOutput interacts with customer by displaying information to customers 
			TicketServiceOutput ticketServiceOutput = new TicketServiceOutput(ticketService);
			ticketServiceOutput.processTicketService();  // display process login info  
		} catch (Exception e) {
			System.out.println("Error occured in ticketservice App is: "+e);
		}
    }
}
