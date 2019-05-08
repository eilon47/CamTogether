package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IAlbumService;
import CamTogether.RestApi.services.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import xmls.CTImage;
import xmls.CommandsEnum;
import xmls.RequestHeader;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    IImageService imageService;

    @PostMapping("/")
    ResponseEntity<String> postImage(@PathVariable String album, @RequestBody CTImage ctImage){
        xmls.RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.ADD_NEW_PHOTO_TO_ALBUM);
        header.setUsername(ctImage.getUserName());
        return imageService.post(header, ctImage);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{album}/{imageName}")
    ResponseEntity<CTImage> getImage(@PathVariable String imageName, @PathVariable String album, @RequestBody String username) {
        xmls.RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.GET_IMAGE);
        header.setUsername(username);
        return imageService.get(header,imageName, album);
    }

}
