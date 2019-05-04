package CamTogether.RestApi.services;

import org.springframework.stereotype.Service;
import xmls.User;

@Service
public class UserService extends AbstractService implements IUserService {
    @Override
    public String registerNewUser(User user) {
        return "Not Implemented Yet!";
    }

    @Override
    public String loginUser(String userName, String password) {
        return "Login with userName = " + userName + "and password = " + password;
    }

}
