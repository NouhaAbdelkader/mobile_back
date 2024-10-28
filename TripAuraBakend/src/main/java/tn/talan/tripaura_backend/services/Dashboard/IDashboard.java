package tn.talan.tripaura_backend.services.Dashboard;

import tn.talan.tripaura_backend.entities.Gender;
import tn.talan.tripaura_backend.entities.RoleType;
import tn.talan.tripaura_backend.entities.UserTripAura;

import java.util.List;
import java.util.Set;

public interface IDashboard {
    UserTripAura addUserTripAura(UserTripAura userTripAura, RoleType roleType);
    public List<UserTripAura> getUsersTripAura();

    public UserTripAura updateuserTripAura(UserTripAura userTripAura) ;

    void  deleteUserTripAura(String userTripAuraId);
    List<UserTripAura> searchUsersTripAura(String email , String firstName , String lastName , String cin , String number , Gender gender , Set<RoleType> roles);

    List<UserTripAura> searchAgents();
    List<UserTripAura> searchTravellers();

    List<UserTripAura> searchCompanies();
    List<UserTripAura> getActiveAgents();
    List<String> getNewAgentsThisMonth();
    List<UserTripAura> getActiveTravellers();
    List<String> getNewTravellersThisMonth();
    List<UserTripAura> getActiveCompanies();
    List<String> getNewCompaniesThisMonth();
    public UserTripAura changePassword(UserTripAura userTripAura) ;

    Boolean verifPassword(UserTripAura userTripAura , String currentPassword);
}
