package tn.talan.tripaura_backend.services.accomodationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.talan.tripaura_backend.entities.Circuit.City;
import tn.talan.tripaura_backend.entities.accommodation.Accommodation;
import tn.talan.tripaura_backend.entities.accommodation.AccommodationType;

import java.io.IOException;
import java.util.List;

public interface AccomodationService {
    public ResponseEntity<?> createAccomodation(Accommodation accommodation, MultipartFile image, String fileType) ;
    public Accommodation getAccommodationById(String id);
    public ResponseEntity<?> updateAccommodation(Accommodation a) ;
    public List<Accommodation> getAllAccommodation();
    public List<Accommodation> getAllAccommodationByType(AccommodationType accommodationType);
    public List<Accommodation> getAllAccommodationByCity(String city);
    public List<Accommodation> getAllAccommodationNotFull();
    public List<Accommodation> getAllAccommodationFull();
    public List<Accommodation> getAllAccommodationByTypeAndCity(AccommodationType accommodationType, String city);
    public void deleteAccomodation(String id);
    public List<Accommodation> gettAllAccommodationByRoomType(String roomType);
    public String uploadMessageMedia(String idAccomm, MultipartFile file,
                                     String fileType) throws IOException;
    public byte[] getMediaAsBytes(String mediaId) throws IOException;

    public ResponseEntity<?> addAccomodation(Accommodation accommodation);
    public List<Accommodation> findAccommodationByRateNumber(int n) ;
    public List<Accommodation> filterAccommodations(String  countryName,String city, Integer rateNumber, Boolean full, AccommodationType accomType);




}
