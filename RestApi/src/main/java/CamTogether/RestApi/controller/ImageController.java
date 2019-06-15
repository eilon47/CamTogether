package CamTogether.RestApi.controller;

import CamTogether.RestApi.services.IImageService;
import converters.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xmls.CTImage;
import xmls.CommandsEnum;
import xmls.RequestHeader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
        RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.ADD_NEW_PHOTO_TO_ALBUM);
        header.setUsername(ctImage.getUserName());
        return imageService.post(header, ctImage);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/image/imgDetails")
    ResponseEntity<CTImage> getImage(@RequestBody Map<String, String> info) throws IOException {
        String album = info.get("albumName");
        String username = info.get("userName");
        String imageName = info.get("imageName");
        //CTImage img = createCTImage();
        RequestHeader header = new RequestHeader();
        header.setCommand(CommandsEnum.GET_IMAGE);
        header.setUsername(username);
        return imageService.get(header,imageName, album);
    }


    private CTImage createCTImage() throws IOException {
        //Read the JSON file
        CTImage img = new CTImage();
        File f = new File("C:\\Users\\green\\Desktop\\camTogether\\CamTogether\\RestApi\\src\\main\\resources\\images\\8.jpg");
        BufferedImage imgg = ImageIO.read(f);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( imgg, "jpg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        img.setImageData(imageInByte);
        //Iterate over this map
        img.setAlbumName("albumName");
        img.setLatitude(0);
        img.setLongitude(0);
        img.setUserName("userName");
        img.setTitle("title");
        img.setImageWidth(0);
        img.setImageHeight(0);
        img.setImageSize(0);
        img.setDate(null);
        img.setImageName("imageName");
        return img;
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
