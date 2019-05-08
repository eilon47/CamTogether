package CamTogether.RestApi.services;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xmls.*;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class ImageService extends AbstractService implements IImageService{


    @Override
    public ResponseEntity<String> post(RequestHeader header, CTImage image) {
        NewImageRequestBody requestBody = new NewImageRequestBody();
        requestBody.setAlbum(image.getAlbumName());
        requestBody.setImage(image);

        RequestHeader headerRequest = new RequestHeader();
        headerRequest.setCommand(CommandsEnum.ADD_NEW_PHOTO_TO_ALBUM);
        headerRequest.setUsername(image.getUserName());

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeader(headerRequest);

        ResponseMessage responseMessage = messageToServerAndResponse(requestMessage, requestBody);
        if(!responseMessage.getHeader().isCommandSuccess())
            return ResponseEntity.badRequest().body(responseMessage.getHeader().getReason());
        return ResponseEntity.ok().body("Images was added successfully");
    }

    @Override
    public ResponseEntity<CTImage> get(RequestHeader header, String image, String album) {
        GetImageRequestBody requestBody = new GetImageRequestBody();
        requestBody.setAlbum(album);
        requestBody.setImageName(image);
        requestBody.setUsername(header.getUsername());
        RequestMessage request = new RequestMessage();
        request.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(request, requestBody);
        if (!responseMessage.getHeader().isCommandSuccess()) {
            return ResponseEntity.badRequest().header(responseMessage.getHeader().getReason()).body(null);
        }
        try {
            GetImageResponseBody responseBody = xmlConverter.serializeFromString(responseMessage.getBody(), GetImageResponseBody.class);
            CTImage img = responseBody.getImage();
            if(img == null || img.getImageData().length == 0)
                return ResponseEntity.notFound().header("Data was not found").build();
            return ResponseEntity.ok(img);
        } catch (JAXBException e) {
            logger.warn("Could not parse server response [" + responseMessage.getBody() + "]", e );
            return ResponseEntity.badRequest().header("Could not get image data").body(null);
        }
    }
}
