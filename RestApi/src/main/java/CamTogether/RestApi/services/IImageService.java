package CamTogether.RestApi.services;


import org.springframework.http.ResponseEntity;
import xmls.CTImage;
import xmls.RequestHeader;

public interface IImageService {

    ResponseEntity<String> post(RequestHeader header, CTImage image);
    ResponseEntity<CTImage> get(RequestHeader header, String image, String album);
}
