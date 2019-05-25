package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xmls.CommandsEnum;
import xmls.RequestHeader;
import xmls.User;

import java.util.LinkedHashMap;

@RestController
@CrossOrigin(origins = "*")

public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/login/{username}")
    public ResponseEntity<String> login(@PathVariable String username, @RequestBody Object body) {
        RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.LOGIN_WITH_USER);
        header.setUsername(username);
        return userService.login(header, username, ((LinkedHashMap<String,String>)body).get("password"));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.CREATE_NEW_USER);
        header.setUsername(user.getUserName());
        return userService.register(header, user);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<String> update(@PathVariable String user, @RequestBody User userDetails) {
        RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.UPDATE_USER_PROFILE);
        header.setUsername(user);
        return userService.updateUser(header, userDetails);
    }

}
