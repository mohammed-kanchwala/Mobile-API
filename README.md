# Mobile-API
Mobile Search API services with Java 8 and Spring Boot

# Service Details
Fetches a Json of List of Mobiles from a rest endpoint which is fed as an input to provide an API to search for
 Mobiles based on different search criteria like annouceDate, priceEur, price
, brand etc.

#Sample Queries
http://localhost:8443/mobile/search : returns List of All the Mobiles available

http://localhost:8443/mobile/search?price=200 : returns a List of Mobiles whose price matches the input i.e. (200).

http://localhost:8443/mobile/search?announceDate=1999&price=200 : would return a List of Mobiles whose announceDate is
(1999) and price is (200).


## Architectural Diagram
![Alt text](Mobile-API-Architechture.JPG?raw=true "Architectural Diagram")


## Coverage Report
![Alt text](Mobile-API-code-coverage.JPG?raw=true "Coverage Report")


## JUnit Execution Report
![Alt text](Mobile-API-junit-result.JPG?raw=true "JUnit Execution Report")


## Running JUnit and Coverage Test

1.  Using Eclipse
      
	i.	Right click on your project in the Project Explorer then select "Coverage As" > "JUnit Test". Eclipse will run the test and generate a report about the Junit execution as well as the coverage result. 

2.  Using Maven 
      
	i. Install Maven. 
      
	ii. Go to the project directory, then run mvn test. Maven will run the test and generate the Junit execution report. Coverage report will be generated at PROJECT_DIRECTORY\target\surefire-reports\
