
##Project 
* This ticket-service application is used to find, hold, and reserve seats for a venue.
* Seats are named in following manner:
   * A : seat available
   * h : seat on hold
   * x : seat is reserved

###Features
* Find available seats in a specific level 
* Hold and reserve the best available seats 


### Dependencies for building project: 
* Java SDK 8
* Maven
* Git


### ASSUMPTIONS:
* Customer will be asked to enter a valid Email Id.
* Seat held will only be valid for some seconds
* Used In memory storage, so only one customer can login and book seats 	
* If user mentions more than 1 level to find seats, then it checks availability in all the levels until it finds requested number of available seats.
* If requested number of seats are more than available vacant seats in requested level, then it gives customer 2 options:
	* either to book only the available seats	
	* go back to options, and choose level number and seats again.
* A seat hold Id will be provided to customer after seats holding and this hold id will be  asked before reserving seats.


## To import and run project on local machine 
* Clone project
* Navigate to the ticket-service directory, then
    *  Type: mvn compile
    *  Type: mvn package
* An executable .jar file will be built inside "target" directory


# Run
* java -jar target/ticket-service-0.0.1-SNAPSHOT.jar


# To run all tests:
*  mvn test

