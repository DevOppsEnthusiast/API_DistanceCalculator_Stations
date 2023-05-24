# API_DistanceCalculator_Stations

This project provides a web service that calculates the straight-line distance between any two long-distance railway stations in Germany.  
The service utilizes a CSV file containing the latitude and longitude coordinates of the stations, which is made available by DB Station&Service AG.   
The CSV file includes information on approximately 360 stations served by ICs and ICEs.  

This project implements a web service with a REST API to calculate the distance between two stations.   
The distance is expressed in kilometers as the crow flies and is rounded to the nearest whole kilometer.   

The service offers the following REST API endpoint:  
**GET /api/v1/distance/{from}/{to}**

Example request:  
**GET /api/v1/distance/FF/BLS**

Example response (JSON):  
{  
  "from": "Frankfurt(Main)Hbf",  
  "to": "Berlin Hbf",  
  "distance": 423,  
  "unit": "km"  
}  

The web service is built using Java with Spring Boot.

# Prerequisites

To run and test the web service, you need the following components:

Java Development Kit (JDK) 8 or later
An integrated development environment like IntelliJ IDEA or Eclipse (optional)
Maven as the build tool (optional)

# Installation and Execution

Clone the GitHub repository or download the source code as a ZIP file.
Import the project into your IDE (if available).
Make sure that the required dependencies and libraries are downloaded (this is usually done automatically by Maven).
Open the DistanceCalculatorStationsApplication.java file and run the application or use your preferred build tool to compile and run the application.

# Configuration

The path to the CSV file containing the station data is set in the DistanceCalculatorStationsApplication class:

private static final String CSV_FILE_PATH = "..\\entfernungsrechnerBahnhoefe\\D_Bahnhof_2020_alle.CSV";

Please ensure that the path to the CSV file is correct. 
If the CSV file is located at a different location, adjust the path accordingly.

# API Documentation

GET /api/v1/distance/{from}/{to}
Calculates the distance between the specified stations.

    Path parameters:
        {from}: The DS100 code of the origin station.
        {to}: The DS100 code of the destination station.

    Example request:
      GET /api/v1/distance/FF/BLS

    Example response (JSON):

{
  "from": "Frankfurt(Main)Hbf",
  "to": "Berlin Hbf",
  "distance": 423,
  "unit": "km"
}
