package khara.ticketservice.IO;

import java.util.Scanner;

public class MyReader {

	// get input from user
	private  Scanner scanner = new Scanner(System.in);

	public  Scanner getScanner() {
		return scanner;
	}

	public  void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public String readLine(){
		return this.scanner.nextLine();
	}
	
	public int readInt(){
		return this.scanner.nextInt();
	}
}
