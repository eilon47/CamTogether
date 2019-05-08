package CamTogether.RestApi.services;

import org.springframework.stereotype.Service;
import xmls.CommandsEnum;
import xmls.*;

@Service
public class UserService extends AbstractService implements IUserService {

    @Override
    public String registerNewUser(RequestHeader header, User user) {
        NewUserRequestBody requestBody = new NewUserRequestBody();
        requestBody.setUser(user);
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if(responseMessage.getHeader().isCommandSuccess()){
            return "Success!";
        }
        return responseMessage.getHeader().getReason();
    }

    @Override
    public String loginUser(RequestHeader header,String userName, String password) {
        LoginRequestBody requestBody = new LoginRequestBody();
        requestBody.setUsername(userName);
        requestBody.setPassword(password);
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if (responseMessage.getHeader().isCommandSuccess()){
            return "Success!";
        }
        return responseMessage.getHeader().getReason();
    }

    public String updateUserProfile(RequestHeader header,User user){
        UpdateUserProfileRequestBody requestBody = new UpdateUserProfileRequestBody();
        requestBody.setUser(user);
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if (responseMessage.getHeader().isCommandSuccess()){
            return "Success!";
        }
        return responseMessage.getHeader().getReason();
    }

}
