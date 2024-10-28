package tn.talan.tripaura_backend.dto;


public class LoginResponse {

    private long expiresIn;
    private UserTripAuraDTO userDto ;

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;

    }

    public LoginResponse setUserDto(UserTripAuraDTO userDto) {
        this.userDto = userDto;
        return this;

    }
    public UserTripAuraDTO getUserDto() {
        return userDto ;
    }

}