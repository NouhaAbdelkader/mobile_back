package tn.talan.tripaura_backend.services.Dashboard;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tn.talan.tripaura_backend.entities.Flights.Airline;
import tn.talan.tripaura_backend.entities.Gender;
import tn.talan.tripaura_backend.entities.RoleType;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.entities.notification.NotificationRequest;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.repositories.UserTripAuraRepo;
import tn.talan.tripaura_backend.services.notification.FCMService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardImp implements IDashboard{
    private final UserTripAuraRepo userTripAuraRepo;
    private final PasswordEncoder passwordEncoder;
    private MongoTemplate mongoTemplate;
    private FCMService fcmService;

    //  ***************************** Accounts management *******************************
@Override
public UserTripAura addUserTripAura(UserTripAura userTripAura, RoleType roleType) {
    if (userTripAuraRepo.existsUserTripAurasByEmail(userTripAura.getEmail())) {
        throw new CustomException("there is already a user trip Aura with this email");
    }

    if (userTripAuraRepo.existsUserTripAuraByNumber(userTripAura.getNumber()) || userTripAura.getNumber().length() != 8) {
        throw new CustomException("there is already a user trip Aura with this number");
    } else {
        String encodedPassword = passwordEncoder.encode(userTripAura.getPassword());
        userTripAura.setPassword(encodedPassword);
        userTripAura.setRoles(new HashSet<>(Collections.singleton(roleType)));
        return userTripAuraRepo.save(userTripAura);
    }
}
    @Override
    public List<UserTripAura> getUsersTripAura() {
        return userTripAuraRepo.findAll();
    }
    @Override
    public UserTripAura updateuserTripAura(UserTripAura userTripAura) {

        return userTripAuraRepo.save(userTripAura);
    }

    @Override
    public void deleteUserTripAura(String userTripAuraId) {
        if (!userTripAuraRepo.existsById(userTripAuraId)) {
            throw new CustomException("userTripAura not found");
        }
        userTripAuraRepo.deleteById(userTripAuraId);
    }

    @Override
    public List<UserTripAura> searchUsersTripAura(String email, String firstName, String lastName, String cin, String number, Gender gender, Set<RoleType> roles) {
        Query query = new Query();

        if (email != null ) {
            query.addCriteria(Criteria.where("email").is(email));
        }
        if (firstName != null ){
            query.addCriteria(Criteria.where("firstName").is(firstName));
        }
        if (lastName != null) {
            query.addCriteria(Criteria.where("lastName").is(lastName));
        }
        if (cin != null) {
            query.addCriteria(Criteria.where("country").is(cin));
        }
        if (number != null) {
            query.addCriteria(Criteria.where("cin").is(number));
        }
        if (gender != null) {
            query.addCriteria(Criteria.where("gender").is(gender));
        }
        if (roles != null) {
            query.addCriteria(Criteria.where("roles").is(roles));
        }


        System.out.println("Query: " + query.toString()); // Ajoutez ce log pour vérifier la requête MongoDB

        List<UserTripAura> userTripAuras = mongoTemplate.find(query, UserTripAura.class);

        // Log found users
        userTripAuras.forEach(userTripAura -> System.out.println("Found users: " + userTripAura.toString()));
        System.out.println("users: " + userTripAuras);
        return userTripAuras;
    }

    @Override
    public List<UserTripAura> searchAgents() {
        List<UserTripAura> users = userTripAuraRepo.findAll();
        List<UserTripAura> agents = users.stream()
                .filter(user -> user.getRoles().contains(RoleType.HOTEL_AGENT) ||
                        user.getRoles().contains(RoleType.FLIGHT_AGENT) ||
                        user.getRoles().contains(RoleType.ACTIVITY_AGENT) ||
                        user.getRoles().contains(RoleType.ACCOUNT_AGENT) ||
                        user.getRoles().contains(RoleType.SUPER_ADMIN))
                .collect(Collectors.toList());
        return agents;
    }

    @Override
    public List<UserTripAura> searchTravellers() {
        List<UserTripAura> users = userTripAuraRepo.findAll();
        List<UserTripAura> travellers = users.stream()
                .filter(user -> user.getRoles().contains(RoleType.TRAVELER))
                .collect(Collectors.toList());
        return travellers;
    }

    @Override
    public List<UserTripAura> searchCompanies() {
        List<UserTripAura> users = userTripAuraRepo.findAll();
        List<UserTripAura> companies = users.stream()
                .filter(user -> user.getRoles().contains(RoleType.COMPANY))
                .collect(Collectors.toList());
        return companies;    }





    @Override
    public List<UserTripAura> getActiveAgents() {
        return userTripAuraRepo.findAll().stream()
                .filter(user -> user.getRoles().contains(RoleType.ACCOUNT_AGENT) || user.getRoles().contains(RoleType.SUPER_ADMIN))
                .sorted(Comparator.comparingLong(UserTripAura::getTotalLoggedInTime).reversed()) // Trier par le temps total de connexion
                .limit(5) // Limiter à 5 utilisateurs les plus actifs
                .collect(Collectors.toList());
    }



    @Override
    public List<String> getNewAgentsThisMonth() {
        LocalDate now = LocalDate.now();
        return userTripAuraRepo.findAll().stream()
                .filter(user -> user.getRoles().contains(RoleType.ACCOUNT_AGENT) && isCreatedThisMonth(user.getCreatedDate(), now))
                .map(UserTripAura::getFirstName)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserTripAura> getActiveTravellers() {
        return userTripAuraRepo.findAll().stream()
                .filter(user -> user.getRoles().contains(RoleType.TRAVELER))
                .sorted(Comparator.comparingLong(UserTripAura::getTotalLoggedInTime).reversed()) // Trier par le temps total de connexion
                .limit(5) // Limiter à 5 utilisateurs les plus actifs
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getNewTravellersThisMonth() {
        LocalDate now = LocalDate.now();
        return userTripAuraRepo.findAll().stream()
                .filter(user -> user.getRoles().contains(RoleType.TRAVELER) && isCreatedThisMonth(user.getCreatedDate(), now))
                .map(UserTripAura::getFirstName)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserTripAura> getActiveCompanies() {
        return userTripAuraRepo.findAll().stream()
                .filter(user -> user.getRoles().contains(RoleType.COMPANY))
                .sorted(Comparator.comparingLong(UserTripAura::getTotalLoggedInTime).reversed()) // Trier par le temps total de connexion
                .limit(5) // Limiter à 5 utilisateurs les plus actifs
                .collect(Collectors.toList());
    }


    @Override
    public List<String> getNewCompaniesThisMonth() {
        LocalDate now = LocalDate.now();
        return userTripAuraRepo.findAll().stream()
                .filter(user -> user.getRoles().contains(RoleType.COMPANY) && isCreatedThisMonth(user.getCreatedDate(), now))
                .map(UserTripAura::getFirstName)
                .collect(Collectors.toList());
    }

    @Override
    public UserTripAura changePassword(UserTripAura userTripAura) {
        String encodedPassword = passwordEncoder.encode(userTripAura.getPassword());
        userTripAura.setPassword(encodedPassword);
        return userTripAuraRepo.save(userTripAura);

    }

    @Override
    public Boolean verifPassword(UserTripAura userTripAura, String currentPassword) {
        // Check if the provided password matches the encoded password stored in the user entity
        if (passwordEncoder.matches(currentPassword, userTripAura.getPassword())) {
            return true;
        }
        return false;
    }


    private boolean isCreatedThisMonth(LocalDateTime createdDate, LocalDate now) {
        if (createdDate == null) {
            return false; // Handle null case
        }
        return createdDate.getMonth() == now.getMonth() && createdDate.getYear() == now.getYear();
    }


    //  ***************************** Flights management *******************************



    //  ***************************** Airlines management *******************************


    //  ***************************** Activities management *******************************


    //  ***************************** Accommodations management *******************************

}
