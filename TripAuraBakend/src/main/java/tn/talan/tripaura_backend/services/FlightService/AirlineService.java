package tn.talan.tripaura_backend.services.FlightService;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.talan.tripaura_backend.entities.Flights.Airline;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.repositories.flightRepo.AirlineRepo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Service
@AllArgsConstructor
public class AirlineService implements IAirlineService {
 AirlineRepo airlineRepo ;
    private final UrlValidationService urlValidationService;
    private MongoTemplate mongoTemplate;
    @Override
    public Airline addAirline(Airline airline, MultipartFile logoFile) {
        if (airlineRepo.existsAirlineByCode(airline.getCode())) {
            throw new CustomException("There is already an airline with this code.");
        }
        if (logoFile == null || logoFile.isEmpty()) {
            throw new CustomException("Logo file is required.");
        }

        if (airlineRepo.existsAirlineByName(airline.getName())) {
            throw new CustomException("There is already an airline with this name.");
        }

        // Upload the logo to GridFS
        String logoFileId = uploadLogoToGridFS(logoFile);
        airline.setLogoUrl(logoFileId); // Store the GridFS file ID as the logoUrl

        return airlineRepo.save(airline);
    }

    private String uploadLogoToGridFS(MultipartFile logoFile) {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb());

        try (InputStream inputStream = logoFile.getInputStream()) {
            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(1048576) // 1MB chunks
                    .metadata(new Document("type", "image")
                            .append("title", logoFile.getOriginalFilename())
                            .append("uploaded", true));

            ObjectId fileId = gridFSBucket.uploadFromStream(logoFile.getOriginalFilename(), inputStream, options);
            return fileId.toHexString();
        } catch (IOException e) {
            throw new CustomException("Failed to upload logo: " + e.getMessage());
        }
    }

    public byte[] getLogoAsBytes(String logoFileId) throws IOException {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb());
        ObjectId fileId = new ObjectId(logoFileId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridFSBucket.downloadToStream(fileId, outputStream);
        return outputStream.toByteArray();

}


    @Override
    public List<Airline> getAirlines() {
        return airlineRepo.findAll();
    }

    @Override
    public void deleteAirline(String AirlineId) {
        if (!airlineRepo.existsById(AirlineId)) {
            throw new CustomException("Flight not found");
        }
        airlineRepo.deleteById(AirlineId);
    }
    @Override
    public Airline updateAirline(Airline airline, MultipartFile logoFile) {
        // Vérifiez que l'ID n'est pas null
        if (airline.getId() == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }

        if (!airlineRepo.existsById(airline.getId())) {
            throw new CustomException("Airline not found");
        }

        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                // Upload the new logo file to GridFS
                GridFSBucket gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb());
                ObjectId fileId;
                try (InputStream inputStream = logoFile.getInputStream()) {
                    fileId = gridFSBucket.uploadFromStream(logoFile.getOriginalFilename(), inputStream);
                }
                airline.setLogoUrl(fileId.toHexString());  // Store the new logo file ID
            } catch (IOException e) {
                throw new CustomException("Failed to upload logo: " + e.getMessage());
            }
        }

        return airlineRepo.save(airline);
    }



    public byte[] getLogo(String logoId) {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridFSBucket.downloadToStream(new ObjectId(logoId), outputStream);
        return outputStream.toByteArray();
    }
    @Override
    public List<Airline> searchAirlines(String name, String code, String country) {
        Query query = new Query();

        if (name != null ) {
            query.addCriteria(Criteria.where("name").is(name));
        }
        if (code != null ){
            query.addCriteria(Criteria.where("code").is(code));
        }
        if (country != null) {
            query.addCriteria(Criteria.where("country").in(country));
        }


        System.out.println("Query: " + query.toString()); // Ajoutez ce log pour vérifier la requête MongoDB

        List<Airline> airlines = mongoTemplate.find(query, Airline.class);

        // Log found flights
        airlines.forEach(airline -> System.out.println("Found airline: " + airline.toString()));
        System.out.println("airlines: " + airlines);
        return airlines;
    }

    public byte[] downloadImage(String urlString) throws IOException {
        URL url = new URL(urlString);
        try (InputStream in = url.openStream()) {
            return in.readAllBytes();
        }

    }
}
