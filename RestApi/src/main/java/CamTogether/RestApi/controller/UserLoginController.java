package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")

public class UserLoginController {
    @Autowired
    IUserService userService;

    @GetMapping("/{userName}:{password}")
    public String login(@PathVariable String userName, @PathVariable String password) {
        return userService.loginUser(userName,password);
    }

}
