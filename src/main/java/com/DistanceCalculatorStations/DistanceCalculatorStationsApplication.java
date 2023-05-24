package com.DistanceCalculatorStations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class DistanceCalculatorStationsApplication {
    private static final String CSV_FILE_PATH = "CsvStations\\D_Bahnhof_2020_alle.CSV";

    public static void main(String[] args) {
        SpringApplication.run(DistanceCalculatorStationsApplication.class, args);
    }

    @RestController
    public class DistanceController {
        private Map<String, Station> stations;
        private DistanceCalculator distanceCalculator;

        public DistanceController(DistanceCalculator distanceCalculator) {
            this.distanceCalculator = distanceCalculator;
            stations = readStationsFromCSV();
        }

        @GetMapping("/api/v1/distance/{from}/{to}")
        public DistanceResponse getDistance(@PathVariable("from") String fromCode,
                                            @PathVariable("to") String toCode) {
            Station fromStation = stations.get(fromCode);
            Station toStation = stations.get(toCode);

            String errorMessage = "";

            if (fromStation == null) {
                errorMessage = fromCode + " is not a valid DS-100 Code for long-distance stations. <br>";
            }
            if (toStation == null) {
                errorMessage = errorMessage + toCode + " is not a valid DS-100 Code for long-distance stations.";
            }
            if (errorMessage != "") {
                throw new IllegalArgumentException(errorMessage);
            }

            double distance = distanceCalculator.calculateDistance(fromStation.getLatitude(), fromStation.getLongitude(),
                    toStation.getLatitude(), toStation.getLongitude());

            DistanceResponse response = new DistanceResponse();
            response.setFrom(fromStation.getName());
            response.setTo(toStation.getName());
            response.setDistance(distance);
            response.setUnit("km");

            return response;
        }

        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public String handleIllegalArgumentException(IllegalArgumentException ex) {
            return ex.getMessage();
        }
    }

    private Map<String, Station> readStationsFromCSV() {
        Map<String, Station> stations = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String code = parts[1];
                String name = parts[3];
                double latitude = Double.parseDouble(parts[6].replace(",", "."));
                double longitude = Double.parseDouble(parts[5].replace(",", "."));

                if (parts[4].equals("FV")) {
                    Station station = new Station();
                    station.setName(name);
                    station.setLatitude(latitude);
                    station.setLongitude(longitude);

                    stations.put(code, station);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }
}
