package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IAlbumService;
import CamTogether.RestApi.services.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xmls.CTImage;

import java.util.List;
import java.util.Map;

@RestController
public class ImageController {

    @Autowired
    IImageService imageService;

    @PostMapping("/image")
    String postImage(@RequestBody CTImage ctImage){
        return imageService.postImage(ctImage);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/image/{imageName}")
    CTImage getImage(@PathVariable String imageName, @RequestBody Map<String, String> info) {
        String album = info.get("album");
        String username = info.get("username");
        return imageService.getImage(imageName, album, username);
    }

}
