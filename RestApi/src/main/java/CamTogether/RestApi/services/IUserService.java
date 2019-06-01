package CamTogether.RestApi.services;

import org.springframework.http.ResponseEntity;
import xmls.RequestHeader;
import xmls.User;

public interface IUserService {

    ResponseEntity<String> register(RequestHeader header, User user);

    ResponseEntity<String> login(RequestHeader header, String userName, String password);

    ResponseEntity<String> updateUser(RequestHeader header,User user);
    ResponseEntity<User> getUserDetails(RequestHeader header, String username);
}
