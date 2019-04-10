package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IAlbumService;
import CamTogether.RestApi.services.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import xmls.CTImage;

@RestController
public class ImageController {

    @Autowired
    IImageService imageService;

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping("/image/{path}")
    CTImage getImage(@PathVariable String path) {
        return imageService.getImage(path);
    }

}
