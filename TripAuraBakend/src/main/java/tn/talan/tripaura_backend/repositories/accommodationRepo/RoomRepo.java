package tn.talan.tripaura_backend.repositories.accommodationRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.accommodation.Accommodation;
import tn.talan.tripaura_backend.entities.accommodation.Room;
import tn.talan.tripaura_backend.entities.accommodation.RoomType;
import tn.talan.tripaura_backend.entities.accommodation.ViewType;
import java.util.List;

public interface RoomRepo extends MongoRepository<Room, String> {
    public Room findRoomById(String id);
    List<Room> findByAccommodationOrderByRoomNumberDesc(Accommodation accommodation);
    List<Room> findRoomByAccommodation(Accommodation accommodation);
    List<Room> findRoomByAccommodationAndRoomType(Accommodation accommodation, RoomType roomType);
    List<Room> findRoomByAccommodationAndRoomTypeAndViewType(Accommodation accommodation, RoomType roomType, ViewType viewType);
    List<Room> findRoomByAccommodationAndViewType(Accommodation accommodation, ViewType viewType);
    List<Room> findRoomByAccommodationAndCribAvailable(Accommodation accommodation, boolean cribAvailable);
    List<Room> findRoomByAccommodationAndReserved(Accommodation accommodation, boolean reserved);


    List<Room> findRoomByAccommodationOrderByDateDesc(Accommodation accommodation);
}

