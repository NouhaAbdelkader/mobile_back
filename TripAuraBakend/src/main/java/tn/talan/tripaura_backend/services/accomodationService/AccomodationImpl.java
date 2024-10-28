package tn.talan.tripaura_backend.services.accomodationService;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.talan.tripaura_backend.entities.Circuit.City;
import tn.talan.tripaura_backend.entities.Circuit.Country;
import tn.talan.tripaura_backend.entities.accommodation.*;
import tn.talan.tripaura_backend.repositories.Circuit.CityRepo;
import tn.talan.tripaura_backend.repositories.Circuit.CountryRepo;
import tn.talan.tripaura_backend.repositories.accommodationRepo.AccommodationRepo;
import tn.talan.tripaura_backend.repositories.accommodationRepo.RoomRepo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
@Service
@AllArgsConstructor
public class AccomodationImpl implements AccomodationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccomodationImpl.class);
    public AccommodationRepo accommodationRepo;
    public RoomRepo roomRepo;
    private MongoTemplate mongoTemplate;
    private CityRepo cityRepo;
    private CountryRepo countryRepo;

    private static final Logger logger = LoggerFactory.getLogger(AccomodationImpl.class);


    @Override

    public ResponseEntity<?> updateAccommodation(Accommodation accommodation) {
        if (accommodation.getNumberOfRoom() != this.calculTotalRoom(accommodation)) {

            return ResponseEntity.badRequest().body("The total number of rooms does not match the sum of specific room types and views.");
        }
        try {
            Accommodation ac = accommodationRepo.findAccommodationById(accommodation.getId());

            accommodation.setDate(ac.getDate());
            Accommodation updatedAccommodation = accommodationRepo.save(accommodation);
            return ResponseEntity.ok(updatedAccommodation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the accommodation.");
        }
    }

    @Override
    public void deleteAccomodation(String id) {
        //accommodationRepo.deleteById(id);
        Accommodation accommodation = accommodationRepo.findAccommodationById(id);
        List<Room> rooms = roomRepo.findRoomByAccommodation(accommodation);

        if (accommodation != null) {
            LOGGER.info("************Accommodation Not null***********");
            if (rooms != null) {
                roomRepo.deleteAll(rooms);
            }

            accommodationRepo.delete(accommodation);
        }

    }

    @Override
    public ResponseEntity<?> createAccomodation(Accommodation accommodation, MultipartFile image, String fileType) {
        String uploadedMediaId = null;
        Date now = new Date();
        if (accommodation.getNumberOfRoom() != this.calculTotalRoom(accommodation)) {
            return ResponseEntity
                    .badRequest()
                    .body("The total number of rooms does not match the sum of specific room types and views.");
        }
        try {
            uploadedMediaId = this.uploadMessageMedia(accommodation.getId(), image, fileType);
            logger.info("Creating accommodation Service : {}", uploadedMediaId);
            accommodation.setImage(uploadedMediaId);
            accommodation.setDate(now);
            Accommodation savedAccommodation = accommodationRepo.save(accommodation);
            generateAndSaveRooms(savedAccommodation);
            return ResponseEntity.ok(savedAccommodation);
        } catch (Exception e) {
            logger.info("n est pas Creating accommodation Service : {}", uploadedMediaId);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the accommodation.");
        }
    }


    public ResponseEntity<?> addAccomodation(Accommodation accommodation) {
        if (accommodation.getNumberOfRoom() != this.calculTotalRoom(accommodation)) {
            return ResponseEntity
                    .badRequest()
                    .body("The total number of rooms does not match the sum of specific room types and views.");
        }
        try {
            Accommodation savedAccommodation = accommodationRepo.save(accommodation);
            generateAndSaveRooms(savedAccommodation);
            return ResponseEntity.ok(savedAccommodation);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the accommodation.");
        }
    }


    private void generateAndSaveRooms(Accommodation accommodation) {

        int roomNumber = 1;

        //  for SINGLE type rooms
        roomNumber = generateRooms(accommodation, RoomType.SINGLE, ViewType.GARDEN_VIEW, accommodation.getNumberOfRoomSingleViewGarden(), roomNumber);
        roomNumber = generateRooms(accommodation, RoomType.SINGLE, ViewType.Pool_VIEW, accommodation.getNumberOfRoomSingleViewPool(), roomNumber);
        roomNumber = generateRooms(accommodation, RoomType.SINGLE, ViewType.SEA_VIEW, accommodation.getNumberOfRoomSingleViewSea(), roomNumber);

        //  for DOUBLE type rooms
        roomNumber = generateRooms(accommodation, RoomType.DOUBLE, ViewType.GARDEN_VIEW, accommodation.getNumberOfRoomDoubleViewGarden(), roomNumber);
        roomNumber = generateRooms(accommodation, RoomType.DOUBLE, ViewType.Pool_VIEW, accommodation.getNumberOfRoomDoubleViewPool(), roomNumber);
        roomNumber = generateRooms(accommodation, RoomType.DOUBLE, ViewType.SEA_VIEW, accommodation.getNumberOfRoomDoubleViewSea(), roomNumber);

        //  for TRIPLE type rooms
        roomNumber = generateRooms(accommodation, RoomType.TRIPLE, ViewType.GARDEN_VIEW, accommodation.getNumberOfRoomTripleViewGarden(), roomNumber);
        roomNumber = generateRooms(accommodation, RoomType.TRIPLE, ViewType.Pool_VIEW, accommodation.getNumberOfRoomTripleViewPool(), roomNumber);
        roomNumber = generateRooms(accommodation, RoomType.TRIPLE, ViewType.SEA_VIEW, accommodation.getNumberOfRoomTripleViewSea(), roomNumber);


    }

    private int generateRooms(Accommodation accommodation, RoomType roomType, ViewType viewType, int count, int startRoomNumber) {
        Date now = new Date();
        for (int i = 0; i < count; i++) {
            Room room = new Room();
            room.setRoomNumber(startRoomNumber + i); // pour la sq room number
            room.setReserved(false);
            room.setRoomType(roomType);
            room.setViewType(viewType);
            room.setDate(now);
            room.setCribAvailable(false);
            room.setAccommodation(accommodation);
            roomRepo.save(room);
        }
        return startRoomNumber + count; // Return the next start room number
    }

    public int calculTotalRoom(Accommodation accommodation) {
        return accommodation.getNumberOfRoomDoubleViewGarden() + accommodation.getNumberOfRoomDoubleViewPool() + accommodation.getNumberOfRoomDoubleViewSea() + accommodation.getNumberOfRoomSingleViewGarden() + accommodation.getNumberOfRoomSingleViewPool() + accommodation.getNumberOfRoomSingleViewSea() + accommodation.getNumberOfRoomTripleViewGarden() + accommodation.getNumberOfRoomTripleViewPool() + accommodation.getNumberOfRoomTripleViewSea();
    }

    @Override
    public String uploadMessageMedia(String idAccomm, MultipartFile file, String fileType) throws IOException {
        if (idAccomm == null) {
            logger.error("Message media must contain an Accommodation ID.");
            return null;
        }
        logger.info("Uploading media of type: {}", fileType);

        // Create a GridFS bucket
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb());

        // Convert the MultipartFile to an InputStream
        try (InputStream inputStream = file.getInputStream()) {
            // Create some custom options (like chunk size or metadata)
            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(1048576) // for example, 1MB chunks
                    .metadata(new Document("type", fileType)
                            .append("title", file.getOriginalFilename() != null ? file.getOriginalFilename().replaceAll(" ", "-") : "uploaded media")
                            .append("accomId", idAccomm));

            // Upload the file to GridFS and get the generated ID
            ObjectId fileId = gridFSBucket.uploadFromStream(file.getOriginalFilename(), inputStream, options);

            logger.info("Media uploaded successfully with ID: {}", fileId.toHexString());
            return fileId.toHexString();
        } catch (Exception e) {
            logger.error("An error occurred while uploading media: {}", e.getMessage(), e);
            return null;
        }
    }


    @Override
    public Accommodation getAccommodationById(String id) {
        return accommodationRepo.findAccommodationById(id);
    }

    @Override
    public List<Accommodation> findAccommodationByRateNumber(int n) {
        return accommodationRepo.findAccommodationByRateNumber(n);
    }


    @Override
    public List<Accommodation> getAllAccommodation() {
        return accommodationRepo.findAccommodationByOrderByDateDesc();
    }

    @Override
    public List<Accommodation> getAllAccommodationByType(AccommodationType accommodationType) {
        return accommodationRepo.findAccommodationByAccomType(accommodationType);
    }

    @Override
    public List<Accommodation> getAllAccommodationByCity(String city) {
        City c= cityRepo.findCitiesByNom(city);
        return accommodationRepo.findAccommodationByCity(c);
    }

    @Override
    public List<Accommodation> getAllAccommodationNotFull() {
        return accommodationRepo.findAccommodationByFull(false);
    }

    @Override
    public List<Accommodation> getAllAccommodationFull() {
        return accommodationRepo.findAccommodationByFull(true);
    }

    @Override
    public List<Accommodation> getAllAccommodationByTypeAndCity(AccommodationType accommodationType, String city) {
        City c= cityRepo.findCitiesByNom(city);
        return accommodationRepo.findAccommodationByAccomTypeAndCity(accommodationType, c);
    }


    @Override
    public List<Accommodation> gettAllAccommodationByRoomType(String roomType) {
        return null;
    }


    public byte[] getMediaAsBytes(String mediaId) throws IOException {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb());
        ObjectId fileId = new ObjectId(mediaId); // Convert the string ID back to ObjectId
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridFSBucket.downloadToStream(fileId, outputStream);
        return outputStream.toByteArray();
    }


    public List<Accommodation> filterAccommodations(String countryName ,String city, Integer rateNumber, Boolean full, AccommodationType accomType) {
        Query query = new Query();
        City c= cityRepo.findCitiesByNom(city);
        Country country= countryRepo.findCountryByNom(countryName);
        if (country != null) {
            query.addCriteria(Criteria.where("country._id").is(country.getId()));
        }
        if (c != null) {
            query.addCriteria(Criteria.where("city").is(c));
        }

        if (rateNumber != null) {
            query.addCriteria(Criteria.where("rateNumber").is(rateNumber));
        }
        if (full != null) {
            query.addCriteria(Criteria.where("full").is(full));
        }
        if (accomType != null) {
            query.addCriteria(Criteria.where("accomType").is(accomType));
        }

        return mongoTemplate.find(query, Accommodation.class);
    }




}
