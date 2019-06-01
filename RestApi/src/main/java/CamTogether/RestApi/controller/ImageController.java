package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xmls.CTImage;
import xmls.RequestHeader;

import java.util.Map;

@RestController
public class ImageController {

    static class ImageMessage {
        public CTImage info;
    }

    @Autowired
    IImageService imageService;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/image", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<String> postImage(@RequestBody CTImage ctImage){
        if(ctImage!= null)
            System.out.println("OKED");
            //return ResponseEntity.ok().build();
        //return ResponseEntity.badRequest().build();
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
