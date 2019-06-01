package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IImageService;
import converters.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xmls.CTImage;
import xmls.RequestHeader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class ImageController {

    static class ImageMessage {
        public CTImage info;
    }

    @Autowired
    IImageService imageService;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/image")
    ResponseEntity<String> postImage(@RequestBody MultipartFile file, @RequestParam MultiValueMap<String, String> img){
        CTImage ctImage = fromJsonString(img.get("img").get(0));
        try {
            ctImage.setImageData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageService.post(new RequestHeader(), ctImage);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/image/{imageName}")
    ResponseEntity<CTImage> getImage(@PathVariable String imageName, @RequestBody Map<String, String> info) {
        String album = info.get("album");
        String username = info.get("username");
        return imageService.get(new RequestHeader(),imageName, album);
    }


    private CTImage fromJsonString(String j) {
        //Read the JSON file
        CTImage img = new CTImage();
        JsonElement root = new JsonParser().parse(j);
        //Get the content of the first map
        JsonObject object = root.getAsJsonObject();
        //Iterate over this map
        img.setAlbumName(object.get("albumName").toString().replaceAll("\"", ""));
        img.setImageData(null);
        img.setLatitude(object.get("latitude").getAsFloat());
        img.setLongitude(object.get("longitude").getAsFloat());
        img.setUserName(object.get("userName").getAsString());
        img.setTitle(object.get("title").getAsString());
        img.setImageWidth(object.get("imageWidth").getAsInt());
        img.setImageHeight(object.get("imageHeight").getAsInt());
        img.setImageSize(object.get("imageSize").getAsInt());
        img.setDate(object.get("date").getAsString());
        img.setImageName(object.get("imageName").getAsString());
        return img;
    }
}
