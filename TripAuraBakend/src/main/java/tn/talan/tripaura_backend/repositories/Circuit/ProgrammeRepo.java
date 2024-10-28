package tn.talan.tripaura_backend.repositories.Circuit;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Circuit.Circuit;
import tn.talan.tripaura_backend.entities.Circuit.Country;
import tn.talan.tripaura_backend.entities.Circuit.Programme;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface ProgrammeRepo extends MongoRepository<Programme, String> {
    Programme findProgrammeById(String id);
    List<Programme> findProgrammeByCircuit(Circuit acc) ;
   /* @Query("{ 'city.country': ?0, " +
            "'startDate': { $gte: ?1 }, " +
            "'endDate': { $lte: ?2 }, " +
            "'$nor': [ { 'startDate': { $lt: ?2, $gt: ?1 } }, { 'endDate': { $lt: ?2, $gt: ?1 } } ] }")
    List<Programme> findProgrammesByCountryAndDate(Country country, Date startDate, Date endDate);*/
   @Query("{ 'city.country.nom': ?0, 'startDate': { $gte: ?1 }, 'endDate': { $lte: ?2 } }")
   List<Programme> findProgrammesByCountryAndDate(String countryName, Date startDate, Date endDate);

    @Query("{ 'city': { $in: ?0 }, 'startDate': { $gte: ?1 }, 'endDate': { $lte: ?2 } }")
    List<Programme> findProgrammesByCitiesAndDateRange(List<String> cityIds, Date startDate, Date endDate);

}
