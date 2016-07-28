package khara.ticketservice.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 

import org.apache.commons.lang3.StringUtils;

import khara.ticketservice.IO.MyReader;
//import khara.ticketservice.exception.TicketServiceException;
import khara.ticketservice.model.SeatHoldData;
import khara.ticketservice.model.SeatLevelData;
import khara.ticketservice.utils.TicketServiceConstants;


/*
 * This class interacts with the customer by displaying information to customers
 */
public class TicketServiceOutput {
	private static String loggedInEmail;
	private TicketService ticketService;

	public TicketServiceOutput(TicketService ticketService) {
		this.ticketService = ticketService;

	}

	public static String getLoggedInEmail() {
		return loggedInEmail;
	}

	public static void setLoggedInEmail(String loggedInEmail) {
		TicketServiceOutput.loggedInEmail = loggedInEmail;
	}

	/* -----------------------------------------------------------------*/
	public String getCustomerEmail() {
		return validateCustomerEmail();
	}
	public String validateCustomerEmail() {
		String customerEmail = null;
		boolean isFirstAttempt = true;
		
		while (!validateId(customerEmail)) {

			if (isFirstAttempt) {
				System.out.print("Your Email:  ");
			} else {
				System.out.print("Email not valid. Enter a valid Email:  ");
			}
			customerEmail = getReader().readLine();
			isFirstAttempt = false;
		}
		return customerEmail;
	}
	
	private boolean validateId(String customerEmail) {
		boolean isValid = false;
		if (!StringUtils.isEmpty(customerEmail)) {
			Pattern pattern = Pattern
					.compile("[a-zA-Z]*[0-9]*@[a-zA-Z]*.[a-zA-Z]*");
			Matcher matcher = pattern.matcher(customerEmail);
			if (matcher.matches()) {
				isValid = true;
			}
		}
		return isValid;
	}

	public void processTicketService() { //throws TicketServiceException {
		TicketServiceOutput.loggedInEmail = null;
		System.out.println("Enter your email to login");
		String customerEmail = getCustomerEmail();
		if (customerEmail == null ) {
			System.out.println("Login unsuccessful. Try again:");
			processTicketService();
		} else {
			TicketServiceOutput.loggedInEmail = customerEmail;
			displaySeatLevelInfo();
			displayMainMenu();
			processMenuChoices();
		}
	}	
	
	public void displaySeatLevelInfo() {
		System.out.println();
		System.out.println("***************************************************************");
		System.out.println("*           Seating arrangement and pricing details           *");
		System.out.println("***************************************************************");
		
		Map<Integer, SeatLevelData> seatingInfoMap = ticketService.getSeatingInfoMap();
		if (seatingInfoMap != null && !seatingInfoMap.isEmpty()) {
			System.out.print("Level Id");
			System.out.print("    Level Name ");
			System.out.print("       Price ");
			System.out.print("     Vacant Seats ");
			System.out.println();
			for (Integer level : seatingInfoMap.keySet()) {
				SeatLevelData seatLevelInfo = seatingInfoMap.get(level);
				if (seatLevelInfo != null) {
					System.out.print("   " + level);
					System.out.print("        " + seatLevelInfo.getName());
					System.out.print("         " + seatLevelInfo.getPrice());
					System.out.print("       " + seatLevelInfo.getTotalSeatsInCurrentLevel());
					System.out.println();

				}
			}
			//System.out.println();
		}

	}
	
	public void displayMainMenu() {
		System.out.println("***************************************************************");
		System.out.println("Press 1: Check Number of Seats Available");
		System.out.println("Press 2: Find And Hold Seats");
		System.out.println("Press 3: Reserve Seats");
		System.out.println("Press 4: Logout from your account");
		System.out.println();

	}
	
	public String validateMenuChoice() {
		String choice = null;
		//boolean isFirstAttempt = true;
		while (StringUtils.isEmpty(choice)) {			
			System.out.print("Choose Your Option(1-4) : ");
			choice = getReader().readLine();
		}
		return choice;
	}

	
	public void processMenuChoices() {//TicketServiceException {
		String choice = validateMenuChoice();
		int menuChoice;
		if (choice != null && !choice.isEmpty()) {
			try{
				menuChoice = Integer.parseInt(choice);
		    }catch(NumberFormatException e){
		    	menuChoice = 0;
		    }

			
			switch (menuChoice) {
			case 1:
				displayAvailabilityMenu();
				break;

			case 2:
				SeatHoldData seatHold = displayFindAndHoldSeatMenu();
				if(seatHold == null){
					displayMainMenu();
					processMenuChoices();
				}else{
					seatHeldReport(seatHold);
					System.out.println("-------------------------------------------------------");
					System.out.println("NOTE:   Seat held will expire in " + TicketServiceConstants.HOLD_TIME_END+" seconds");
					System.out.println("        Use hold ID '"+seatHold.getSeatHoldId()+"' to reserve seats ."); 
					System.out.println("*******************************************************");
					System.out.println("*******************************************************");
					ticketService.startTimer(seatHold.getSeatHoldId(),seatHold.getCustomerEmail());										
				}
				break;				

			case 3:
				displayReserveSeatMenu();
				break;

			case 4: 
				ticketService.removeSeatHeld();
				System.out.print("You have logged out successfully");
				processTicketService();
				getReader().getScanner().close();
				break;
			default :
				System.out.println("Invalid option \n");
				processMenuChoices();
			}	
			
			System.out.print("Go back to main menu? \nPress Y or N : ");
			if(getReader().readLine().equalsIgnoreCase("Y")){
				displaySeatLevelInfo();
				displayMainMenu();				
				processMenuChoices();
			}else{
				ticketService.removeSeatHeld();
				System.out.print("You have logSystem.out.printged out successfully");
				processTicketService();
			}
		}

	}
	
	
	public SeatHoldData displayFindAndHoldSeatMenu() {

		System.out.print("Enter number of seats to hold:");
		int seatsToHold = getReader().readInt();
		System.out.print("Enter minimum level:");
		int minLevelId = getReader().readInt();
		System.out.print("Enter maximum level:");
		int maxLevelId = getReader().readInt();

		SeatHoldData seatHoldObj = ticketService.findAndHoldSeats(seatsToHold,
				Optional.of(minLevelId), Optional.of(maxLevelId),
				TicketServiceOutput.loggedInEmail);
		return seatHoldObj;
	}
	
