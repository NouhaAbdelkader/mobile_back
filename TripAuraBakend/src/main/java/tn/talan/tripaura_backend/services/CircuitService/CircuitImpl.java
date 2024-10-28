package tn.talan.tripaura_backend.services.CircuitService;

import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.talan.tripaura_backend.entities.Circuit.*;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.entities.accommodation.Accommodation;
import tn.talan.tripaura_backend.entities.activities.ActivityTripAura;
import tn.talan.tripaura_backend.repositories.Circuit.*;
import tn.talan.tripaura_backend.repositories.UserTripAuraRepo;
import tn.talan.tripaura_backend.repositories.accommodationRepo.AccommodationRepo;
import tn.talan.tripaura_backend.repositories.activities.ActivityTripAuraRepo;
import tn.talan.tripaura_backend.repositories.flightRepo.FlightRepo;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.bson.types.ObjectId;




@Service
@AllArgsConstructor
public class CircuitImpl implements  CircuitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CircuitImpl.class);
    public CircuitRepo circuitRepo;
    private ProgrammeRepo programmeRepo;
    private CityRepo cityRepo;
    private final DestinationRepo destinationRepo;
    private final ActivityTripAuraRepo activityTripAuraRepo;
    private final AccommodationRepo accommodationRepo;
    private final FlightRepo flightRepo;
    private final CountryRepo countryRepo;
    private UserTripAuraRepo userTripAuraRepo;


    private MongoTemplate mongoTemplate;

    /* public Circuit addCircuitWithProgrammes(Circuit circuit, List<Programme> programmes) {
         Circuit savedCircuit = circuitRepo.save(circuit);
         for (Programme programme : programmes) {
             programme.setCircuit(savedCircuit);
             programmeRepo.save(programme);
         }
         savedCircuit.setProgrammes(programmes);
         return circuitRepo.save(savedCircuit);
     }*/
    private SequenceGeneratorService sequenceGeneratorService;

    @Transactional
    public Circuit addCircuitWithProgrammes(Circuit circuit, List<MultipartFile> galleryImages) {
        validateCircuit(circuit);
        validateProgrammes(circuit);
        Date now = new Date();

        try {
            // Créer d'abord le circuit sans les images pour obtenir un ID
            Circuit initialSavedCircuit = circuitRepo.save(circuit);
            LOGGER.info("Initial circuit created: {}", initialSavedCircuit.getId());

            // Upload gallery images
            List<String> uploadedImageIds = uploadGalleryImages(initialSavedCircuit.getId(), galleryImages);
            initialSavedCircuit.setGallerie(uploadedImageIds);
            initialSavedCircuit.setDateDeCreation(now);
            initialSavedCircuit.setPersonalized(false);
            // Mettre à jour le circuit avec les IDs des images
            Circuit updatedCircuit = circuitRepo.save(initialSavedCircuit);
            LOGGER.info("Circuit updated with gallery images: {}", updatedCircuit);

            List<Programme> programmes = circuit.getProgrammes().stream()
                    .map(programme -> {
                        programme.setCircuit(updatedCircuit);
                        programme.setId(generateProgrammeId());
                        return programme;
                    })
                    .collect(Collectors.toList());

            List<Programme> savedProgrammes = programmeRepo.saveAll(programmes);
            updatedCircuit.setProgrammes(savedProgrammes);

            LOGGER.info("Programmes saved: {}", savedProgrammes);
            return circuitRepo.save(updatedCircuit);
        } catch (Exception e) {
            LOGGER.error("Error while adding circuit with programmes", e);
            throw e;
        }
    }

    private List<String> uploadGalleryImages(String circuitId, List<MultipartFile> galleryImages) {
        List<String> uploadedImageIds = new ArrayList<>();
        for (MultipartFile image : galleryImages) {
            try {
                String uploadedMediaId = uploadMessageMedia(circuitId, image, image.getContentType());
                if (uploadedMediaId != null) {
                    uploadedImageIds.add(uploadedMediaId);
                    LOGGER.info("Image uploaded for circuit {}: {}", circuitId, uploadedMediaId);
                }
            } catch (IOException e) {
                LOGGER.error("Error uploading image for circuit {}", circuitId, e);
            }
        }
        return uploadedImageIds;
    }

    private String uploadMessageMedia(String circuitId, MultipartFile file, String fileType) throws IOException {
        if (circuitId == null) {
            LOGGER.error("Message media must contain a Circuit ID.");
            return null;
        }
        LOGGER.info("Uploading media of type: {}", fileType);

        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb());

        try (InputStream inputStream = file.getInputStream()) {
            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(1048576)
                    .metadata(new Document("type", fileType)
                            .append("title", file.getOriginalFilename() != null ? file.getOriginalFilename().replaceAll(" ", "-") : "uploaded media")
                            .append("circuitId", circuitId));

            ObjectId fileId = gridFSBucket.uploadFromStream(file.getOriginalFilename(), inputStream, options);

            LOGGER.info("Media uploaded successfully with ID: {}", fileId.toHexString());
            return fileId.toHexString();
        } catch (Exception e) {
            LOGGER.error("An error occurred while uploading media: {}", e.getMessage(), e);
            return null;
        }
    }

    private void validateCircuit(Circuit circuit) {
        if (circuit.getCountryName() != null) {
            Country c = countryRepo.findCountryByNom(circuit.getCountryName());
            if (c == null) {
                throw new IllegalArgumentException(" Erreur dans Circuit: Country name not found: " + circuit.getCountryName());
            }
            circuit.setCountry(c);
        }

        if (circuit.getVolNumber() != null) {
            Flight flight = flightRepo.findFlightByFlightNumber(circuit.getVolNumber());
            validateFlight(flight, circuit);
            circuit.setVol(flight);
        }
        if (circuit.getCountryDeparture() != null && circuit.getCountry() != null &&
                circuit.getCountryDeparture().getNom().equals(circuit.getCountry().getNom())) {
            throw new IllegalArgumentException("Erreur dans Circuit: Country of departure and destination cannot be the same");
        }
    }

    private void validateFlight(Flight flight, Circuit circuit) {
        if (flight == null) {
            throw new IllegalArgumentException(" Erreur dans Circuit: Flight number not found: " + circuit.getVolNumber());
        }
        if (!flight.getDestination().equals(circuit.getCountry().getNom())) {
            throw new IllegalArgumentException("Erreur dans Circuit: Flight destination must match the circuit's country");
        }

        LocalDate circuitDepartureDate = convertToLocalDate(circuit.getDateDepart());
        LocalDate circuitArrivalDate = convertToLocalDate(circuit.getDateArrive());
        LocalDate flightDepartureDate = convertToLocalDate(flight.getDepartureDate());
        LocalDate flightReturnArrivalDate = convertToLocalDate(flight.getReturnDate());

        if (!circuitDepartureDate.equals(flightDepartureDate) ||
                !circuitArrivalDate.equals(flightReturnArrivalDate)) {
            throw new IllegalArgumentException("Erreur dans Circuit: Circuit departure and arrival dates must match the flight dates");
        }
    }

    private LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void validateProgrammes(Circuit circuit) {
        List<Programme> programmes = circuit.getProgrammes();
        int programmeCount = programmes.size();

        for (int i = 0; i < programmeCount; i++) {
            Programme programme = programmes.get(i);
            try {
                validateProgrammeDates(programme, circuit);
                validateProgrammeCity(programme, circuit);
                validateProgrammeAccommodation(programme);
                validateProgrammeActivities(programme);
                validateProgrammeFlight(programme);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Erreur dans le programme " + (i + 1) + " sur " + programmeCount + " : " + e.getMessage());
            }
        }
    }

    private void validateProgrammeDates(Programme programme, Circuit circuit) {
        LocalDate programmeStartDate = convertToLocalDate(programme.getStartDate());
        LocalDate programmeEndDate = convertToLocalDate(programme.getEndDate());
        if (programme.getStartDate().before(circuit.getDateDepart()) ||
                programme.getEndDate().after(circuit.getDateArrive())) {
            throw new IllegalArgumentException("Les dates du programme doivent être comprises dans les dates du circuit");
        }
        if (programmeStartDate.isAfter(programmeEndDate)) {
            throw new IllegalArgumentException("La date de début du programme doit être antérieure à la date de fin du programme");
        }
    }

    private void validateProgrammeCity(Programme programme, Circuit circuit) {
        if (programme.getCityName() != null) {
            City city = cityRepo.findCitiesByNom(programme.getCityName());
            if (city == null) {
                throw new IllegalArgumentException("Nom de ville non trouvé : " + programme.getCityName());
            }
            if (!city.getCountry().getNom().equals(circuit.getCountry().getNom())) {
                throw new IllegalArgumentException("La ville n'est pas dans le pays du circuit");
            }
            programme.setCity(city);
        }
    }

    private void validateProgrammeAccommodation(Programme programme) {
        if (programme.getAccommodationName() != null) {
            Accommodation accommodation = accommodationRepo.findAccommodationByName(programme.getAccommodationName());
            if (accommodation == null) {
                throw new IllegalArgumentException("Nom d'hébergement non trouvé : " + programme.getAccommodationName());
            }
            if (!programme.getCity().getNom().equals(accommodation.getCity().getNom())) {
                throw new IllegalArgumentException("La ville de l'hébergement ne correspond pas à la ville du programme");
            }
            programme.setHebergement(accommodation);
        }
    }

    private void validateProgrammeActivities(Programme programme) {
        if (programme.getActivityName() != null && !programme.getActivityName().isEmpty()) {
            List<ActivityTripAura> activities = activityTripAuraRepo.findByNameIn(programme.getActivityName());
            if (activities == null || activities.isEmpty()) {
                throw new IllegalArgumentException("Noms d'activités non trouvés : " + programme.getActivityName());
            }

            // Convertir les dates du programme en LocalDate pour ignorer l'heure
            LocalDate programmeStartDate = convertToLocalDate2(programme.getStartDate());
            LocalDate programmeEndDate = convertToLocalDate2(programme.getEndDate());

            for (ActivityTripAura activity : activities) {
                // Convertir les dates de l'activité en LocalDate pour ignorer l'heure
                LocalDate activityStartDate = convertToLocalDate2(activity.getDateOfBegining());
                LocalDate activityEndDate = convertToLocalDate2(activity.getDateOfEnding());

                if (activityStartDate.isBefore(programmeStartDate) ||
                        activityEndDate.isAfter(programmeEndDate)) {
                    throw new IllegalArgumentException("Les dates de l'activité " + activity.getName() + " doivent être comprises dans les dates du programme");
                }
            }
            programme.setActivities(activities);
        }
    }

    private LocalDate convertToLocalDate2(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    private void validateProgrammeFlight(Programme programme) {
        if (programme.getFlightNumber() != null) {
            Flight programmeFlight = flightRepo.findFlightByFlightNumber(programme.getFlightNumber());
            if (programmeFlight == null) {
                throw new IllegalArgumentException("Numéro de vol non trouvé : " + programme.getFlightNumber());
            }

            if (!programmeFlight.getDestination().equals(programme.getCity().getCountry().getNom())) {
                throw new IllegalArgumentException("La destination du vol doit correspondre au pays de la ville du programme");
            }

            LocalDate programmeStartDate = convertToLocalDate(programme.getStartDate());
            LocalDate programmeEndDate = convertToLocalDate(programme.getEndDate());
            LocalDate flightDepartureDate = convertToLocalDate(programmeFlight.getDepartureDate());
            LocalDate flightReturnArrivalDate = convertToLocalDate(programmeFlight.getReturnDate());

            if (!programmeStartDate.equals(flightDepartureDate) ||
                    !programmeEndDate.equals(flightReturnArrivalDate)) {
                throw new IllegalArgumentException("Les dates du programme doivent correspondre aux dates du vol");
            }
            // Validation : la date de départ doit être antérieure à la date d'arrivée




            programme.setVol(programmeFlight);
        }
    }

    private String generateProgrammeId() {
        return Long.toString(sequenceGeneratorService.generateSequence(Programme.SEQUENCE_NAME));
    }



    @Override
    public void deleteCircuit(String id) {
        Circuit circuit = circuitRepo.findCircuitById(id);
        List<Programme> programmes = programmeRepo.findProgrammeByCircuit(circuit);

        if (programmes != null) {
            programmeRepo.deleteAll(programmes);
        }

        circuitRepo.delete(circuit);
    }

    public void deleteProgramme(String id) {
        Programme programme = programmeRepo.findProgrammeById(id);
        //Circuit c = circuitRepo.findCircuitByProgrammes_Id(id);
        if (programme != null) {
            Circuit circuit = circuitRepo.findCircuitByProgrammes_Id(id);
            if (circuit != null) {
                circuit.getProgrammes().removeIf(p -> p.getId().equals(id));
                circuitRepo.save(circuit);
            }
            programmeRepo.delete(programme);
        }
    }
    @Override
    public Circuit getCircuitById(String id) {
        return circuitRepo.findCircuitById(id);
    }
    public List<Circuit> getCircuitBuUser(String id){
        UserTripAura u= userTripAuraRepo.findUserTripAuraById(id);
        if(u != null) {
            return circuitRepo.findCircuitByUserPersonnalizedOrderByDateDeCreationDesc(u);
        }
        return  null ;

    }

    @Override
    public List<Circuit> getAllCircuits() {
        return circuitRepo.findCircuitByOrderByDateDeCreationDesc();
    }

    @Override
    public List<Programme> findProgByCircuit(String acc) {
        Circuit c= circuitRepo.findCircuitById(acc);
        return programmeRepo.findProgrammeByCircuit(c);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional
    public Circuit updateCircuitWithProgrammes(Circuit circuit) {
        validateCircuit(circuit);
        validateProgrammes(circuit);

        try {
            // Fetch existing circuit

            Circuit existingCircuit = circuitRepo.findCircuitById(circuit.getId());

            // Update circuit details
            circuit.setGallerie(existingCircuit.getGallerie());
            circuit.setDateDeCreation(existingCircuit.getDateDeCreation());
            circuit.setPersonalized(existingCircuit.isPersonalized());

            // Update programmes
            List<Programme> programmes = circuit.getProgrammes().stream()
                    .map(programme -> {
                        if (programme.getId() == null || programme.getId().isEmpty() || programme.getId() == "0"){
                            // Create a new programme if the ID is null
                            programme.setId(generateProgrammeId());
                        }
                        programme.setCircuit(circuit);
                        return programme;
                    })
                    .collect(Collectors.toList());

            List<Programme> savedProgrammes = programmeRepo.saveAll(programmes);
            circuit.setProgrammes(savedProgrammes);

            LOGGER.info("Programmes updated: {}", savedProgrammes);
            return circuitRepo.save(circuit);
        } catch (Exception e) {
            LOGGER.error("Error while updating circuit with programmes", e);
            throw e;
        }
    }
     public List<Flight >getFlightByDepartureAndDestination(String departure, String destination) {
         return flightRepo.findFlightByDepartureAndDestination(departure, destination);
     }

    public List<Circuit> filterCircuits(String countryName, String city, String theme, String type, Date startDate, Date endDate) {
        Query query = new Query();
        City c = cityRepo.findCitiesByNom(city);
        Country country = countryRepo.findCountryByNom(countryName);
        if (country != null) {
            query.addCriteria(Criteria.where("country._id").is(country.getId()));
        }
        if (c != null) {
            query.addCriteria(Criteria.where("city").is(c));
        }
        if (theme != null) {
            query.addCriteria(Criteria.where("themee").is(theme));
        }
        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (startDate != null) {
            query.addCriteria(Criteria.where("dateDepart").is(startDate));
        }
        if (endDate != null) {
            query.addCriteria(Criteria.where("dateArrive").is(endDate));
        }
        return mongoTemplate.find(query, Circuit.class);
    }



}
