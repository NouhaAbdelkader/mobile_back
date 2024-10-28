package tn.talan.tripaura_backend.services.accomodationService;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.entities.accommodation.Accommodation;
import tn.talan.tripaura_backend.entities.accommodation.Room;
import tn.talan.tripaura_backend.entities.accommodation.RoomType;
import tn.talan.tripaura_backend.entities.accommodation.ViewType;
import tn.talan.tripaura_backend.repositories.accommodationRepo.AccommodationRepo;
import tn.talan.tripaura_backend.repositories.accommodationRepo.RoomRepo;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomImpl implements RoomService {
    private RoomRepo roomRepo;
    private AccommodationRepo accommodationRepo;

    @Override
    public ResponseEntity<?> updateRoom(Room room) {
        Date now = new Date();
        Room m = roomRepo.findRoomById(room.getId());

        try {
            room.setDate(m.getDate());
            Room updatedRoom = roomRepo.save(room);
            return ResponseEntity.ok(updatedRoom);
        } catch (DataAccessException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database access error occurred while updating the room.");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while updating the room.");
        }
    }

    @Override
    public void deleteRoom(String id) {
        Room r= roomRepo.findRoomById(id);
        roomRepo.delete(r);

    }

    @Override
    public Room getRommById(String id) {
        return roomRepo.findRoomById(id);
    }

    @Override
    public List<Room> getAllRoomsByAccomodation(String accomodationId) {
        Accommodation a= accommodationRepo.findAccommodationById(accomodationId);
        return roomRepo.findRoomByAccommodationOrderByDateDesc(a);
    }

    @Override
    public List<Room> getAllRoomsByAccomodationAndRoomType(String accomodationId, RoomType roomType) {
        Accommodation a= accommodationRepo.findAccommodationById(accomodationId);
        return roomRepo.findRoomByAccommodationAndRoomType(a,roomType);
    }

    @Override
    public List<Room> getRoomByAccommodationAndRoomTypeAndViewType(String accomodationId, RoomType roomType, ViewType viewType) {
        Accommodation a= accommodationRepo.findAccommodationById(accomodationId);
        return roomRepo.findRoomByAccommodationAndRoomTypeAndViewType(a,roomType,viewType);
    }

    @Override
    public List<Room> getRoomByAccommodationAndViewType(String accomodationId, ViewType viewType) {
        Accommodation a= accommodationRepo.findAccommodationById(accomodationId);
        return roomRepo.findRoomByAccommodationAndViewType(a,viewType);
    }

    @Override
    public List<Room> getRoomByAccommodationAndAavailablity(String accomodationId, Boolean available) {
        return roomRepo.findRoomByAccommodationAndReserved(accommodationRepo.findAccommodationById(accomodationId),available);
    }
}
