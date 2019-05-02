package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xmls.User;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    IUserService userService;

    @GetMapping
    public String getRegister(){
        return "Register Page";
    }

    @PostMapping
    public String postUser(@RequestBody User user){
        return userService.registerNewUser(user);
    }

}
