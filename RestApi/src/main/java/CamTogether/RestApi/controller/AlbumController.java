package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
@RestController
public class AlbumController {

    @Autowired
    IAlbumService albumService;


    @GetMapping("/album")
    String getAlbum(){
        return albumService.getAlbums();
    }

    @GetMapping("/album/{albumName}")
    String getAlbum(@PathVariable String albumName){
        return albumService.getAlbum(albumName);
    }



//    @PostMapping("/album/{albuminfo}")
//    String postAlbum(@PathVariable Album albuminfo) {
//
//    }
}
