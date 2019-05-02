package CamTogether.RestApi.services;

import org.springframework.stereotype.Service;
import xmls.User;

@Service
public class UserService extends AbstractService implements IUserService {
    @Override
    public String registerNewUser(User user) {
        return "Not Implemented Yet!";
    }
}
