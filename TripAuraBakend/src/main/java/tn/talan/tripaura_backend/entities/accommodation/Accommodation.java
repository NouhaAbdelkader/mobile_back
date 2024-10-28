package tn.talan.tripaura_backend.entities.accommodation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import tn.talan.tripaura_backend.entities.Circuit.City;
import tn.talan.tripaura_backend.entities.Circuit.Country;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "Accommodation")
@JsonIgnoreProperties({ "rooms"})
public class Accommodation implements Serializable {
    @Id
    private String id;
    @Indexed
    @NotEmpty(message = "Accommodation type cannot be empty")
    private AccommodationType  accomType;
    @Indexed
    @NotEmpty(message = "Accommodation name cannot be empty")
    private  String name;
    @Indexed
   @NotEmpty(message = "Address cannot be empty")
    private  String address;
    @DBRef
   @NotEmpty(message = "City cannot be empty")
    private City city;
    @DBRef
    @NotEmpty(message = "City cannot be empty")
    private Country country;
    @Indexed
   @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private  String email;
    @Indexed
    private Date date ;
    @Indexed
    private  String description;
    @Indexed
    private int numberOfRoom=0;
    @Indexed
    private boolean pool=true ;
    @Indexed
    private boolean full = false ;
    @Indexed
   // @NotEmpty(message = "Check in time is mandatory")
    private  int checkInTime;
    @Indexed
    //@NotEmpty(message = "Check out time is mandatory")
    private   int  checkOutTime;
    @Indexed
    private int rateNumber=0 ;

    @Indexed
    private String image ;
    @DBRef
    private List<Room> rooms;

    //Room generation attributes
    @Indexed
    //@NotEmpty(message = "number of Rooms is mandatory")
    private int numberOfRoomSingleViewGarden=0;
    @Indexed
    //@NotEmpty(message = "number of Rooms is mandatory")
    private int numberOfRoomSingleViewPool=0;
    @Indexed
   // @NotEmpty(message = "number of Rooms is mandatory")
    private int numberOfRoomSingleViewSea=0;
    @Indexed
   // @NotEmpty(message = "number of Rooms is mandatory")
    private int numberOfRoomTripleViewGarden=0;
    @Indexed
   // @NotEmpty(message = "number of Rooms is mandatory")
    private int numberOfRoomTripleViewPool=0;
    @Indexed
    //@NotEmpty(message = "number of Rooms is mandatory")
    private int numberOfRoomTripleViewSea=0;
    @Indexed
    //@NotEmpty(message = "number of Rooms is mandatory")
    private int numberOfRoomDoubleViewGarden=0;
    @Indexed
    //@NotEmpty(message = "number of Rooms is mandatory")
    private int numberOfRoomDoubleViewSea=0;
    @Indexed
   // @NotEmpty(message = "number of Rooms is mandatory")
    private int numberOfRoomDoubleViewPool=0;
    @Indexed
    private float prix ;





}
