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
public class ImageController {

    @Autowired
    IImageService imageService;

    @CrossOrigin(origins = "*")
    @PostMapping("/image")
    ResponseEntity<String> postImage(@RequestBody CTImage ctImage){
        System.out.println(ctImage);
        return imageService.post(new RequestHeader(), ctImage);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/image/{imageName}")
    ResponseEntity<CTImage> getImage(@PathVariable String imageName, @RequestBody Map<String, String> info) {
        String album = info.get("album");
        String username = info.get("username");
        return imageService.get(new RequestHeader(),imageName, album);
    }
}
