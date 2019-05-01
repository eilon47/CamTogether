package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IAlbumService;
import CamTogether.RestApi.services.IImageService;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xmls.CTImage;

import java.util.List;
import java.util.Map;

@RestController
public class ImageController {

    @Autowired
    IImageService imageService;

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping("/image/{imageName}")
    CTImage getImage(@PathVariable String imageName, @RequestBody Map<String, String> info) {
        String album = info.get("album");
        String username = info.get("username");
        return imageService.getImage(imageName, album, username);
    }

}
