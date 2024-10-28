package tn.talan.tripaura_backend.services.CircuitService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.entities.Circuit.*;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.repositories.Circuit.CircuitRepo;
import tn.talan.tripaura_backend.repositories.Circuit.CityRepo;
import tn.talan.tripaura_backend.repositories.Circuit.CountryRepo;
import tn.talan.tripaura_backend.repositories.Circuit.ProgrammeRepo;

import java.util.*;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.talan.tripaura_backend.repositories.UserTripAuraRepo;
import tn.talan.tripaura_backend.repositories.flightRepo.FlightRepo;

import java.util.List;
import java.util.stream.Collectors;

import static tn.talan.tripaura_backend.entities.Circuit.Theme.DETENTE;
import static tn.talan.tripaura_backend.entities.Circuit.TypeCircuit.VOYAGEUR_SOLO;

@Service
@AllArgsConstructor
public class CircuitGenerationService {

    private ProgrammeRepo programmeRepository;
    private CircuitRepo circuitRepository;
    private CountryRepo countryRepo;
    private CityRepo cityRepository;
    private FlightRepo flightRepo;
    private UserTripAuraRepo userTripAuraRepository;
    private static final Logger logger = LoggerFactory.getLogger(CircuitGenerationService.class);

    public Circuit generateCustomCircuit(String userId, String countryName, Date startDate, Date endDate, String countryDepartName) {
        Country country = countryRepo.findCountryByNom(countryName);
        Country countryDepart = countryRepo.findCountryByNom(countryDepartName);
        //Flight f = flightRepo.findFlightByFlightNumber(numeroVol);
        UserTripAura userTripAura = userTripAuraRepository.findUserTripAuraById(userId);
        if (country == null) {
            throw new CustomException("Pays non trouvé : " + countryName);
        }

        List<City> cities = cityRepository.findCitiesByCountry(country);
        List<String> cityIds = cities.stream().map(City::getId).collect(Collectors.toList());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logger.info("Recherche de programmes pour le pays : {}, entre les dates : {} et {}",
                country.getNom(), sdf.format(startDate), sdf.format(endDate));

        List<Programme> availableProgrammes = programmeRepository.findProgrammesByCitiesAndDateRange(cityIds, startDate, endDate);
        logger.info("Programmes trouvés : {}", availableProgrammes.size());

        if (availableProgrammes.isEmpty()) {
            throw new CustomException("Aucun programme ne correspond aux critères spécifiés pour le pays " + countryName + ".");
        }

        List<Programme> selectedProgrammes = selectProgrammes(availableProgrammes, startDate, endDate);

        if (selectedProgrammes.isEmpty()) {
            throw new CustomException("Impossible de créer un circuit valide avec les programmes disponibles pour le pays " + countryName + ".");
        }
        // Rechercher les vols qui correspondent aux critères
        List<Flight> matchingFlights = flightRepo.findFlightsByDepartureAndDestinationAndDepartureDateAndReturnDate(
                countryDepart.getNom(), country.getNom(), startDate, endDate);
        logger.info("flightsssssssss"+matchingFlights.size());

        if (matchingFlights.isEmpty()) {
            throw new CustomException("Aucun vol ne correspond aux critères spécifiés pour le départ " + countryDepartName + " vers " + countryName + ".");
        }

        // Choisir un vol aléatoire parmi ceux qui correspondent aux critères
        Random random = new Random();
        Flight selectedFlight = matchingFlights.get(random.nextInt(matchingFlights.size()));

        Circuit circuit = new Circuit();
        // Récupérer les circuits existants pour le même pays
        List<Circuit> existingCircuits = circuitRepository.findCircuitByCountry(country);



        // Sélectionner un circuit aléatoire parmi les existants
        if (!existingCircuits.isEmpty()) {
           // Random random = new Random();
            Circuit randomCircuit = existingCircuits.get(random.nextInt(existingCircuits.size()));

            // Définir le circuit personnalisé
            if (randomCircuit.getGallerie() != null && !randomCircuit.getGallerie().isEmpty()) {
                circuit.setGallerie(new ArrayList<>(randomCircuit.getGallerie()));
            }}
        circuit.setTitre("Circuit de " + userTripAura.getFirstName()+ " à "+country.getNom());
        circuit.setCountryName(countryName);
        circuit.setCountry(country);
        circuit.setCountryDeparture(countryDepart);
        circuit.setDateDepart(startDate);
        circuit.setVol(selectedFlight);
        circuit.setType(VOYAGEUR_SOLO);
        circuit.setThemee(DETENTE);
        circuit.setVolNumber(selectedFlight.getFlightNumber());
        circuit.setDateArrive(endDate);
        circuit.setProgrammes(selectedProgrammes);
        circuit.setDateDeCreation(new Date());
        circuit.setPersonalized(true);
        circuit.setUserPersonnalized(userTripAura);

        return circuitRepository.save(circuit);
    }

    private List<Programme> selectProgrammes(List<Programme> availableProgrammes, Date startDate, Date endDate) {
        List<Programme> selectedProgrammes = new ArrayList<>();
        Set<String> usedCities = new HashSet<>();
        Random random = new Random();

        // Créer une liste de tous les programmes éligibles
        List<Programme> eligibleProgrammes = new ArrayList<>();

        for (Programme programme : availableProgrammes) {
            if (programme.getStartDate().compareTo(startDate) >= 0 &&
                    programme.getEndDate().compareTo(endDate) <= 0) {
                eligibleProgrammes.add(programme);
            }
        }

        // Tant qu'il y a des programmes éligibles
        while (!eligibleProgrammes.isEmpty()) {
            // Choisir un programme aléatoire parmi les éligibles
            int randomIndex = random.nextInt(eligibleProgrammes.size());
            Programme randomProgramme = eligibleProgrammes.get(randomIndex);

            // Vérifier si le programme choisi peut être ajouté
            if (!usedCities.contains(randomProgramme.getCity().getNom()) &&
                    !hasDateConflict(selectedProgrammes, randomProgramme)) {
                selectedProgrammes.add(randomProgramme);
                usedCities.add(randomProgramme.getCity().getNom());
            }

            // Retirer le programme de la liste des éligibles, qu'il ait été sélectionné ou non
            eligibleProgrammes.remove(randomIndex);
        }

        // Trier les programmes sélectionnés par date de début
        selectedProgrammes.sort(Comparator.comparing(Programme::getStartDate));

        return selectedProgrammes;
    }

    private boolean hasDateConflict(List<Programme> programmes, Programme newProgramme) {
        for (Programme p : programmes) {
            if ((newProgramme.getStartDate().compareTo(p.getStartDate()) >= 0 && newProgramme.getStartDate().compareTo(p.getEndDate()) < 0) ||
                    (newProgramme.getEndDate().compareTo(p.getStartDate()) > 0 && newProgramme.getEndDate().compareTo(p.getEndDate()) <= 0) ||
                    (newProgramme.getStartDate().compareTo(p.getStartDate()) <= 0 && newProgramme.getEndDate().compareTo(p.getEndDate()) >= 0)) {
                return true;
            }
        }
        return false;
    }


    /*private boolean hasDateConflict(List<Programme> programmes, Programme newProgramme) {
        for (Programme programme : programmes) {
            if (newProgramme.getStartDate().before(programme.getEndDate()) &&
                    newProgramme.getEndDate().after(programme.getStartDate())) {
                return true; // Conflit de dates trouvé
            }
        }
        return false;
    }*/
}
