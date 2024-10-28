package tn.talan.tripaura_backend.repositories.flightRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import org.springframework.data.mongodb.core.query.Query;
import tn.talan.tripaura_backend.entities.Flights.FlightClass;
import tn.talan.tripaura_backend.entities.Flights.FlightType;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class FlightRepoImpl implements FlightRepoCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Flight> findBySearchCriteria(String departure, String destination, Date departureDate, Date returnDate, FlightClass flightClass, FlightType flightType) {
        Query query = new Query();

        if (departure != null ) {
            query.addCriteria(Criteria.where("departure").is(departure));
        }
        if (destination != null ){
            query.addCriteria(Criteria.where("destination").is(destination));
        }
        if (departureDate != null) {
            query.addCriteria(Criteria.where("departureDate").gte(startOfDay(departureDate)).lt(endOfDay(departureDate)));
        }
        if (returnDate != null) {
            query.addCriteria(Criteria.where("returnDate").gte(startOfDay(returnDate)).lt(endOfDay(returnDate)));
        }
        if (flightClass != null) {
            query.addCriteria(Criteria.where("flightClasses").in(flightClass.toString()));
        }
        if (flightType != null) {
            query.addCriteria(Criteria.where("flightTypes").in(flightType.toString()));
        }

        System.out.println("Query: " + query.toString()); // Ajoutez ce log pour vérifier la requête MongoDB

        List<Flight> flights = mongoTemplate.find(query, Flight.class);

        // Log found flights
        flights.forEach(flight -> System.out.println("Found flight: " + flight.toString()));
        System.out.println("flights: " + flights);
        return flights;
    }
    private Date startOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date endOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}
