package CamTogether.RestApi.services;

import xmls.RequestHeader;
import xmls.User;

public interface IUserService {

    String registerNewUser(RequestHeader header, User user);

    String loginUser(RequestHeader header, String userName, String password);

    String updateUserProfile(RequestHeader header,User user);
}
