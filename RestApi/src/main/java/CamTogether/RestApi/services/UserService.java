package CamTogether.RestApi.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xmls.CommandsEnum;
import xmls.*;

import javax.jws.soap.SOAPBinding;
import javax.xml.bind.JAXBException;

@Service
public class UserService extends AbstractService implements IUserService {

    @Override
    public ResponseEntity<String> register(RequestHeader header, User user) {
        NewUserRequestBody requestBody = new NewUserRequestBody();
        requestBody.setUser(user);
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if(responseMessage.getHeader().isCommandSuccess()){
            return ResponseEntity.ok("OKed");
        }

        return ResponseEntity.badRequest().body(responseMessage.getHeader().getReason());
    }

    @Override
    public ResponseEntity<String> login(RequestHeader header,String userName, String password) {
        LoginRequestBody requestBody = new LoginRequestBody();
        requestBody.setUsername(userName);
        requestBody.setPassword(password);
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if (responseMessage.getHeader().isCommandSuccess()){
            return ResponseEntity.ok("OKed");
        }
        return ResponseEntity.badRequest().body(responseMessage.getHeader().getReason());
    }

    @Override
    public ResponseEntity<String> addFriend(RequestHeader header, String friend) {
        AddOrGetFriendRequestBody requestBody = new AddOrGetFriendRequestBody();
        requestBody.setUsername(friend);
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if(responseMessage.getHeader().isCommandSuccess()){
            return ResponseEntity.ok("OKed");
        }
        return ResponseEntity.badRequest().body(responseMessage.getHeader().getReason());
    }

    public ResponseEntity<String> updateUser(RequestHeader header,User user){
        UpdateUserProfileRequestBody requestBody = new UpdateUserProfileRequestBody();
        requestBody.setUser(user);
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if (responseMessage.getHeader().isCommandSuccess()){
            return ResponseEntity.ok("OKed");
        }
        return ResponseEntity.badRequest().body(responseMessage.getHeader().getReason());
    }

    public ResponseEntity<User> getUserDetails(RequestHeader header,String username) {
        GetUserDetailsRequestBody requestBody = new GetUserDetailsRequestBody();
        requestBody.setUsername(username);
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if (responseMessage.getHeader().isCommandSuccess()){
            try {
                GetUserDetailsResponseBody responseBody = xmlConverter.serializeFromString(responseMessage.getBody(), GetUserDetailsResponseBody.class);
                User user = responseBody.getUser();
                return ResponseEntity.ok(user);
            } catch (JAXBException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }


}
