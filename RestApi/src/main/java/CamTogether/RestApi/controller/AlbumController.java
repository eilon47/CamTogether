package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IAlbumService;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xmls.AlbumsList;
import xmls.CTAlbum;
import xmls.DummyObject;

@RestController
@RequestMapping("/album")
@CrossOrigin(origins = "http://localhost:8081")
public class AlbumController {

    @Autowired
    IAlbumService albumService;

    @GetMapping("/{userName}")
    AlbumsList getAlbums(@PathVariable String userName){
        return albumService.getAlbums(userName);
    }
    @GetMapping("/{userName}/{albumName}")
    CTAlbum getAlbum(@PathVariable String userName,@PathVariable String albumName) {
        CTAlbum ct = albumService.getAlbum(userName,albumName);
        return ct;
    }
    @PostMapping("/{userName}")
    String postAlbum(@PathVariable String userName, @RequestBody CTAlbum reqBody) {
        System.out.println(reqBody);
        return albumService.postAlbum(userName,reqBody);
    }
}
