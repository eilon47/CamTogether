package CamTogether.RestApi.services;

import xmls.User;

public interface IUserService {

    String registerNewUser(User user);

    String loginUser(String userName, String password);
}