	public void displayReserveSeatMenu() {
		try {
			System.out.println();
			String customerEmail = TicketServiceOutput.loggedInEmail;
			System.out.println("Enter seatHoldId in which you held seats:");
			int seatHoldId = getReader().readInt();
			ticketService.reserveSeats(seatHoldId, customerEmail);
			System.out.println("---------------------------------------------------------");
			System.out.println("   !!CONGRATULATIONS!! You successfully reserved seats   ");
			System.out.println("---------------------------------------------------------");
		}
		catch (Exception e) {
			e.printStackTrace(); 
		} 
	}
	
	public void printSeatsInLevel(Integer levelId) {

		Map<Integer, SeatLevelData> seatingInfoMap = ticketService.getSeatingInfoMap();

		if (seatingInfoMap != null && !seatingInfoMap.isEmpty()) {
			SeatLevelData seatLevelInfo = seatingInfoMap.get(levelId);
			if (seatLevelInfo != null) {
				int row = seatLevelInfo.getRowCount();
				int column = seatLevelInfo.getSeatsPerRowCount();
				System.out.print("        ");
				for(int colIndex=1; colIndex <=column; colIndex++){					
					if(colIndex < 10){
						System.out.print(colIndex+" ");
					}else if(colIndex <= 100){
						System.out.print(colIndex+"");
					} 					
				}
				System.out.println();
				String[][] seatArray = seatLevelInfo.getSeatingArray();

				for (int rowCount = 0; rowCount < row; rowCount++) {
					int displayRow = rowCount + 1;
					if(displayRow <10){  
						System.out.print("Row" + displayRow + ":   "); 
					}
					else{ 
						System.out.print("Row" + displayRow + ":  ");
					}					
					for(int columnCount = 0; columnCount < column; columnCount++) {
						System.out.print(seatArray[rowCount][columnCount]+ " ");						
					}
					System.out.println();
				}
			}
		}
	}
	
	public void displayAvailabilityMenu() {

		System.out.print("Enter venue level number : ");
		String levelId = getReader().readLine();
		Integer integer = null;
		System.out.println();
		System.out.println("******************************");
		System.out.println("   A : seat available");
		System.out.println("   h : seat on hold");
		System.out.println("   x : seat is reserved");
		System.out.println("******************************");
		System.out.println("Seats Availability for level number "+levelId+" ");
		System.out.println("-----------------------------------");
		if (levelId != null && !levelId.isEmpty()) {
			integer = Integer.parseInt(levelId);
			//System.out.println("Level Selected :   " + levelId);
			printSeatsInLevel(Integer.parseInt(levelId));
		} else {
			System.out.println("All   Levels         ");
		}
		System.out.println("Available Seats  :"
		+ ticketService.numSeatsAvailable(Optional.ofNullable(integer)));

	}

	
	public void seatHeldReport(SeatHoldData seatHold) {

		if (seatHold.getSeatHeldMap().isEmpty()) {
			System.out.println("No seats available in the requested level:");
		} else {
			System.out.println("*******************************************************");
			System.out.println("*****************  SEAT HOLD DETAILS  *****************");
			System.out.println("*******************************************************");			
			System.out.println("****    Customer Email: " + seatHold.getCustomerEmail());
			System.out.println("****    Seat Hold Id: " + seatHold.getSeatHoldId());
			System.out.println("****    Seats requested: " + seatHold.getRequestSeatCount());
			System.out.println("****    Total Price of held seats: " + seatHold.getTotalPrice());			
			
			if (seatHold.getNonHeldSeatCount() > 0) {
				int seatsHeldCount = seatHold.getRequestSeatCount()
						- seatHold.getNonHeldSeatCount();
				System.out.println("    "+seatsHeldCount + " seats only are held. This Level is full");				
			}
		}
	}
		
	public MyReader getReader() {
		MyReader reader = new MyReader();
		return reader;
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	