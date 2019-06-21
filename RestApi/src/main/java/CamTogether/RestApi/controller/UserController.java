package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IUserService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xmls.CTImage;
import xmls.CommandsEnum;
import xmls.RequestHeader;
import xmls.User;

import java.io.IOException;
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
        //return ResponseEntity.ok("OKed");
        return userService.login(header, username, ((LinkedHashMap<String,String>)body).get("password"));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.CREATE_NEW_USER);
        header.setUsername(user.getUserName());
        //return ResponseEntity.ok("OKed");
        return userService.register(header, user);
    }

    @PostMapping("/update/{userName}")
    public ResponseEntity<String> update(@PathVariable String userName, @RequestBody User user) throws IOException {
        RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.UPDATE_USER_PROFILE);
        header.setUsername(userName);
        //return ResponseEntity.ok("OKed");

        return userService.updateUser(header, user);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserDetails(@PathVariable String username){
        RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.GET_USER_DETAILS);
        header.setUsername(username);
        // return ResponseEntity.ok(new User());

        return userService.getUserDetails(header, username);
    }

    @GetMapping("/update/friends/{username}/{friend}")
    public ResponseEntity<String> addFriend(@PathVariable String  username, @PathVariable String friend) {
        RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.ADD_FRIEND);
        header.setUsername(username);
        return userService.addFriend(header, friend);

    }

    private User fromJsonString(String j) {
        User user = new User();
        JsonElement root = new JsonParser().parse(j);
        //Get the content of the first map
        JsonObject object = root.getAsJsonObject();

        user.setUserName(object.get("userName").getAsString());
        user.setEmail(object.get("email").getAsString());
        user.setBirthday(object.get("birthday").getAsString());
        user.setDescription(object.get("description").getAsString());
        user.setJoinDate(object.get("joinDate").getAsString());
        user.setPassword(object.get("password").getAsString());
        return user;
    }
}
