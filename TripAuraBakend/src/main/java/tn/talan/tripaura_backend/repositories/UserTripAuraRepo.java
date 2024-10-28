package tn.talan.tripaura_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.RoleType;
import tn.talan.tripaura_backend.entities.UserTripAura;

public interface UserTripAuraRepo extends MongoRepository<UserTripAura, String> {
    public Boolean existsUserTripAurasByEmail(String email);
    public Boolean existsUserTripAuraByNumber(String number);
    public UserTripAura findUserTripAuraByEmail(String email);
    public UserTripAura findUserTripAuraById(String id);
    public UserTripAura findUserTripAuraByRoles(RoleType roleType);


}
