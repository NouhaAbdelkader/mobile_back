package tn.talan.tripaura_backend.services.accomodationService;

import org.springframework.http.ResponseEntity;
import tn.talan.tripaura_backend.entities.accommodation.Room;
import tn.talan.tripaura_backend.entities.accommodation.RoomType;
import tn.talan.tripaura_backend.entities.accommodation.ViewType;

import java.util.List;

public interface RoomService {
    public ResponseEntity<?> updateRoom(Room room);
    void deleteRoom(String id);
    Room getRommById(String id);
    List<Room> getAllRoomsByAccomodation(String accomodationId);

    List<Room> getAllRoomsByAccomodationAndRoomType(String accomodationId, RoomType roomType);

    List<Room> getRoomByAccommodationAndRoomTypeAndViewType(String accomodationId, RoomType roomType, ViewType viewType);

    List<Room> getRoomByAccommodationAndViewType(String accomodationId , ViewType viewType);
    List<Room> getRoomByAccommodationAndAavailablity(String accomodationId , Boolean available);


}
