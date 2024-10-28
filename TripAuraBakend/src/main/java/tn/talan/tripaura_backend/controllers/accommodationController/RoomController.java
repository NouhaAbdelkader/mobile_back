package tn.talan.tripaura_backend.controllers.accommodationController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.talan.tripaura_backend.entities.accommodation.Accommodation;
import tn.talan.tripaura_backend.entities.accommodation.Room;
import tn.talan.tripaura_backend.entities.accommodation.RoomType;
import tn.talan.tripaura_backend.entities.accommodation.ViewType;
import tn.talan.tripaura_backend.services.accomodationService.RoomService;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "rooms")
@RequestMapping("/romms")
public class RoomController {
    private RoomService roomService;
    @GetMapping("/getRooms/{id}")
    public List<Room> getRoomsByIdAccommodation(@PathVariable String id) {
      return  roomService.getAllRoomsByAccomodation(id);

    }
    @GetMapping("/room/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable String id) {
        Room room = roomService.getRommById(id);
        if (room != null) {
            return new ResponseEntity<>(room, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/room")
    public ResponseEntity<?> updateRoom(@RequestBody Room r) {
        return roomService.updateRoom(r );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable String id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("room not found with id: " + id);
        }
    }

    @GetMapping("/rooms/accommodation/{accomodationId}/roomType/{roomType}")
    public List<Room> getAllRoomsByAccomodationAndRoomType(@PathVariable String accomodationId, @PathVariable RoomType roomType) {
        return roomService.getAllRoomsByAccomodationAndRoomType(accomodationId, roomType);
    }

    @GetMapping("/rooms/accommodation/{accomodationId}/viewType/{viewType}")
    public List<Room> getRoomByAccommodationAndViewType(@PathVariable String accomodationId, @PathVariable ViewType viewType) {
        return roomService.getRoomByAccommodationAndViewType(accomodationId, viewType);
    }

    @GetMapping("/rooms/accommodation/{accomodationId}/availability")
    public List<Room> getRoomByAccommodationAndAavailablity(@PathVariable String accomodationId, @RequestParam Boolean available) {
        return roomService.getRoomByAccommodationAndAavailablity(accomodationId, available);
    }
}
