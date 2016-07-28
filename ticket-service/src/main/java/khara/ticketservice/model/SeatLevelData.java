package khara.ticketservice.model;

/*
 * class to store seats related data in particular level
 */
public class SeatLevelData {
	private String seatName;
	private int rowCount;
	private int seatsInRowCount;
	private double seatPrice;
	private int totalSeatsInCurrentLevel;
	private int heldSeats;
	private int reservedSeats;
	private String[][] seatingArray;

	public String getName() {
		return seatName;
	}

	public void setName(String name) {
		this.seatName = name;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getSeatsPerRowCount() {
		return seatsInRowCount;
	}

	public void setSeatsPerRowCount(int seatsPerRowCount) {
		this.seatsInRowCount = seatsPerRowCount;
	}

	public double getPrice() {
		return seatPrice;
	}

	public void setPrice(double price) {
		this.seatPrice = price;
	}

	public int getTotalSeatsInCurrentLevel() {
		return totalSeatsInCurrentLevel;
	}

	public void settotalSeatsInCurrentLevel(int totalAvailableSeats) {
		this.totalSeatsInCurrentLevel = totalAvailableSeats;
	}

	public int getHeldSeats() {
		return heldSeats;
	}

	public void setHeldSeats(int heldSeats) {
		this.heldSeats = heldSeats;
	}

	public int getReservedSeats() {
		return reservedSeats;
	}

	public void setReservedSeats(int reservedSeats) {
		this.reservedSeats = reservedSeats;
	}

	public String[][] getSeatingArray() {
		return seatingArray;
	}

	public void setSeatingArray(String[][] seatingArray) {
		this.seatingArray = seatingArray;
	}
}
