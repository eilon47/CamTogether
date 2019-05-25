package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xmls.*;
import xmls.RequestHeader;

@RestController
@RequestMapping("/album")
@CrossOrigin(origins = "*")
public class AlbumController {

    @Autowired
    IAlbumService albumService;

    @GetMapping("/{userName}")
    ResponseEntity<AlbumsList> getAlbums(@PathVariable String userName){
        xmls.RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.GET_ALBUMS_LIST);
        header.setUsername(userName);
        return albumService.getAlbums(header);
    }
    @GetMapping("/{userName}/{albumName}")
    ResponseEntity<CTAlbum> getAlbum(@PathVariable String userName,@PathVariable String albumName) {
        xmls.RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.GET_ALBUM);
        header.setUsername(userName);
        return albumService.getAlbum(header,albumName);
    }
    @PostMapping("/{userName}")
    ResponseEntity<String> postAlbum(@PathVariable String userName, @RequestBody CTAlbum reqBody) {
        xmls.RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.CREATE_NEW_ALBUM);
        header.setUsername(userName);
        return albumService.postAlbum(header,reqBody);
    }

    @PostMapping("/{userName}/{albumName}")
    ResponseEntity<String> addUser(@PathVariable String userName,@PathVariable String albumName, @RequestBody String userToAdd) {
        xmls.RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.ADD_USER_TO_ALBUM);
        header.setUsername(userName);
        return albumService.addUserToAlbum(header, userToAdd, albumName);
    }

    @PostMapping("/{userName}/{albumName}/rules")
    ResponseEntity<String> updateRules(@PathVariable String userName, @PathVariable String albumName, @RequestBody Rules rules) {
        xmls.RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.UPDATE_ALBUM_RULES);
        header.setUsername(userName);
        return albumService.updateRules(header, albumName, rules);
    }

}
