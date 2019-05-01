package CamTogether.RestApi.services;


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
    public CTImage getImage(String image, String album, String username){
        GetImageRequestBody requestBody = new GetImageRequestBody();
        requestBody.setAlbum(album);
        requestBody.setImageName(image);
        requestBody.setUsername(username);

        HeaderRequest header = new HeaderRequest();
        header.setUsername(username);
        header.setCommand(CommandsEnum.GET_IMAGE);

        RequestMessage request = new RequestMessage();
        request.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(request, requestBody);
        try {
            GetImageResponseBody responseBody = xmlConverter.serializeFromString(responseMessage.getBody(), GetImageResponseBody.class);
            return responseBody.getImage();
        } catch (JAXBException e) {
            logger.warn("Could not parse server response [" + responseMessage.getBody() + "]", e );
            return null;
        }
    }



}
